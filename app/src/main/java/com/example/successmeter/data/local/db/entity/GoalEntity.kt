package com.example.successmeter.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Goal")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val chiefAimId: Long,
    val parentGoalId: Long?, //Null if directly under chief aim
    val title: String,
    val description: String,
    val targetDate: Long,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val isArchived: Boolean = false,
    val orderIndex: Int, // optional manual ordering within siblings

)
