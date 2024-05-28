package com.nikolovlazar.goodbyemoney.features.auth.infrastructure.repositories

import com.nikolovlazar.goodbyemoney.features.auth.domain.datasource.AuthDataSource
import com.nikolovlazar.goodbyemoney.features.auth.domain.models.User
import com.nikolovlazar.goodbyemoney.features.auth.domain.repository.AuthRepository
import com.nikolovlazar.goodbyemoney.features.auth.infrastructure.datasources.AuthDataSourceImpl

class AuthRepositoryImpl(private val dataSource: AuthDataSource = AuthDataSourceImpl()) : AuthRepository() {

    override suspend fun checkAuthStatus(token: String): User {
        return dataSource.checkAuthStatus(token)
    }

    override suspend fun login(email: String, password: String): User {
        print("loguqado con exito")
        return dataSource.login(email, password)
    }

    override suspend fun register(email: String, password: String, fullName: String): User {
        return dataSource.register(email, password, fullName)
    }
}
