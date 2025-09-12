package com.mipa.readerandroid.model.feature

data class Chapter(
    val id: String,
    val title: String,
    val wordCount: Int,
    val isVip: Boolean = false,
    val isLocked: Boolean = false
)