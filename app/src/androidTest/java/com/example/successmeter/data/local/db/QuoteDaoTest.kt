package com.example.successmeter.data.local.db

// Android / test framework
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4

// Coroutines / Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

// JUnit (use Assert.* to avoid “unresolved reference” on assertEquals/assertTrue)
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

// Project types
import com.example.successmeter.data.local.db.dao.QuoteDao
import com.example.successmeter.data.local.db.entity.QuoteEntity

/**
 * Instrumented DAO tests:
 * - Runs on an Android device/emulator (androidTest source set).
 * - Uses an in-memory Room database so nothing is written to disk.
 */
@RunWith(AndroidJUnit4::class)
class QuoteDaoTest {

    private lateinit var db: AppDatabase          // in-memory DB created fresh per test
    private lateinit var dao: QuoteDao            // system under test

    @Before
    fun createDb() {
        // Context for building the in-memory DB
        val context = ApplicationProvider.getApplicationContext<Context>()

        // Build an in-memory Room database; main thread allowed here for test simplicity
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        // Get the DAO we want to test
        dao = db.quoteDao()
    }

    @After
    fun closeDb() = db.close()

    /**
     * Insert two quotes, soft-delete one, and verify observeAll() excludes soft-deleted rows.
     */
    @Test
    fun insert_and_observeAll_excludes_soft_deleted() = runBlocking {
        val id1 = dao.upsert(QuoteEntity(text = "A", author = "X", isFavorite = false))
        val id2 = dao.upsert(QuoteEntity(text = "B", author = "Y", isFavorite = true))

        dao.softDelete(id1) // mark first as deleted

        val list = dao.observeAll().first()
        assertEquals(1, list.size)
        assertEquals(id2, list.first().id)
    }

    /**
     * Verify search() matches across text, author, and tagsCsv (LIKE queries).
     * Note: We pass %pattern% to leverage LIKE in the DAO.
     */
    @Test
    fun search_matches_text_author_and_tags() = runBlocking {
        dao.upsert(
            QuoteEntity(
                text = "Discipline equals freedom",
                author = "Jocko",
                tagsCsv = "discipline,focus"
            )
        )
        dao.upsert(
            QuoteEntity(
                text = "Mountains by stones",
                author = "Confucius",
                tagsCsv = "patience"
            )
        )

        val byText = dao.search("%discipline%").first()
        val byAuthor = dao.search("%confu%").first()
        val byTag = dao.search("%focus%").first()

        assertTrue(byText.any { it.text.contains("discipline", ignoreCase = true) })
        assertTrue(byAuthor.any { it.author?.contains("confu", ignoreCase = true) == true })
        assertTrue(byTag.any { (it.tagsCsv ?: "").contains("focus", ignoreCase = true) })
    }

    /**
     * Verify filterByTag() finds partial matches inside tagsCsv via LIKE.
     */
    @Test
    fun filterByTag_uses_like_on_tagsCsv() = runBlocking {
        dao.upsert(QuoteEntity(text = "M", author = "A", tagsCsv = "habits,excellence"))
        dao.upsert(QuoteEntity(text = "N", author = "B", tagsCsv = "discipline"))

        val results = dao.filterByTag("%habit%").first()
        assertEquals(1, results.size)
        assertTrue(results.first().tagsCsv?.contains("habit", ignoreCase = true) == true)
    }

    /**
     * Verify pickRandomFavorite() returns only favorites (or null if no favorites exist).
     * Re-check multiple times because ORDER BY RANDOM().
     */
    @Test
    fun pickRandomFavorite_only_returns_favorites() = runBlocking {
        dao.upsert(QuoteEntity(text = "X", author = "A", isFavorite = false))
        dao.upsert(QuoteEntity(text = "Y", author = "B", isFavorite = true))

        repeat(10) {
            val q = dao.pickRandomFavorite()
            assertTrue(q == null || q.isFavorite)
        }
    }
}
