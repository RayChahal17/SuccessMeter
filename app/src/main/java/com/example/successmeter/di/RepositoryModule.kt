package com.example.successmeter.di

import com.example.successmeter.data.local.db.dao.QuoteDao
import com.example.successmeter.data.repo.QuoteRepositoryRoom
import com.example.successmeter.domain.repo.QuoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides @Singleton
    fun provideQuoteRepository(dao: QuoteDao): QuoteRepository = QuoteRepositoryRoom(dao)
//Hilt can now “deliver” a DB, the DAO, and the repository anywhere we ask via @Inject.


}


