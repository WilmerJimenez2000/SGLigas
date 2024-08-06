package com.example.sgligas.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.sgligas.*
import com.example.sgligas.Ligas.InformacionLiga
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException

class Login : AppCompatActivity() {
    private lateinit var edtCorreo: EditText
    private lateinit var edtContrasena: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtCorreo = findViewById(R.id.edtCorreo)
        edtContrasena = findViewById(R.id.edtPassword)

        cargarInicioDeSesion()
        val btnComprobar: Button = findViewById(R.id.btnLogin)
        btnComprobar.setOnClickListener {
            comprobarCredenciales(it)
        }
        val edtOlvidoContrasena: TextView = findViewById(R.id.edtOlvidoContrasena)
        edtOlvidoContrasena.setOnClickListener {
            irActividad(RecuperarContrasena::class.java)
        }
        val edtirRegistro: TextView = findViewById(R.id.edtIrReg)
        edtirRegistro.setOnClickListener {
            irActividad(InsertarUsuario::class.java)
        }
        val continuarInvitado: TextView = findViewById(R.id.textView_continuar_invitado)

        continuarInvitado.setOnClickListener {

            val fileName = "cache_user.txt"
            val filePath = File(this@Login.filesDir, fileName)
            val nuevosDatos =
                "{\"correo\":\"usuario_invitado@gmail.com\",\"tipo_usuario\":\"invitado\"}"

            filePath.writeText(nuevosDatos)




            irActividad(MainActivity::class.java)
        }
    }

    fun comprobarCredenciales(view: View) {
        val correoUsuario = edtCorreo.text.toString().trim()
        val contrasena = edtContrasena.text.toString()


        Log.e("correo", "$correoUsuario")



        if (correoUsuario.isNotBlank() && contrasena.isNotBlank()) {
            // Se verifica si el formato del correo electrónico es válido
            if (!Patterns.EMAIL_ADDRESS.matcher(correoUsuario).matches()) {
                Toast.makeText(applicationContext, "Correo inválido", Toast.LENGTH_SHORT).show()
                limpiarCampos()
                return
            }
        } else {
            Toast.makeText(
                applicationContext,
                "Campos obligatorios vacíos, ingrese datos",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val url = consultaBaseDeDatos.obtenerURLConsulta("1_login.php")
        val client = OkHttpClient()
        val formBody = FormBody.Builder()
            .add("correo", correoUsuario)
            .add("password", contrasena)
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
                    Log.e("HTTP Response", responseBody ?: "")



                    if (responseBody == "{\"success\":false}") {
                        Toast.makeText(
                            applicationContext,
                            "Contraseña incorrecta",
                            Toast.LENGTH_SHORT
                        ).show()

                        limpiarCampos()
                    } else
                        if (responseBody == "{\"correo\":false}") {
                            Toast.makeText(
                                applicationContext,
                                "Usuario no registrado",
                                Toast.LENGTH_SHORT
                            ).show()

                            limpiarCampos()
                        } else {
                            val json = JSONObject(responseBody)
                            val jsonUsuario = json.getString("userData")
                            val usuarioLogin = JSONObject(jsonUsuario)
                            val tipo_usuario = usuarioLogin.getString("tipo_usuario")

                            limpiarCampos()

                            UsuarioLogin(usuarioLogin.toString())



                            if (tipo_usuario == "presidente") {
                                obtenerinformaciónLiga(correoUsuario)
                            } else if (tipo_usuario == "hincha") {
                                irActividadP(MainActivity::class.java)
                            }
                        }
                }
            }
        })
    }

    private fun limpiarCampos() {
        edtCorreo.text.clear()
        edtContrasena.text.clear()
    }

    fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }

    fun irActividad(
        clase: Class<*>,
        id: Int,
        direccion: String,
        fecha_fundacion: String,
        nombre_liga: String
    ) {
        val intent = Intent(this, clase)
        intent.putExtra("ID", id)
        intent.putExtra("direccion", direccion)
        intent.putExtra("fecha_fundacion", fecha_fundacion)
        intent.putExtra("nombre_liga", nombre_liga)// Agregar la ID como un extra al intent
        startActivity(intent)
    }

    fun irActividadP(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }

    fun UsuarioLogin(usuarioLogin: String) {
        val fileName = "cache_user.txt"
        val filePath = File(this@Login.filesDir, fileName)
        val nuevosDatos = usuarioLogin

        filePath.writeText(nuevosDatos)
        val cachedData = filePath.readText()
    }

    fun cargarInicioDeSesion() {
        val archivoUsuario = File(this.filesDir, "cache_user.txt")

        if (archivoUsuario.exists()) {
            val UsuarioLogin = File(this@Login.filesDir, "cache_user.txt").readText()
            val json = JSONObject(UsuarioLogin)
            val jsonUsuario = json.getString("tipo_usuario")
            val correoUsuario = json.getString("correo")




            if (jsonUsuario == "presidente") {
                obtenerinformaciónLiga(correoUsuario)
            } else if (jsonUsuario == "invitado") {
                irActividadP(MainActivity::class.java)
            } else {
                irActividadP(MainActivity::class.java)
            }
        } else {
            Log.e("No","existe el archivo")
        }
    }

    fun obtenerinformaciónLiga(correoUsuario: String) {
        val url = consultaBaseDeDatos.obtenerURLConsulta("2_mostrar_liga_presidente.php")
        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("correo_admin", correoUsuario)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonData = response.body?.string()



                    if (jsonData == "{\"datos\":false}") {
                        runOnUiThread {
                            Toast.makeText(
                                this@Login,
                                "No tiene liga registrada. Por favor, acceda al sistema web para registrarla.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }






                    try {
                        val jsonObject = JSONObject(jsonData)

                        if (jsonObject.has("datos")) {
                            val datosArray = jsonObject.getJSONArray("datos")
                            val ligaJson = datosArray.getJSONObject(0)
                            val nombreLiga = ligaJson.getString("nombre_liga")
                            val fechaFundacion = ligaJson.getString("fecha_fundacion")
                            val direccion = ligaJson.getString("direccion")
                            val id_liga = ligaJson.getInt("id_liga")
                            val estado = ligaJson.getString("estado")

                            if (estado == "activo") {

                                irActividad(
                                    InformacionLiga::class.java,
                                    id_liga,
                                    direccion,
                                    fechaFundacion,
                                    nombreLiga
                                )
                                guardarIdLiga(id_liga.toString())
                            } else {
                            }
                        }


                        runOnUiThread {
                            //Toast.makeText(this@Mostrar_Ligas, "Ingreso a la funcion", Toast.LENGTH_SHORT).show();

                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
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

    fun guardarIdLiga(idLiga: String) {
        val fileName = "cache_idliga.txt"
        val filePath = File(this@Login.filesDir, fileName)
        val nuevosDatos = idLiga

        filePath.writeText(nuevosDatos)
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, MyForegroundService::class.java)
        startService(intent)
    }
}