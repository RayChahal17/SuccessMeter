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

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val goalsRepository: GoalsRepository,
) : ViewModel() {

    // The screen's state: right now, just the list of chief aims.
    val uiState: StateFlow<GoalsUiState> =
        goalsRepository
            .observeChiefAims()          // Flow<List<ChiefAim>>
            .map { aims ->
                // Wrap the list of chief aims into a GoalsUiState object.
                GoalsUiState(chiefAims = aims)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = GoalsUiState()
            )
}
