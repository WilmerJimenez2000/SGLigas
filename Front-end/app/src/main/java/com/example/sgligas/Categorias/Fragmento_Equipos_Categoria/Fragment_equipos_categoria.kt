package com.example.sgligas.Categorias.Fragmento_Equipos_Categoria

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.*
import com.example.sgligas.Equipos.Equipo
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.File

class Fragment_equipos_categoria : Fragment() {
    private val listaDeEquipos = mutableListOf<Equipo>()
    private lateinit var layoutMensajeNoEquipos: LinearLayout
    private lateinit var cardViewNoEquiposCategoria: CardView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_equipos_categoria, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView_equipos_categoria)
        val numero_equipos: TextView = view.findViewById(R.id.numero_equipos)

        cardViewNoEquiposCategoria = view.findViewById(R.id.cardView_equipos_categoria)
        layoutMensajeNoEquipos = view.findViewById(R.id.layout_mensaje_no_equipos_categoria)
        val numeroEquipos = arguments?.getString("numeroEquipos") ?: "No disponible"




        numero_equipos.text = numeroEquipos
        val listaEquipos = File(requireContext().filesDir, "cache_file.txt").readText()
        val jsonParser = JsonParser()
        val json: JsonObject = jsonParser.parse(listaEquipos).asJsonObject


        if (json.has("datos")) {
            val datosArray = json.getAsJsonArray("datos")

            if (datosArray != null && !datosArray.isEmpty()) {
                // Se trabaja con el array de datos
                for (element in datosArray) {
                    // Se realiza las operaciones necesarias
                    val idEquipo = element.asJsonObject.get("id_equipo").asInt
                    val nombreEquipo = element.asJsonObject.get("nombre_equipo").asString
                    val presidente = element.asJsonObject.get("presidente").asString
                    val colores = element.asJsonObject.get("colores").asString
                    val escudo = element.asJsonObject.get("escudo").asString
                    val fecha_fundacion = element.asJsonObject.get("fecha_fundacion").asString
                    // Se crea instancias de la clase Categoria o realiza las operaciones deseadas
                    // Se crea una instancia de la clase Categoria
                    val equipo =
                        Equipo(idEquipo, nombreEquipo, fecha_fundacion, presidente, escudo, colores)
                    // Se agrega la instancia a la lista
                    listaDeEquipos.add(equipo)
                }
            } else {
                recyclerView.visibility = View.GONE
                cardViewNoEquiposCategoria.visibility = View.GONE
                layoutMensajeNoEquipos.visibility = View.VISIBLE
            }
        }
        val adapter =
            Adaptador_equipos(listaDeEquipos, object : Adaptador_equipos.OnItemClickListener {
                override fun onItemClick(equipo: Equipo) {
                    // Se maneja la acción al presionar un equipo
                }
            })

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 4) // 4 columnas
        recyclerView.adapter = adapter

        return view
    }

    fun irActividad(clase: Class<*>, id: Int) {
        val intent = Intent(requireContext(), clase)
        intent.putExtra("ID", id)
        startActivity(intent)
    }
}
