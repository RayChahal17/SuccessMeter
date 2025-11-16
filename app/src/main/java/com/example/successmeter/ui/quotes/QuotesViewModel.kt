package com.example.successmeter.ui.quotes

// ---- Imports kept explicit for clarity ----
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.successmeter.data.local.db.entity.QuoteEntity
import com.example.successmeter.domain.repo.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * QuotesViewModel
 * One job: provide a filtered list of quotes and apply simple mutations.
 *
 * Mental model:
 * - Inputs (from UI): search text, "favorites only" switch.
 * - Source of truth: Flow from Room via repository.
 * - Output to UI: a StateFlow<List<QuoteEntity>> you collect in the Fragment.
 * - Mutations: setFavorite, softDelete, incrementUses (fire-and-forget).
 */
@HiltViewModel
class QuotesViewModel @Inject constructor(
    private val repo: QuoteRepository
) : ViewModel() {

    // ---------- UI INPUTS (write from Fragment) ----------

    // Search text typed by the user.
    // Expose as functions (setQuery) to keep callers simple.
    private val query = MutableStateFlow("")

    // Whether to show only favorites.
    private val favoritesOnly = MutableStateFlow(false)

    // Optional public setters if you prefer not to expose the flows:
    fun setQuery(text: String) { query.value = text }
    fun setFavoritesOnly(enabled: Boolean) { favoritesOnly.value = enabled }

    // ---------- DATA SOURCE (read from repository) ----------

    // Stream of all active quotes from Room (already filters out soft-deleted in DAO).
    // Any DB change auto-emits here.
    private val allQuotes: Flow<List<QuoteEntity>> = repo.observeAll()

    // ---------- UI OUTPUT (collect from Fragment) ----------

    /**
     * The list the UI shows, derived from:
     * - DB stream (allQuotes)
     * - search text (query)
     * - favorites filter (favoritesOnly)
     *
     * Notes:
     * - debounce(150): avoid recomputing on every keystroke.
     * - distinctUntilChanged(): skip duplicates when value didn't actually change.
     * - stateIn(): turns cold Flows into a hot StateFlow that survives collectors.
     */
    @OptIn(FlowPreview::class)
    val quotes: StateFlow<List<QuoteEntity>> =
        combine(
            allQuotes,
            query.debounce(150).map { it.trim().lowercase() }.distinctUntilChanged(),
            favoritesOnly
        ) { all, needle, favOnly ->
            // 1) Text filter (matches text, author, or tags)
            val textFiltered = if (needle.isEmpty()) {
                all
            } else {
                all.filter { q ->
                    q.text.lowercase().contains(needle) ||
                            (q.author ?: "").lowercase().contains(needle) ||
                            (q.tagsCsv ?: "").lowercase().contains(needle)
                }
            }

            // 2) Favorites filter (if enabled)
            if (favOnly) textFiltered.filter { it.isFavorite } else textFiltered
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = emptyList()
            )

    // ---------- ONE-TIME SETUP ----------

    // Seed local DB once (idempotent in repo).
    init {
        viewModelScope.launch { repo.ensureSeeded() }
    }

    // ---------- INTENTS / MUTATIONS (call from Fragment) ----------

    /** Toggle the star from the adapter: vm.setFavorite(id, newFav) */
    fun setFavorite(id: Long, fav: Boolean) = viewModelScope.launch {
        repo.setFavorite(id, fav)
        // No manual refresh needed: Room re-emits allQuotes -> quotes updates.
    }

    /** Optional: used if you keep a delete action in the row/menu. */
    fun softDelete(id: Long) = viewModelScope.launch {
        repo.softDelete(id)
    }

    /** Optional: used if you increment a "uses" counter on copy/share. */
    fun incrementUses(id: Long) = viewModelScope.launch {
        repo.incrementUses(id)
    }

    // ---------- OPTIONAL HELPERS (if you show a random quote elsewhere) ----------

    suspend fun pickRandomFavorite(): QuoteEntity? = repo.pickRandomFavorite()
    suspend fun pickRandomAny(): QuoteEntity? = repo.pickRandomAny()
}
