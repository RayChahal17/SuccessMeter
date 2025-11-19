package com.example.successmeter.domain.model

import java.time.Instant
import java.time.LocalDate

/**
 * Flat domain model for a Goal.
 *
 * This is how the Goals feature "thinks" about a goal:
 * - Uses LocalDate for targetDate (calendar date).
 * - Uses Instant for createdAt / completedAt / updatedAt.
 * - No children here – the tree use case will build GoalNode from a list of these.
 */


data class Goal(
    val id: Long,                  // DB id (from Room / Firestore later)
    val chiefAimId: Long,          // which ChiefAim this belongs to
    val parentGoalId: Long?,       // null if directly under the ChiefAim

    val title: String,             // short human-readable name
    val description: String?,      // optional longer text

    val targetDate: LocalDate,     // real calendar date (tree + "remaining days" use this)

    val isCompleted: Boolean,      // true if done
    val completedAt: Instant?,     // when it was completed (null if not yet)

    val isArchived: Boolean,       // soft-delete flag

    val createdAt: Instant,        // when it was created
    val updatedAt: Instant?,       // last edit time (null if never updated)

    val orderIndex: Int,           // manual order among siblings in the tree
)