package com.mipa.readerandroid.repository.nao


import retrofit2.http.DELETE
import com.mipa.readerandroid.model.dto.BookListDto
import com.mipa.readerandroid.model.feature.Book
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface BookNao {

    @GET("${Domain.bookPublic}/list")
    suspend fun getBookList(
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("category") category: String?
    ): ApiResponse<BookListDto>

    @GET("${Domain.bookPivate}/my-books")
    suspend fun getMyBookList(
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Header("Authorization") token: String
    ): ApiResponse<BookListDto>

    @GET("${Domain.bookPublic}/get")
    suspend fun getBook(
        @Query("bookId") bookId: String
    ): ApiResponse<Book>

    @POST("${Domain.bookPivate}/add")
    suspend fun addBook(
        @Header("Authorization") token: String,
        @Body bookDto: Book
    ): ApiResponse<Boolean>

    @POST("${Domain.bookPivate}/update")
    suspend fun updateBook(
        @Header("Authorization") token: String,
        @Body bookDto: Book,
        @Query("bookId") bookId: String
    ): ApiResponse<Boolean>

    @DELETE("${Domain.bookPivate}/remove")
    @Headers("Content-Type: application/json")
    suspend fun removeBook(
        @Header("Authorization") token: String,
        @Query("bookId") bookId: String
    ): ApiResponse<Boolean>

    @Multipart
    @POST("${Domain.bookPivate}/update-cover-image")
    suspend fun updateCoverImg(
        @Header("Authorization") token: String,
        @Part bookCoverImg: MultipartBody.Part,
        @Query("bookId") bookId: String
    ): ApiResponse<String>
}