package com.example.successmeter.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quote")
data class QuoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val author: String? = null,           // allow null if your schema did
    val tagsCsv: String? = null,
    val sourceUrl: String? = null,
    val isFavorite: Boolean = false,
    val uses: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null           // <-- must exist & be nullable
)


