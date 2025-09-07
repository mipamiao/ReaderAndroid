package com.mipa.readerandroid.repository.nao

import com.mipa.readerandroid.model.dto.UserInfoResponse
import com.mipa.readerandroid.model.dto.UserLoginRequest
import com.mipa.readerandroid.model.dto.UserLoginResponse
import com.mipa.readerandroid.model.dto.UserRegisterRequest
import retrofit2.http.GET
import retrofit2.http.Header

import retrofit2.http.POST
import retrofit2.http.Query

interface UserNao {

    @POST("${Domain.authPublic}/login")
    fun login(userLoginRequest: UserLoginRequest): ApiResponse<Token<UserLoginResponse>>

    @POST("${Domain.authPublic}/register")
    fun register(userRegisterRequest: UserRegisterRequest): ApiResponse<Boolean>

    @GET("${Domain.authPrivate}/profile")
    fun getUserProfile(
        @Header("Authorization") token: String,
        @Query("userId") useId: String
    ): ApiResponse<UserInfoResponse>
}
