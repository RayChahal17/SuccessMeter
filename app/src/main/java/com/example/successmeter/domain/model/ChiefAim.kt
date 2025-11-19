package com.example.successmeter.domain.model

import java.time.Instant

data class ChiefAim(
    val id: Long,
    val title: String,
    val description: String?,
    val rank: ChiefAimRank,
    val createdAt: Instant,
    val updatedAt: Instant?,
    val isArchived: Boolean,
    )
