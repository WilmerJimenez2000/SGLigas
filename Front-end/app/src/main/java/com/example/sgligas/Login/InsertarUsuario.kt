package com.example.sgligas.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import com.example.sgligas.MainActivity
import com.example.sgligas.R
import com.example.sgligas.consultaBaseDeDatos
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class InsertarUsuario : AppCompatActivity() {
    private lateinit var edtCorreo: EditText
    private lateinit var edtNombre: EditText
    private lateinit var edtContrasena1: EditText
    private lateinit var edtContrasena2: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertar_usuario)

        edtCorreo = findViewById(R.id.edtCorreoReg)
        edtNombre = findViewById(R.id.edtNombreReg)
        edtContrasena1 = findViewById(R.id.edtPass1Reg)
        edtContrasena2 = findViewById(R.id.edtPass2Reg)
        val imgreturn: ImageView = findViewById(R.id.imgreturn)
        imgreturn.setOnClickListener {
            irActividad(MainActivity::class.java)
        }
        val edtIrInicarContrasena: TextView = findViewById(R.id.edtirIniciarsecion)
        edtIrInicarContrasena.setOnClickListener {
            irActividad(Login::class.java)
        }
        val btnRegistrar: Button = findViewById(R.id.btn_registrarse)
        btnRegistrar.setOnClickListener {
            comprobarCredenciales(it)
        }
    }

    fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }

    fun comprobarCredenciales(view: View) {
        val correoUsuario = edtCorreo.text.toString()
        val nombreUsuario = edtNombre.text.toString()
        val contrasena1 = edtContrasena1.text.toString()
        val contrasena2 = edtContrasena2.text.toString()

        if (correoUsuario.isNotBlank() && nombreUsuario.isNotBlank() && contrasena1.isNotBlank() && contrasena2.isNotBlank()) {
            // Se verifica si el formato del correo electrónico es válido
            if (!Patterns.EMAIL_ADDRESS.matcher(correoUsuario)
                    .matches() || contrasena1.length < 8 ||
                !nombreUsuario.matches("[a-zA-Z\\s]+".toRegex())
            ) {
                Toast.makeText(applicationContext, "Datos incorrectos", Toast.LENGTH_SHORT).show()
                limpiarCampos()
                return
            }
            // Se verifica si las contraseñas coinciden
            if (contrasena1 != contrasena2) {
                Toast.makeText(
                    applicationContext,
                    "Las contraseñas no coinciden",
                    Toast.LENGTH_SHORT
                ).show()

                edtContrasena1.text.clear()
                edtContrasena2.text.clear()

                return
            }
            val url = consultaBaseDeDatos.obtenerURLConsulta("1_insertar_usuario.php")
            val client = OkHttpClient()
            val formBody = FormBody.Builder()
                .add("correo", correoUsuario)
                .add("nombre", nombreUsuario)
                .add("password", contrasena1)
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
                            "Error al conectar con el servidor",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val json = response.body?.string()
                    val gson = Gson()
                    val jsonResponse = gson.fromJson(json, JsonResponse::class.java)

                    runOnUiThread {
                        if (jsonResponse.success) {
                            Toast.makeText(
                                applicationContext,
                                "Usuario registrado exitosamente",
                                Toast.LENGTH_SHORT
                            ).show()
                            Toast.makeText(applicationContext, "Inicie sesión", Toast.LENGTH_SHORT)
                                .show()
                            irActividad(Login::class.java)
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "El usuario ya existe",
                                Toast.LENGTH_SHORT
                            ).show()
                            limpiarCampos()
                        }
                    }
                }
            })
        } else {
            Toast.makeText(
                applicationContext,
                "Campos obligatorios vacíos, ingrese datos",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
    }

    data class JsonResponse(val success: Boolean, val noInserto: Boolean)

    private fun limpiarCampos() {
        edtCorreo.text.clear()
        edtNombre.text.clear()
        edtContrasena1.text.clear()
        edtContrasena2.text.clear()
    }
}