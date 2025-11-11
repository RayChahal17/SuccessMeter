package com.example.successmeter.ui.quotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.successmeter.data.local.db.entity.QuoteEntity
import com.example.successmeter.domain.repo.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuotesViewModel @Inject constructor(
    private val repo: QuoteRepository,
    // Optional: keep query/favorites across process death
    // savedState: SavedStateHandle
) : ViewModel() {

    // UI inputs
    val query = MutableStateFlow("")
    val favoritesOnly = MutableStateFlow(false)

    // Base stream from repo (active, soft-delete filtered)
    private val allQuotes: Flow<List<QuoteEntity>> = repo.observeAll()

    // Combined UI list (search + favorites filter)
    val quotes: StateFlow<List<QuoteEntity>> =
        combine(allQuotes, query, favoritesOnly) { all, q, fav ->
            val needle = q.trim().lowercase()
            all
                .filter { quote ->
                    if (needle.isEmpty()) true else
                        quote.text.lowercase().contains(needle) ||
                                (quote.author ?: "").lowercase().contains(needle) ||
                                (quote.tagsCsv ?: "").lowercase().contains(needle)
                }
                .filter { quote -> if (fav) quote.isFavorite else true }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // One-time seed (no-op if already seeded)
    init {
        viewModelScope.launch { repo.ensureSeeded() }
    }

    // Actions
    fun softDelete(id: Long) = viewModelScope.launch { repo.softDelete(id) }
    fun incrementUses(id: Long) = viewModelScope.launch { repo.incrementUses(id) }
    suspend fun pickRandomFavorite() = repo.pickRandomFavorite()
    suspend fun pickRandomAny() = repo.pickRandomAny()
}
