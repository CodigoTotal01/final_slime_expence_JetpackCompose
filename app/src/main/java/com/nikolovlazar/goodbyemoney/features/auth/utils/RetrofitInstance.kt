package com.nikolovlazar.goodbyemoney.features.auth.utils

import com.nikolovlazar.goodbyemoney.features.auth.domain.repository.AuthRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object RetrofitClient{
    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Util.Base)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}