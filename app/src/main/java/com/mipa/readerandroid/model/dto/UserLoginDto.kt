package com.mipa.readerandroid.model.dto

data class UserLoginRequest(
    val userName:String,
    val password: String)

data class UserLoginResponse(
    val userId: String,
    val userName: String,
    val email: String,
    val role: String,
    val createdAt: String
)
