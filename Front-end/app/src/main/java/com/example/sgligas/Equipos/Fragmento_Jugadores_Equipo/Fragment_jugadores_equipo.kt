package com.example.sgligas.Equipos.Fragmento_Jugadores_Equipo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.Partidos.Fragmento_Estadisticas_Partido.EstadisticaJugador
import com.example.sgligas.R
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import org.json.JSONObject
import java.io.File


class Fragment_jugadores_equipo : Fragment() {


    private var tarjetasPosicionJugadorList = mutableListOf<Tarjeta_posicion_jugador>()
    private var listaDeJugadoresEstadistica: MutableList<EstadisticaJugador> = mutableListOf()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_jugadores_equipo, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerViewMain: RecyclerView = view.findViewById(R.id.recycler_main_jugadores_posicion)

        // Crear una lista de Tarjeta_posicion_jugador que contiene todos los meses y partidos




        val listajugadoresEstadisticasEquipo= File(requireContext().filesDir, "cache_JEstadisticasEquipo.txt").readText()

        Log.e("eston sosn las estadisticas jugadores que llegan al framgmento ---------", "$listajugadoresEstadisticasEquipo")

        try {
            val jsonParser = JsonParser()
            val json: JsonObject = jsonParser.parse(listajugadoresEstadisticasEquipo).asJsonObject

            if (json.has("datos")) {
                val partidosElement = json.getAsJsonArray("datos")

                if (partidosElement != null && !partidosElement.isEmpty()) {

                    for (element in partidosElement) {


                        val json = JSONObject(element.toString())

                        val nombreJugador = json.getString("nombre")
                        val CI = json.getInt("CI")
                        val pj = json.getInt("pj")
                        val rojas = json.getInt("rojas")
                        val amarillas = json.getInt("amarilla")
                        val goles = json.getInt("goles")
                        val goles_recibidos = json.getInt("goles_recibidos")
                        val autogoles = json.getInt("autogoles")
                        val posicion = json.getString("posicion")
                        val foto = json.getString("foto")
                        val numeroCamiseta = json.optInt("num_camiseta", 0)


                        // Crea una instancia de la clase Partido
                        val jugadorEstadistica = EstadisticaJugador(
                            nombreJugador,
                            CI,
                            pj,

                            rojas,
                            amarillas,
                            goles,
                            goles_recibidos,
                            autogoles,

                            posicion,
                            foto,
                            numeroCamiseta
                        )

                        listaDeJugadoresEstadistica.add(jugadorEstadistica)

                    }


                    // Creamos un Map para almacenar los partidos agrupados por número de jornada
                    val jugadoresPorPosicion: Map<String, List<EstadisticaJugador>> =
                        listaDeJugadoresEstadistica.groupBy { it.posicion }


                    for ((posicion, jugadoresPorPosicion) in jugadoresPorPosicion) {

                        tarjetasPosicionJugadorList.add(
                            Tarjeta_posicion_jugador(
                                posicion.toUpperCase(),
                                jugadoresPorPosicion
                            )
                        )


                    }


                    // Configurar el RecyclerView principal
                    val adaptador1 =
                        AdaptadorPrincipalJugadores(tarjetasPosicionJugadorList, requireContext())
                    recyclerViewMain.adapter = adaptador1
                    recyclerViewMain.layoutManager = LinearLayoutManager(requireContext())


                }else{

                    Log.e("No existe jugadores",  "Jugadores")
                }



// Aquí puedes hacer lo que necesites con listaPartidos
            }  else {
                Log.e("fragmento", "'partidos' no está presente en la respuesta")
            }

        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            Log.e("fragmento", "Error al parsear el JSON de jugadoresEstadisticas: $listajugadoresEstadisticasEquipo")
        }



    }





}