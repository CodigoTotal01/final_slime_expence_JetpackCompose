package com.nikolovlazar.goodbyemoney.features.auth.viewModel

import android.content.Context
import android.util.Patterns
import androidx.compose.runtime.currentComposer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import com.nikolovlazar.goodbyemoney.features.tracker.viewmodels.AddScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction2
import android.util.Log



class LoginViewModel(private val loginUserCallBack: suspend (String, String) -> Unit) : ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _isLoginEnable = MutableLiveData<Boolean>()
    val isLoginEnable: LiveData<Boolean> = _isLoginEnable

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _isLoginEnable.value = enableLogin(email, password)
    }

    private fun enableLogin(email: String, password: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length > 4

    fun onFormSubmit() {
        _authState.value = AuthState(authStatus = AuthStatus.CHECKING)
        viewModelScope.launch {
            try {
                loginUserCallBack(email.value!!, password.value!!)
                _authState.value = AuthState(authStatus = AuthStatus.AUTHENTICATED)
            } catch (e: Exception) {
                _authState.value = AuthState(
                    authStatus = AuthStatus.NOT_AUTHENTICATED,
                    errorMessage = "Error no controlado - login"
                )
            }
        }
    }
}

class LoginViewModelFactory(
    private val authViewModelFactory: AuthViewModelFactory,
    private val context: Context
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            // Crear una instancia de AuthViewModel usando AuthViewModelFactory
            val authViewModel = ViewModelProvider(ViewModelStore(), authViewModelFactory).get(AuthViewModel::class.java)

            // Definir el callback usando la instancia de AuthViewModel
            val loginUserCallBack: suspend (String, String) -> Unit = { email, password ->
                authViewModel.loginUser(email, password)
            }

            return LoginViewModel(loginUserCallBack) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
