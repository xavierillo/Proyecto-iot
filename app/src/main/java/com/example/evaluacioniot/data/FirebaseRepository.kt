package com.example.evaluacioniot.data

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.tasks.await

data class SemaforoConfigFirebase(
    var tiempoRojo: Int = 3000,
    var tiempoVerde: Int = 2000,
    var tiempoAmarillo: Int = 1000
)

object FirebaseRepository {
    private val db = FirebaseDatabase.getInstance().reference
    private val configRef = db.child("configuracion")

    suspend fun getConfig(): SemaforoConfigFirebase? {
        return try {
            val snapshot = configRef.get().await()
            snapshot.getValue(SemaforoConfigFirebase::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun setConfig(config: SemaforoConfigFirebase) {
        try {
            configRef.setValue(config).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun updateTiempoRojo(nuevo: Int) {
        try {
            configRef.child("tiempoRojo").setValue(nuevo).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
