package com.example.successmeter.domain.repo

import com.example.successmeter.domain.model.ChiefAim
import com.example.successmeter.domain.model.Goal
import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface GoalsRepository {

    fun observeChiefAims(): Flow<List<ChiefAim>>

    fun observeGoalsForChiefAim(
        chiefAimId: Long,
    ): Flow<List<Goal>>

    suspend fun upsertChiefAim(aim: ChiefAim): Long

    suspend fun upsertGoal(goal: Goal): Long

    suspend fun markGoalCompleted(
        goalId: Long,
        completedAt: Instant,
    )

    suspend fun archiveGoal(goalId: Long)
}