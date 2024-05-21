package com.nikolovlazar.goodbyemoney.features.auth.domain.datasource

import com.nikolovlazar.goodbyemoney.features.auth.domain.models.User

abstract class AuthDataSource {
    @Throws(Exception::class)
    abstract suspend fun login(email: String, password: String): User

    @Throws(Exception::class)
    abstract suspend fun register(email: String, password: String, fullName: String): User

    @Throws(Exception::class)
    abstract suspend fun checkAuthStatus(token: String): User
}