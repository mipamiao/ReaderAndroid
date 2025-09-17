package com.mipa.readerandroid.model.dto

data class ChapterDto(
    var authorId: String? = null,
    var bookId: String? = null,
    var title: String? = null,
    var content: String? = null,
    var order: Int? = null,
)