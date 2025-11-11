package com.example.successmeter.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.successmeter.data.local.db.entity.QuoteEntity
import com.example.successmeter.domain.repo.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val quotes: QuoteRepository
) : ViewModel() {
    //@HiltViewModel: tells Hilt “I’ll build this ViewModel and inject its dependencies.”
    //@Inject constructor(...): “Give me a QuoteRepository when you create me.”
    //Why a repository here? So UI doesn’t care how data is stored (Room now; Firestore later).
    val recent = quotes.observeRecent().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )
    //quotes.observeRecent() returns a Flow<List<QuoteEntity>> from Room. Flow = stream of updates.
    //.stateIn(...) turns that stream into a hot, stateful stream that always has a current value:
    //scope = viewModelScope → coroutines tied to ViewModel’s lifecycle (auto-cancel on clear).
    //SharingStarted.Lazily → don’t start collecting until someone actually observes (saves work).
    //initialValue = emptyList() → before DB responds, we show an empty list instead of null.
    //Plain English: recent is “the latest list of quotes, always available,”
    //and it updates itself when the database changes.
    fun addSample() {
        viewModelScope.launch {
            quotes.upsert(
                QuoteEntity(
                    text = "You can always edit a bad page; you can’t edit a blank page.",
                    author = "Jodi Picoult"
                )
            )
        }
        //viewModelScope.launch { ... } runs on a background thread safely.
        //quotes.upsert(...) writes to DB via the repository.
        //When DB changes, the Flow emits a new list ⇒ the UI updates automatically.
    }
}
