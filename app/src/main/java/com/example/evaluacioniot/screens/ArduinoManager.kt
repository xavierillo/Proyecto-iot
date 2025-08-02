package com.example.evaluacioniot.screens

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import java.io.IOException
import java.util.*

@SuppressLint("StaticFieldLeak")
object ArduinoManager {


    private lateinit var context: Context
    private var bluetoothSocket: BluetoothSocket? = null

    private const val DEVICE_NAME = "HC-06"
    private val UUID_SPP: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    fun init(appContext: Context) {
        context = appContext.applicationContext
    }

    fun isConnected(): Boolean {
        return bluetoothSocket?.isConnected == true
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun connect(): Boolean {
        if (isConnected()) return true
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            println("Bluetooth no disponible o no activado")
            return false
        }

        val device: BluetoothDevice? = bluetoothAdapter.bondedDevices.firstOrNull {
            it.name == DEVICE_NAME
        }

        if (device == null) {
            println("Dispositivo '$DEVICE_NAME' no emparejado")
            return false
        }

        return try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID_SPP)
            bluetoothSocket?.connect()
            println("Conectado a $DEVICE_NAME")
            true
        } catch (e: IOException) {
            println("Error al conectar con $DEVICE_NAME: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    fun sendCommand(command: String) {
        try {
            bluetoothSocket?.outputStream?.write(command.toByteArray())
            println("Comando enviado: $command")
        } catch (e: IOException) {
            println("Error al enviar comando: ${e.message}")
        }
    }

    fun disconnect() {
        try {
            bluetoothSocket?.close()
            println("Conexión cerrada")
        } catch (e: IOException) {
            println("Error al cerrar conexión: ${e.message}")
        }
    }
}
