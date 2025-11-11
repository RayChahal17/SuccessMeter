package com.example.successmeter.domain.repo

import com.example.successmeter.data.local.db.entity.QuoteEntity
import kotlinx.coroutines.flow.Flow

interface QuoteRepository {
    fun observeRecent(limit: Int = 50): Flow<List<QuoteEntity>>
    suspend fun upsert(quote: QuoteEntity)





    // “Why do I need a repository at all if the DAO already has the same functions?”
    //Let’s unpack that slowly, in plain English, and then I’ll show you how this layering pays off later.
    //💡 The short answer
    //DAO = “How to talk to the database engine (Room).”
    //Repository = “How the app feature wants to get its data, no matter where it comes from.”
    //Today, they both call the same Room code.
    //Tomorrow, your Repository can:
    //pull from Room and Firestore (or cache + network merge),
    //apply extra logic (sorting, filtering, mapping),
    //coordinate multiple DAOs,
    //or handle error wrapping, background dispatchers, etc.
    //So:
    //👉 DAO = low-level local SQL
    //👉 Repository = high-level business source of truth

}


