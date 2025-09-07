package com.mipa.readerandroid.model.dto

import java.time.LocalDateTime

data class UserInfoResponse(
    var userId: String,
    var userName: String,
    var email: String,
    var role: String,
    var createdAt: String
)