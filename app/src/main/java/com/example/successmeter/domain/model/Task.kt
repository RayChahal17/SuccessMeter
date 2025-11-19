package com.example.successmeter.domain.model

import java.time.Instant

data class Task(
    val id: Long,
    val goalId: Long,
    val title: String,
    val description: String?,
    val quadrant: EisenhowerQuadrant,
    val isCatalogue: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant?,
    val isCompleted: Boolean,
    val completedAt: Instant?,
    val isArchived: Boolean,
)
