package com.example.successmeter.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.successmeter.domain.model.ChiefAim
import com.example.successmeter.domain.repo.GoalsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(private val goalsRepository: GoalsRepository) : ViewModel(){

    // Expose all non-archived chief aims as a StateFlow so the UI can observe it.

    val chiefAims : StateFlow<List<ChiefAim>> = goalsRepository
        .observeChiefAims()  // Flow<List<ChiefAim>>
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

}