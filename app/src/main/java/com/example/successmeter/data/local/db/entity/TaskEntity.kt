package com.example.successmeter.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.successmeter.domain.model.EisenhowerQuadrant

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val goalId: Long,
    val title: String,
    val description: String? = null,
    val quadrant: EisenhowerQuadrant,
    val isCatalogue: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long? = null,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val isArchived: Boolean = false,
)
