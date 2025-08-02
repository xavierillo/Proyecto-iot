package com.example.evaluacioniot.screens

import androidx.compose.foundation.Image
import com.example.evaluacioniot.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.Icon
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController


@Composable
fun HomeScreen(navController: NavController) {
    Scaffold { innerPadding ->
        HomeContent(navController = navController, contentPadding = innerPadding)
    }
}

@Composable
fun HomeContent(navController: NavController, contentPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Logo opcional si lo tienes en drawable
        Image(
            painter = painterResource(id = R.drawable.gopark360_logo_sin_f2),
            contentDescription = "GoPark360 Logo",
            modifier = Modifier
                .size(160.dp)
                .padding(bottom = 24.dp)
        )

        Text(
            text = "¡Bienvenido a GoPark360!",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Text(
            text = "Gestiona tu estacionamiento de forma inteligente",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = { navController.navigate("accion_screen") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Ir a Acción")
        }

        Button(
            onClick = { navController.navigate("configuracion_screen") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Ir a configuración")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val context = LocalContext.current
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}