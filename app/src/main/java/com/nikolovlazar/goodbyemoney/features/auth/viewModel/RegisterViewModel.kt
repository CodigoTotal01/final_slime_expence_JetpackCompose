package com.nikolovlazar.goodbyemoney.features.auth.viewModel


import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterViewModel(private val registerUserCallBack: suspend (String, String, String) -> Unit)  : ViewModel() {

    private val _fullName = MutableLiveData<String>()
    val fullName: LiveData<String> = _fullName

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _confirmPassword = MutableLiveData<String>()
    val confirmPassword: LiveData<String> = _confirmPassword

    private val _isRegisterEnabled = MutableLiveData<Boolean>()
    val isRegisterEnabled: LiveData<Boolean> = _isRegisterEnabled

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading



    fun onRegisterChanged(fullName: String, email: String, password: String, confirmPassword: String) {
        _fullName.value = fullName
        _email.value = email
        _password.value = password
        _confirmPassword.value = confirmPassword
        _isRegisterEnabled.value = checkRegisterEnabled(fullName, email, password, confirmPassword)
    }

    private fun checkRegisterEnabled(fullName: String, email: String, password: String, confirmPassword: String): Boolean {
        return fullName.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                password.length >= 6 && password == confirmPassword
    }


    fun onFormSubmit() {
        viewModelScope.launch {
            registerUserCallBack(email.value!!, password.value!!, fullName.value!!);
        }
    }
    suspend fun onRegisterSelected() {
        _isLoading.value = true
        // Simula un retraso de 4 segundos para mostrar la carga
        delay(4000)
        _isLoading.value = false
    }
}

class RegisterViewModelFactory(
    private val authViewModelFactory: AuthViewModelFactory,
    private val context: Context

) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            // Crear una instancia de AuthViewModel usando AuthViewModelFactory
            val authViewModel = ViewModelProvider(
                ViewModelStore(),
                authViewModelFactory
            ).get(AuthViewModel::class.java)

            // Definir el callback usando la instancia de AuthViewModel
            val registerUserCallBack: suspend (String, String, String) -> Unit = { email, password, fullName ->
                authViewModel.registerUser(email, password, fullName)
            }

            return RegisterViewModel(registerUserCallBack) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class -Register")
    }
}
