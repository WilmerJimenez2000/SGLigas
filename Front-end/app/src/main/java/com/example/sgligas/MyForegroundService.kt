package com.example.sgligas

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MyForegroundService : Service() {
    private val handler = Handler(Looper.getMainLooper())
    private val myRunnable = object : Runnable {
        override fun run() {
            // Aquí va la función que se va a ejecutar
            obtenerTodasLasLigas()
            // Programar la próxima ejecución en 1 minuto (60000 milisegundos)
            handler.postDelayed(this, 60000)
        }
    }

    override fun onCreate() {
        super.onCreate()
        handler.post(myRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(myRunnable)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun obtenerTodasLasLigas() {
        val url = consultaBaseDeDatos.obtenerURLConsulta("3_mostrar_categorias_ligas.php")
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonData = response.body?.string()
                    // Guardar jsonData en cache_cat_ligas.txt
                    jsonData?.let {
                        guardarEnArchivo(it, "cache_cat_ligas.txt")
                    }

                    Log.e("este es lo que da las ligas de todo ", "$jsonData ")
                } else {
                    // Se maneja los errores en la respuesta HTTP
                    Log.e("Error HTTP", "Código: ${response.code}")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                // Se maneja los errores de conexión
                Log.e("Error de conexión", "Falló la conexión: ${e.message}")
            }
        })
    }

    private fun guardarEnArchivo(txt: String, nombreArchivo: String) {
        try {
            val archivo = File(this@MyForegroundService.filesDir, nombreArchivo)
            FileOutputStream(archivo).use { outputStream ->
                outputStream.write(txt.toByteArray())
            }
            Log.d("Archivo", "Archivo guardado en: ${archivo.absolutePath}")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("Archivo", "Error al guardar el archivo: ${e.message}")
        }
    }
}
