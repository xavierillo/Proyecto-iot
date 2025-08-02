package com.example.evaluacioniot.navigation

import ParkingViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.text.input.KeyboardType
import com.example.evaluacioniot.data.SemaforoConfigFirebase
import com.example.evaluacioniot.screens.ArduinoManager

@Composable
fun ConfiguracionScreen(viewModel: ParkingViewModel = viewModel()) {
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

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start
        ) {
            Text("Configuración", style = MaterialTheme.typography.headlineMedium)

            Text("Configurar tiempos del semáforo", style = MaterialTheme.typography.headlineSmall)

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

    ConfiguracionScreen(viewModel = fakeViewModel)
}

fun enviarComandosSemaforo(config: SemaforoConfigFirebase) {
    if (ArduinoManager.isConnected()) {
        ArduinoManager.sendCommand("SET_RED=${config.tiempoRojo}\n")
        ArduinoManager.sendCommand("SET_GREEN=${config.tiempoVerde}\n")
        ArduinoManager.sendCommand("SET_YELLOW=${config.tiempoAmarillo}\n")
        ArduinoManager.sendCommand("RUN\n")
    }
}
