package com.mipa.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class UserProfileViewModel: ViewModel() {
    // 私有状态，外部只读
    private val _isLogin = mutableStateOf(false)
    val isLogin: State<Boolean> get() = _isLogin

    fun login(){
        _isLogin.value = !_isLogin.value
    }
}