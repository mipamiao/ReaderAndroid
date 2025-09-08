package com.mipa.readerandroid.view.composedata

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mipa.readerandroid.model.dto.UserLoginRequest
import com.mipa.readerandroid.model.dto.UserRegisterRequest
import com.mipa.readerandroid.service.UserService

// 改为 data class 以支持属性变化观察
class RegisterPageCD : ViewModel() {
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    fun register(){
        val res = UserService.register(UserRegisterRequest(userName =  name, email = email, password =  password, role = "ROLE_READER"))
        Log.e("TAG", "register: $res", )
    }
}

class LoginPageCD : ViewModel() {
    var name by mutableStateOf("")
    var password by mutableStateOf("")

    fun login(){
        val res = UserService.login(UserLoginRequest(userName = name, password = password))
        Log.e("TAG", "register: $res", )
    }
}
