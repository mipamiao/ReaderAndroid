package com.mipa.readerandroid.repository.nao

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileNao {
    @Multipart
    @POST("upload/single")
    suspend fun uploadSingleFile(
        @Part file: MultipartBody.Part
    ): ApiResponse<Boolean>
}