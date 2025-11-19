package com.example.successmeter.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ChiefAim")
data class ChiefAimEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val rank: ChiefAimRank,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isArchived: Boolean = false
    ) {
    enum class ChiefAimRank {
        PRIMARY,
        SECONDARY,
        TERTIARY
    }
}

