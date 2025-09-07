package com.mipa.readerandroid.repository.nao

data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val data: T? = null
) {
    fun isSuccess(): Boolean {
        return code == 200
    }
}

data class Token<T>(
    val token: String,
    val data: T
)