package com.example.sgligas.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.sgligas.MainActivity
import com.example.sgligas.R
import com.example.sgligas.consultaBaseDeDatos
import okhttp3.*
import java.io.IOException

class CambiarContrasena : AppCompatActivity() {
    private lateinit var contrasena1: EditText
    private lateinit var contrasena2: EditText
    private var correoUsuario: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambiar_contrasena)

        contrasena1 = findViewById(R.id.edtClave1)
        contrasena2 = findViewById(R.id.edtClave2)

        correoUsuario = intent.getStringExtra("correoUsuario") ?: ""
        val imgreturn: ImageView = findViewById(R.id.imgreturn2)
        imgreturn.setOnClickListener {
            irActividad(MainActivity::class.java)
        }
        val cambiar: Button = findViewById(R.id.btnCambiar)
        cambiar.setOnClickListener {
            validarContraseñas()
        }
    }

    private fun validarContraseñas() {
        val clave1 = contrasena1.text.toString()
        val clave2 = contrasena2.text.toString()

        if (clave1.isEmpty() || clave2.isEmpty()) {
            Toast.makeText(this, "Las contraseñas no pueden estar vacías", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (clave1.length < 8 || clave2.length < 8) {
            Toast.makeText(
                this,
                "Las contraseñas deben tener al menos 8 dígitos",
                Toast.LENGTH_SHORT
            ).show()
            limpiarCampos()
            return
        }

        if (clave1 == clave2) {
            // Las contraseñas coinciden
            cambiarContraseña(correoUsuario, clave1)
        } else {
            // Las contraseñas no coinciden
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        }
    }

    private fun limpiarCampos() {
        contrasena1.text.clear()
        contrasena2.text.clear()
    }

    private fun cambiarContraseña(correo: String, nuevaContraseña: String) {
        val url = consultaBaseDeDatos.obtenerURLConsulta("cambiar_contrasena.php")
        val client = OkHttpClient()
        val formBody = FormBody.Builder()
            .add("correo", correo)
            .add("contrasena", nuevaContraseña)
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
                    if (response.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "Contraseña cambiada correctamente. Por favor inicie sesión",
                            Toast.LENGTH_SHORT
                        ).show()
                        irActividad(Login::class.java)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Error al cambiar la contraseña",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }

    fun irActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}
