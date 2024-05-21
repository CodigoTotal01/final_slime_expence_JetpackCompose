package com.nikolovlazar.goodbyemoney.features.auth.infrastructure.mappers

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nikolovlazar.goodbyemoney.features.auth.domain.models.User


class UserMapper {
    companion object {
        fun userJsonToEntity(json: Map<String, Any>): User {
            return User(
                id = json["id"] as String,
                email = json["email"] as String,
                fullName = json["fullName"] as String,
                roles = (json["roles"] as List<*>).map { it as String },
                token = json["token"] as String
            )
        }
    }
}
