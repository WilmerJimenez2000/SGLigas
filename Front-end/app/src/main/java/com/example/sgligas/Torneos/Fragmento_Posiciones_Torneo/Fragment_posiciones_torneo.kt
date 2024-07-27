package com.example.sgligas.Torneos.Fragmento_Posiciones_Torneo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.R
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import org.json.JSONObject
import java.io.File


class Fragment_posiciones_torneo : Fragment() {

    private var tablaPosiciones: MutableList<EquipoPosiciones> = mutableListOf()

    private lateinit var recyclerEquipoClasificacion: RecyclerView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_posiciones_partido, container, false)

        recyclerEquipoClasificacion=view.findViewById(R.id.recycler_equipos_clasificacion)

        tablaPosiciones.clear()



        val tablaPosicion = File(requireContext().filesDir, "cache_tClasificacion.txt").readText()

        // monstrar tabla de posiciones
        // mosrtar la estadis de los jugadore sen equiopo
        // hice es unos cambios en la parte de regisra resultados y estadisticas

        try {
            val jsonParser = JsonParser()
            val json: JsonObject = jsonParser.parse(tablaPosicion).asJsonObject


            if (json.has("tabla")) {
                val datosArray = json.getAsJsonArray("tabla")

                // Trabaja con el array de datos
                for (equipo in datosArray) {

                    val equipoObject= JSONObject(equipo.toString())



                    val nombre=equipoObject.getString("nombre_equipo")
                    val escudo=equipoObject.getString("escudo")
                    val pg=equipoObject.getInt("pg")
                    val pe=equipoObject.getInt("pe")
                    val gf=equipoObject.getInt("gf")
                    val gc= equipoObject.getInt("gc")
                    val puntos=equipoObject.getInt("puntos")
                    val gol_diferencia=equipoObject.getInt("gol_diferencia")


                    val objetoEquipo= EquipoPosiciones(nombre, escudo, pg, pe, gf, gc, puntos, gol_diferencia)
                    tablaPosiciones.add(objetoEquipo)


                }

                // Crear y establecer el adaptador después de analizar exitosamente el JSON
                val adaptador = AdaptadorPosiciones(tablaPosiciones, object : AdaptadorPosiciones.OnItemClickListener {
                    override fun onItemClick(equipo: EquipoPosiciones) {
                        // Manejar el clic en el elemento de la tabla de posiciones
                        // Puedes mostrar un Toast con el nombre del equipo aquí
                    }
                })

                recyclerEquipoClasificacion.adapter = adaptador
                recyclerEquipoClasificacion.layoutManager = LinearLayoutManager(requireContext())
            }


        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            Log.e("fragmento", "Error al parsear el JSON de partidos: $tablaPosicion")
        }


        return view

    }


}