package com.mipa.readerandroid.view.composedata

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.base.MyApp
import com.mipa.readerandroid.model.feature.UserProfile
import com.mipa.readerandroid.repository.AppNet
import com.mipa.readerandroid.service.UserService
import com.mipa.readerandroid.view.activity.MainActivity
import com.mipa.readerandroid.view.activity.RegisterAndLoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object MePageCD : ViewModel() {
    private val _userProfile = mutableStateOf(UserProfile())
    val userProfile:State<UserProfile> = _userProfile

    var avatarUrl = mutableStateOf<String?>(null)
        private set

    fun isLogin(): Boolean {
        return _userProfile.value.userId != null
    }

    fun updateUserProfile(userProfile: UserProfile){
        _userProfile.value = userProfile
        avatarUrl.value = userProfile.avatarUrl
    }

    fun onClickUserProfile(naviController: NavHostController){
        if(!isLogin()){
            naviController.navigate(ConstValue.ROUTER_AUTH_PAGE)
        }else {
            naviController.navigate(ConstValue.ROUTER_ME_DETAIL)
        }
    }

    fun onClickMyBooksItem(naviController: NavHostController){
        naviController.navigate(ConstValue.ROUTER_MY_BOOKS){
            launchSingleTop = true
        }
    }

    fun quitLogin(naviController: NavHostController){
        updateUserProfile(UserProfile())
        naviController.navigate(ConstValue.ROUTER_MEPAGE){
            launchSingleTop = true
            popUpTo(0)
        }
    }

    fun uploadAvatar(uri: Uri) {
        viewModelScope.launch {
            val url = withContext(Dispatchers.IO){
                UserService.uploadAvatar(uri)
            }
            ConstValue.showOPstate(url != null)
            avatarUrl.value = AppNet.BASE_URL + url
        }
    }
}