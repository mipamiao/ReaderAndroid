package com.mipa.readerandroid.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

object UserViewModel:ViewModel() {
    var userId by mutableStateOf("")
    var userName by mutableStateOf("")
    var email by mutableStateOf("")
    var role by mutableStateOf("")
    var createdAt by mutableStateOf("")
}