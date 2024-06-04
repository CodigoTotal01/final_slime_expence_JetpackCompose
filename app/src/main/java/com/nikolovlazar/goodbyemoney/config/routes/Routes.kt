package com.nikolovlazar.goodbyemoney.config.routes

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nikolovlazar.goodbyemoney.features.auth.infrastructure.repositories.AuthRepositoryImpl
import com.nikolovlazar.goodbyemoney.features.auth.pages.LoginScreen
import com.nikolovlazar.goodbyemoney.features.auth.pages.RegisterScreen
import com.nikolovlazar.goodbyemoney.features.auth.viewModel.AuthViewModelFactory
import com.nikolovlazar.goodbyemoney.features.auth.viewModel.KeyValueStorageService
import com.nikolovlazar.goodbyemoney.features.tracker.pages.Add
import com.nikolovlazar.goodbyemoney.features.tracker.pages.Categories
import com.nikolovlazar.goodbyemoney.features.tracker.pages.Expenses
import com.nikolovlazar.goodbyemoney.features.tracker.pages.Reports
import com.nikolovlazar.goodbyemoney.features.tracker.pages.Settings

@Composable
fun NavGraph(navController: NavHostController, innerPadding: PaddingValues) {

    val context = LocalContext.current

    val authRepository = AuthRepositoryImpl()

    val keyValueStorageService = KeyValueStorageService(context)

    // Crear instancia de AuthViewModelFactory
    val authViewModelFactory = AuthViewModelFactory(authRepository, context);

    fun retornarLoginOrExpences(): String {
        return if (keyValueStorageService.getToken() != "") {
            "expenses"
        } else {
            "login"
        }
    }

    NavHost(navController = navController, startDestination = retornarLoginOrExpences()) {
        composable("login") {
            LoginScreen(navController, onLoginSuccess = {
                navController.navigate("expenses") {
                    popUpTo(0);
                }
            })
        }

        composable("register") {
            RegisterScreen(navController, onRegisterSuccess = {
                navController.navigate("expenses") {
                    popUpTo(0);
                }
            })
        }

        composable("expenses") {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                Expenses(navController)
            }
        }
        composable("reports") {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                Reports()
            }
        }
        composable("add") {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                Add(navController)
            }
        }
        composable("settings") {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                Settings(navController)
            }
        }
        composable("settings/categories") {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                Categories(navController)
            }
        }

    }
}