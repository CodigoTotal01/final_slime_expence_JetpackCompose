package com.nikolovlazar.goodbyemoney.features.auth.pages

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.background

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nikolovlazar.goodbyemoney.R
import com.nikolovlazar.goodbyemoney.features.auth.domain.repository.AuthRepository
import com.nikolovlazar.goodbyemoney.features.auth.infrastructure.repositories.AuthRepositoryImpl
import com.nikolovlazar.goodbyemoney.features.auth.viewModel.AuthViewModel
import com.nikolovlazar.goodbyemoney.features.auth.viewModel.AuthViewModelFactory
import com.nikolovlazar.goodbyemoney.features.auth.viewModel.KeyValueStorageService
import com.nikolovlazar.goodbyemoney.features.auth.viewModel.LoginViewModel

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.*
import com.nikolovlazar.goodbyemoney.features.auth.viewModel.LoginViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(navController: NavController, onLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(AuthRepositoryImpl(), context))
    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(authViewModel))

    val isAuthenticated by authViewModel.isAuthenticated.observeAsState()
    val errorMessage by authViewModel.errorMessage.observeAsState()

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated == true) {
            onLoginSuccess()
        } else if (isAuthenticated == false) {
            Toast.makeText(context, errorMessage ?: "Verificar datos", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Expense Management") }) },
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            HeaderForm(Modifier.align(Alignment.TopEnd))
            BodyLogin(Modifier.align(Alignment.Center), loginViewModel)
            FooterLogin(Modifier.align(Alignment.BottomCenter), navController)
        }
    }
}

@Composable
fun BodyLogin(
    modifier: Modifier,
    loginViewModel: LoginViewModel,
) {
    val email: String by loginViewModel.email.observeAsState(initial = "")
    val password: String by loginViewModel.password.observeAsState(initial = "")
    val isLoginEnable: Boolean by loginViewModel.isLoginEnable.observeAsState(initial = false)

    Column(modifier = modifier) {
        ImageLogo(Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.size(16.dp))
        EmailTextArea(email) {
            loginViewModel.onLoginChanged(email = it, password = password)
        }
        Spacer(modifier = Modifier.size(4.dp))
        PasswordTextArea(password) {
            loginViewModel.onLoginChanged(email = email, password = it)
        }
        Spacer(modifier = Modifier.size(16.dp))
        LoginButton(isLoginEnable) {
            loginViewModel.onFormSubmit()
        }
        Spacer(modifier = Modifier.size(16.dp))
    }
}
@Composable
fun LoginButton(loginEnable: Boolean, onLoginClicked: () -> Unit) {
    Button(
        onClick = { onLoginClicked() },
        enabled = loginEnable,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4EA8E9),
            disabledContainerColor = Color(0xFF78C8F9),
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Text(text = "Log In")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextArea(password: String, onTextChanged: (String) -> Unit) {
    var passwordVisibility by remember { mutableStateOf(false) }
    TextField(
        value = password,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Password") },
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            containerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val imagen = if (passwordVisibility) {
                painterResource(id = R.drawable.eye_close)
            } else {
                painterResource(id = R.drawable.eye_open)
            }
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(painter = imagen, contentDescription = "show password")
            }
        },
        visualTransformation = if (passwordVisibility) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailTextArea(email: String = "test1@google.com", onTextChanged: (String) -> Unit) {
    TextField(
        value = email,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Email") },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            containerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun ImageLogo(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.slimeform),
        contentDescription = "logo",
        modifier = modifier
    )
}

@Composable
fun HeaderForm(modifier: Modifier) {
    val activity = LocalContext.current as Activity
    Icon(
        imageVector = Icons.Default.Close,
        contentDescription = "close app",
        modifier = modifier.clickable { activity.finish() }
    )
}

@Composable
fun FooterLogin(modifier: Modifier, navController: NavController) {
    Column(modifier = modifier.fillMaxWidth()) {
        Divider(
            Modifier
                .background(Color(0xFFF9F9F9))
                .height(1.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(24.dp))
        SignUp(navController)
        Spacer(modifier = Modifier.size(24.dp))
    }
}

@Composable
fun SignUp(navController: NavController) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(
            text = "Don't have an account?",
            fontSize = 12.sp,
            color = Color(0xFFB5B5B5)
        )
        Text(
            text = " Sign Up",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4EA8E9),
            modifier = Modifier
                .padding(start = 4.dp)
                .clickable { navController.navigate("register") }
        )
    }
}

