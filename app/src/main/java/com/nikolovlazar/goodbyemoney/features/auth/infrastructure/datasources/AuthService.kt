package com.nikolovlazar.goodbyemoney.features.auth.infrastructure.datasources

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
//Definir servicio de retrofit
interface AuthService {
    @GET("auth/check-status")
    fun checkAuthStatus(@Header("Authorization") token: String): Call<Map<String, Any>>

    @POST("auth/login")
    fun login(@Body credentials: Map<String, String>): Call<Map<String, Any>>

    @POST("auth/register")
    fun register(@Body userData: Map<String, String>): Call<Map<String, Any>>
}