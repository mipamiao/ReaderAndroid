package com.mipa.readerandroid.model.dto

data class UserRegisterRequest(
    val userName: String,
    val password: String,
    val email: String,
    val role: String
)