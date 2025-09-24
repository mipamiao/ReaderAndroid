package com.mipa.readerandroid.repository.nao

import com.mipa.readerandroid.model.dto.ChapterDto
import com.mipa.readerandroid.model.dto.ChapterListDTO
import com.mipa.readerandroid.model.feature.Chapter
import com.mipa.readerandroid.model.feature.ChapterInfo
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ChapterNao {

    @GET("${Domain.chapterPublic}/info")
    suspend fun getChapterInfo(
        @Query("bookId") bookId: String,
        @Query("chapterId") chapterId: String
    ): ApiResponse<ChapterInfo>

    @GET("${Domain.chapterPublic}/get")
    suspend fun getChapter(
        @Query("bookId") bookId: String,
        @Query("chapterId") chapterId: String
    ): ApiResponse<Chapter>

    @GET("${Domain.chapterPublic}/list")
    suspend fun listChapter(
        @Query("bookId") bookId: String,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
    ): ApiResponse<ChapterListDTO>

    @POST("${Domain.chapterPrivate}/add")
    suspend fun addChapter(
        @Header("Authorization") token: String,
        @Body chapterDto: ChapterDto
    ): ApiResponse<ChapterInfo>

    @POST("${Domain.chapterPrivate}/update")
    suspend fun updateChapter(
        @Header("Authorization") token: String,
        @Body chapterDto: ChapterDto,
        @Query("chapterId") chapterId: String
    ): ApiResponse<Boolean>

    @DELETE("${Domain.chapterPrivate}/remove")
    suspend fun removeChapter(
        @Header("Authorization") token: String,
        @Query("bookId") bookId: String,
        @Query("chapterId") chapterId: String
    ): ApiResponse<Boolean>

}