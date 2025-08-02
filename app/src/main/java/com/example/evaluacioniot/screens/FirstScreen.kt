package com.example.evaluacioniot.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@Composable
fun FirstScreen(navController: NavController) {
    // La lambda de content ahora recibe 'innerPadding' (o el nombre que prefieras)
    Scaffold { innerPadding ->
        BodyContent(navController, innerPadding) // Pasa el padding a BodyContent
    }
}
@Composable
fun BodyContent(navController: NavController, contentPadding: PaddingValues) { // Acepta PaddingValues como parámetro
    Column(
        modifier = Modifier
            .padding(contentPadding) // APLICA el padding aquí
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Hola - Navegación primero")
        Button(onClick = {
            navController.navigate("second_screen")
        }) {
            Text("Navega")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FirstScreenPreview() {
    val navController = rememberNavController()
    FirstScreen(navController = NavController(LocalContext.current))
}