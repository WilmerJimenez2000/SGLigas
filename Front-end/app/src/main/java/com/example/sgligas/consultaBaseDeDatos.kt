package com.example.sgligas

import android.util.Log


object consultaBaseDeDatos {

    // Declara una función que retorna un valor y se puede acceder desde cualquier clase
    fun obtenerURLConsulta(archivoPHP: String): String {
        // Implementa la lógica para obtener la información aquí
        val url = "http://192.168.100.13/tesis/SGLigas/Modelo/$archivoPHP"
        //val url = "http://172.29.128.113/tesis/SGLigas/Modelo/$archivoPHP"
        //val url = "http://172.29.128.106/tesis/SGLigas/Modelo/$archivoPHP"

        Log.e("Este es es link que se llama", "$url")

        return url



    }
}