package com.nikolovlazar.goodbyemoney.features.auth.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nikolovlazar.goodbyemoney.features.auth.domain.repository.AuthRepository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val user = authRepository.login(email, password)
                setLoggedUser(user)
            } catch (e: CustomError) {
                Log.d("CustomErrorAuthViewmodel", e.message.toString())
                updateErrorState(e.message)
            } catch (e: Exception) {
                Log.d("ErrorGeneral", e.message.toString())
                updateErrorState("Error no controlado - login")
            }
        }
    }

    private fun setLoggedUser(user: User) {
        authState.value = AuthState(
            authStatus = AuthStatus.AUTHENTICATED,
            user = user,
            errorMessage = ""
        )
        keyValueStorageService.setToken(user.token)
    }

    private fun updateErrorState(errorMessage: String) {
        authState.value = AuthState(
            authStatus = AuthStatus.NOT_AUTHENTICATED,
            user = null,
            errorMessage = errorMessage
        )
    }

    fun logout(errorMessage: String) {
        authState.value = AuthState(
            authStatus = AuthStatus.NOT_AUTHENTICATED,
            user = null,
            errorMessage = errorMessage
        )
        keyValueStorageService.removeToken()
    }
}

class AuthViewModelFactory(
    private val authRepository: AuthRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            var authRepository = AuthRepositoryImpl();
            val keyValueStorageService = KeyValueStorageService(context);
            return AuthViewModel(authRepository, keyValueStorageService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}