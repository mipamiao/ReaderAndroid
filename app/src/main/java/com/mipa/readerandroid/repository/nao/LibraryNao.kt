package com.mipa.readerandroid.repository.nao

import com.mipa.readerandroid.model.dto.LibraryRequestDto
import com.mipa.readerandroid.model.feature.Library
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface LibraryNao {
    @GET("${Domain.LibraryPrivate}/list")
    suspend fun listLibrary(
        @Header("Authorization") token: String,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): ApiResponse<List<Library>>

    @POST("${Domain.LibraryPrivate}/add")
    suspend fun addLibrary(
        @Header("Authorization") token: String,
        @Body dto: LibraryRequestDto
    ): ApiResponse<Boolean>

    @POST("${Domain.LibraryPrivate}/update")
    suspend fun updateLibrary(
        @Header("Authorization") token: String,
        @Body dto: LibraryRequestDto
    ): ApiResponse<Boolean>

    @DELETE("${Domain.LibraryPrivate}/remove")
    suspend fun deleteLibrary(
        @Header("Authorization") token: String,
        @Query("bookId") bookId: String
    ): ApiResponse<Boolean>
}