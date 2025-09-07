package com.mipa.readerandroid.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mipa.readerandroid.model.UserEntity
import com.mipa.readerandroid.repository.dao.UserDao

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        // Singleton 模式，避免同时打开多个数据库实例
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ReaderDB" // 数据库文件名
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}