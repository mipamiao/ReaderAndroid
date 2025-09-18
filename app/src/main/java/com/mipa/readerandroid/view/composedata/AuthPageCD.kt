package com.mipa.readerandroid.view.composedata

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.model.dto.UserLoginRequest
import com.mipa.readerandroid.model.dto.UserRegisterRequest
import com.mipa.readerandroid.service.UserService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Optional

class AuthPageCD: ViewModel() {

    var isLoginScreen = mutableStateOf(true)

    var register_name = mutableStateOf("")
    var register_email = mutableStateOf("")
    var register_password = mutableStateOf("")

    @SuppressLint("CheckResult")
    fun register(naviController: NavHostController) {
        Observable.fromCallable {
            //ConstValue.delay()
            val res = UserService.register(
                UserRegisterRequest(
                    userName = register_name.value,
                    email = register_email.value,
                    password = register_password.value,
                    role = "ROLE_READER"
                )
            )
            Log.e("TAG", "register: ${res.toString()}")
            res
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { res ->
                if (res) isLoginScreen.value = true
            }
    }

    fun switchToLogin(){
        isLoginScreen.value = true
    }

    var login_name = mutableStateOf("")
    var login_password = mutableStateOf("")

    @SuppressLint("CheckResult")
    fun login(naviController: NavHostController) {

        Observable.fromCallable {
            //ConstValue.delay()
            val res = UserService.login(
                UserLoginRequest(
                    userName = login_name.value,
                    password = login_password.value
                )
            )
            Optional.ofNullable(res)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { res ->
                res.ifPresent {
                    MePageCD.updateUserProfile(res.get())
                    naviController.navigate(ConstValue.ROUTER_MEPAGE) {
                        launchSingleTop = true
                        popUpTo(0)
                    }
                }
            }
    }

    fun switchToRegister(){
        isLoginScreen.value = false
    }

}