package com.nikolovlazar.goodbyemoney.features.auth.pages


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHome () {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "¡Hola mundo!") },
            )
        },
        content = {
            // Contenido de tu aplicación aquí
            Box(
            ) {
                Text(text = "¡Hola mundo!")
            }
        }
    )
}
