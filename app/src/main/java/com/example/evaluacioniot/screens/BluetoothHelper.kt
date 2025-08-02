package com.example.evaluacioniot.screens

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import androidx.annotation.RequiresPermission
import java.io.IOException
import java.util.*

@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
fun connectToHC06(deviceName: String = "HC-06"): BluetoothSocket? {
    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
        println("Bluetooth no disponible o no activado")
        return null
    }

    val device = bluetoothAdapter.bondedDevices.firstOrNull {
        it.name == deviceName
    } ?: run {
        println("Dispositivo '$deviceName' no emparejado")
        return null
    }

    val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // UUID SPP estándar
    return try {
        val socket = device.createRfcommSocketToServiceRecord(uuid)
        socket.connect()
        println("Conectado a $deviceName")
        socket
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}
