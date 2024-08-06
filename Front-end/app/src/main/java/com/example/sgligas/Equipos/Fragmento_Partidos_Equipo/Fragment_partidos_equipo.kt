package com.example.sgligas.Equipos.Fragmento_Partidos_Equipo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.Partidos.Partido
import com.example.sgligas.R
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import org.json.JSONObject
import java.io.File

class Fragment_partidos_equipo : Fragment() {
    private val tarjetasMesPartidoList = mutableListOf<Tarjeta_mes_partido>()
    private var listaDePartidos: MutableList<Partido> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_partidos_equipo, container, false)
        val recyclerViewMain: RecyclerView = view.findViewById(R.id.recycler_main_partidos_mes)
        val idTorneo = arguments?.getString("idTorneo") ?: "-1"
        val listaPartidosEquipo = File(requireContext().filesDir, "cache_PEquipo.txt").readText()



        try {
            val jsonParser = JsonParser()
            val json: JsonObject = jsonParser.parse(listaPartidosEquipo).asJsonObject

            if (json.has("partidos")) {
                val partidosElement = json.getAsJsonArray("partidos")

                for (element in partidosElement) {
                    val json = JSONObject(element.toString())
                    val numJornada = json.getInt("num_jornada")
                    val idPartido = json.getInt("id_partidos")
                    val nombreEquipoLocal = json.getString("nombre_local")
                    val escudoEquipoLocal = json.getString("escudo_local")
                    val idEquipoLocal = json.getInt("id_equipo_local")
                    val golesEquipoLocal = json.getInt("goles_local")
                    val golesEquipoVisitante = json.getInt("goles_visitante")
                    val idEquipoVisitante = json.getInt("id_equipo_visitante")
                    val nombreEquipoVisitante = json.getString("nombre_visitante")
                    val escudoEquipoVisitante = json.getString("escudo_visitante")
                    val fecha = json.getString("fecha_partido")
                    val hora = json.getString("hora_partido")
                    val vocal = json.getString("vocal")
                    val veedor = json.getString("veedor")
                    val cancha = json.getString("cancha")
                    val idTorneo = json.getInt("id_torneo")
                    val estado = json.getString("estado")
                    // Se crea una instancia de la clase Partido
                    val partido = Partido(
                        numJornada,
                        idPartido,
                        nombreEquipoLocal,
                        escudoEquipoLocal,
                        idEquipoLocal,
                        golesEquipoLocal,
                        golesEquipoVisitante,
                        idEquipoVisitante,
                        nombreEquipoVisitante,
                        escudoEquipoVisitante,
                        fecha,
                        hora,
                        vocal,
                        veedor,
                        cancha,
                        idTorneo,
                        estado
                    )

                    listaDePartidos.add(partido)
                }
                // Se crea un Map para almacenar los partidos agrupados por número de jornada
                val partidosPorJornada: Map<Int, List<Partido>> =
                    listaDePartidos.groupBy { it.numero_Jornada }



                for ((numJornada, partidosEnJornada) in partidosPorJornada) {
                    tarjetasMesPartidoList.add(
                        Tarjeta_mes_partido(
                            numJornada,
                            partidosEnJornada
                        )
                    )
                }

            } else {
                Log.e("fragmento", "'partidos' no está presente en la respuesta")
            }
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            Log.e("fragmento", "Error al parsear el JSON de partidos: $listaPartidosEquipo")
        }
        // Se configura el RecyclerView principal
        val adaptador1 =
            AdaptadorPrincipalPartidos(tarjetasMesPartidoList, requireContext(), idTorneo)
        recyclerViewMain.adapter = adaptador1
        recyclerViewMain.layoutManager = LinearLayoutManager(requireContext())



        return view
    }
}
