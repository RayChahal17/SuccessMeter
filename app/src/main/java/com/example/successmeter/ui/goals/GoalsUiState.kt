package com.example.successmeter.ui.goals

import com.example.successmeter.domain.model.ChiefAim

/**
 * All the state the Goals screen needs from the ViewModel.
 *
 * Right now:
 * - list of chief aims
 * - which chief aim (by rank) is selected
 *
 * We will add more fields later (goal tree, loading, etc.).
 */

data class GoalsUiState(
    val chiefAims: List<ChiefAim> = emptyList(),
    val isLoading: Boolean = false,
)
