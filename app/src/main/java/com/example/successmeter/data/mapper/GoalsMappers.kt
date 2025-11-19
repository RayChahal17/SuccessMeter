package com.example.successmeter.data.mapper

import com.example.successmeter.data.local.db.entity.ChiefAimEntity
import com.example.successmeter.data.local.db.entity.GoalEntity
import com.example.successmeter.data.local.db.entity.TaskEntity
import com.example.successmeter.domain.model.ChiefAim
import com.example.successmeter.domain.model.ChiefAimRank
import com.example.successmeter.domain.model.Goal
import com.example.successmeter.domain.model.Task
import java.time.Instant
import java.time.LocalDate

/**
 * Map a Room GoalEntity into a domain Goal.
 */
fun GoalEntity.toDomain(): Goal {
    return Goal(
        id = id,
        chiefAimId = chiefAimId,
        parentGoalId = parentGoalId,
        title = title,
        description = description,
        // targetDate is stored as Long (epochDay) in the entity,
        // we convert it to LocalDate for the domain model.
        targetDate = targetDate.toLocalDateFromEpochDay(),
        isCompleted = isCompleted,
        // completedAt: Long? -> Instant?
        completedAt = completedAt?.toInstant(),
        isArchived = isArchived,
        // createdAt / updatedAt: Long -> Instant
        createdAt = createdAt.toInstant(),
        updatedAt = updatedAt?.toInstant(),
        orderIndex = orderIndex,
    )
}

/**
 * Map a domain Goal back into a Room GoalEntity.
 */
fun Goal.toEntity(): GoalEntity {
    return GoalEntity(
        id = id,
        chiefAimId = chiefAimId,
        parentGoalId = parentGoalId,
        title = title,
        description = description,
        // LocalDate -> Long epochDay
        targetDate = targetDate.toEpochDayLong(),
        // Instant -> Long millis
        createdAt = createdAt.toEpochMillis(),
        updatedAt = updatedAt?.toEpochMillis(),
        isCompleted = isCompleted,
        completedAt = completedAt?.toEpochMillis(),
        isArchived = isArchived,
        orderIndex = orderIndex,
    )
}

fun ChiefAimEntity.toDomain(): ChiefAim {
    return ChiefAim(
        id= id,
        title = title,
        description = description,
        rank = rank,
        createdAt = createdAt.toInstant(),
        updatedAt = updatedAt?.toInstant(),
        isArchived = isArchived,
    )
}

fun ChiefAim.toEntity(): ChiefAimEntity {
    return ChiefAimEntity(
        id = id,                      // 0L means "let Room autogenerate"
        title = title,
        description = description,
        rank = rank,
        createdAt = createdAt.toEpochMillis(),
        updatedAt = updatedAt?.toEpochMillis(),
        isArchived = isArchived,
    )
}

fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        goalId = goalId,
        title = title,
        description = description,
        quadrant = quadrant,              // same enum in both layers
        isCatalogue = isCatalogue,
        createdAt = createdAt.toInstant(),
        updatedAt = updatedAt?.toInstant(),
        isCompleted = isCompleted,
        completedAt = completedAt?.toInstant(),
        isArchived = isArchived,
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        goalId = goalId,
        title = title,
        description = description,
        quadrant = quadrant,
        isCatalogue = isCatalogue,
        createdAt = createdAt.toEpochMillis(),
        updatedAt = updatedAt?.toEpochMillis(),
        isCompleted = isCompleted,
        completedAt = completedAt?.toEpochMillis(),
        isArchived = isArchived,
    )
}


// Convert Long epoch millis to Instant
fun Long.toInstant(): Instant = Instant.ofEpochMilli(this)

// Convert Instant to Long epoch millis
fun Instant.toEpochMillis(): Long = this.toEpochMilli()

// Convert Long epochDay to LocalDate
fun Long.toLocalDateFromEpochDay(): LocalDate = LocalDate.ofEpochDay(this)

// Convert LocalDate to Long epochDay
fun LocalDate.toEpochDayLong(): Long = this.toEpochDay()
