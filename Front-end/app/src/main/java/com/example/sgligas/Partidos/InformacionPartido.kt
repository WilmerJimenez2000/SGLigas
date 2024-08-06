package com.example.sgligas.Partidos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.sgligas.FragmentAdapter
import com.example.sgligas.Equipos.InformacionEquipo
import com.example.sgligas.Partidos.Fragmento_Alineacion_Partido.Fragment_alineacion_partido
import com.example.sgligas.Partidos.Fragmento_Alineacion_Partido.Fragment_registrar_alineacion_partido
import com.example.sgligas.Partidos.Fragmento_Informacion_Partido.Fragment_informacion_partido
import com.example.sgligas.Partidos.Fragmento_Estadisticas_Partido.Fragment_registrar_estadisticas
import com.example.sgligas.Partidos.Fragmento_Resultado_Partido.Fragment_registrar_partido
import com.example.sgligas.R
import com.example.sgligas.consultaBaseDeDatos
import com.google.android.material.tabs.TabLayout
import com.google.gson.JsonSyntaxException
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import kotlin.properties.Delegates

class InformacionPartido : AppCompatActivity() {
    private lateinit var boton_voler: ImageButton
    private lateinit var nombreELocal: TextView
    private lateinit var nombreEVisitante: TextView
    private lateinit var resultadoPartido: TextView
    private lateinit var fechaPartido: TextView
    private lateinit var horaPartido: TextView
    private lateinit var escudoLocal: ImageView
    private lateinit var escudoVisitante: ImageView
    private lateinit var fragmentAdapter: FragmentAdapter
    private var idPartidos by Delegates.notNull<Int>()
    private lateinit var nombreEquipoLocal: String
    private lateinit var escudoEquipoLocal: String
    private var idEquipoLocal by Delegates.notNull<Int>()
    private var golesEquipoLocal by Delegates.notNull<Int>()
    private var golesEquipoVisitante by Delegates.notNull<Int>()
    private var idEquipoVisitante by Delegates.notNull<Int>()
    private lateinit var nombreEquipoVisitante: String
    private lateinit var escudoEquipoVisitante: String
    private lateinit var fecha: String
    private lateinit var hora: String
    private lateinit var vocal: String
    private lateinit var veedor: String
    private lateinit var cancha: String
    private lateinit var estado: String
    private lateinit var idTorneo: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacion_partido)

        boton_voler = findViewById(R.id.btn_regresar)
        boton_voler.setOnClickListener {
            onBackPressed()
        }


        idTorneo = intent.getStringExtra("idTorneo").toString()

        val informacionPartido = File(this@InformacionPartido.filesDir, "cache_file.txt").readText()




        try {
            val json = JSONObject(informacionPartido)


            idPartidos = json.getInt("id_partidos")
            nombreEquipoLocal = json.getString("nombre_equipo_local")
            escudoEquipoLocal = json.getString("escudo_equipo_local")
            idEquipoLocal = json.getInt("id_equipo_local")
            golesEquipoLocal = json.getInt("goles_equipo_local")
            golesEquipoVisitante = json.getInt("goles_equipo_visitante")
            idEquipoVisitante = json.getInt("id_equipo_visitante")
            nombreEquipoVisitante = json.getString("nombre_equipo_visitante")
            escudoEquipoVisitante = json.getString("escudo_equipo_visitante")
            fecha = json.getString("fecha")
            hora = json.getString("hora")
            vocal = json.getString("vocal")
            veedor = json.getString("veedor")
            cancha = json.getString("cancha")
            estado = json.getString("estado")

        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            Log.e("fragmento", "Error al parsear el JSON de partidos: $informacionPartido")
        }



        nombreELocal = findViewById(R.id.nombre_Elocal)
        nombreEVisitante = findViewById(R.id.nombre_EVisitante)
        resultadoPartido = findViewById(R.id.textview_resultado_partido)
        fechaPartido = findViewById(R.id.textView_fecha_partido)
        horaPartido = findViewById(R.id.textView_hora_partido)
        escudoLocal = findViewById(R.id.imagen_logo_equipo1)
        escudoVisitante = findViewById(R.id.imagen_logo_equipo2)



        Picasso.get().load(escudoEquipoLocal).into(escudoLocal)
        Picasso.get().load(escudoEquipoVisitante).into(escudoVisitante)




        nombreELocal.text = nombreEquipoLocal
        nombreEVisitante.text = nombreEquipoVisitante
        resultadoPartido.text = "${golesEquipoLocal} - ${golesEquipoVisitante}"
        fechaPartido.text = fecha
        horaPartido.text = hora




        escudoLocal.setOnClickListener {
            irActividadInfor(InformacionEquipo::class.java, idEquipoLocal)
        }


        escudoVisitante.setOnClickListener {
            irActividadInfor(InformacionEquipo::class.java, idEquipoVisitante)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarUsuario()
        if (jsonUsuario != "presidente") {
            obtenerJugadoresEquipoLocal(idEquipoLocal.toString())
            obtenerJugadoresEquipoVisitante(idEquipoVisitante.toString())
        }
        cargarDatos(vocal, veedor, cancha)
    }

    fun cargarDatos(vocal: String, veedor: String, cancha: String) {
        var viewPager = findViewById(R.id.viewPager_partido) as ViewPager
        var tablayout = findViewById(R.id.tablayout_partido) as TabLayout

        fragmentAdapter = FragmentAdapter(supportFragmentManager)






        if (jsonUsuario == "presidente") {
            comprobarResgistroEstadisticas(idTorneo.toInt(), idPartidos)
            val bundle = Bundle()


            val fragmentRegistrarPartido = Fragment_registrar_partido()
            fragmentRegistrarPartido.arguments = bundle
            val fragmentRegistrarEstadisticas = Fragment_registrar_estadisticas()
            fragmentRegistrarEstadisticas.arguments = bundle



            fragmentAdapter.addFragment(
                Fragment_registrar_alineacion_partido(), "REGISTRAR ALINEACIÓN"
            )
            fragmentAdapter.addFragment(fragmentRegistrarPartido, "REGISTRAR RESULTADO")
            fragmentAdapter.addFragment(fragmentRegistrarEstadisticas, "REGISTRAR ESTADÍSTICAS")

            comprobarResgistroEstadisticas(idTorneo.toInt(), idPartidos)
        } else {
            val bundle = Bundle()

            bundle.putString("vocal", vocal)
            bundle.putString("veedor", veedor)
            bundle.putString("cancha", cancha)
            val fragmentInfoPartido = Fragment_informacion_partido()
            fragmentInfoPartido.arguments = bundle

            fragmentAdapter.addFragment(fragmentInfoPartido, "INFO")
            fragmentAdapter.addFragment(Fragment_alineacion_partido(), "ALINEACIÓN")
        }

        viewPager.adapter = fragmentAdapter
        tablayout.setupWithViewPager(viewPager)


        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                val fragmentTitle = fragmentAdapter.getPageTitle(position).toString()
                if (fragmentTitle == "REGISTRAR ESTADÍSTICAS") {
                    // Se encuentra la posición del fragmento "REGISTRAR RESULTADO"
                    if (estado != "jugado") {
                        Toast.makeText(
                            this@InformacionPartido,
                            "Registre lo resultados, para poder registrar las estadísticas.",
                            Toast.LENGTH_SHORT
                        ).show()
                        val resultFragmentPosition = getFragmentPosition("REGISTRAR RESULTADO")
                        if (resultFragmentPosition != -1) {
                            // Se cambia al fragmento "REGISTRAR RESULTADO"
                            viewPager.currentItem = resultFragmentPosition
                        }
                    } else {
                    }
                }
            }
        })
    }

    fun obtenerJugadoresEquipoLocal(idEquipo: String) {
        val url = consultaBaseDeDatos.obtenerURLConsulta("5_mostrar_jugadores.php")
        val client = OkHttpClient()
        val requestBody = FormBody.Builder().add("id_equipo", idEquipo).build()
        val request = Request.Builder().url(url).post(requestBody).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonData = response.body?.string()
                    try {
                        val jsonObject = JSONObject(jsonData.toString())



                        if (jsonObject.has("datos")) {
                            val fileName = "cache_ELocal.txt"
                            val filePath = File(this@InformacionPartido.filesDir, fileName)
                            val nuevosDatos = jsonData ?: ""

                            Log.e("este es el guardado del equipo local", "$jsonData")

                            filePath.writeText(nuevosDatos)
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

    fun obtenerJugadoresEquipoVisitante(idEquipo: String) {
        val url = consultaBaseDeDatos.obtenerURLConsulta("5_mostrar_jugadores.php")
        val client = OkHttpClient()
        val requestBody = FormBody.Builder().add("id_equipo", idEquipo).build()
        val request = Request.Builder().url(url).post(requestBody).build()


        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonData = response.body?.string()




                    try {
                        val jsonObject = JSONObject(jsonData.toString())



                        if (jsonObject.has("datos")) {
                            val fileName = "cache_EVisitante.txt"
                            val filePath = File(this@InformacionPartido.filesDir, fileName)
                            val nuevosDatos = jsonData ?: ""
                            Log.e("este es el guardado del equipo Visitante", "$jsonData")


                            filePath.writeText(nuevosDatos)
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
        val intent = Intent(this@InformacionPartido, clase)
        startActivity(intent)
    }

    fun irActividadInfor(clase: Class<*>, id: Int) {
        val intent = Intent(this@InformacionPartido, clase)


        intent.putExtra("ID", id)

        startActivity(intent)
    }

    // Método para obtener la posición del fragmento por su título
    private fun getFragmentPosition(title: String): Int {
        for (i in 0 until fragmentAdapter.count) {
            if (fragmentAdapter.getPageTitle(i) == title) {
                return i
            }
        }
        return -1 // Retorna -1 si no se encuentra el fragmento
    }

    private lateinit var jsonUsuario: String
    private fun cargarUsuario() {
        val archivoUsuario = File(this.filesDir, "cache_user.txt")

        if (archivoUsuario.exists()) {
            val UsuarioLogin = archivoUsuario.readText()
            val json = JSONObject(UsuarioLogin)

            jsonUsuario = json.getString("tipo_usuario")

            if (estado == "porjugar") {
                resultadoPartido.text = "-"
            }
        }
    }

    private fun comprobarResgistroEstadisticas(idTorneo: Int, idPartidos: Int) {
        val url = consultaBaseDeDatos.obtenerURLConsulta("7_comprobar_estadisticas_jugador.php")
        val client = OkHttpClient()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("id_torneo", idTorneo.toString())
            .addFormDataPart("id_partido", idPartidos.toString())
        val request = Request.Builder()
            .url(url)
            .post(requestBody.build())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonData = response.body?.string()

                    Log.e("resultado de la comprobacion", "$jsonData")
                    jsonData?.let {
                        val jsonObject = JSONObject(it)
                        val existenRegistros = jsonObject.optBoolean("existen_registros")
                        val noExistenRegistros = jsonObject.optBoolean("no_existen_registros")
                        if (existenRegistros) {
                            guardarComprobacionEstadisticas(jsonData)
                        } else if (noExistenRegistros) {
                            guardarComprobacionEstadisticas(jsonData)
                        } else {
                            println("Ni existen registros ni no existen registros")
                        }
                    }
                } else {
                    Log.e("Error respuesat HTTP", "Fragment registrar estadísticas")
                }
            }

            override fun onFailure(call: Call, e: IOException) {

// Se maneja los errores de conexión
                Log.e("Error de conexión", "Falló la conexión: ${e.message}")
            }
        })
    }

    fun guardarComprobacionEstadisticas(resultado: String) {
        val fileName = "cache_ComEst.txt"
        val filePath = File(this@InformacionPartido.filesDir, fileName)
        val nuevosDatos = resultado

        filePath.writeText(nuevosDatos)
    }
}
