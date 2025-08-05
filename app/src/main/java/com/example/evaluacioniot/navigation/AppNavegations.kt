package com.example.evaluacioniot.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.evaluacioniot.screens.AccionScreen
import com.example.evaluacioniot.screens.ConfiguracionScreen
import com.example.evaluacioniot.screens.HomeScreen
import com.example.evaluacioniot.screens.SplashScreen

@Composable
fun AppNavigation(startDestination: String = AppScreens.Splash.route) {
        val navController = rememberNavController()
        NavHost(navController, startDestination = startDestination) {
                composable(AppScreens.Splash.route) {
                        SplashScreen(navController, AppScreens.HomeScreen.route)
                }
                composable(route = AppScreens.HomeScreen.route) {
                        HomeScreen(navController)
                }
                composable(route = AppScreens.AccionScreen.route) {
                        AccionScreen(navController)
                }
                composable(route = AppScreens.ConfiguracionScreen.route) {
                        ConfiguracionScreen(navController)
                }
        }
}