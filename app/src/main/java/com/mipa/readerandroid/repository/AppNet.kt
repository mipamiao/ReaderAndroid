package com.mipa.readerandroid.repository

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.mipa.readerandroid.model.feature.Chapter
import com.mipa.readerandroid.repository.nao.BookNao
import com.mipa.readerandroid.repository.nao.ChapterNao
import com.mipa.readerandroid.repository.nao.LibraryNao
import com.mipa.readerandroid.repository.nao.UserNao
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object AppNet {
    private const val BASE_URL = "http://192.168.1.9:8080"

    // 创建日志拦截器
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // 打印完整日志
    }

    // 创建 OkHttpClient
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addNetworkInterceptor(StethoInterceptor())
        .connectTimeout(10, TimeUnit.SECONDS) // 连接超时
        .readTimeout(10, TimeUnit.SECONDS)    // 读取超时
        .writeTimeout(10, TimeUnit.SECONDS)   // 写入超时
        .build()

    // 创建 Retrofit 实例
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun userNao(): UserNao {
        return retrofit.create(UserNao::class.java)
    }

    fun bookNao(): BookNao {
        return retrofit.create(BookNao::class.java)
    }

    fun chapterNao(): ChapterNao {
        return retrofit.create(ChapterNao::class.java)
    }

    fun libraryNao(): LibraryNao {
        return retrofit.create(LibraryNao::class.java)
    }
}