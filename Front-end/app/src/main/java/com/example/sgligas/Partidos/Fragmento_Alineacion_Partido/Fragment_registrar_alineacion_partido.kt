package com.example.sgligas.Partidos.Fragmento_Alineacion_Partido

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.Jugadores.Jugador
import com.example.sgligas.R
import com.example.sgligas.consultaBaseDeDatos
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import kotlin.properties.Delegates

class Fragment_registrar_alineacion_partido : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageView: ImageView
    private var jugadoresEquipoTL: MutableList<Jugador> = mutableListOf()
    private var jugadoresEquipoTV: MutableList<Jugador> = mutableListOf()
    lateinit var adapter: Adaptador_ingresar_alineacion
    private val listaDeJugadoresAlineacion: MutableList<JugadorAlineacion> = mutableListOf()
    private lateinit var nombre: String
    private lateinit var foto: String
    private var posicion by Delegates.notNull<Int>()
    private lateinit var guardarAlineacion: Button
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
    private lateinit var linearLayoutRegistroA: LinearLayout
    private lateinit var linearLayoutNoRegistroA: LinearLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.fragment_registrar_alineacion_partido, container, false)
        linearLayoutNoRegistroA = view.findViewById(R.id.LinearLayout_Primario_RAlineacion)
        linearLayoutRegistroA = view.findViewById(R.id.LinearLayout_Secundario_RAlineacion)


        recyclerView = view.findViewById(R.id.recyclerView_alineacion)
        imageView = view.findViewById(R.id.myImageView)
        guardarAlineacion = view.findViewById(R.id.btn_guardar_alineacion)
        val informacionPartido = File(requireContext().filesDir, "cache_file.txt").readText()



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
            estado = json.getString("estado")

            recuperarInfoEquipoLocal(idEquipoLocal.toString())
            recuperarInfoEquipoVisitante(idEquipoVisitante.toString())

            recuperarAlineacionPartido(idPartidos.toString())
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            Log.e("fragmento", "Error al parsear el JSON de partidos: $informacionPartido")
        }
        val cantidadJugadores = 70

        listaDeJugadoresAlineacion.clear()
        val jugadoresLocales = (1..cantidadJugadores / 2).map {
            listaDeJugadoresAlineacion.add(
                JugadorAlineacion(
                    "Jugador $it",
                    "escudo$it",
                    "Local",
                    "$nombreEquipoLocal",
                    "Invisible",
                    "Si"/* Otros atributos */
                )
            )
        }
        val jugadoresVisitantes = (cantidadJugadores / 2 + 1..cantidadJugadores).map {
            listaDeJugadoresAlineacion.add(
                JugadorAlineacion(
                    "Jugador $it",
                    "escudo$it",
                    "Visitante",
                    "$nombreEquipoVisitante",
                    "Invisible",
                    "Si" /* Otros atributos */
                )
            )
        }



        adapter = Adaptador_ingresar_alineacion(
            listaDeJugadoresAlineacion,
            object : Adaptador_ingresar_alineacion.OnItemClickListener {
                override fun onItemClick(position: Int, jugador: JugadorAlineacion) {
                    mostrarDialogo(jugador)
                    posicion = position
                    // Captura el contenido del RecyclerView como un Bitmap
                    val recyclerViewBitmap: Bitmap = captureRecyclerView(recyclerView)
                    // Establece el Bitmap en el ImageView
                    // imageView.setImageBitmap(recyclerViewBitmap)
                }
            })

        imageView.visibility = View.GONE

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 7) // 4 columnas
        recyclerView.adapter = adapter
        /// Guardar alineacion
        guardarAlineacion.setOnClickListener {
            for (jugador in listaDeJugadoresAlineacion) {
                jugador.fondo = "No"
            }
            val json = Gson().toJson(listaDeJugadoresAlineacion)
            val jsonObject = JSONObject()
            jsonObject.put("alineacion", json)

            registrarAlineacion(idPartidos, jsonObject.toString())


            adapter.notifyDataSetChanged()
        }







        return view
    }

    // Función para capturar una imagen de un RecyclerView
    private fun captureRecyclerView(recyclerView: RecyclerView): Bitmap {
        // Crea un bitmap con el tamaño del RecyclerView
        val bitmap =
            Bitmap.createBitmap(recyclerView.width, recyclerView.height, Bitmap.Config.ARGB_8888)
        // Crea un lienzo para dibujar en el bitmap
        val canvas = Canvas(bitmap)
        // Dibuja el contenido del RecyclerView en el lienzo
        recyclerView.draw(canvas)

        return bitmap
    }

    fun registrarAlineacion(idPartido: Int, alineacion: String) {
        val url = consultaBaseDeDatos.obtenerURLConsulta("7_insertar_alineacion.php")
        val client = OkHttpClient()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("id_partido", idPartido.toString())
            .addFormDataPart("alineacion", alineacion)
        val request = Request.Builder()
            .url(url)
            .post(requestBody.build())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonData = response.body?.string()


                    activity?.runOnUiThread {
                        Toast.makeText(requireContext(), "Registro Existoso", Toast.LENGTH_SHORT)
                            .show()

                        requireActivity().onBackPressed()
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

    fun mostrarDialogo(jugador: JugadorAlineacion) {
        val builder = AlertDialog.Builder(requireContext())
        val equipo = jugador.nombreEquipo

        builder.setTitle("Seleccione Jugador del Equipo $equipo")
        val view = layoutInflater.inflate(R.layout.dialogo_jugador_alineacion, null)
        val lista_jugadores_alineacion: Spinner =
            view.findViewById(R.id.spinner_jugadores_alineacion)
        val adapterTL = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            jugadoresEquipoTL.map { it.nombre }
        )
        adapterTL.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val adapterTV = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            jugadoresEquipoTV.map { it.nombre }
        )
        adapterTV.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        if (jugador.equipo == "Local") {
            lista_jugadores_alineacion.adapter = adapterTL
        } else {
            lista_jugadores_alineacion.adapter = adapterTV
        }

        lista_jugadores_alineacion.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position >= 0) {
                        if (jugador.equipo == "Local") {
                            val jugadorSeleccionado = jugadoresEquipoTL[position]
                            lista_jugadores_alineacion.setSelection(position)
                            nombre = jugadorSeleccionado.nombre
                            foto = jugadorSeleccionado.foto
                        } else {
                            val jugadorSeleccionado = jugadoresEquipoTV[position]
                            lista_jugadores_alineacion.setSelection(position)
                            nombre = jugadorSeleccionado.nombre
                            foto = jugadorSeleccionado.foto
                        }
                    } else {
                        Log.e("Error", "Índice fuera de límites: $position")
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>) {
                    // No se ha seleccionado ningún jugador
                }
            }

        builder.setView(view)
        builder.setPositiveButton("Guardar") { dialog, _ ->
            val jugadorEnPosicion = listaDeJugadoresAlineacion[posicion]
            jugadorEnPosicion.nombre = nombre
            jugadorEnPosicion.escudo = foto
            jugadorEnPosicion.estado = "Visible"
            // Eliminar el jugador seleccionado de la lista original
            if (jugador.equipo == "Local") {
                jugadoresEquipoTL.removeAt(lista_jugadores_alineacion.selectedItemPosition)
                adapterTL.notifyDataSetChanged()
            } else {
                jugadoresEquipoTV.removeAt(lista_jugadores_alineacion.selectedItemPosition)
                adapterTV.notifyDataSetChanged()
            }
            // Notificar al adaptador de RecyclerView sobre el cambio
            adapter.notifyDataSetChanged()
        }

        builder.setNegativeButton("Cancelar") { _, _ -> }
        // Crear y mostrar el AlertDialog
        val alertDialog = builder.create()
        alertDialog.show()
        // Obtener los botones del AlertDialog y cambiar el color del texto
        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        positiveButton.setTextColor(
            ContextCompat.getColor(requireContext(), R.color.textoColorDialogo)
        )
        negativeButton.setTextColor(
            ContextCompat.getColor(requireContext(), R.color.textoColorDialogo)
        )
    }

    fun recuperarInfoEquipoLocal(idEquipo: String) {
        jugadoresEquipoTL.clear()

        jugadoresEquipoTL.add(Jugador(0, "", "", "", 0, 0, "", ""))
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
                            try {
                                val jsonParser = JsonParser()
                                val json: JsonObject = jsonParser.parse(jsonData).asJsonObject


                                if (json.has("datos")) {
                                    val datosArray = json.getAsJsonArray("datos")
                                    // Trabaja con el array de datos
                                    for (jugadorEVisitante in datosArray) {
                                        val jugador = JSONObject(jugadorEVisitante.toString())
                                        val CI = jugador.getInt("CI")
                                        val nombre = jugador.getString("nombre")
                                        val posicion = jugador.getString("posicion")
                                        val fecha_nacimiento = jugador.getString("fecha_nacimiento")
                                        val id_equipo = jugador.getInt("id_equipo")
                                        val estatura = jugador.getInt("estatura")
                                        val estado = jugador.getString("estado")
                                        val foto = jugador.getString("foto")
                                        val objetoJugador = Jugador(
                                            CI,
                                            nombre,
                                            posicion,
                                            fecha_nacimiento,
                                            id_equipo,
                                            estatura,
                                            estado,
                                            foto
                                        )


                                        jugadoresEquipoTL.add(objetoJugador)
                                    }
                                }
                            } catch (e: JsonSyntaxException) {
                                e.printStackTrace()
                                Log.e("fragmento", "Error al parsear el JSON de partidos")
                            }
                        } else {
                            // Se maneja el caso donde "datos" no está presente en la respuesta
                            Log.e("fragmento", "No hay datos en la respuesta")

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

    fun recuperarInfoEquipoVisitante(idEquipo: String) {
        jugadoresEquipoTV.clear()

        jugadoresEquipoTV.add(Jugador(0, "", "", "", 0, 0, "", ""))
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
                            try {
                                val jsonParser = JsonParser()
                                val json: JsonObject = jsonParser.parse(jsonData).asJsonObject


                                if (json.has("datos")) {
                                    val datosArray = json.getAsJsonArray("datos")
                                    for (jugadorEVisitante in datosArray) {
                                        val jugador = JSONObject(jugadorEVisitante.toString())
                                        val CI = jugador.getInt("CI")
                                        val nombre = jugador.getString("nombre")
                                        val posicion = jugador.getString("posicion")
                                        val fecha_nacimiento = jugador.getString("fecha_nacimiento")
                                        val id_equipo = jugador.getInt("id_equipo")
                                        val estatura = jugador.getInt("estatura")
                                        val estado = jugador.getString("estado")
                                        val foto = jugador.getString("foto")
                                        val objetoJugador = Jugador(
                                            CI,
                                            nombre,
                                            posicion,
                                            fecha_nacimiento,
                                            id_equipo,
                                            estatura,
                                            estado,
                                            foto
                                        )


                                        jugadoresEquipoTV.add(objetoJugador)
                                    }
                                }
                            } catch (e: JsonSyntaxException) {
                                e.printStackTrace()
                                Log.e("fragmento", "Error al parsear el JSON de partidos")
                            }
                        } else {
                            // Manejar el caso donde "datos" no está presente en la respuesta
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

    fun recuperarAlineacionPartido(idPartido: String) {
        val url = consultaBaseDeDatos.obtenerURLConsulta("7_mostrar_alineacion.php")
        val client = OkHttpClient()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("id_partido", idPartido)
        val request = Request.Builder()
            .url(url)
            .post(requestBody.build())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val jsonData = response.body?.string()



                try {
                    val jsonObject = JSONObject(jsonData.toString())



                    if (jsonObject.has("datos")) {
                        Log.e("si hay datos ########", "")

                        activity?.runOnUiThread {
                            linearLayoutRegistroA.visibility = View.VISIBLE
                            linearLayoutNoRegistroA.visibility = View.INVISIBLE
                        }
                    } else {
                        Log.e("No ", "tiene datos")

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                // Se maneja los errores de conexión
                Log.e("Error de conexión", "Falló la conexión: ${e.message}")
            }
        })
    }
}