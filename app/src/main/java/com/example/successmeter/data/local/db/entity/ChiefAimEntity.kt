package com.example.successmeter.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.successmeter.domain.model.ChiefAimRank

@Entity(tableName = "chief_aims")
data class ChiefAimEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String? = null,
    val rank: ChiefAimRank,
    val createdAt: Long,
    val updatedAt: Long? = null,
    val isArchived: Boolean = false
    )

