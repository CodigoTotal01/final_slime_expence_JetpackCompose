package com.nikolovlazar.goodbyemoney.features.auth.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nikolovlazar.goodbyemoney.features.auth.domain.repository.AuthRepository

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import com.nikolovlazar.goodbyemoney.features.auth.domain.models.User
import com.nikolovlazar.goodbyemoney.features.auth.infrastructure.errors.CustomError

class KeyValueStorageService(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

    fun setToken(token: String) {
        with(sharedPreferences.edit()) {
            putString("token", token)
            apply()
        }
    }

    fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }

    fun removeToken() {
        with(sharedPreferences.edit()) {
            remove("token")
            apply()
        }
    }
}
enum class AuthStatus {
    CHECKING,
    AUTHENTICATED,
    NOT_AUTHENTICATED
}


data class AuthState(
    var authStatus: AuthStatus = AuthStatus.CHECKING,
    var user: User? = null,
    var errorMessage: String = ""
)

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val keyValueStorageService: KeyValueStorageService
) : ViewModel() {

    val authState = MutableLiveData(AuthState())

    suspend fun loginUser(email: String, password: String) {
        try {
            val user = authRepository.login(email, password)
            setLoggedUser(user)
        } catch (e: CustomError) {
            logout(e.message)
        } catch (e: Exception) {
            logout("Error no controlado - login ")
        }
    }

    suspend fun register(email: String, password: String, fullName: String) {
        try {
            val user = authRepository.register(email, password, fullName)
            keyValueStorageService.setToken(user.token)
            authState.value = AuthState(
                authStatus = AuthStatus.AUTHENTICATED,
                user = user,
                errorMessage = ""
            )
        }  catch (e: CustomError) {
            logout(e.message)
        } catch (e: Exception) {
            logout("Error no controlado - register ")
        }
    }

    suspend fun checkAuthStatus() {
        val token = keyValueStorageService.getToken()
        if (token == null) {
            logout()
        } else {
            try {
                val user = authRepository.checkAuthStatus(token)
                setLoggedUser(user)
            } catch (e: Exception) {
                logout()
            }
        }
    }

    private fun setLoggedUser(user: User) {
        keyValueStorageService.setToken(user.token)
        authState.value = AuthState(
            authStatus = AuthStatus.AUTHENTICATED,
            user = user,
            errorMessage = ""
        )
    }

    fun logout(errorMessage: String? = null) {
        keyValueStorageService.removeToken()
        authState.value = AuthState(
            authStatus = AuthStatus.NOT_AUTHENTICATED,
            user = null,
            errorMessage = errorMessage ?: ""
        )
    }


}