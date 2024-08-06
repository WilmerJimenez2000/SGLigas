package com.example.sgligas.Equipos

import Fragment_informacion_equipo
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.example.sgligas.Equipos.Fragmento_Jugadores_Equipo.Fragment_jugadores_equipo
import com.example.sgligas.Equipos.Fragmento_Partidos_Equipo.Fragment_partidos_equipo
import com.example.sgligas.FragmentAdapter
import com.example.sgligas.R
import com.example.sgligas.consultaBaseDeDatos
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException

class InformacionEquipo : AppCompatActivity() {
    private lateinit var boton_voler: ImageButton
    private lateinit var nombre_equipo: TextView
    private lateinit var fragmentAdapter: FragmentAdapter
    private lateinit var imagenEscudo: ImageView
    private lateinit var nombreEquipo: String
    private lateinit var fechaFundacion: String
    private lateinit var presidente: String
    private lateinit var escudo: String
    private lateinit var colores: String
    private lateinit var listapartidosEquipo: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacion_equipo)
        val id = intent.getIntExtra("ID", -1)
        val idTorneo = File(this@InformacionEquipo.filesDir, "cache_t.txt").readText()


        obtenerPartidosEquipo(id.toString(), idTorneo)

        obtenerJugadoresEquipo(id.toString())

        obtenerEstadisticasJugadoresEquipo(id.toString())



        boton_voler = findViewById(R.id.btn_regresar)
        boton_voler.setOnClickListener {

            onBackPressed()
        }


        nombre_equipo = findViewById(R.id.nombre_equipo)
        imagenEscudo = findViewById(R.id.imagen_logo)
    }

    private fun obtenerPartidosEquipo(idEquipo: String, idTorneo: String) {
        val url = consultaBaseDeDatos.obtenerURLConsulta("7_mostrar_partidos_equipo.php")
        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("id_torneo", idTorneo)
            .add("id_equipo", idEquipo)
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
                        val jsonObject = JSONObject(jsonData.toString())



                        if (jsonObject.has("partidos")) {
                            listapartidosEquipo = jsonData ?: ""

                            obtenerInformacionEquipo(idEquipo, listapartidosEquipo, idTorneo)
                        } else {
                            // Se maneja el caso donde "datos" no está presente en la respuesta
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                    // Se maneja errores en la respuesta HTTP

                    Log.e("Error HTTP", "Código: ${response.code}")



                }
            }

            override fun onFailure(call: Call, e: IOException) {

                // Se maneja los errores de conexión
                Log.e("Error de conexión", "Falló la conexión: ${e.message}")
            }
        })
    }

    private fun obtenerInformacionEquipo(
        idEquipo: String,
        listapartidosEquipo: String,
        idTorneo: String
    ) {
        val url = consultaBaseDeDatos.obtenerURLConsulta("4_mostrar_equipo.php")
        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("id_equipo", idEquipo)
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

                        if (jsonObject.has("datos")) {
                            val datosArray = jsonObject.getJSONArray("datos")
                            val jsonInformacionEquipo = datosArray.getString(0)
                            val json = JSONObject(jsonInformacionEquipo)

                            nombreEquipo = json.getString("nombre_equipo")
                            fechaFundacion = json.getString("fecha_fundacion")
                            presidente = json.getString("presidente")
                            escudo = json.getString("escudo")
                            colores = json.getString("colores")



                            runOnUiThread {
                                nombre_equipo.text = nombreEquipo.toUpperCase()
                                Picasso.get().load(escudo).into(imagenEscudo)
                                cargarDatos(
                                    presidente,
                                    fechaFundacion,
                                    colores,
                                    listapartidosEquipo,
                                    idTorneo
                                )
                            }
                        } else {
                            // Se maneja el caso donde "datos" no está presente en la respuesta
                            Log.e("fragmento", "No hay datos en la respuesta")

                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()

                        Log.e("fragmento", "Error al parsear el JSON")

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

    fun obtenerJugadoresEquipo(idEquipo: String) {
        val url = consultaBaseDeDatos.obtenerURLConsulta("5_mostrar_jugadores.php")
        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("id_equipo", idEquipo)
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
                        val jsonObject = JSONObject(jsonData.toString())



                        if (jsonObject.has("datos")) {
                            cargarJugadores(jsonData ?: "")
                        } else {
                            // Se maneja el caso donde "datos" no está presente en la respuesta
                            Log.e("", "No hay datos en la respuesta")

                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()

                        Log.e("", "Error al parsear el JSON")

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

    fun obtenerEstadisticasJugadoresEquipo(idEquipo: String) {
        val url =
            consultaBaseDeDatos.obtenerURLConsulta("5_mostrar_estadisticas_jugador_equipo.php")
        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("id_equipo", idEquipo)
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
                        val jsonObject = JSONObject(jsonData.toString())


                        if (jsonObject.has("datos")) {
                            cargarJugadoresEstadisticas(jsonData ?: "")
                        } else {
                            // Se maneja el caso donde "datos" no está presente en la respuesta
                            Log.e("", "No hay datos en la respuesta")

                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.e("", "Error al parsear el JSON")

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

    fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }

    fun cargarDatos(
        presidente: String,
        fechaFundacion: String,
        colores: String,
        listapartidosEquipo: String,
        idTorneo: String
    ) {
        val fileName = "cache_PEquipo.txt"
        val filePath = File(this@InformacionEquipo.filesDir, fileName)
        val nuevosDatos = listapartidosEquipo
        filePath.writeText(nuevosDatos)
        var viewPager = findViewById(R.id.viewPager_equipo) as ViewPager
        var tablayout = findViewById(R.id.tablayout_equipo) as TabLayout
        val bundle = Bundle()
        bundle.putString("presidente", presidente)
        bundle.putString("fechaFundacion", fechaFundacion)
        bundle.putString("colores", colores)
        bundle.putString("idTorneo", idTorneo)
        val fragmentInfoEquipo = Fragment_informacion_equipo()
        fragmentInfoEquipo.arguments = bundle
        val fragmentPartidosEquipo = Fragment_partidos_equipo()
        fragmentInfoEquipo.arguments = bundle

        fragmentAdapter = FragmentAdapter(supportFragmentManager)







        fragmentAdapter.addFragment(fragmentInfoEquipo, "INFO")
        fragmentAdapter.addFragment(fragmentPartidosEquipo, "PARTIDOS")
        fragmentAdapter.addFragment(Fragment_jugadores_equipo(), "JUGADORES")

        viewPager.adapter = fragmentAdapter
        tablayout.setupWithViewPager(viewPager)
    }

    fun cargarJugadores(listajugadoresEquipo: String) {
        val fileName = "cache_JEquipo.txt"
        val filePath = File(this@InformacionEquipo.filesDir, fileName)
        val nuevosDatos = listajugadoresEquipo

        filePath.writeText(nuevosDatos)
    }

    fun cargarJugadoresEstadisticas(listajugadoresEstadisticasEquipo: String) {
        val fileName = "cache_JEstadisticasEquipo.txt"
        val filePath = File(this@InformacionEquipo.filesDir, fileName)
        val nuevosDatos = listajugadoresEstadisticasEquipo

        filePath.writeText(nuevosDatos)
    }
}
















