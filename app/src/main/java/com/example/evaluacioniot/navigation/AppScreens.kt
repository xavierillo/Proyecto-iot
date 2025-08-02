package com.example.evaluacioniot.navigation

sealed class AppScreens(val route: String) {
    object Splash : AppScreens("splash")

    object HomeScreen : AppScreens("home")

    object AccionScreen : AppScreens("accion_screen")

    object ConfiguracionScreen : AppScreens("configuracion_screen")
    object FirstScreen: AppScreens("first_screen")
    object SecondScreen: AppScreens("second_screen")

}