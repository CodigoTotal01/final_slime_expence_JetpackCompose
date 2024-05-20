package com.nikolovlazar.goodbyemoney.features.auth.pages


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nikolovlazar.goodbyemoney.R
import com.nikolovlazar.goodbyemoney.features.auth.viewModel.RegisterViewModel


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterScreen(
    navController: NavController,
    onRegisterSuccess: () -> Unit,
    vm: RegisterViewModel = viewModel()
) {

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Gestión de Gastos: Registro") })
        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            HeaderForm(Modifier.align(Alignment.TopEnd))
            BodyRegister(Modifier.align(Alignment.Center), vm, onRegisterSuccess)
            FooterRegister(Modifier.align(Alignment.BottomCenter), navController)
        }
    }

}

@Composable
fun BodyRegister(
    modifier: Modifier,
    registerViewModel: RegisterViewModel,
    onRegisterSuccess: () -> Unit
) {
    val fullName: String by registerViewModel.fullName.observeAsState(initial = "")
    val email: String by registerViewModel.email.observeAsState(initial = "")
    val password: String by registerViewModel.password.observeAsState(initial = "")
    val confirmPassword: String by registerViewModel.confirmPassword.observeAsState(initial = "")
    val isRegisterEnabled: Boolean by registerViewModel.isRegisterEnabled.observeAsState(initial = false)

    Column(modifier = modifier) {
        ImageLogo(Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.size(16.dp))
        FullNameTextArea(fullName) {
            registerViewModel.onRegisterChanged(
                fullName = it,
                email = email,
                password = password,
                confirmPassword = confirmPassword
            )
        }
        Spacer(modifier = Modifier.size(4.dp))
        EmailTextArea(email) {
            registerViewModel.onRegisterChanged(
                fullName = fullName,
                email = it,
                password = password,
                confirmPassword = confirmPassword
            )
        }
        Spacer(modifier = Modifier.size(4.dp))
        PasswordTextArea(password) {
            registerViewModel.onRegisterChanged(
                fullName = fullName,
                email = email,
                password = it,
                confirmPassword = confirmPassword
            )
        }
        Spacer(modifier = Modifier.size(4.dp))
        ConfirmPasswordTextArea(confirmPassword) {
            registerViewModel.onRegisterChanged(
                fullName = fullName,
                email = email,
                password = password,
                confirmPassword = it
            )
        }
        Spacer(modifier = Modifier.size(16.dp))

        RegisterButton(isRegisterEnabled) {
            onRegisterSuccess()
        }
        Spacer(modifier = Modifier.size(16.dp))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullNameTextArea(fullName: String, onTextChanged: (String) -> Unit) {
    TextField(
        value = fullName,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Full Name") },
        maxLines = 1,
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black, // Color de texto para ambos estados, enfocado y sin enfocar
            containerColor = Color(0xFFFAFAFA),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmPasswordTextArea(confirmPassword: String, onTextChanged: (String) -> Unit) {
    var passwordVisibility by remember { mutableStateOf(false) }
    TextField(
        value = confirmPassword,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Confirm Password") },

        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black, // Color de texto para ambos estados, enfocado y sin enfocar

            containerColor = Color(0xFFFAFAFA),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisibility) {
                painterResource(id = R.drawable.eye_close)
            } else {
                painterResource(id = R.drawable.eye_open)
            }
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(painter = image, contentDescription = "show password")
            }
        },
        visualTransformation = if (passwordVisibility) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }
    )
}

@Composable
fun RegisterButton(registerEnabled: Boolean, onRegisterClicked: () -> Unit) {
    Button(
        onClick = { onRegisterClicked() },
        enabled = registerEnabled,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4EA8E9),
            disabledContainerColor = Color(0xFF78C8F9),
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Text(text = "Register")
    }
}

@Composable
fun FooterRegister(modifier: Modifier, navController: NavController) {
    Column(modifier = modifier.fillMaxWidth()) {
        Divider(
            Modifier
                .background(Color(0xFFF9F9F9))
                .height(1.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(24.dp))
        TextNavLogin(navController)
        Spacer(modifier = Modifier.size(24.dp))
    }
}


@Composable
fun TextNavLogin(navController: NavController) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(
            text = "¿Ya tienes una cuenta?",
            fontSize = 12.sp,
            color = Color(0xFFB5B5B5)
        )
        Text(
            text = " Inicia sesión",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4EA8E9),
            modifier = Modifier
                .padding(start = 4.dp)
                .clickable { navController.navigate("login") }
        )
    }
}
