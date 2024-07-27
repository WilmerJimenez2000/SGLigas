package com.example.sgligas.Torneos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.example.sgligas.FragmentAdapter
import com.example.sgligas.R
import com.example.sgligas.Torneos.Fragmento_Posiciones_Torneo.Fragment_posiciones_torneo
import com.example.sgligas.Torneos.Fragmento_Informacion_Torneo.Fragment_informacion_torneo
import com.example.sgligas.Torneos.Fragmento_Partidos_Torneo.Fragment_partidos_torneo
import com.example.sgligas.consultaBaseDeDatos
import com.google.android.material.tabs.TabLayout
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException

class InformacionTorneo : AppCompatActivity() {


    private lateinit var nombreTorneo: TextView

    private lateinit var fragmentAdapter: FragmentAdapter

    private lateinit var etapa: String
    private lateinit var fechaInicio: String
    private lateinit var fechaFin: String
    private lateinit var idTorneo: String
    private lateinit var idCategoria: String
    private lateinit var grupo: String
    private lateinit var numEquipoGrupos: String

    private var tablaPosicion: String? = null


    private lateinit var regresar: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacion_torneo)



        regresar=findViewById(R.id.btn_regresar)
        regresar.setOnClickListener{
            onBackPressed()
        }


        val fileName = "cache_t.txt"
        val filePath = File(this@InformacionTorneo.filesDir, fileName)

        val contenidoArchivo = filePath.readText()

        idTorneo = contenidoArchivo




        etapa=intent.getStringExtra("etapa").toString()
        fechaInicio=intent.getStringExtra("fecha_inicio").toString()
        fechaFin= intent.getStringExtra("fecha_fin").toString()


        idCategoria=intent.getIntExtra("id_categoria",-1).toString()
        grupo= intent.getStringExtra("grupo").toString()
        numEquipoGrupos= intent.getIntExtra("num_equipo_grupos", -1).toString()


        Log.e("Este es el ID de torneo que se prueba","$idTorneo")


        nombreTorneo=findViewById(R.id.nombre_torneo)
        nombreTorneo.text=etapa.toUpperCase()



        obtenerClasificacionTorneo(idTorneo)

        obtenerPartidosTorneo(idTorneo)






    }


    override fun onResume() {
        super.onResume()


        val idTorneo = leerArchivo("cache_t.txt")
        val cargarDatos = leerArchivo("cache_CargarL.txt")

        if (cargarDatos == "True") {
            obtenerPartidosTorneo(idTorneo)
            escribirArchivo("cache_CargarL.txt", "False")
            Log.e("La actividad informacion torneo esta siendo vista de nuevo", "True")

        }

    }



    private fun leerArchivo(fileName: String, defaultValue: String = ""): String {
        val filePath = File(this@InformacionTorneo.filesDir, fileName)
        return if (filePath.exists()) {
            filePath.readText()
        } else {
            defaultValue
        }
    }

    private fun escribirArchivo(fileName: String, content: String) {
        val filePath = File(this@InformacionTorneo.filesDir, fileName)
        filePath.writeText(content)
    }









    private fun obtenerPartidosTorneo(id_Torneo: String) {


        val url = consultaBaseDeDatos.obtenerURLConsulta("7_mostrar_partidos.php")

        val client = OkHttpClient()

        val requestBody = FormBody.Builder()
            .add("id_torneo", id_Torneo)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonData = response.body?.string()

                    Log.e("esta es la  impresion de responde informacio torneo", "$jsonData")



                    try {
                        val jsonObject = JSONObject(jsonData)



                        if (jsonObject.has("partidos")) {


                            runOnUiThread {



                                cargarDatos(fechaInicio, fechaFin, grupo, numEquipoGrupos, jsonData ?: "")



                            }



                        } else {
                            // Manejar el caso donde "datos" no está presente en la respuesta
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


    private fun obtenerClasificacionTorneo(id_Torneo: String){

        val url = consultaBaseDeDatos.obtenerURLConsulta("4_mostrar_tabla_de_posiciones.php")


        val client = OkHttpClient()

        val requestBody = FormBody.Builder()
            .add("id_torneo", id_Torneo)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()



        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonData = response.body?.string().toString()


                    try {
                        val jsonObject = JSONObject(jsonData)



                        if (jsonObject.has("tabla")) {


                            runOnUiThread {

                                // Nombre del archivo en caché
                                val fileName = "cache_tClasificacion.txt"

                                val filePath = File(this@InformacionTorneo.filesDir, fileName)

                                val nuevosDatos = jsonData

                                filePath.writeText(nuevosDatos)

                                val cachedData = filePath.readText()





                            }



                        } else {
                            // Manejar el caso donde "datos" no está presente en la respuesta
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


    fun cargarDatos(fechaInicio: String, fechaFin: String, grupo: String, numEquipoGrupos: String, listaPartidosTorneo: String){

        var viewPager = findViewById (R.id.viewPager_torneo) as ViewPager
        var tablayout = findViewById(R.id.tablayout_torneo) as TabLayout




        // Nombre del archivo en caché
        val fileName = "cache_file.txt"

// Ruta completa del archivo
        val filePath = File(this@InformacionTorneo.filesDir, fileName)

// Datos nuevos para almacenar en caché
        val nuevosDatos = listaPartidosTorneo

// Escribir los nuevos datos en el archivo (reemplazando los datos existentes)
        filePath.writeText(nuevosDatos)

// Recuperar todos los datos de la caché
        val cachedData = filePath.readText()

        ////

        val bundle = Bundle()


        bundle.putString("fechaInicio",fechaInicio)
        bundle.putString("fechaFin", fechaFin)
        bundle.putString("grupo", grupo)
        bundle.putString("numEquipoGrupos", numEquipoGrupos)








        val fragmentInfoTorneo = Fragment_informacion_torneo()
        fragmentInfoTorneo.arguments=bundle

        val fragmentPartidosTorneo= Fragment_partidos_torneo()
        fragmentPartidosTorneo.arguments=bundle



        fragmentAdapter= FragmentAdapter(supportFragmentManager)


        val archivoUsuario = File(this.filesDir, "cache_user.txt")



        if (archivoUsuario.exists()) {

            val UsuarioLogin = archivoUsuario.readText()


            val json = JSONObject(UsuarioLogin)

            val jsonUsuario = json.getString("tipo_usuario")





            if (jsonUsuario == "presidente") {

                fragmentAdapter.addFragment(fragmentPartidosTorneo, "GESTIÓN PARTIDOS")



            }else{

                fragmentAdapter.addFragment(fragmentInfoTorneo, "INFO")


                fragmentAdapter.addFragment(fragmentPartidosTorneo, "PARTIDOS")
                fragmentAdapter.addFragment(Fragment_posiciones_torneo(), "POSICIONES")
            }
        }




        viewPager.adapter=fragmentAdapter
        tablayout.setupWithViewPager(viewPager)




    }



}