package com.example.successmeter.data.repo

import com.example.successmeter.data.local.db.dao.ChiefAimDao
import com.example.successmeter.data.local.db.dao.GoalDao
import com.example.successmeter.data.mapper.toDomain
import com.example.successmeter.data.mapper.toEntity
import com.example.successmeter.data.mapper.toEpochMillis
import com.example.successmeter.domain.model.ChiefAim
import com.example.successmeter.domain.model.Goal
import com.example.successmeter.domain.repo.GoalsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject


/**
 * Room-backed implementation of GoalsRepository.
 *
 * Orchestrates:
 * - ChiefAimDao (for chief aims)
 * - GoalDao (for goals)
 * and maps entities <-> domain models.
 */
class GoalsRepositoryRoom @Inject constructor(
    private val chiefAimDao: ChiefAimDao,
    private val goalDao: GoalDao,
) : GoalsRepository {

    /**
     * Observe all non-archived chief aims.
     *
     * ChiefAimDao -> Flow<List<ChiefAimEntity>> -> Flow<List<ChiefAim>>
     */
    override fun observeChiefAims(): Flow<List<ChiefAim>> {
        return chiefAimDao
            .observeAllChiefAims()
            .map { entityList ->
                entityList.map { entity -> entity.toDomain() }
            }
    }

    /**
     * Observe all non-archived goals for a given chief aim.
     *
     * This will feed the Goal Tree use case later.
     */
    override fun observeGoalsForChiefAim(
        chiefAimId: Long,
    ): Flow<List<Goal>> {
        return goalDao
            .observeGoalsForChiefAim(chiefAimId)
            .map { entityList ->
                entityList.map { entity -> entity.toDomain() }
            }
    }

    /**
     * Insert or update a ChiefAim.
     */
    override suspend fun upsertChiefAim(aim: ChiefAim): Long {
        return chiefAimDao.upsertChiefAim(aim.toEntity())
    }

    /**
     * Insert or update a Goal.
     */
    override suspend fun upsertGoal(goal: Goal): Long {
        return goalDao.upsertGoal(goal.toEntity())
    }

    /**
     * Mark a goal as completed at a specific Instant.
     *
     * Domain gives us Instant; DAO expects Long millis.
     */
    override suspend fun markGoalCompleted(
        goalId: Long,
        completedAt: Instant,
    ) {
        goalDao.markGoalCompleted(
            goalId = goalId,
            completedAt = completedAt.toEpochMillis(),
        )
    }

    /**
     * Soft-delete (archive) a goal.
     */
    override suspend fun archiveGoal(goalId: Long) {
        goalDao.archiveGoal(goalId)
    }
}