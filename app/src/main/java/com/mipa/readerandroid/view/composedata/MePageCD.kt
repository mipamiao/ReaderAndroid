package com.mipa.readerandroid.view.composedata

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.BaseCD
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.base.combineHost
import com.mipa.readerandroid.model.feature.UserProfile
import com.mipa.readerandroid.service.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MePageCD : BaseCD() {
    private val _userProfile = mutableStateOf(UserProfile())
    val userProfile:State<UserProfile> = _userProfile

    var avatarUrl = mutableStateOf<String?>(null)
        private set

    fun isLogin(): Boolean {
        return _userProfile.value.userId != null
    }

    fun updateUserProfile(userProfile: UserProfile){
        _userProfile.value = userProfile
       avatarUrl.value = userProfile.avatarUrl?.combineHost()
    }

    fun onClickUserProfile(naviController: NavHostController){
        if(!isLogin()){
            CDMap.put(AuthPageCD())
            naviController.navigate(ConstValue.ROUTER_AUTH_PAGE)
        }else {
            //CDMap.put(MeDetailCD())
            naviController.navigate(ConstValue.ROUTER_ME_DETAIL)
        }
    }

    fun onClickMyBooksItem(naviController: NavHostController){
        CDMap.put(MyBookPageCD())
        naviController.navigate(ConstValue.ROUTER_MY_BOOKS){
            launchSingleTop = true
        }
    }

    fun quitLogin(naviController: NavHostController) {
        updateUserProfile(UserProfile())
        naviController.navigate(ConstValue.ROUTER_MEPAGE) {
            launchSingleTop = true
            popUpTo(0)
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                UserService.clearUserProfile()
            }
        }
    }

    fun uploadAvatar(uri: Uri) {
        viewModelScope.launch {
            val url = withContext(Dispatchers.IO){
                UserService.uploadAvatar(uri)
            }
            ConstValue.showOPstate(url != null)
            avatarUrl.value = url?.combineHost()?:""
        }
    }

}