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

enum class AuthStatus {
    CHECKING,
    AUTHENTICATED,
    NOT_AUTHENTICATED
}


data class AuthState(
    var authStatus: AuthStatus = AuthStatus.CHECKING,
    var user: User? = null,
    var errorMessage: String = ""
) {
    fun copyWith(
        authStatus: AuthStatus? = this.authStatus,
        user: User? = this.user,
        errorMessage: String? = this.errorMessage
    ): AuthState {
        return AuthState(
            authStatus = authStatus ?: this.authStatus,
            user = user ?: this.user,
            errorMessage = errorMessage ?: this.errorMessage
        )
    }
}

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val keyValueStorageService: KeyValueStorageService
) : ViewModel() {

    private val _authState = MutableLiveData(AuthState())
    val authState: LiveData<AuthState> = _authState

    init {
        // Inicializa el estado de autenticación, si es necesario
        _authState.value = AuthState()
    }
    fun loginUser(email: String, password: String) {
        _authState.value = _authState.value?.copyWith(authStatus = AuthStatus.CHECKING)
        viewModelScope.launch {
            try {
                val user = authRepository.login(email, password)
                setLoggedUser(user)
            } catch (e: CustomError) {
                Log.d("CustomErrorAuthViewModel", e.message.toString())
                updateErrorState(e.message)
            } catch (e: Exception) {
                Log.d("ErrorGeneral", e.message.toString())
                updateErrorState("Error no controlado - login")
            }
        }
    }
    fun registerUser(email: String, password: String, fullName: String) {
        _authState.value = _authState.value?.copyWith(authStatus = AuthStatus.CHECKING)
        viewModelScope.launch {
            try {
                val user = authRepository.register(email, password, fullName)
                setLoggedUser(user)
            } catch (e: CustomError) {
                Log.d("CustomErrorAuthViewModel", e.message.toString())
                updateErrorState(e.message)
            } catch (e: Exception) {
                Log.d("ErrorGeneral", e.message.toString())
                updateErrorState("Error no controlado - registro")
            }
        }
    }
    private fun setLoggedUser(user: User) {
        _authState.value = AuthState(
            authStatus = AuthStatus.AUTHENTICATED,
            user = user,
            errorMessage = ""
        )
        Log.d("posi", _authState.value?.authStatus.toString())
        keyValueStorageService.setToken(user.token)
    }

    private fun updateErrorState(errorMessage: String) {
        _authState.value = AuthState(
            authStatus = AuthStatus.NOT_AUTHENTICATED,
            user = null,
            errorMessage = errorMessage
        )
        Log.d("negativo", _authState.value?.authStatus.toString())

        keyValueStorageService.removeToken()
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
