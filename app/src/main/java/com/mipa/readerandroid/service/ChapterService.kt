package com.mipa.readerandroid.service

import com.mipa.readerandroid.model.dto.ChapterDto
import com.mipa.readerandroid.model.feature.Chapter
import com.mipa.readerandroid.model.feature.ChapterInfo
import com.mipa.readerandroid.repository.AppNet
import com.mipa.readerandroid.repository.nao.TokenMgr

object ChapterService {

    private val chapterNao by lazy {
        AppNet.chapterNao()
    }

    suspend fun getChapterInfo(bookId: String, chapterId: String): ChapterInfo? {
        val res = chapterNao.getChapterInfo(bookId, chapterId)
        if (res.isSuccess()) {
            res.data?.let {
                return it
            }
        }
        return null
    }

    suspend fun getChapter(bookId: String, chapterId: String): Chapter? {
        val res = chapterNao.getChapter(bookId, chapterId)
        if (res.isSuccess()) {
            res.data?.let {
                return it
            }
        }
        return null
    }

    suspend fun listChapters(bookId: String): List<ChapterInfo> {
        val res = chapterNao.listChapter(bookId)
        if(res.isSuccess()){
            res.data?.let {
                return it
            }
        }
        return emptyList<ChapterInfo>()
    }

    suspend fun addChapter(chapterDto: ChapterDto): ChapterInfo?{
        val res = chapterNao.addChapter(TokenMgr.getTokenWithPrefix(), chapterDto)
        if(res.isSuccess()){
            res.data?.let {
                return it
            }
        }
        return null
    }

    suspend fun updateChapter(chapterDto: ChapterDto, chapterId: String): Boolean {
        val res = chapterNao.updateChapter(TokenMgr.getTokenWithPrefix(), chapterDto, chapterId)
        return res.isSuccess()
    }

    suspend fun removeChapter(bookId: String, chapterId: String): Boolean {
        val res = chapterNao.removeChapter(TokenMgr.getTokenWithPrefix(), bookId, chapterId)
        return res.isSuccess()
    }

}