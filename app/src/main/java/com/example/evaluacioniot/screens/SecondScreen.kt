@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.evaluacioniot.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.evaluacioniot.navigation.AppNavigation
import com.example.evaluacioniot.ui.theme.EvaluacionIotTheme

@Composable
fun SecondScreen(navController: NavController) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Navegación segundo") },

                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.clickable {
                            navController.navigate("first_screen"){
                                popUpTo("first_screen") { inclusive = true }
                            }
                        }
                    )
                }
            )
        }
    ) { innerPadding ->
        SecondBodyContent(navController, innerPadding) // Pasa el padding a BodyContent
    }
}

@Composable
fun SecondBodyContent(navController: NavController, contentPadding: PaddingValues) { // Acepta PaddingValues como parámetro
    Column(
        modifier = Modifier
            .padding(contentPadding) // APLICA el padding aquí
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Hola - Navegación segundo")
        Button(onClick = {
            navController.navigate("first_screen")
        }) {
            Text("Navega")
        }
    }
}

@Preview
@Composable
fun SecondScreenPreview() {
    var navController = NavController(LocalContext.current)
    SecondScreen(navController)
}