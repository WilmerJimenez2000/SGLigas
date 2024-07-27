package com.example.sgligas.Inicio

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.sgligas.*
import com.example.sgligas.Login.Login
import com.example.sgligas.Login.RecuperarContrasena
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException

class Fragment_perfil : Fragment() {


    private lateinit var correoUsuario: TextView
    private lateinit var cerrarSesion: Button
    private lateinit var cambiarContrasena: Button
    private lateinit var irIniciarSesion: Button
    private lateinit var eliminarCuenta: Button

    private lateinit var correo: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view= inflater.inflate(R.layout.fragment_perfil, container, false)




        correoUsuario= view.findViewById(R.id.TextView_correo_perfil)
        cerrarSesion= view.findViewById(R.id.btn_cerrar_sesion)
        cambiarContrasena= view.findViewById(R.id.btn_cambiar_contrasena)
        irIniciarSesion=view.findViewById(R.id.btn_ir_a_iniciar_sesion)
        eliminarCuenta=view.findViewById(R.id.btn_eliminar_cuenta)

        irIniciarSesion.setOnClickListener {



                val archivoUsuario = File(requireContext().filesDir, "cache_user.txt")

                if (archivoUsuario.exists()) {
                    // El archivo existe, intenta borrarlo
                    if (archivoUsuario.delete()) {
                        // Borrado exitoso
                        // Realiza acciones adicionales si es necesario
                        Log.i("Borrado", "El archivo cache_user.txt ha sido borrado exitosamente.")
                    } else {
                        // Fallo al borrar el archivo
                        // Realiza acciones alternativas o maneja el caso según sea necesario
                        Log.e("Error de borrado", "Fallo al intentar borrar el archivo cache_user.txt.")
                    }
                } else {
                    // El archivo no existe
                    // Realiza acciones alternativas o maneja el caso según sea necesario
                    Log.i("No existe", "El archivo cache_user.txt no existe en el directorio.")
                }

                irActividad(Login::class.java)


        }


        cerrarSesion.setOnClickListener {


            val archivoUsuario = File(requireContext().filesDir, "cache_user.txt")

            if (archivoUsuario.exists()) {
                // El archivo existe, intenta borrarlo
                if (archivoUsuario.delete()) {
                    // Borrado exitoso
                    // Realiza acciones adicionales si es necesario
                    Log.i("Borrado", "El archivo cache_user.txt ha sido borrado exitosamente.")
                } else {
                    // Fallo al borrar el archivo
                    // Realiza acciones alternativas o maneja el caso según sea necesario
                    Log.e("Error de borrado", "Fallo al intentar borrar el archivo cache_user.txt.")
                }
            } else {
                // El archivo no existe
                // Realiza acciones alternativas o maneja el caso según sea necesario
                Log.i("No existe", "El archivo cache_user.txt no existe en el directorio.")
            }





            irActividad(Login::class.java)


        }

        cambiarContrasena.setOnClickListener {
            irActividad(RecuperarContrasena::class.java)
        }


        eliminarCuenta.setOnClickListener {

            mostrarDialogo(correo)
        }





        val archivoUsuario = File(requireContext().filesDir, "cache_user.txt")

        if (archivoUsuario.exists()) {

            val usuario_perfil = archivoUsuario.readText()


// Parsear el JSON
            val jsonParser = JsonParser()
            val json: JsonObject = jsonParser.parse(usuario_perfil).asJsonObject

// Obtener los atributos
            correo = json.get("correo").asString
            val tipoUsuario = json.get("tipo_usuario").asString



            if(tipoUsuario=="invitado"){


                cambiarContrasena.visibility= View.GONE
                cerrarSesion.visibility= View.GONE
                irIniciarSesion.visibility= View.VISIBLE
                eliminarCuenta.visibility= View.GONE



            }else{
                correoUsuario.text = correo

            }




            Log.e("este es la info ahoriaaaaaa", "$correo , $tipoUsuario")

        }else{



        }





        return view
    }



    fun mostrarDialogo(correo : String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Eliminar Cuenta")
        val view = layoutInflater.inflate(R.layout.dialogo_eliminar_cuenta, null)





        builder.setView(view)

        builder.setPositiveButton("Aceptar") { dialog, _ ->


            Log.e("Se acepto eliminar  la cuenta de: ", "$correo")

            eliminarCuenta(correo)



        }



        builder.setNegativeButton("Cancelar") { _, _ ->

            Log.e("Se cancelo eliminar  la cuenta de: ", "$correo")

        }

        // Crear el AlertDialog
        val alertDialog = builder.create()

        // Mostrar el AlertDialog
        alertDialog.show()


        // Obtener los botones del AlertDialog
        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        // Cambiar el color del texto de los botones
        positiveButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.textoColorDialogo
            )
        ) // Cambia R.color.textColor al color que desees
        negativeButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.textoColorDialogo
            )
        ) // Cambia R.color.textColor al color que desees
    }



    private fun eliminarCuenta(correo: String) {

        val url= consultaBaseDeDatos.obtenerURLConsulta("1_eliminar_usuario_hincha.php")

        val client = OkHttpClient()

        val requestBody = FormBody.Builder()
            .add("correo", correo)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonData = response.body?.string()



                    try {
                        val jsonObject = JSONObject(jsonData)

                        activity?.runOnUiThread {
                            mostrarMensaje("Se ha eliminado la cuenta correctamente")


                            val archivoUsuario = File(requireContext().filesDir, "cache_user.txt")

                            if (archivoUsuario.exists()) {
                                // El archivo existe, intenta borrarlo
                                if (archivoUsuario.delete()) {
                                    // Borrado exitoso
                                    // Realiza acciones adicionales si es necesario
                                    Log.i("Borrado", "El archivo cache_user.txt ha sido borrado exitosamente.")
                                } else {
                                    // Fallo al borrar el archivo
                                    // Realiza acciones alternativas o maneja el caso según sea necesario
                                    Log.e("Error de borrado", "Fallo al intentar borrar el archivo cache_user.txt.")
                                }
                            } else {
                                // El archivo no existe
                                // Realiza acciones alternativas o maneja el caso según sea necesario
                                Log.i("No existe", "El archivo cache_user.txt no existe en el directorio.")
                            }



                            irActividad(Login::class.java)

                        }


                    } catch (e: JSONException) {
                        e.printStackTrace()

                    }
                } else {
                    // Manejar errores en la respuesta HTTP
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                // Manejar errores de conexión
            }
        })
    }



    fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(requireContext(), clase)
        startActivity(intent)
    }


    fun mostrarMensaje(mensaje: String) {
        // Aquí puedes mostrar el mensaje de la manera que prefieras,
        // por ejemplo, usando un AlertDialog o un Toast.
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
    }






}