package com.nikolovlazar.goodbyemoney.features.tracker.auth.viewModel


import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay

class RegisterViewModel : ViewModel() {

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

        Log.d("MiApp", "soy valido ? : ${isRegisterEnabled.value}")
        return fullName.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                password.length > 6 && password == confirmPassword
    }

    suspend fun onRegisterSelected() {
        _isLoading.value = true
        // Simula un retraso de 4 segundos para mostrar la carga
        delay(4000)
        _isLoading.value = false
    }
}