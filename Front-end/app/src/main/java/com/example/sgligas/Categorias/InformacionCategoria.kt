package com.example.sgligas.Categorias

import com.example.sgligas.Categorias.Fragmento_Equipos_Categoria.Fragment_equipos_categoria
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.example.sgligas.Categorias.Fragmento_Torneo_Categoria.Fragment_torneos_categoria
import com.example.sgligas.FragmentAdapter
import com.example.sgligas.R
import com.example.sgligas.consultaBaseDeDatos
import com.google.android.material.tabs.TabLayout
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.File

class InformacionCategoria : AppCompatActivity() {
    private lateinit var regresar: ImageButton
    private lateinit var nombre_categoria: TextView
    private lateinit var fragmentAdapter: FragmentAdapter
    private lateinit var numeroEquipos: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacion_categoria)

        regresar = findViewById(R.id.btn_regresar)

        regresar.setOnClickListener {

            onBackPressed()
        }
        val id = intent.getIntExtra("ID", -1)
        val nombreCategoria = intent.getStringExtra("nombre_categoria")
        numeroEquipos = intent.getIntExtra("num_equipos", -1).toString()


        obtenerTorneosCategoria(id.toString())

        nombre_categoria = findViewById(R.id.nombre_categoria)

        nombre_categoria.text = nombreCategoria.toString().toUpperCase()
    }

    private fun obtenerTorneosCategoria(idCategoria: String) {
        val url1 = consultaBaseDeDatos.obtenerURLConsulta("6_mostar_torneos.php")
        val client1 = OkHttpClient()
        val requestBody1 = FormBody.Builder()
            .add("id_categoria", idCategoria)
            .build()
        val request1 = Request.Builder()
            .url(url1)
            .post(requestBody1)
            .build()

        client1.newCall(request1).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonData = response.body?.string()

                    obtenerEquiposCategoria(jsonData.toString(), idCategoria)
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

    fun obtenerEquiposCategoria(listaTorneos: String, idCategoria: String) {
        val url = consultaBaseDeDatos.obtenerURLConsulta("4_mostrar_equipos_categoria.php")
        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("id_categoria", idCategoria)  // Se establece los parámetros
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
                            runOnUiThread {
                                cargarDatos(numeroEquipos, jsonData ?: "", listaTorneos)
                            }
                        } else {

                            // Se maneja el caso donde "datos" no está presente en la respuesta

                            Log.e("No hay datos en la respuesta", ".")

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

    fun cargarDatos(numeroEquipos: String, listaEquipos: String, listaTorneos: String) {
        var viewPager = findViewById(R.id.viewPager_categoria) as ViewPager
        var tablayout = findViewById(R.id.tablayout_categoria) as TabLayout



        Log.e("Mensaje", "Estos son los equipos de la categoria $listaEquipos")
        val fileName = "cache_file.txt"
        val filePath = File(this@InformacionCategoria.filesDir, fileName)
        val nuevosDatos = listaEquipos

        filePath.writeText(nuevosDatos)
        val bundle = Bundle()


        bundle.putString("numeroEquipos", numeroEquipos)
        bundle.putString("listaTorneo", listaTorneos)


        Log.e("se ejecuto la funcion de cargar datos en informacion categoria", "$listaTorneos")
        val fragmentEquiposCategoria = Fragment_equipos_categoria()
        fragmentEquiposCategoria.arguments = bundle
        val fragmentTorneosCategoria = Fragment_torneos_categoria()
        fragmentTorneosCategoria.arguments = bundle




        fragmentAdapter = FragmentAdapter(supportFragmentManager)

        fragmentAdapter.addFragment(fragmentTorneosCategoria, "TORNEOS")
        fragmentAdapter.addFragment(fragmentEquiposCategoria, "EQUIPOS")


        viewPager.adapter = fragmentAdapter
        tablayout.setupWithViewPager(viewPager)
    }
}






