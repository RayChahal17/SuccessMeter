package com.example.successmeter.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomSQLiteQuery
import com.example.successmeter.data.local.db.entity.QuoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {

    // READS — always exclude soft-deleted
    @Query("""
        SELECT * FROM quote
        WHERE deletedAt IS NULL
        ORDER BY isFavorite DESC, uses DESC, createdAt DESC
    """)
    fun observeAll(): Flow<List<QuoteEntity>>

    @Query("""
        SELECT * FROM quote
        WHERE deletedAt IS NULL
          AND (text LIKE :q OR author LIKE :q OR tagsCsv LIKE :q)
        ORDER BY isFavorite DESC, uses DESC, createdAt DESC
    """)
    fun search(q: String): Flow<List<QuoteEntity>>

    @Query("""
        SELECT * FROM quote
        WHERE deletedAt IS NULL
          AND tagsCsv LIKE :tagNeedle
        ORDER BY isFavorite DESC, uses DESC, createdAt DESC
    """)
    fun filterByTag(tagNeedle: String): Flow<List<QuoteEntity>>

    // WRITES
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: QuoteEntity): Long

    // Soft delete sets a timestamp (millis). Any non-null means “deleted”.
    @Query("UPDATE quote SET deletedAt = strftime('%s','now')*1000 WHERE id = :id")
    suspend fun softDelete(id: Long)

    @Query("UPDATE quote SET isFavorite = :fav WHERE id = :id")
    suspend fun updateFavorite(id: Long, fav: Boolean)

    // Helpers
    @Query("""
        SELECT * FROM quote
        WHERE deletedAt IS NULL AND isFavorite = 1
        ORDER BY RANDOM() LIMIT 1
    """)
    suspend fun pickRandomFavorite(): QuoteEntity?

    @Query("""
        SELECT * FROM quote
        WHERE deletedAt IS NULL
        ORDER BY RANDOM() LIMIT 1
    """)
    suspend fun pickRandomAny(): QuoteEntity?

    @Query("UPDATE quote SET uses = uses + 1 WHERE id = :id")
    suspend fun incrementUses(id: Long)

    @Query("SELECT COUNT(*) FROM quote WHERE deletedAt IS NULL")
    suspend fun count(): Int
}

