package com.mipa.readerandroid.repository.nao

import androidx.datastore.preferences.protobuf.Api
import com.mipa.readerandroid.model.dto.BookmarkInfoDto
import com.mipa.readerandroid.model.dto.BookmarkRequestDto
import com.mipa.readerandroid.model.feature.Bookmark
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface BookmarkNao {

    @POST(value = "${Domain.bookmarkPrivate}/add")
    suspend fun addBookmark(
        @Header("Authorization") token: String,
        @Body dto: BookmarkRequestDto
    ): ApiResponse<Bookmark>

    @POST(value = "${Domain.bookmarkPrivate}/update")
    suspend fun updateBookmark(
        @Header("Authorization") token: String,
        @Body dto: BookmarkRequestDto,
        @Query("bookmarkId") bookmarkId: String
    ): ApiResponse<Boolean>

    @GET(value = "${Domain.bookmarkPrivate}/list-all")
    suspend fun listAllBookmark(
        @Header("Authorization") token: String,
        @Query("bookId") bookId: String
    ): ApiResponse<List<Bookmark>>

    @DELETE(value = "${Domain.bookmarkPrivate}/del")
    suspend fun delBookmark(
        @Header("Authorization") token: String,
        @Query("bookmarkId") bookmarkId: String
    ): ApiResponse<Boolean>
}