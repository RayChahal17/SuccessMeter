package com.example.successmeter.ui.goals

import com.example.successmeter.domain.model.ChiefAim
import com.example.successmeter.domain.model.ChiefAimRank

/**
 * All the state the Goals screen needs from the ViewModel.
 *
 * Right now:
 * - list of chief aims
 * - loading flag
 * - how many chief aims there are
 * - which chief aim rank is selected (if any)
 *
 * Later we’ll add:
 * - goals for selected chief aim
 * - tasks for selected goal
 * - error messages, dialogs, etc.
 */
data class GoalsUiState(
    // Data coming from the domain/repository:
    val chiefAims: List<ChiefAim> = emptyList(),

    // UI status:
    val isLoading: Boolean = false,

    // Derived data (computed from chiefAims):
    val numberOfChiefAims: Int = 0,

    // UI-only selection (which tab/rank is selected):
    // null means “nothing selected yet”.
    val selectedChiefAimRank: ChiefAimRank? = null,
)
