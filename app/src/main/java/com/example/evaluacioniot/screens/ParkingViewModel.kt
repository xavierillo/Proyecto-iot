import android.Manifest
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evaluacioniot.data.FirebaseRepository
import com.example.evaluacioniot.data.SemaforoConfigFirebase
import com.example.evaluacioniot.screens.ArduinoManager
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

open class ParkingViewModel : ViewModel() {
    val _carCount = MutableStateFlow(0)
    val carCount = _carCount.asStateFlow()

    val _doorOpen = MutableStateFlow(false)
    val doorOpen = _doorOpen.asStateFlow()

    val _sensorActive = MutableStateFlow(false)
    val sensorActive = _sensorActive.asStateFlow()

    val _trafficLightState = MutableStateFlow(TrafficLightState.Red)
    val trafficLightState = _trafficLightState.asStateFlow()

    val _maxCapacity = MutableStateFlow(10)
    val maxCapacity = _maxCapacity.asStateFlow()

    val _sensorAutoMode = MutableStateFlow(false)
    val sensorAutoMode = _sensorAutoMode.asStateFlow()

    var semaforoConfig = MutableStateFlow(SemaforoConfigFirebase())

    fun cargarConfiguracionDesdeFirebase() {
        viewModelScope.launch {
            FirebaseRepository.getConfig()?.let {
                semaforoConfig.value = it
            }
        }
    }

    fun guardarConfiguracionEnFirebase() {
        viewModelScope.launch {
            Log.d("ParkingViewModel", "Guardando configuración en Firebase: $semaforoConfig")
            FirebaseRepository.setConfig(semaforoConfig.value)
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun abrirPuerta() {
        viewModelScope.launch(Dispatchers.IO) {
            if (!ArduinoManager.isConnected()) {
                val connected = ArduinoManager.connect()
                if (!connected) {
                    Log.e("ParkingManager", "No se pudo conectar con el HC-06")
                    return@launch
                }
            }

            ArduinoManager.sendCommand("RUN\n") // Abrir puerta
            withContext(Dispatchers.Main) {
                _doorOpen.value = true
                cycleLightsAndCount()
            }
        }
    }

    fun detectarAuto() {
        _sensorActive.value = true
        cycleLightsAndCount()
    }

    private fun cycleLightsAndCount() {
        viewModelScope.launch {
            val config = semaforoConfig.value  // 🔁 Usa la configuración actual

            _trafficLightState.value = TrafficLightState.Red
            delay(config.tiempoRojo.toLong())

            _trafficLightState.value = TrafficLightState.Green
            delay(config.tiempoVerde.toLong())

            _trafficLightState.value = TrafficLightState.Yellow
            delay(config.tiempoAmarillo.toLong())

            _trafficLightState.value = TrafficLightState.Red
            _carCount.value += 1
            _sensorActive.value = false
            _doorOpen.value = false
            guardarEstadoEnFirebase()
        }
    }


    fun setMaxCapacity(value: Int) {
        _maxCapacity.value = value
    }

    fun reiniciarContador() {
        _carCount.value = 0
    }

    fun toggleSensorAutoMode() {
        _sensorAutoMode.value = !_sensorAutoMode.value
    }

    fun guardarEstadoEnFirebase() {
        viewModelScope.launch {
            val estado = mapOf(
                "carCount" to _carCount.value,
                "maxCapacity" to _maxCapacity.value,
                "sensorAutoMode" to _sensorAutoMode.value
            )
            FirebaseDatabase.getInstance()
                .reference.child("estado")
                .setValue(estado)
        }
    }

    fun cargarEstadoDesdeFirebase() {
        viewModelScope.launch {
            val snapshot = FirebaseDatabase.getInstance()
                .reference.child("estado")
                .get().await()

            snapshot.child("carCount").getValue(Int::class.java)?.let {
                _carCount.value = it
            }
            snapshot.child("maxCapacity").getValue(Int::class.java)?.let {
                _maxCapacity.value = it
            }
            snapshot.child("sensorAutoMode").getValue(Boolean::class.java)?.let {
                _sensorAutoMode.value = it
            }
        }
    }

    fun setTrafficLightState(state: TrafficLightState) {
        _trafficLightState.value = state
    }

    fun resetSensorAndDoor() {
        _sensorActive.value = false
        _doorOpen.value = false
    }

}

enum class TrafficLightState {
    Red, Yellow, Green
}
