package com.example.successmeter.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.successmeter.data.local.db.entity.QuoteEntity
import com.example.successmeter.domain.repo.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: QuoteRepository
) : ViewModel() {
    //@HiltViewModel: tells Hilt “I’ll build this ViewModel and inject its dependencies.”
    //@Inject constructor(...): “Give me a QuoteRepository when you create me.”
    //Why a repository here? So UI doesn’t care how data is stored (Room now; Firestore later).

    val quote : StateFlow<List<QuoteEntity>> = repo.observeAll().stateIn(
        viewModelScope,SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // seed once (safe no-op if already seeded)
        viewModelScope.launch { repo.ensureSeeded() }
    }

    fun delete(id: Long) =viewModelScope.launch { repo.softDelete(id) }

    fun favoriteRandon() = viewModelScope.launch { repo.pickRandomFavorite()?.let { repo.incrementUses(it.id) } }



}
