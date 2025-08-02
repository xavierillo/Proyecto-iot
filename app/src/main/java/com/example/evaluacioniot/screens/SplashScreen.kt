package com.example.evaluacioniot.screens

import com.example.evaluacioniot.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.evaluacioniot.navigation.AppScreens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, startDestination: String) {
    LaunchedEffect(Unit) {
        delay(2000) // espera 2 segundos
        navController.navigate(startDestination) {
            popUpTo(AppScreens.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.gopark360_logo_sin_f2), // Esto debería funcionar                contentDescription = "GoPark360 Logo",
                contentDescription = "GoPark360 Logo",
                modifier = Modifier
                    .size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Cargando...", style = MaterialTheme.typography.headlineMedium)
        }
    }
}

