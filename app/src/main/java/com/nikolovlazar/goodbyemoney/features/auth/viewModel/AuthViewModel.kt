package com.nikolovlazar.goodbyemoney.features.auth.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nikolovlazar.goodbyemoney.features.auth.domain.repository.AuthRepository
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nikolovlazar.goodbyemoney.features.auth.domain.models.User
import com.nikolovlazar.goodbyemoney.features.auth.infrastructure.errors.CustomError
import com.nikolovlazar.goodbyemoney.features.auth.infrastructure.repositories.AuthRepositoryImpl
import kotlinx.coroutines.launch

class KeyValueStorageService(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

    fun setToken(token: String) {
        with(sharedPreferences.edit()) {
            putString("token", token)
            apply()
        }
    }

    fun getToken(): String? {
        return sharedPreferences.getString("token", "")
    }

    fun removeToken() {
        with(sharedPreferences.edit()) {
            remove("token")
            apply()
        }
    }
}


class AuthViewModel(
    private val authRepository: AuthRepository,
    private val keyValueStorageService: KeyValueStorageService
) : ViewModel() {

    val isAuthenticated = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val user = authRepository.login(email, password)
                keyValueStorageService.setToken(user.token)
                isAuthenticated.postValue(true)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Login error: ${e.message}")
                isAuthenticated.postValue(false)
                errorMessage.postValue("Verificar datos")
            }
        }
    }

    fun registerUser(email: String, password: String, fullName: String) {
        viewModelScope.launch {
            try {
                val user = authRepository.register(email, password, fullName)
                keyValueStorageService.setToken(user.token)
                isAuthenticated.postValue(true)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Register error: ${e.message}")
                isAuthenticated.postValue(false)
                errorMessage.postValue("Error al registrar. Verificar datos")
            }
        }
    }
}
class AuthViewModelFactory(
    private val authRepository: AuthRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            val keyValueStorageService = KeyValueStorageService(context)
            return AuthViewModel(authRepository, keyValueStorageService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
