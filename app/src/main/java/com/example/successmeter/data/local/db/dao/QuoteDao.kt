package com.example.successmeter.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.successmeter.data.local.db.entity.QuoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(quote: QuoteEntity)

    // keyword "upsert" is a shorthand for “update or insert”.

    @Query("SELECT * FROM quote ORDER BY id DESC LIMIT :limit")
    fun observeRecent(limit: Int = 50): Flow<List<QuoteEntity>>

    // By naming it observeRecent(), you’re telling other developers (and yourself) what kind of behavior to expect
    // — that it’s observable / reactive / LiveData, not a one-time fetch.
    // Flow<List<QuoteEntity>> tells ROOM to generate code that emits updates each time the quote table changes.”
}



