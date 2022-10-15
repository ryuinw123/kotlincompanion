package com.example.kmitlcompanion.cache.mapper

import com.example.kmitlcompanion.cache.entities.User
import com.example.kmitlcompanion.data.model.UserData
import javax.inject.Inject

class UserMapper @Inject constructor(

) {
    fun mapToData(it: User): UserData {
        return UserData(
            token = it.token,
            email = it.email
        )
    }

    fun mapToEntity(it: UserData): User {
        return User(
            token = it.token,
            email = it.email
        )
    }
}