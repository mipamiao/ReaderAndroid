package com.mipa.readerandroid.model.dto

import androidx.compose.ui.semantics.Role
import java.time.LocalDateTime

data class UserLoginRequest(
    val userName:String,
    val passward: String)

data class UserLoginResponse(
    val userId: String,
    val userName: String,
    val email: String,
    val role: String,
    val createdAt: String
)
