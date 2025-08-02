package com.example.evaluacioniot.screens

import ParkingViewModel
import TrafficLightState
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AccionScreen(viewModel: ParkingViewModel = viewModel()) {
    val carCount by viewModel.carCount.collectAsState()
    val doorOpen by viewModel.doorOpen.collectAsState()
    val lightState by viewModel.trafficLightState.collectAsState()
    val sensorActive by viewModel.sensorActive.collectAsState()
    val config by viewModel.semaforoConfig.collectAsState()

    LaunchedEffect(sensorActive) {
        if (sensorActive) {
            viewModel.setTrafficLightState(TrafficLightState.Red)
            delay(config.tiempoRojo.toLong())
            viewModel.setTrafficLightState(TrafficLightState.Green)
            delay(config.tiempoVerde.toLong())
            viewModel.setTrafficLightState(TrafficLightState.Yellow)
            delay(config.tiempoAmarillo.toLong())
            viewModel.setTrafficLightState(TrafficLightState.Red)
            viewModel.resetSensorAndDoor()
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Control de Aparcamiento", style = MaterialTheme.typography.headlineMedium)

            TrafficLight(lightState)

            Text("Autos dentro: $carCount", style = MaterialTheme.typography.titleLarge)

            Button(
                onClick = {
                    viewModel.abrirPuerta()
                },
                enabled = !doorOpen
            ) {
                Text(if (doorOpen) "Puerta Abierta" else "Abrir Puerta")
            }
        }
    }
}

@Composable
fun TrafficLight(state: TrafficLightState) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LightCircle(color = Color.Red, isOn = state == TrafficLightState.Red)
        LightCircle(color = Color.Yellow, isOn = state == TrafficLightState.Yellow)
        LightCircle(color = Color.Green, isOn = state == TrafficLightState.Green)
    }
}

@Composable
fun LightCircle(color: Color, isOn: Boolean) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(if (isOn) color else color.copy(alpha = 0.3f))
    )
}

@Preview(showBackground = true)
@Composable
fun AccionScreenPreview() {
    val fakeViewModel = remember {
        object : ParkingViewModel() {
            init {
                _carCount.value = 3
                _doorOpen.value = false
                _sensorActive.value = true
                _trafficLightState.value = TrafficLightState.Green
            }
        }
    }
    AccionScreen(viewModel = fakeViewModel)
}
