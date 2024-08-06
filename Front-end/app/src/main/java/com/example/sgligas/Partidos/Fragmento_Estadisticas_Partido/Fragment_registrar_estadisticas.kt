package com.example.sgligas.Partidos.Fragmento_Estadisticas_Partido

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.*
import com.example.sgligas.Jugadores.Jugador
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONObject
import java.io.File
import java.io.IOException
import kotlin.properties.Delegates
import java.util.concurrent.TimeUnit

class Fragment_registrar_estadisticas : Fragment() {
    private lateinit var escudoLocal: ImageView
    private lateinit var escudoVisitante: ImageView
    private lateinit var lista_jugadores_T_L: Spinner
    private lateinit var lista_jugadores_T_V: Spinner
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
    private var estadisticasJugadoresSeleccionados = ArrayList<EstadisticaJugador>()
    private lateinit var recyclerViewJugadores: RecyclerView
    private lateinit var adapter: AdaptadorJugadorEstadistica
    private var jugadoresEquipoTL: MutableList<Jugador> = mutableListOf()
    private var jugadoresEquipoTV: MutableList<Jugador> = mutableListOf()
    private lateinit var boton_registrar: Button
    private var jugadores: MutableList<EstadisticaJugador> = mutableListOf()
    private var jugadoresAIngresarEstadisticas: MutableList<MutableList<Int>> = mutableListOf()
    private var validacion: Boolean = true
    private lateinit var NGoles: String
    private lateinit var NGolesRecibidos: String
    private lateinit var LinearLayoutPrincipal: LinearLayout
    private lateinit var LinearLayoutSecundaria: LinearLayout
    private lateinit var textoInformeEquipoLocal: EditText
    private lateinit var textoInformeEquipoVisitante: EditText
    private lateinit var nombreArbitro: EditText
    private lateinit var ingresarJugadoresTitulares: Button
    private lateinit var jugadoresSeleccionadosTL: List<Jugador>
    private lateinit var jugadoresSeleccionadosTV: List<Jugador>
    private lateinit var scrollViewTL: ScrollView
    private lateinit var checkBoxJugadoresTL: MutableList<CheckBox>
    private lateinit var scrollViewTV: ScrollView
    private lateinit var checkBoxJugadoresTV: MutableList<CheckBox>
    private lateinit var escudoLocal2: ImageView
    private lateinit var escudoVisitante2: ImageView
    private lateinit var linearLayoutRegistrarEstadisticas: LinearLayout
    private lateinit var mensajeIngreseJT: TextView
    private var savingSnackbar: Snackbar? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_registrar_estadisticas, container, false)
        recuperarInfoEquipoLocal()
        recuperarInfoEquipoVisitante()

        escudoLocal = view.findViewById(R.id.image_equipo_L)
        escudoVisitante = view.findViewById(R.id.image_equipo_V)

        LinearLayoutPrincipal = view.findViewById(R.id.LinearLayout_principal)
        LinearLayoutSecundaria = view.findViewById(R.id.LinearLayout_secundario)
        textoInformeEquipoLocal = view.findViewById(R.id.editText_informe_local)
        textoInformeEquipoVisitante = view.findViewById(R.id.editText_informe_visitante)
        nombreArbitro = view.findViewById(R.id.editText_nombre_arbitro)
        ingresarJugadoresTitulares = view.findViewById(R.id.btn_ingresar_jugadores_titulares)

        linearLayoutRegistrarEstadisticas =
            view.findViewById(R.id.LInearLayoutRegistrarEstadisticas)

        mensajeIngreseJT = view.findViewById(R.id.textView_mensaje)


        ingresarJugadoresTitulares.setOnClickListener {
            mostrarDialogoJugadoresTitulares()
        }
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
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            Log.e("fragmento", "Error al parsear el JSON de partidos: $informacionPartido")
        }


        Picasso.get().load(escudoEquipoLocal).into(escudoLocal)
        Picasso.get().load(escudoEquipoVisitante).into(escudoVisitante)
        boton_registrar = view.findViewById(R.id.btn_registrar_estadisticas)

        lista_jugadores_T_L = view.findViewById(R.id.spinner_jugadores_T_L)
        lista_jugadores_T_V = view.findViewById(R.id.spinner_jugadores_T_V)

        recyclerViewJugadores = view.findViewById(R.id.recycler_jugadores)



        return view
    }

    override fun onResume() {
        super.onResume()



        recuperarInfoEquipoLocal()
        recuperarInfoEquipoVisitante()
        comprobaciónRegistroEstadisticas()


        boton_registrar.setOnClickListener {
            jugadoresSeleccionadosTL = jugadoresSeleccionadosTL.drop(1)
            jugadoresSeleccionadosTV = jugadoresSeleccionadosTV.drop(1)
            val listaCombinadaJT = jugadoresSeleccionadosTL + jugadoresSeleccionadosTV

            jugadoresAIngresarEstadisticas.clear()



            for (jugadorCombinadaJT in listaCombinadaJT) {
                Log.e(
                    "EstadisticasJugador",
                    "CI: ${jugadorCombinadaJT.CI} "
                )

                jugadoresAIngresarEstadisticas.add(
                    mutableListOf(
                        jugadorCombinadaJT.CI,
                        0,
                        0,
                        0,
                        0,
                        0
                    )
                )
            }



            for (jugador in jugadores) {
                // CI del jugador a modificar y nuevas estadísticas
                val nuevasEstadisticas = listOf(
                    jugador.rojas,
                    jugador.amarillas,
                    jugador.goles,
                    jugador.goles_recibidos,
                    jugador.autogoles
                )
                // Modificar el jugador con el CI dado
                for (jugadorT in jugadoresAIngresarEstadisticas) {
                    if (jugadorT[0] == jugador.CI) {
                        for (i in 1 until jugadorT.size) {
                            jugadorT[i] = nuevasEstadisticas[i - 1]
                        }
                        break
                    }
                }
            }


            for (jugador in jugadoresAIngresarEstadisticas) {
                println("CI: ${jugador[0]}, Rojas: ${jugador[1]}, Amarillas: ${jugador[2]}, Goles: ${jugador[3]}, Goles recibidos: ${jugador[4]}, Autogoles: ${jugador[5]}")
            }
            val informeEquipoLocal = textoInformeEquipoLocal.text.toString().trim()
            val informeEquipoVisitante = textoInformeEquipoVisitante.text.toString().trim()
            val nombreDelArbitro = nombreArbitro.text.toString().trim()


            if (informeEquipoLocal.isEmpty() || informeEquipoVisitante.isEmpty()) {
                mostrarMensaje("Por favor ingresa los informes de los equipos")
            } else if (nombreDelArbitro.isEmpty()) {
                mostrarMensaje("Por favor ingresa el nombre del árbitro")
            } else {
                registrarEstadísticasJugadores(
                    idTorneo.toString(),
                    idPartidos.toString(),
                    jugadoresAIngresarEstadisticas
                )

                guardarInformesDeSansciones(
                    idPartidos.toString(),
                    informeEquipoLocal,
                    informeEquipoVisitante,
                    nombreDelArbitro
                )
            }
        }
    }

    fun mostrarMensaje(mensaje: String) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
    }

    fun mostrarDialogo(jugadorSeleccionado: Jugador) {
        val builder = AlertDialog.Builder(requireContext())
        val nombre_Jugador = jugadorSeleccionado.nombre
        builder.setTitle("$nombre_Jugador")
        val view = layoutInflater.inflate(R.layout.dialogo_jugador_estadisticas, null)
        val numeroAmarillas = view.findViewById<EditText>(R.id.numero_amarillas)
        val numeroRojas = view.findViewById<EditText>(R.id.numero_rojas)
        val numeroGoles = view.findViewById<EditText>(R.id.numero_goles)
        val numeroAutogoles = view.findViewById<EditText>(R.id.numero_autogoles)
        val numeroGRecibidos = view.findViewById<EditText>(R.id.numero_goles_recibidos)
        val layoutGoles = view.findViewById<LinearLayout>(R.id.layout_goles)
        val layoutGoleRecibidos = view.findViewById<LinearLayout>(R.id.layout_goles_recibidos)
        val spaceGol = view.findViewById<Space>(R.id.space_gol)
        val spaceGolRecibidos = view.findViewById<Space>(R.id.space_gol_recibidos)


        if (jugadorSeleccionado.posicion == "Portero") {
            layoutGoles.visibility = View.GONE
            spaceGol.visibility = View.GONE
        } else {
            layoutGoleRecibidos.visibility = View.GONE
            spaceGolRecibidos.visibility = View.GONE
        }


        builder.setView(view)

        builder.setPositiveButton("Guardar") { dialog, _ ->
            val NAmarillas = numeroAmarillas.text.toString()
            val NRojas = numeroRojas.text.toString()
            val NAutogoles = numeroAutogoles.text.toString()
            //////
            if (jugadorSeleccionado.posicion == "Portero") {
                NGolesRecibidos = numeroGRecibidos.text.toString()
                NGoles = "0"

                validacion = validarCampos(NAmarillas, NRojas, NAutogoles, NGolesRecibidos)
            } else {
                NGoles = numeroGoles.text.toString()
                NGolesRecibidos = "0"
                validacion = validarCampos(NAmarillas, NRojas, NAutogoles, NGoles)
            }





            if (!validacion) {
                mostrarMensaje("Todos los campos deben estar llenos")
            } else {
                val jugadorEstadistica = EstadisticaJugador(
                    jugadorSeleccionado.nombre,
                    jugadorSeleccionado.CI,
                    0,
                    NRojas.toInt(),
                    NAmarillas.toInt(),
                    NGoles.toInt(),
                    NGolesRecibidos.toInt(),
                    NAutogoles.toInt(),
                    jugadorSeleccionado.posicion,
                    jugadorSeleccionado.foto,
                    0
                )

                estadisticasJugadoresSeleccionados.add(jugadorEstadistica)

                adapter = AdaptadorJugadorEstadistica(
                    estadisticasJugadoresSeleccionados,
                    object : AdaptadorJugadorEstadistica.OnItemClickListener {
                        override fun onItemClick(estadisticaJugador: EstadisticaJugador) {
                            // Aqui se puede manejar el clic en el elemento de la lista

                        }

                        override fun onItemDeleteClick(position: Int) {
                            // Aquí se maneja la eliminación del jugador en la posición indicada
                            adapter.eliminarJugador(position)
                            jugadores = adapter.obtenerListaJugadoresSeleccionados()

                            for (jugador in jugadores) {
                                Log.e(
                                    "EstadisticasJugador",
                                    "Nombre: ${jugador.nombreJ}, TA: ${jugador.amarillas}, TR: ${jugador.rojas}, Goles: ${jugador.goles}, Autogoles: ${jugador.autogoles}"
                                )
                            }
                        }
                    })


                jugadores = adapter.obtenerListaJugadoresSeleccionados()

                for (jugador in jugadores) {
                    Log.e(
                        "EstadisticasJugador",
                        "Nombre: ${jugador.nombreJ}, TA: ${jugador.amarillas}, TR: ${jugador.rojas}, Goles: ${jugador.goles}, Autogoles: ${jugador.autogoles}"
                    )
                }




                recyclerViewJugadores.adapter = adapter
                recyclerViewJugadores.layoutManager = LinearLayoutManager(requireContext())
            }
            // Posicionar el Spinner en el primer jugador después de aceptar
            lista_jugadores_T_L.setSelection(0)
            lista_jugadores_T_V.setSelection(0)
        }



        builder.setNegativeButton("Cancelar") { _, _ ->
            // Posicionar el Spinner en el primer jugador después de cancelar
            lista_jugadores_T_L.setSelection(0)
            lista_jugadores_T_V.setSelection(0)
        }
        val alertDialog = builder.create()
        alertDialog.show()
        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        positiveButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.textoColorDialogo
            )
        )
        negativeButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.textoColorDialogo
            )
        )
    }

    fun mostrarDialogoJugadoresTitulares() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Seleccionar Jugadores Titulares")
        val view = layoutInflater.inflate(R.layout.dialogo_seleccionar_jugadores_titulares, null)

        escudoLocal2 = view.findViewById(R.id.image_equipo_L_JT)
        escudoVisitante2 = view.findViewById(R.id.image_equipo_V_JT)

        scrollViewTL = view.findViewById(R.id.scrollView_jugadoresTL)
        checkBoxJugadoresTL = mutableListOf()
        val linearLayoutJugadoresTL: LinearLayout = view.findViewById(R.id.linearLayout_jugadoresTL)
        // Se agrega el CheckBox para cada jugador
        for ((index, jugador) in jugadoresEquipoTL.withIndex()) {
            val checkBox = CheckBox(requireContext())
            checkBox.text = jugador.nombre
            checkBox.id = index // Asignamos el índice como el ID del CheckBox para identificación
            checkBoxJugadoresTL.add(checkBox)
            linearLayoutJugadoresTL.addView(checkBox)
        }
        // Se obtiene el ColorStateList desde el recurso
        val colorStateListL =
            ContextCompat.getColorStateList(requireContext(), R.color.checkbox_color_selector)
        checkBoxJugadoresTL.forEach { checkBox ->
            checkBox.buttonTintList = colorStateListL
        }

        scrollViewTV = view.findViewById(R.id.scrollView_jugadoresTV)
        checkBoxJugadoresTV = mutableListOf()
        val linearLayoutJugadoresTV: LinearLayout = view.findViewById(R.id.linearLayout_jugadoresTV)
        // Se agrega el CheckBox para cada jugador
        for ((index, jugador) in jugadoresEquipoTV.withIndex()) {
            val checkBox = CheckBox(requireContext())
            checkBox.text = jugador.nombre
            checkBox.id = index // Asignamos el índice como el ID del CheckBox para identificación
            checkBoxJugadoresTV.add(checkBox)
            linearLayoutJugadoresTV.addView(checkBox)
        }
        // Se obtiene el ColorStateList desde el recurso
        val colorStateListV =
            ContextCompat.getColorStateList(requireContext(), R.color.checkbox_color_selector)
        checkBoxJugadoresTV.forEach { checkBox ->
            checkBox.buttonTintList = colorStateListV
        }

        Picasso.get().load(escudoEquipoLocal).into(escudoLocal2)
        Picasso.get().load(escudoEquipoVisitante).into(escudoVisitante2)
        builder.setView(view)
        builder.setPositiveButton("Guardar", null)
        builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
        val alertDialog = builder.create()
        alertDialog.show()
        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            jugadoresSeleccionadosTL =
                obtenerJugadoresSeleccionados(checkBoxJugadoresTL, jugadoresEquipoTL)
            jugadoresSeleccionadosTV =
                obtenerJugadoresSeleccionados(checkBoxJugadoresTV, jugadoresEquipoTV)
            // Validación del número mínimo de jugadores
            if (jugadoresSeleccionadosTL.size < 8) {
                mostrarMensaje("El equipo ${nombreEquipoLocal} no tiene el número mínimo de jugadores.")
            } else if (jugadoresSeleccionadosTV.size < 8) {
                mostrarMensaje("El equipo ${nombreEquipoVisitante} no tiene el número mínimo de jugadores.")
            } else {
                cargarJugadoresTitularesParaEstadisticas()

                Toast.makeText(
                    requireContext(),
                    "Registre las estadísticas de los jugadores titulares",
                    Toast.LENGTH_SHORT
                ).show()

                linearLayoutRegistrarEstadisticas.visibility = View.VISIBLE
                mensajeIngreseJT.visibility = View.GONE

                alertDialog.dismiss()
            }
        }
        positiveButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.textoColorDialogo
            )
        )
        val negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        negativeButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.textoColorDialogo
            )
        )
    }

    private fun guardarInformesDeSansciones(
        idPartido: String,
        informeEquipoLocal: String,
        informeEquipoVisitante: String,
        nombreDelArbitro: String
    ) {
        val url = consultaBaseDeDatos.obtenerURLConsulta("8_insertar_sancion_informe.php")
        val client = OkHttpClient()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("id_partido", idPartido)
            .addFormDataPart("informe_local", informeEquipoLocal)
            .addFormDataPart("informe_visitante", informeEquipoVisitante)
            .addFormDataPart("arbitro", nombreDelArbitro)
        val request = Request.Builder()
            .url(url)
            .post(requestBody.build())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonData = response.body?.string()

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

    private fun comprobaciónRegistroEstadisticas() {
        val fileName = "cache_ComEst.txt"
        val filePath = File(requireContext().filesDir, fileName)

        if (filePath.exists()) {
            try {
                val fileContent = filePath.readText()
                val jsonObject = JSONObject(fileContent)
                val existenRegistros = jsonObject.getBoolean("existen_registros")
                // Se usa el valor booleano directamente aquí
                if (existenRegistros) {
                    LinearLayoutPrincipal.visibility = View.GONE
                    LinearLayoutSecundaria.visibility = View.VISIBLE
                    Log.d("InformacionPartido", "Existen registros: true")
                } else {
                    Log.d("InformacionPartido", "Existen registros: false")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Log.d("InformacionPartido", "El archivo no existe")
        }
    }

    fun registrarEstadísticasJugadores(
        idTorneo: String,
        idPartido: String,
        jugadores: List<List<Any>>
    ) {
        val url = consultaBaseDeDatos.obtenerURLConsulta("7_modificar_resultados_nuevo.php")
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("id_torneo", idTorneo)
            .addFormDataPart("id_partido", idPartido)
            .addFormDataPart("opcion", "jugadores")
        // Se agrega los datos de jugadores al formulario
        for ((indexJugador, jugador) in jugadores.withIndex()) {
            for ((index, value) in jugador.withIndex()) {
                requestBody.addFormDataPart("jugadores[$indexJugador][$index]", value.toString())
            }
        }
        val request = Request.Builder()
            .url(url)
            .post(requestBody.build())
            .build()
        // Mostrar el Snackbar de "Guardando..."
        savingSnackbar = Snackbar.make(
            requireView(),
            "Guardando...",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            show()
        }

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                requireActivity().runOnUiThread {
                    // Cancelar el Snackbar de "Guardando..." y mostrar el de "Registro Exitoso"
                    savingSnackbar?.dismiss()
                    Snackbar.make(
                        requireView(),
                        "Registro Exitoso",
                        Snackbar.LENGTH_SHORT
                    ).show()

                    mostrarMensaje("Registro Exitoso")

                    if (::adapter.isInitialized) {
                        estadisticasJugadoresSeleccionados.clear()
                        adapter.notifyDataSetChanged()
                    } else {
                        Log.e("Error", "El adaptador no está inicializado")
                    }

                    requireActivity().onBackPressed()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                requireActivity().runOnUiThread {
                    // Cancelar el Snackbar de "Guardando..." y mostrar el mensaje de error
                    savingSnackbar?.dismiss()
                    Snackbar.make(
                        requireView(),
                        "Error de conexión",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                Log.e("Error de Conexión", e.message ?: "Error desconocido")
            }
        })
    }

    fun recuperarInfoEquipoLocal() {
        jugadoresEquipoTL.clear()
        val informacionEquipoLocal = File(requireContext().filesDir, "cache_ELocal.txt").readText()




        try {
            val jsonParser = JsonParser()
            val json: JsonObject = jsonParser.parse(informacionEquipoLocal).asJsonObject


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


                    jugadoresEquipoTL.add(objetoJugador)
                }
            }
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            Log.e("fragmento", "Error al parsear el JSON de partidos: $informacionEquipoLocal")
        }
    }

    fun recuperarInfoEquipoVisitante() {
        jugadoresEquipoTV.clear()
        val informacionEquipoVisitante =
            File(requireContext().filesDir, "cache_EVisitante.txt").readText()



        try {
            val jsonParser = JsonParser()
            val json: JsonObject = jsonParser.parse(informacionEquipoVisitante).asJsonObject


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
            Log.e("fragmento", "Error al parsear el JSON de partidos: $informacionEquipoVisitante")
        }
    }

    fun validarCampos(
        NAmarillas: String,
        NRojas: String,
        NAutogoles: String,
        NGolesoRecibidos: String,
        ): Boolean {
        if (NAmarillas.isEmpty() || NRojas.isEmpty() || NAutogoles.isEmpty() || NGolesoRecibidos.isEmpty()) {
            return false
        }




        return true
    }

    // Esta función toma una lista de CheckBox y devuelve una lista de jugadores seleccionados
    fun obtenerJugadoresSeleccionados(
        checkBoxes: List<CheckBox>,
        jugadores: List<Jugador>
    ): MutableList<Jugador> {
        // Inicializa la lista con un jugador vacío al principio
        val jugadoresSeleccionados = mutableListOf(Jugador(0, "", "", "", 0, 0, "", ""))
        // Itera sobre los CheckBoxes y agrega los jugadores seleccionados a la lista
        for ((index, checkBox) in checkBoxes.withIndex()) {
            if (checkBox.isChecked) {
                jugadoresSeleccionados.add(jugadores[index])
            }
        }

        return jugadoresSeleccionados
    }

    fun cargarJugadoresTitularesParaEstadisticas() {
        val adapterTL = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            jugadoresSeleccionadosTL.map { it.nombre })
        adapterTL.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        lista_jugadores_T_L.adapter = adapterTL
        val adapterTV = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            jugadoresSeleccionadosTV.map { it.nombre })
        adapterTV.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        lista_jugadores_T_V.adapter = adapterTV
        // CAPTURAR CUANDO SE SELECCIONE UN JUGADOR
        lista_jugadores_T_L.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    val jugadorSeleccionado = jugadoresEquipoTL[position - 1]
                    mostrarDialogo(jugadorSeleccionado)
                    lista_jugadores_T_L.setSelection(0)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // No se ha seleccionado ningún jugador
            }
        }

        lista_jugadores_T_V.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    val jugadorSeleccionado = jugadoresEquipoTV[position - 1]
                    mostrarDialogo(jugadorSeleccionado)
                    lista_jugadores_T_V.setSelection(0)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // No se ha seleccionado ningún jugador
            }
        }
    }
}
