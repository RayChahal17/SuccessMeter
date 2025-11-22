package com.example.successmeter.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.successmeter.domain.repo.GoalsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

// Marks this as a Hilt-managed ViewModel. Hilt will create it and
// handle injecting its dependencies (like GoalsRepository).
@HiltViewModel
class GoalsViewModel @Inject constructor(
    // Dependency from the domain layer: we depend on the *interface*,
    // not on the Room implementation directly.
    private val goalsRepository: GoalsRepository,
) : ViewModel() { // Extends AndroidX ViewModel (lives across config changes)

    // Public, read-only StateFlow that the Fragment will observe.
    // Type: GoalsUiState = all the data the Goals screen needs.
    val uiState: StateFlow<GoalsUiState> =
        // 1) Start from the repository. This returns a Flow<List<ChiefAim>>.
        goalsRepository
            .observeChiefAims()          // Flow<List<ChiefAim>> from Room via repository

            // 2) Transform the raw list into a "screen model" (GoalsUiState).
            .map { aims ->
                // `map` is called for every value emitted by the Flow.
                // Here `aims` is the latest list of chief aims from the DB.

                // Wrap the list of chief aims into a GoalsUiState object.
                // We can also set isLoading = false because data has arrived.
                GoalsUiState(
                    chiefAims = aims,
                    isLoading = false,
                    numberOfChiefAims = aims.size,
                    )
            }

            // 3) Convert the cold Flow<GoalsUiState> into a hot StateFlow<GoalsUiState>.
            .stateIn(
                scope = viewModelScope, // CoroutineScope tied to this ViewModel's lifecycle

                // WhileSubscribed(5_000) means:
                // - Start collecting the upstream Flow when there's at least one collector
                // - Keep it active for 5 seconds after the last collector disappears
                //   (useful to avoid thrashing when fragments are recreated quickly)
                started = SharingStarted.WhileSubscribed(5_000),

                // Initial value that collectors will see *before* any data arrives
                // from the database. This prevents nulls and makes the UI simple.
                initialValue = GoalsUiState(
                    chiefAims = emptyList(),
                    isLoading = true, // we can treat the very beginning as "loading"
                    numberOfChiefAims = 0,

                    ),
            )
}
