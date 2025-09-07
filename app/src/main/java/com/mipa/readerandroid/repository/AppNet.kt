package com.mipa.readerandroid.repository

import com.mipa.readerandroid.repository.nao.UserNao
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object AppNet {
    private const val BASE_URL = "localhost:8080/"

    // 创建日志拦截器
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // 打印完整日志
    }

    // 创建 OkHttpClient
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS) // 连接超时
        .readTimeout(30, TimeUnit.SECONDS)    // 读取超时
        .writeTimeout(30, TimeUnit.SECONDS)   // 写入超时
        .build()

    // 创建 Retrofit 实例
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

//    // 创建 API 服务实例
//    val userProfileNetRepo: UserProfileNetRepo by lazy {
//        retrofit.create(UserProfileNetRepo::class.java)
//    }

    fun userNao(): UserNao{
        return retrofit.create(UserNao::class.java)
    }

}