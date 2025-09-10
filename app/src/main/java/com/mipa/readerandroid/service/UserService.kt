package com.mipa.readerandroid.service

import android.nfc.Tag
import android.util.Log
import com.mipa.readerandroid.base.MyApp
import com.mipa.readerandroid.base.SharePreferenceMgr
import com.mipa.readerandroid.model.dto.UserLoginRequest
import com.mipa.readerandroid.model.dto.UserLoginResponse
import com.mipa.readerandroid.model.dto.UserRegisterRequest
import com.mipa.readerandroid.model.feature.UserProfile
import com.mipa.readerandroid.repository.AppDatabase
import com.mipa.readerandroid.repository.AppNet
import com.mipa.readerandroid.repository.nao.ApiResponse
import com.mipa.readerandroid.repository.nao.Token
import com.mipa.readerandroid.repository.nao.TokenMgr
import com.mipa.readerandroid.service.converter.UserEntityConverter
import com.mipa.readerandroid.service.converter.UserProfileConverter
import retrofit2.Call

//todo 将其改造为mao，dao，nao的层次结构，外部无需分开调用，由service本身决定
object UserService {
    const val TAG = "UserService"

    val userDao by lazy {
        AppDatabase.getInstance(MyApp.getInstance().getContext()).userDao()
    }

    val userNao by lazy {
        AppNet.userNao()
    }

    fun saveUserProfile(userProfile: UserProfile): Boolean{
        userDao.deleteAll()
        userDao.insert(UserEntityConverter.fromUserProfile(userProfile))
        return true
    }

    /**
     * 如果没有用户处在登录态，则返回null
     */
    fun loadUserProfile(): UserProfile? {
        val users = userDao.getAll()
        if (users.isEmpty()) return null
        else {
            return UserProfileConverter
                .fromUserEntity(userDao.getAll().get(0))
        }
    }

    fun clearUserProfile(){
        userDao.deleteAll()
    }

    fun login(userLoginRequest: UserLoginRequest): UserProfile? {
        try {
            val call = userNao.login(userLoginRequest)
            val response = call.execute()
            if (response.isSuccessful) {
                response.body()?.let {
                    if (it.isSuccess()) {
                        it.data?.let { data ->
                            TokenMgr.setToken(data.token)
                            return UserProfileConverter.fromUserLoginResponse(data.userInfo)
                        }
                    }
                }
            }
            return null
        } catch (e: Exception) {
            Log.e(TAG, "login: $e")
            return null
        }
    }

    fun register(userRegisterRequest: UserRegisterRequest): Boolean {
        try {
            val call = userNao.register(userRegisterRequest)
            val response = call.execute()
            response.body()?.let {
                return it.isSuccess()
            }
            return false
        } catch (e: Exception) {
            Log.e(TAG, "register: $e")
            return false
        }
    }

    fun getUserInfo(userId: String): UserProfile? {
        try {
            val call = userNao.getUserProfile(TokenMgr.getToken(), userId)
            val response = call.execute()
            if (response.isSuccessful) {
                response.body()?.let {
                    if (it.isSuccess() && it.data != null) {
                        return UserProfileConverter.fromuUserInfoResponse(it.data)
                    }
                }
            }
            return null
        } catch (e: Exception) {
            Log.e(TAG, "getUserInfo: $e")
            return null
        }
    }

}