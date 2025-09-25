package com.mipa.readerandroid.repository.nao

import com.mipa.readerandroid.model.dto.BookListDto
import com.mipa.readerandroid.model.feature.Book
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchNao {

    @GET("${Domain.searchPublic}/books")
    suspend fun searchByKeyword(
        @Query("keyword") keyword: String,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): ApiResponse<BookListDto>
}