package com.mipa.readerandroid.repository.nao

import com.mipa.readerandroid.model.dto.UserInfoResponse
import com.mipa.readerandroid.model.dto.UserLoginRequest
import com.mipa.readerandroid.model.dto.UserLoginResponse
import com.mipa.readerandroid.model.dto.UserRegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header

import retrofit2.http.POST
import retrofit2.http.Query

interface UserNao {

    @POST("${Domain.authPublic}/login")
    fun login(@Body userLoginRequest: UserLoginRequest): Call<ApiResponse<Token<UserLoginResponse>>>

    @POST("${Domain.authPublic}/register")
    fun register(@Body userRegisterRequest: UserRegisterRequest): Call<ApiResponse<Boolean>>

    @GET("${Domain.authPrivate}/profile")
    fun getUserProfile(
        @Header("Authorization") token: String,
        @Query("userId") useId: String
    ): Call<ApiResponse<UserInfoResponse>>
}
