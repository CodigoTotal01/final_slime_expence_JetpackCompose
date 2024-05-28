package com.nikolovlazar.goodbyemoney.features.auth.infrastructure.datasources

import com.nikolovlazar.goodbyemoney.features.auth.domain.datasource.AuthDataSource
import com.nikolovlazar.goodbyemoney.features.auth.domain.models.User
import com.nikolovlazar.goodbyemoney.features.auth.infrastructure.errors.CustomError
import com.nikolovlazar.goodbyemoney.features.auth.infrastructure.mappers.UserMapper
import com.nikolovlazar.goodbyemoney.features.auth.utils.RetrofitClient

import retrofit2.HttpException
import java.io.IOException
class AuthDataSourceImpl : AuthDataSource() {
    private val authService: AuthService = RetrofitClient.instance.create(AuthService::class.java)

    override suspend fun checkAuthStatus(token: String): User {
        return try {
            val response = authService.checkAuthStatus("Bearer $token").execute()
            if (response.isSuccessful) {
                val user = UserMapper.userJsonToEntity(response.body()!!)
                user
            } else {
                throw CustomError(response.errorBody()?.string() ?: "Credenciales no válidas")
            }
        } catch (e: IOException) {
            throw CustomError("Revisar conexión a internet")
        } catch (e: HttpException) {
            throw CustomError("Error en la solicitud: ${e.message()}")
        } catch (e: Exception) {
            throw Exception()
        }
    }

    override suspend fun login(email: String, password: String): User {
        return try {
            val credentials = mapOf("email" to email, "password" to password)
            print(credentials)
            val response = authService.login(credentials).execute()
            if (response.isSuccessful) {
                val user = UserMapper.userJsonToEntity(response.body()!!)
                user
            } else {
                throw CustomError(response.errorBody()?.string() ?: "Credenciales no válidas")
            }
        } catch (e: IOException) {
            throw CustomError("Revisar conexión a internet")
        } catch (e: HttpException) {
            throw CustomError("Error en la solicitud: ${e.message()}")
        } catch (e: Exception) {
            throw Exception()
        }
    }

    override suspend fun register(email: String, password: String, fullName: String): User {
        return try {
            val registrationData = mapOf("email" to email, "password" to password, "fullName" to fullName)
            val response = authService.register(registrationData).execute()
            if (response.isSuccessful) {
                val user = UserMapper.userJsonToEntity(response.body()!!)
                user
            } else {
                throw CustomError(response.errorBody()?.string() ?: "Error en el registro")
            }
        } catch (e: IOException) {
            throw CustomError("Revisar conexión a internet")
        } catch (e: HttpException) {
            throw CustomError("Error en la solicitud: ${e.message()}")
        } catch (e: Exception) {
            throw Exception()
        }
    }
}