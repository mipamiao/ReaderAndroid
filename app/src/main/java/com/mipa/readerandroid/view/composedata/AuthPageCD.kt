package com.mipa.readerandroid.view.composedata

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.BaseCD
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.base.DialogController
import com.mipa.readerandroid.model.dto.UserLoginRequest
import com.mipa.readerandroid.model.dto.UserRegisterRequest
import com.mipa.readerandroid.service.UserService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Optional

class AuthPageCD: BaseCD() {

    var isLoginScreen = mutableStateOf(true)

    var register_name = mutableStateOf("")
    var register_email = mutableStateOf("")
    var register_password = mutableStateOf("")

    val dialogController = DialogController()

    fun register(naviController: NavHostController) {
        dialogController.show()
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                ConstValue.delay()
                UserService.register(
                    UserRegisterRequest(
                        userName = register_name.value,
                        email = register_email.value,
                        password = register_password.value,
                        role = "ROLE_READER"
                    )
                )
            }
            Log.e("TAG", "register: ${res.toString()}")
            if (res) switchToLogin()
            dialogController.dismiss()
        }
    }

    fun switchToLogin(){
        isLoginScreen.value = true
    }

    var login_name = mutableStateOf("")
    var login_password = mutableStateOf("")

    fun login(naviController: NavHostController) {
        dialogController.show()

        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                ConstValue.delay()
                UserService.login(
                    UserLoginRequest(
                        userName = login_name.value,
                        password = login_password.value
                    )
                )
            }
            dialogController.dismiss()
            res?.let {
                CDMap.get<MePageCD>().updateUserProfile(res)
                naviController.navigate(ConstValue.ROUTER_MEPAGE) {
                    launchSingleTop = true
                    popUpTo(0)
                }
                withContext(Dispatchers.IO) {
                    UserService.saveUserProfile(res)
                }
            }
            ConstValue.showOPstate(res != null)
        }
    }

    fun switchToRegister(){
        isLoginScreen.value = false
    }


}