package com.mipa.readerandroid.service

import com.mipa.readerandroid.model.dto.LibraryRequestDto
import com.mipa.readerandroid.model.feature.Library
import com.mipa.readerandroid.repository.AppNet
import com.mipa.readerandroid.repository.nao.LibraryNao
import com.mipa.readerandroid.repository.nao.TokenMgr

object LibraryService {
    private val libraryNao by lazy {
        AppNet.libraryNao()
    }

    suspend fun listLibrary(pageNumber: Int, pageSize: Int): List<Library> {
        val res = libraryNao.listLibrary(TokenMgr.getTokenWithPrefix(), pageNumber, pageSize)
        if (res.isSuccess())
            res.data?.let { return it }
        return emptyList()
    }

    suspend fun addLibrary(dto: LibraryRequestDto): Boolean {
        val res = libraryNao.addLibrary(TokenMgr.getTokenWithPrefix(), dto)
        return res.isSuccess()
    }

    suspend fun updateLibrary(dto: LibraryRequestDto): Boolean {
        val res = libraryNao.updateLibrary(TokenMgr.getTokenWithPrefix(), dto)
        return res.isSuccess()
    }

    suspend fun removeLibrary(bookId: String): Boolean {
        val res = libraryNao.deleteLibrary(TokenMgr.getTokenWithPrefix(), bookId)
        return res.isSuccess()
    }
}