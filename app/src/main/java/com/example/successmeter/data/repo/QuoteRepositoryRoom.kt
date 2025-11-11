package com.example.successmeter.data.repo

import com.example.successmeter.data.local.db.dao.QuoteDao
import com.example.successmeter.data.local.db.entity.QuoteEntity
import com.example.successmeter.domain.repo.QuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuoteRepositoryRoom @Inject constructor(
    private val dao: QuoteDao
) : QuoteRepository {
    override fun observeRecent(limit: Int): Flow<List<QuoteEntity>> = dao.observeRecent(limit)
    override suspend fun upsert(quote: QuoteEntity) = dao.upsert(quote)
}



