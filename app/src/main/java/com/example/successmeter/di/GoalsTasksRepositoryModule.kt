package com.example.successmeter.di

import com.example.successmeter.data.repo.GoalsRepositoryRoom
import com.example.successmeter.data.repo.TasksRepositoryRoom
import com.example.successmeter.domain.repo.GoalsRepository
import com.example.successmeter.domain.repo.TasksRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GoalsTasksRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGoalsRepository(
        impl: GoalsRepositoryRoom,
    ): GoalsRepository

    @Binds
    @Singleton
    abstract fun bindTasksRepository(
        impl: TasksRepositoryRoom,
    ): TasksRepository

}