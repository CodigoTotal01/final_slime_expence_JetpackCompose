package com.nikolovlazar.goodbyemoney.features.auth.domain.repository

import com.nikolovlazar.goodbyemoney.features.auth.domain.models.User

abstract class AuthRepository {
    @Throws(Exception::class)
    abstract suspend fun login(email: String, password: String): User

    @Throws(Exception::class)
    abstract suspend fun register(email: String, password: String, fullName: String): User

    @Throws(Exception::class)
    abstract suspend fun checkAuthStatus(token: String): User
}
