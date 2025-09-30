package com.mipa.readerandroid.service

import com.mipa.readerandroid.base.ThrowableOP
import com.mipa.readerandroid.model.dto.BookmarkInfoDto
import com.mipa.readerandroid.model.dto.BookmarkRequestDto
import com.mipa.readerandroid.model.feature.Bookmark
import com.mipa.readerandroid.repository.AppNet
import com.mipa.readerandroid.repository.nao.TokenMgr

object BookmarkService {

    val bookmarkNao by lazy {
        AppNet.bookmarkNao()
    }

    suspend fun addBookmark(dto: BookmarkRequestDto): Boolean? {
        return ThrowableOP.tryOP(default = false) {
            bookmarkNao.addBookmark(TokenMgr.getTokenWithPrefix(), dto).isSuccess()
        }
    }

    suspend fun updateBookmark(dto: BookmarkRequestDto, bookmarkId: String): Boolean {
        return ThrowableOP.tryOP(default = false) {
            bookmarkNao.updateBookmark(TokenMgr.getTokenWithPrefix(), dto, bookmarkId).isSuccess()
        }
    }

    suspend fun delBookmark(bookmarkId: String): Boolean {
        return ThrowableOP.tryOP(default = false) {
            bookmarkNao.delBookmark(TokenMgr.getTokenWithPrefix(), bookmarkId).isSuccess()
        }
    }

    suspend fun listAllBookmark(bookId: String): List<Bookmark> {
        return ThrowableOP.tryOP(default = emptyList()) {
            val res = bookmarkNao.listAllBookmark(TokenMgr.getTokenWithPrefix(), bookId)
            if (res.isSuccess())
                res.data?.let { return@tryOP it }
            return@tryOP emptyList<Bookmark>()
        }
    }

}