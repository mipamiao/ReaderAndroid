package com.mipa.readerandroid.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey
    val bookId: Int,
    val title: String,
    val authorName: String,
    val coverImage: String? = null,
    val description: String,
    val category: String,
    val tags: List<String>,
    val chaptersCount: Int,
    val createdAt: String,
    val updatedAt: String,
    val price: Double,
    val rating: Float
)