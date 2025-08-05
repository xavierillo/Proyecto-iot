package com.example.evaluacioniot.screens

import ParkingViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.evaluacioniot.data.SemaforoConfigFirebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionScreen(navController: NavController, viewModel: ParkingViewModel = viewModel()) {
    val carCount by viewModel.carCount.collectAsState()
    val maxCapacity by viewModel.maxCapacity.collectAsState()
    val sensorAutoMode by viewModel.sensorAutoMode.collectAsState()
    val firebaseConfig by viewModel.semaforoConfig.collectAsState()
    var newCapacity by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.cargarConfiguracionDesdeFirebase()
        viewModel.cargarEstadoDesdeFirebase()
        newCapacity = viewModel.maxCapacity.value.toString()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.clickable {
                            navController.navigate("home"){
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start
        ) {
            Text("Tiempos del semáforo",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 15.sp
            )

            TiempoInput("Rojo (ms)", firebaseConfig.tiempoRojo) { nuevo ->
                viewModel.semaforoConfig.value = firebaseConfig.copy(tiempoRojo = nuevo)
            }

            TiempoInput("Verde (ms)", firebaseConfig.tiempoVerde) { nuevo ->
                viewModel.semaforoConfig.value = firebaseConfig.copy(tiempoVerde = nuevo)
            }

            TiempoInput("Amarillo (ms)", firebaseConfig.tiempoAmarillo) { nuevo ->
                viewModel.semaforoConfig.value = firebaseConfig.copy(tiempoAmarillo = nuevo)
            }

            Button(onClick = {
                viewModel.guardarConfiguracionEnFirebase()
                enviarComandosSemaforo(firebaseConfig)
            }) {
                Text("Guardar y Ejecutar")
            }

            OutlinedTextField(
                value = newCapacity,
                onValueChange = { newCapacity = it },
                label = { Text("Capacidad máxima") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    newCapacity.toIntOrNull()?.let {
                        viewModel.setMaxCapacity(it)
                        viewModel.guardarEstadoEnFirebase()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Actualizar Capacidad")
            }

            Text("Autos actuales: $carCount", style = MaterialTheme.typography.bodyLarge)

            OutlinedButton(
                onClick = {
                    viewModel.reiniciarContador()
                    viewModel.guardarEstadoEnFirebase()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reiniciar contador de autos")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sensor automático:")
                Switch(
                    checked = sensorAutoMode,
                    onCheckedChange = {
                        viewModel.toggleSensorAutoMode()
                        viewModel.guardarEstadoEnFirebase()
                    }
                )
            }
        }
    }
}

@Composable
fun TiempoInput(label: String, valor: Int, onValorCambiado: (Int) -> Unit) {
    OutlinedTextField(
        value = valor.toString(),
        onValueChange = { nuevo ->
            nuevo.toIntOrNull()?.let { onValorCambiado(it) }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun ConfigurationScreenPreview() {
    var navController = NavController(LocalContext.current)

    val fakeViewModel = remember {
        object : ParkingViewModel() {
            init {
                _carCount.value = 2
                _maxCapacity.value = 10
                _sensorAutoMode.value = true
                semaforoConfig.value = SemaforoConfigFirebase(3000, 2000, 1000)
            }
        }
    }

    ConfiguracionScreen(navController, viewModel = fakeViewModel)
}

fun enviarComandosSemaforo(config: SemaforoConfigFirebase) {
    if (ArduinoManager.isConnected()) {
        ArduinoManager.sendCommand("SET_RED=${config.tiempoRojo}\n")
        ArduinoManager.sendCommand("SET_GREEN=${config.tiempoVerde}\n")
        ArduinoManager.sendCommand("SET_YELLOW=${config.tiempoAmarillo}\n")
        ArduinoManager.sendCommand("RUN\n")
    }
}
