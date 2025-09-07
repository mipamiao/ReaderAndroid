package com.mipa.readerandroid.repository.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mipa.readerandroid.model.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAll(): List<UserEntity>

    @Query("SELECT * FROM users WHERE user_id = :userId")
    fun getById(userId: Int): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserEntity)

    @Insert
    fun insertAll(vararg users: UserEntity)

    @Update
    fun update(user: UserEntity)

    @Delete
    fun delete(user: UserEntity)

    @Query("DELETE  from users")
    fun deleteAll()

    // 使用 Kotlin 协程 (需要 room-ktx)
    @Query("SELECT * FROM users")
    suspend fun getAllSuspend(): List<UserEntity>
}