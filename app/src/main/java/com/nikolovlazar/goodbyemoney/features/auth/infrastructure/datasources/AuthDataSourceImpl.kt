package com.nikolovlazar.goodbyemoney.features.auth.infrastructure.datasources

import android.util.Log
import com.nikolovlazar.goodbyemoney.features.auth.domain.datasource.AuthDataSource
import com.nikolovlazar.goodbyemoney.features.auth.domain.models.User
import com.nikolovlazar.goodbyemoney.features.auth.infrastructure.errors.CustomError
import com.nikolovlazar.goodbyemoney.features.auth.infrastructure.mappers.UserMapper
import com.nikolovlazar.goodbyemoney.features.auth.utils.RetrofitClient
import retrofit2.Call
import retrofit2.Callback

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.log

class AuthDataSourceImpl : AuthDataSource() {
    private val authService: AuthService = RetrofitClient.instance.create(AuthService::class.java)

    override suspend fun checkAuthStatus(token: String): User {
        return try {
            // Utilizar una corrutina para esperar la respuesta de la llamada asíncrona
            val response = suspendCoroutine<Response<Map<String, Any>>> { continuation ->
                authService.checkAuthStatus("Bearer $token").enqueue(object : Callback<Map<String, Any>> {
                    override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                        continuation.resume(response)
                    }

                    override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                })
            }

            // Manejar la respuesta obtenida
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

            // Utilizar una corrutina para esperar la respuesta de la llamada asíncrona
            val response = suspendCoroutine<Response<Map<String, Any>>> { continuation ->
                authService.login(credentials).enqueue(object : Callback<Map<String, Any>> {
                    override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                        continuation.resume(response)
                    }
                    override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                })
            }

            // Manejar la respuesta obtenida
            if (response.isSuccessful) {
                val user = UserMapper.userJsonToEntity(response.body()!!)
                Log.d("UserAIP", user.token)
                user
            } else {
                throw CustomError(response.errorBody()?.string() ?: "Credenciales no válidas")
            }
        } catch (e: IOException) {
            Log.d("ConexionError", e.message.toString())
            throw CustomError("Revisar conexión a internet")
        } catch (e: Exception) {
            throw Exception()
        }
    }

    override suspend fun register(email: String, password: String, fullName: String): User {
        return try {
            val registrationData = mapOf("email" to email, "password" to password, "fullName" to fullName)

            // Utilizar una corrutina para esperar la respuesta de la llamada asíncrona
            val response = suspendCoroutine<Response<Map<String, Any>>> { continuation ->
                authService.register(registrationData).enqueue(object : Callback<Map<String, Any>> {
                    override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                        continuation.resume(response)
                    }

                    override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                })
            }

            // Manejar la respuesta obtenida
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