package com.mipa.readerandroid.view.composedata

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.MutableState
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
import com.mipa.readerandroid.view.compose.AuthScreen
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Optional

class RegisterPageCD : ViewModel() {
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    @SuppressLint("CheckResult")
    fun register(naviController: NavHostController) {

        Observable.fromCallable {
            val res = UserService.register(
                UserRegisterRequest(
                    userName = name,
                    email = email,
                    password = password,
                    role = "ROLE_READER"
                )
            )
            Log.e("TAG", "register: ${res.toString()}")
            res
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { res ->
                if (res) naviController.navigate(ConstValue.ROUTER_LOGIN)
            }
    }

    fun switchToLogin(naviController: NavHostController){
        naviController.navigate(ConstValue.ROUTER_LOGIN){
            launchSingleTop = true
        }
    }
}

class LoginPageCD : ViewModel() {
    var name by mutableStateOf("")
    var password by mutableStateOf("")

    @SuppressLint("CheckResult")
    fun login(naviController: NavHostController) {

        Observable.fromCallable {
            val res = UserService.login(UserLoginRequest(userName = name, password = password))
            Optional.ofNullable(res)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { res ->
                res.ifPresent {
                    MePageCD.updateUserProfile(res.get())
                    naviController.navigate(ConstValue.ROUTER_MEPAGE) {
                        launchSingleTop = true
                    }
                }
            }
    }

    fun switchToRegister(naviController: NavHostController){
        naviController.navigate(ConstValue.ROUTER_REGISTER){
            launchSingleTop = true
        }
    }
}
