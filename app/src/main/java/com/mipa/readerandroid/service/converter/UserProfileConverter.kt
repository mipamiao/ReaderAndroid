package com.mipa.readerandroid.service.converter

import com.mipa.readerandroid.model.UserEntity
import com.mipa.readerandroid.model.dto.UserInfoDto
import com.mipa.readerandroid.model.dto.UserInfoResponse
import com.mipa.readerandroid.model.dto.UserLoginResponse
import com.mipa.readerandroid.model.feature.UserProfile

class UserProfileConverter {
    companion object{
        fun fromUserEntity(userEntity: UserEntity): UserProfile{
            return   UserProfile().apply {
                userId = userEntity.userId
                userName = userEntity.userName
                email = userEntity.email
                createdAt = userEntity.createdAt
                role = userEntity.role
                avatarUrl = userEntity.avatarUrl
            }
        }

        fun fromUserLoginResponse(userInfoDto: UserInfoDto): UserProfile {
            return   UserProfile().apply {
                userId = userInfoDto.userId
                userName = userInfoDto.userName
                email = userInfoDto.email
                createdAt = userInfoDto.createdAt
                role = userInfoDto.role
                avatarUrl = userInfoDto.avatarUrl
            }
        }

        fun fromuUserInfoResponse(userInfoResponse: UserInfoResponse): UserProfile{
            return   UserProfile().apply {
                userId = userInfoResponse.userId
                userName = userInfoResponse.userName
                email = userInfoResponse.email
                createdAt = userInfoResponse.createdAt
                role = userInfoResponse.role
                avatarUrl = userInfoResponse.avatarUrl
            }
        }
    }
}