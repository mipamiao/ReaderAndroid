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
    fun getBookList(
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("category") category: String?
    ): Call<ApiResponse<BookListDto>>

    @GET("${Domain.bookPivate}/my-books")
    fun getMyBookList(
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Header("Authorization") token: String
    ): Call<ApiResponse<BookListDto>>

    @GET("${Domain.bookPublic}/get")
    fun getBook(
        @Query("bookId") bookId: String
    ): Call<ApiResponse<Book>>

    @POST("${Domain.bookPivate}/add")
    fun addBook(
        @Header("Authorization") token: String,
        @Body bookDto: Book
    ): Call<ApiResponse<Boolean>>

    @POST("${Domain.bookPivate}/update")
    fun updateBook(
        @Header("Authorization") token: String,
        @Body bookDto: Book,
        @Query("bookId") bookId: String
    ): Call<ApiResponse<Boolean>>

    @DELETE("${Domain.bookPivate}/remove")
    @Headers("Content-Type: application/json")
    fun removeBook(
        @Header("Authorization") token: String,
        @Query("bookId") bookId: String
    ): Call<ApiResponse<Boolean>>

    @Multipart
    @POST("${Domain.bookPivate}/update-cover-image")
    suspend fun updateCoverImg(
        @Header("Authorization") token: String,
        @Part bookCoverImg: MultipartBody.Part,
        @Query("bookId") bookId: String
    ): ApiResponse<String>
}