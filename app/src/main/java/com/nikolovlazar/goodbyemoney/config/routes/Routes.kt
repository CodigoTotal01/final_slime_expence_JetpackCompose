package com.nikolovlazar.goodbyemoney.config.routes

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nikolovlazar.goodbyemoney.features.auth.pages.LoginScreen
import com.nikolovlazar.goodbyemoney.features.auth.pages.MainHome
import com.nikolovlazar.goodbyemoney.features.auth.pages.RegisterScreen
import com.nikolovlazar.goodbyemoney.features.tracker.pages.Add
import com.nikolovlazar.goodbyemoney.features.tracker.pages.Categories
import com.nikolovlazar.goodbyemoney.features.tracker.pages.Expenses
import com.nikolovlazar.goodbyemoney.features.tracker.pages.Reports
import com.nikolovlazar.goodbyemoney.features.tracker.pages.Settings

@Composable
fun NavGraph(navController: NavHostController, innerPadding: PaddingValues){
    NavHost(navController = navController, startDestination = "login"){
        composable("login"){
            LoginScreen(navController, onLoginSuccess = {
                navController.navigate("expenses"){
                    popUpTo(0);
                }
            })
        }

        composable("register"){
            RegisterScreen(navController, onRegisterSuccess = {
                navController.navigate("expenses"){
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