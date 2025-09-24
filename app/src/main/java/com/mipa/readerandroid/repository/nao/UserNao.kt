package com.mipa.readerandroid.repository.nao

import com.mipa.readerandroid.model.dto.UserInfoResponse
import com.mipa.readerandroid.model.dto.UserLoginRequest
import com.mipa.readerandroid.model.dto.UserLoginResponse
import com.mipa.readerandroid.model.dto.UserRegisterRequest
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart

import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface UserNao {

    @POST("${Domain.authPublic}/login")
    suspend fun login(@Body userLoginRequest: UserLoginRequest): ApiResponse<UserLoginResponse>

    @POST("${Domain.authPublic}/register")
    fun register(@Body userRegisterRequest: UserRegisterRequest): Call<ApiResponse<Boolean>>

    @GET("${Domain.authPrivate}/profile")
    fun getUserProfile(
        @Header("Authorization") token: String,
        @Query("userId") useId: String
    ): Call<ApiResponse<UserInfoResponse>>

    @Multipart
    @POST("${Domain.authPrivate}/update-avatar")
    suspend fun uploadAvatar(
        @Header("Authorization") token: String,
        @Part avatar: MultipartBody.Part
    ): ApiResponse<String>
}
