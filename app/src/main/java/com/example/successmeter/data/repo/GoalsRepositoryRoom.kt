package com.example.successmeter.data.repo

import com.example.successmeter.data.local.db.dao.ChiefAimDao
import com.example.successmeter.data.local.db.dao.GoalDao
import com.example.successmeter.data.mapper.toDomain
import com.example.successmeter.data.mapper.toEntity
import com.example.successmeter.domain.model.ChiefAim
import com.example.successmeter.domain.model.Goal
import com.example.successmeter.domain.repo.GoalsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject

class GoalsRepositoryRoom @Inject constructor(
    private val chiefAimDao: ChiefAimDao,
    private val goalDao: GoalDao,
) : GoalsRepository{
    override fun observeChiefAims(): Flow<List<ChiefAim>> {
        return  chiefAimDao.observeAllChiefAims()
            .map{
                entityList -> entityList.map { entity -> entity.toDomain() }
            }
    }

    override fun observeGoalsForChiefAim(chiefAimId: Long): Flow<List<Goal>> {
        return goalDao.observeGoalsForChiefAim(chiefAimId)
            .map{
                entityList -> entityList.map { entity -> entity.toDomain() }
            }
    }

    override suspend fun upsertChiefAim(aim: ChiefAim): Long {
        return chiefAimDao.upsertChiefAim(aim.toEntity())
    }

    override suspend fun upsertGoal(goal: Goal): Long {
        return goalDao.upsertGoal(goal.toEntity())
    }

    override suspend fun markGoalCompleted(goalId: Long, completedAt: Instant) {
        return goalDao.markGoalCompleted(goalId, completedAt.toEpochMilli())
    }

    override suspend fun archiveGoal(goalId: Long) {
        return goalDao.archiveGoal(goalId)
    }

}