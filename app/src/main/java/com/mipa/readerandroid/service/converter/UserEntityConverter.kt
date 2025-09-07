package com.mipa.readerandroid.service.converter

import com.mipa.readerandroid.model.UserEntity
import com.mipa.readerandroid.model.feature.UserProfile

class UserEntityConverter {
    companion object{
        fun fromUserProfile(userProfile: UserProfile): UserEntity{
            return   UserEntity().apply {
                userId = userProfile.userId
                userName = userProfile.userName
                email = userProfile.email
                createdAt = userProfile.createdAt
                role = userProfile.role
            }
        }
    }
}