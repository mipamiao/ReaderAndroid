package com.mipa.readerandroid.model.feature

data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val coverUrl: String? = null,
    val description: String,
    val price: Double,
    val rating: Float
)