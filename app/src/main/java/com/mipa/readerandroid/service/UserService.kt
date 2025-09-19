package com.mipa.readerandroid.service

import android.content.Context
import android.net.Uri
import android.nfc.Tag
import android.util.Log
import android.webkit.MimeTypeMap
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
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.BufferedSink
import retrofit2.Call
import java.io.File
import java.io.InputStream

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
            val call = userNao.getUserProfile(TokenMgr.getTokenWithPrefix(), userId)
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

    suspend fun uploadAvatar(uri: Uri): String? {
        val context = MyApp.getInstance().getContext()
        return try {
            val requestFile = createAvatarPart(context, uri)
            requestFile?.let {
                val response: ApiResponse<String> =
                    userNao.uploadAvatar(TokenMgr.getTokenWithPrefix(), it)
                if (response.isSuccess()) {
                    response.data
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "uploadAvatar: $e")
            null
        }
    }

    private fun createAvatarPart(context: Context, uri: Uri): MultipartBody.Part {
        val mimeType = context.contentResolver.getType(uri) ?: "image/*"
        val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: "jpg"
        val fileName = "avatar_image.$extension"

        // 一次性读入内存，避免 InputStream 被关闭
        val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            ?: return MultipartBody.Part.createFormData("avatar", fileName, ByteArray(0).toRequestBody())

        val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("avatar", fileName, requestBody)
    }


    private fun getFileExtension(context: Context, uri: Uri): String {
        val mimeType = context.contentResolver.getType(uri)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: ""
    }

}