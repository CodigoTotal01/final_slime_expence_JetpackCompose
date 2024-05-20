package com.nikolovlazar.goodbyemoney.config.routes

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nikolovlazar.goodbyemoney.features.tracker.auth.pages.LoginScreen

@Composable
fun NavGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = "login"){
        composable("login"){
            LoginScreen(navController, onLoginSuccess = {
                navController.navigate("home"){
                    popUpTo(0);
                }
            })
        }

//        composable("register"){
//            RegisterScreen(navController, onRegisterSuccess = {
//                navController.navigate("home"){
//                    popUpTo(0);
//                }
//            })
//        }
//
//        composable("home"){
//            MainHome()
//        }

    }
}