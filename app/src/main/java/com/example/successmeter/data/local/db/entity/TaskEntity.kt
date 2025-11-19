package com.example.successmeter.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val goalId: Long,
    val title: String,
    val description: String,
    val quadrant: EisenhowerQuadrant,
    val isCatalogue: Boolean,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val isArchived: Boolean = false,
) {
    enum class EisenhowerQuadrant {
        URGENT_AND_IMPORTANT,
        URGENT_AND_NOT_IMPORTANT,
        NOT_URGENT_AND_IMPORTANT,
        NOT_URGENT_AND_NOT_IMPORTANT
    }
}
