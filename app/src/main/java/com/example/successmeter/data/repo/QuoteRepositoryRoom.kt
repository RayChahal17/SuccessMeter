package com.example.successmeter.data.repo

import com.example.successmeter.data.local.db.dao.QuoteDao
import com.example.successmeter.data.local.db.entity.QuoteEntity
import com.example.successmeter.domain.repo.QuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Room-backed implementation. Delegates to DAO with minimal extra logic.
 * If/when you add network, mapping, or multi-DAO coordination, it lives here.
 */
class QuoteRepositoryRoom @Inject constructor(
    private val dao: QuoteDao
) : QuoteRepository {

    // ---- READ ----
    override fun observeAll(): Flow<List<QuoteEntity>> = dao.observeAll()
    override suspend fun pickRandomFavorite(): QuoteEntity? = dao.pickRandomFavorite()
    override suspend fun pickRandomAny(): QuoteEntity? = dao.pickRandomAny()
    override suspend fun count(): Int = dao.count()

    // ---- WRITE ----
    override suspend fun upsert(q: QuoteEntity): Long = dao.upsert(q)
    override suspend fun softDelete(id: Long) = dao.softDelete(id)
    override suspend fun setFavorite(id: Long, fav: Boolean) = dao.updateFavorite(id, fav)
    override suspend fun incrementUses(id: Long) = dao.incrementUses(id)

    // ---- Seeder (idempotent) ----
    override suspend fun ensureSeeded() {
        if (dao.count() > 0) return
        val samples = listOf(
            QuoteEntity(
                text = "Discipline equals freedom.",
                author = "Jocko Willink",
                tagsCsv = "discipline,focus",
                isFavorite = true
            ),
            QuoteEntity(
                text = "What gets measured gets managed.",
                author = "Peter Drucker",
                tagsCsv = "measurement,management"
            ),
            QuoteEntity(
                text = "The man who moves a mountain begins by carrying away small stones.",
                author = "Confucius",
                tagsCsv = "consistency,patience"
            ),
            QuoteEntity(
                text = "We are what we repeatedly do. Excellence, then, is not an act, but a habit.",
                author = "(Attributed) Aristotle",
                tagsCsv = "habits,excellence"
            ),
            QuoteEntity(
                text = "You miss 100% of the shots you don’t take.",
                author = "Wayne Gretzky",
                tagsCsv = "action,courage"
            )
        )
        samples.forEach { dao.upsert(it) }
    }
}
