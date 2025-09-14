package com.mipa.readerandroid.model.dto

import com.mipa.readerandroid.model.feature.Book


data class BookListDto(
    val books: List<Book>,
    val total: Int,
    val pageNumber: Int,
    val pageSize: Int
)