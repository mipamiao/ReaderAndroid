package com.mipa.readerandroid.view.composedata

import android.content.Intent
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.model.feature.UserProfile
import com.mipa.readerandroid.view.activity.MainActivity
import com.mipa.readerandroid.view.activity.RegisterAndLoginActivity

object MePageCD : ViewModel() {
    private val _userProfile = mutableStateOf(UserProfile())
    val userProfile:State<UserProfile> = _userProfile

    fun isLogin(): Boolean {
        return _userProfile.value.userId != null
    }

    fun updateUserProfile(userProfile: UserProfile){
        _userProfile.value = userProfile
    }

    var mainActivity: MainActivity? = null
    var registerAndLoginActivity: RegisterAndLoginActivity? = null

    fun onClickUserProfile(naviController: NavHostController){
        if(!isLogin()){
            naviController.navigate(ConstValue.ROUTER_LOGIN)
        }else {
            naviController.navigate(ConstValue.ROUTER_ME_DETAIL)
        }
    }

    fun quitLogin(naviController: NavHostController){
        updateUserProfile(UserProfile())
        naviController.navigate(ConstValue.ROUTER_MEPAGE)
    }
}