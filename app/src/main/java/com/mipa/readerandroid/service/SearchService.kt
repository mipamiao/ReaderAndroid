package com.mipa.readerandroid.service

import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.repository.AppNet

object SearchService {
    private val searchNao by lazy {
        AppNet.searchNao()
    }

    suspend fun searchByKeyword(
        keyword: String,
        pageNumber: Int,
        pageSize: Int
    ): List<Book> {
        val res = searchNao.searchByKeyword(keyword, pageNumber, pageSize)
        if (res.isSuccess()) {
            res.data?.books?.let { return it }
        }
        return emptyList()
    }
}