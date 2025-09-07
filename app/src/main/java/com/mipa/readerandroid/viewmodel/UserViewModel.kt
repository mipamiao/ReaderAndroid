package com.mipa.readerandroid.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mipa.readerandroid.model.feature.UserProfile

class UserViewModel:ViewModel() {
    var userProfileState by mutableStateOf(UserProfile())
        private set


}