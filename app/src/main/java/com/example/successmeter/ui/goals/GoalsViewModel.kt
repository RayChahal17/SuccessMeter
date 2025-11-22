package com.example.successmeter.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.successmeter.domain.model.ChiefAim
import com.example.successmeter.domain.model.ChiefAimRank
import com.example.successmeter.domain.repo.GoalsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

// Marks this as a Hilt-managed ViewModel. Hilt will create it and
// handle injecting its dependencies (like GoalsRepository).
@HiltViewModel
class GoalsViewModel @Inject constructor(
    // Dependency from the domain layer: we depend on the *interface*,
    // not on the Room implementation directly.
    private val goalsRepository: GoalsRepository,
) : ViewModel() { // Extends AndroidX ViewModel (lives across config changes)

    // Raw stream of chief aims from the repository.
    // Type: Flow<List<ChiefAim>>
    private val chiefAimsFlow = goalsRepository.observeChiefAims()

    // Which chief aim rank the user (or auto-logic) has selected.
    // null means "nothing selected yet".
    // This is UI-only state, controlled by the ViewModel.
    private val selectedRankFlow = MutableStateFlow<ChiefAimRank?>(null)

    // Public, read-only StateFlow that the Fragment will observe.
    // Type: GoalsUiState = all the data the Goals screen needs.
    val uiState: StateFlow<GoalsUiState> =
    // Combine two streams:
    // 1) chiefAimsFlow: Flow<List<ChiefAim>>
        // 2) selectedRankFlow: StateFlow<ChiefAimRank?>
        combine(
            chiefAimsFlow,
            selectedRankFlow,
        ) { aims, selected ->
            // Decide which rank is *effectively* selected, given:
            // - the current list of aims
            // - the raw selection value
            //
            // Rules:
            // - If selected is not null AND exists in the aims list → keep it.
            // - Else if we have aims → auto-select the first aim's rank.
            // - Else (no aims) → null.
            val effectiveSelected: ChiefAimRank? = when {
                selected != null && aims.any { it.rank == selected } -> selected
                aims.isNotEmpty() -> aims.first().rank
                else -> null
            }

            GoalsUiState(
                chiefAims = aims,
                isLoading = false,                // data has arrived
                numberOfChiefAims = aims.size,    // derived from the list
                selectedChiefAimRank = effectiveSelected,
            )
        }
            // Turn the combined Flow<GoalsUiState> into a StateFlow.
            .stateIn(
                scope = viewModelScope, // CoroutineScope tied to this ViewModel's lifecycle

                // WhileSubscribed(5_000) means:
                // - Start collecting the upstream Flow when there's at least one collector
                // - Keep it active for 5 seconds after the last collector disappears
                //   (helps avoid thrashing when fragments are quickly recreated)
                started = SharingStarted.WhileSubscribed(5_000),

                // Initial value that collectors will see *before* any data arrives
                // from the database. This prevents nulls and makes the UI simple.
                initialValue = GoalsUiState(
                    chiefAims = emptyList(),
                    isLoading = true,          // start as "loading"
                    numberOfChiefAims = 0,
                    selectedChiefAimRank = null,
                ),
            )

    // Called by the UI when the user taps on a chief aim tab/button.
    fun onChiefAimSelected(rank: ChiefAimRank) {
        // Update the selection stream. Because uiState is built
        // from combine(chiefAimsFlow, selectedRankFlow), this will
        // automatically recompute and emit a new GoalsUiState.
        selectedRankFlow.value = rank
    }

    // Debug-only: create some sample chief aims in the DB.
// This shows the WRITE path: VM → Repository → Room.
// The read flow (chiefAimsFlow) will then emit the updated list.
    fun onDebugSeedChiefAims() {
        viewModelScope.launch {
            // Simple example: three aims, one for each rank.
            val now = Instant.now()

            val aims = listOf(
                ChiefAim(
                    id = 0L, // 0L → let Room auto-generate the ID
                    title = "Become world-class Android dev",
                    description = "Primary mission for the next 3 years",
                    rank = ChiefAimRank.PRIMARY,
                    createdAt = now,
                    updatedAt = null,
                    isArchived = false,
                ),
                ChiefAim(
                    id = 0L,
                    title = "Maintain strong health and fitness",
                    description = "Exercise, sleep, nutrition habits",
                    rank = ChiefAimRank.SECONDARY,
                    createdAt = now,
                    updatedAt = null,
                    isArchived = false,
                ),
                ChiefAim(
                    id = 0L,
                    title = "Nurture relationships & family",
                    description = "Quality time + support",
                    rank = ChiefAimRank.TERTIARY,
                    createdAt = now,
                    updatedAt = null,
                    isArchived = false,
                ),
            )

            // Write each aim to the DB through the repository.
            aims.forEach { aim ->
                goalsRepository.upsertChiefAim(aim)
            }
        }
    }

}
