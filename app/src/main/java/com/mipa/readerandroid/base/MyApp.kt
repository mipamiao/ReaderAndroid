package com.mipa.readerandroid.base

import android.app.Application
import android.content.Context
import androidx.room.Room.databaseBuilder
import com.facebook.stetho.Stetho
import com.mipa.readerandroid.repository.AppDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.ThreadPoolExecutor

class MyApp : Application() {
    private var database: AppDatabase? = null
    private val executor: ExecutorService? = null
    private val threadPoolExecutor: ThreadPoolExecutor? = null
    override fun onCreate() {
        super.onCreate()
        // 初始化全局对象
        database = databaseBuilder(this, AppDatabase::class.java, "ReaderDB")
            .fallbackToDestructiveMigration()
            .build()
        instance = this
        Stetho.initializeWithDefaults(this)
    }

    fun getDatabase(): AppDatabase? {
        return database
    }

    fun getContext(): Context {
        return this
    }

    companion object {
        private var instance: MyApp? = null
        fun getInstance(): MyApp = instance!!
    }
}