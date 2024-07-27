package com.example.sgligas.Partidos.Fragmento_Resultado_Partido

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.JsonSyntaxException
import org.json.JSONObject
import java.io.File
import kotlin.properties.Delegates


import android.widget.*
import com.example.sgligas.*
import com.example.sgligas.Jugadores.Jugador
import com.squareup.picasso.Picasso
import okhttp3.*
import java.io.IOException

class Fragment_registrar_partido : Fragment() {

    private lateinit var escudoLocal: ImageView
    private lateinit var escudoVisitante: ImageView

    private lateinit var escudoLocal2: ImageView
    private lateinit var escudoVisitante2: ImageView


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



    private var idTorneo by Delegates.notNull<Int>()
    private lateinit var numeroJornada: String


    private lateinit var boton_registrar: Button

    private lateinit var ngoles_local: EditText
    private lateinit var ngoles_visitante: EditText


    private var glocal: String = ""
    private var gvisitante: String = ""
    private var jugadoresEquipoTL: MutableList<Jugador> = mutableListOf()
    private var jugadoresEquipoTV : MutableList<Jugador> = mutableListOf()


    private lateinit var LinearLayoutPrincipal: LinearLayout
    private lateinit var LinearLayoutSecundario: LinearLayout





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_registrar_partido, container, false)

        escudoLocal = view.findViewById(R.id.imageView_escudo_local)
        escudoVisitante = view.findViewById(R.id.imagen_equipo_visitante)
        escudoLocal2 = view.findViewById(R.id.image_equipo_L_JT)
        escudoVisitante2 = view.findViewById(R.id.image_equipo_V_JT)

        LinearLayoutPrincipal= view.findViewById(R.id.linearLayoutPrincipal)
        LinearLayoutSecundario=view.findViewById(R.id.LinearLayoutSecundario)




        val informacionPartido = File(requireContext().filesDir, "cache_file.txt").readText()

        Log.e("estoy en el fragmento ", "$informacionPartido")


        try {
            val json = JSONObject(informacionPartido)

            numeroJornada = json.getString("numero_jornada")
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
            idTorneo = json.getInt("idTorneo")
            estado=json.getString("estado")


        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            Log.e("fragmento", "Error al parsear el JSON de partidos: $informacionPartido")
        }


        if(estado=="jugado"){


            LinearLayoutPrincipal.visibility= View.GONE
            LinearLayoutSecundario.visibility= View.VISIBLE


        }



        Picasso.get().load(escudoEquipoLocal).into(escudoLocal)
        Picasso.get().load(escudoEquipoVisitante).into(escudoVisitante)
        Picasso.get().load(escudoEquipoLocal).into(escudoLocal2)
        Picasso.get().load(escudoEquipoVisitante).into(escudoVisitante2)


        boton_registrar = view.findViewById(R.id.btn_registrar_partido)
        ngoles_local = view.findViewById(R.id.numero_goles_local)
        ngoles_visitante = view.findViewById(R.id.numero_goles_visitante)






        boton_registrar.setOnClickListener {





            cargarDatosLiga()

            glocal = ngoles_local.text.toString()
            gvisitante = ngoles_visitante.text.toString()

            if (glocal.isEmpty() || gvisitante.isEmpty()) {
                // Al menos un campo está vacío, muestra un mensaje de error
                Toast.makeText(
                    requireContext(),
                    "Por favor, completa todos los campos",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }


            val partido = listOf( "$idEquipoLocal",
                "$glocal",
                "$gvisitante",
                "$idEquipoVisitante",)





            modificarPartido(idTorneo.toString(), idPartidos.toString(), partido)











        }



        return view
    }




    fun modificarPartido(
        idTorneo: String,
        idPartido: String,
        partido: List<String>,

    ) {
        val url = consultaBaseDeDatos.obtenerURLConsulta("7_modificar_resultados_nuevo.php")

        val client = OkHttpClient()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("id_torneo", idTorneo)
            .addFormDataPart(" id_partido", idPartido)
            .addFormDataPart("opcion", "resultados")

        for ((index, value) in partido.withIndex()) {
            requestBody.addFormDataPart("partido[$index]", value)

            Log.e("$index", "$value")
        }



        val request = Request.Builder()
            .url(url)
            .post(requestBody.build())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                // Manejar la respuesta del servidor
                if (response.isSuccessful) {
                    // Éxito
                    val jsonData = response.body?.string()
                    // Procesar la respuesta JSON si es necesario

                    Log.e("estes es lo que sale eeeeeeeeeeeeeee", "$jsonData")

                    activity?.runOnUiThread {

                        Toast.makeText(requireContext(), "Registro Existoso", Toast.LENGTH_SHORT)
                            .show()

                        requireActivity().onBackPressed()


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



    fun limpiarCampos() {
        // Limpiar los campos
        ngoles_local.text.clear()
        ngoles_visitante.text.clear()
    }


    fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(requireContext(), clase)
        startActivity(intent)
    }



    fun cargarDatosLiga(){



            val fileName = "cache_CargarL.txt"

            val filePath = File(requireContext().filesDir, fileName)

            val nuevosDatos = "True"

            filePath.writeText(nuevosDatos)


    }





}






