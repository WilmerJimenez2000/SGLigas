package com.example.sgligas.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.sgligas.R
import com.example.sgligas.consultaBaseDeDatos
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class RecuperarContrasena : AppCompatActivity() {
    private lateinit var edtCorreo: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_contrasena)

        edtCorreo = findViewById(R.id.edtCodigoVer)
        val imgreturn: ImageView = findViewById(R.id.imgreturn)
        imgreturn.setOnClickListener {
            irActividad(Login::class.java)
        }
        val btnComprobar: Button = findViewById(R.id.btnverificarcode)
        btnComprobar.setOnClickListener {
            comprobarUsuario(it)
        }
    }

    fun comprobarUsuario(view: View) {
        val correoUsuario = edtCorreo.text.toString()
        // Se verifica si el formato del correo electrónico es válido
        if (!Patterns.EMAIL_ADDRESS.matcher(correoUsuario).matches()) {
            Toast.makeText(
                applicationContext,
                "Ingrese un correo electrónico válido",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val url = consultaBaseDeDatos.obtenerURLConsulta("recuperar_contraseña.php")
        val client = OkHttpClient()
        val formBody = FormBody.Builder()
            .add("correoUsuario", correoUsuario)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(
                        applicationContext,
                        "Error en la solicitud HTTP",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                response.close()

                runOnUiThread {
                    Log.d("HTTP Response", responseBody ?: "")
                    val gson = Gson()
                    val jsonData = gson.fromJson(responseBody, JsonResponse::class.java)
                    if (jsonData.success) {
                        val correoUsuario = jsonData.correoUsuario
                        val codigoVerificacion = jsonData.codigoVerificacion
                        irActividad1(VerificarCodigo::class.java, correoUsuario, codigoVerificacion)
                    } else {
                        Toast.makeText(applicationContext, jsonData.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })
    }

    fun irActividad1(clase: Class<*>, correo: String, codigo: String) {
        val intent = Intent(this, clase).apply {
            putExtra("correoUsuario", correo)
            putExtra("codigoVerificacion", codigo)
        }
        startActivity(intent)
    }

    fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }

    data class JsonResponse(
        val success: Boolean,
        val message: String,
        val correoUsuario: String,
        val codigoVerificacion: String
    )
}
