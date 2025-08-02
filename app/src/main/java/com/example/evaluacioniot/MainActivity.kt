package com.example.evaluacioniot

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.evaluacioniot.navigation.AppNavigation
import com.example.evaluacioniot.navigation.AppScreens
import com.example.evaluacioniot.screens.ArduinoManager
import com.example.evaluacioniot.ui.theme.EvaluacionIotTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        solicitationBluetooth()
        super.onCreate(savedInstanceState)
        // Inicializar ArduinoManager con el contexto de la aplicación
        ArduinoManager.init(applicationContext)
        enableEdgeToEdge()
        setContent {
            EvaluacionIotTheme {
                Surface (modifier = Modifier.fillMaxSize()) {
                    AppNavigation()
                }
            }
        }
    }

    private fun solicitationBluetooth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.BLUETOOTH_CONNECT), 1)
            }
        }
    }
}






@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EvaluacionIotTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AppNavigation(AppScreens.AccionScreen.route)
        }
    }
}