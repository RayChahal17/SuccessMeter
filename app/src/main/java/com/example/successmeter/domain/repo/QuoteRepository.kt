package com.example.successmeter.domain.repo

import com.example.successmeter.data.local.db.entity.QuoteEntity
import kotlinx.coroutines.flow.Flow

/**
 * QuoteRepository
 * Stable boundary for the feature. Room today; could be Room+network later.
 *
 * Keep it small: the VM does in-memory filtering for now, so we only expose
 * the base list plus a few mutations/utilities.
 */
interface QuoteRepository {

    // ---- READ ----
    /** Live stream of all active (non–soft-deleted) quotes. */
    fun observeAll(): Flow<List<QuoteEntity>>

    /** A random favorite quote, or null if none exist. */
    suspend fun pickRandomFavorite(): QuoteEntity?

    /** A random quote (any), or null if table is empty. */
    suspend fun pickRandomAny(): QuoteEntity?

    /** Count of active quotes (post soft-delete). */
    suspend fun count(): Int


    // ---- WRITE ----
    /** Insert or update a quote; returns row id. */
    suspend fun upsert(q: QuoteEntity): Long

    /** Soft-delete by id (mark deletedAt). */
    suspend fun softDelete(id: Long)

    /** Toggle favorite flag for a quote. */
    suspend fun setFavorite(id: Long, fav: Boolean)

    /** Increment a lightweight "uses" counter. */
    suspend fun incrementUses(id: Long)

    /** Seed a few quotes if the table is empty (idempotent). */
    suspend fun ensureSeeded()
}
