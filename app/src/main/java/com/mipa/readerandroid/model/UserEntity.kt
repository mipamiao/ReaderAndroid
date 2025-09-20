package com.mipa.readerandroid.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity(tableName = "users")
class UserEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null

    @ColumnInfo(name = "user_id")
    var userId: String? = null

    @ColumnInfo(name = "user_name")
    var userName: String? = null

    @ColumnInfo(name = "email")
    var email: String? = null

    @ColumnInfo(name = "role")
    var role: String? = null // reader / author

    @ColumnInfo(name = "created_at")
    var createdAt: String? = null

    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String? = null
}