package com.example.sgligas.Categorias.Fragmento_Torneo_Categoria

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.Torneos.InformacionTorneo
import com.example.sgligas.R
import com.example.sgligas.Torneos.Torneo
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import java.io.File


class Fragment_torneos_categoria : Fragment() {


    private val listaDeTorneo = mutableListOf<Torneo>()

    private lateinit var layoutMensajeNoTorneos: LinearLayout

    private lateinit var recyclerView: RecyclerView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_torneos_categoria, container, false)

        recyclerView= view.findViewById(R.id.recyclerView_torneo_categoria)
        layoutMensajeNoTorneos= view.findViewById(R.id.layout_mensaje_no_torneos)

        val listaTorneo = arguments?.getString("listaTorneo") ?: "No disponible"


        try {
            val jsonParser = JsonParser()
            val json: JsonObject = jsonParser.parse(listaTorneo).asJsonObject

            if (json.has("datos")) {
                val datosArray = json.getAsJsonArray("datos")

                if (datosArray.isEmpty()) {

                    layoutMensajeNoTorneos.visibility= View.VISIBLE
                    recyclerView.visibility= View.GONE


                } else {

                    Log.e("ejecute esto", "estos")



                    // Lista para almacenar instancias de la clase Torneo

                    // Trabaja con el array de datos
                    for (element in datosArray) {
                        // Realiza las operaciones necesarias
                        val etapa = element.asJsonObject.get("etapa")?.asString ?: "No disponible"
                        val idTorneo = element.asJsonObject.get("id_torneo")?.asInt ?: 0
                        val fechaInicio =
                            element.asJsonObject.get("fecha_inicio")?.asString ?: "No disponible"
                        val fechaFin =
                            element.asJsonObject.get("fecha_fin")?.asString ?: "No disponible"
                        val grupo = element.asJsonObject.get("grupo")?.asString ?: "No disponible"
                        val numEquiposGrupos =
                            element.asJsonObject.get("num_equipo_grupos")?.asInt ?: 0
                        val idCategoria = element.asJsonObject.get("id_categoria")?.asInt ?: 0


                        // Crea una instancia de la clase Torneo
                        val torneo = Torneo(
                            idTorneo,
                            etapa,
                            fechaInicio,
                            fechaFin,
                            grupo,
                            numEquiposGrupos,
                            idCategoria
                        )

                        // Agrega la instancia a la lista
                        listaDeTorneo.add(torneo)
                    }
                }

                // Aquí puedes hacer lo que necesites con listaDeTorneo
            } else {
                // Manejar el caso donde "datos" no está presente en la respuesta
                Log.e("fragmento", "No hay datos en la respuesta")
            }
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            Log.e("fragmento", "Error al parsear el JSON: $listaTorneo")
        }


        val adapter =
            Adaptador_torneos(listaDeTorneo, object : Adaptador_torneos.OnItemClickListener {
                override fun onItemClick(torneo: Torneo) {


                    // Llamar a la función irActividad con la clase adecuada
                    irActividad(
                        InformacionTorneo::class.java,
                        torneo.etapa,
                        torneo.fecha_inicio,
                        torneo.fecha_fin,
                        torneo.id_torneo,
                        torneo.id_categoria,
                        torneo.grupo,
                        torneo.num_equipo_grupos
                    ) // Reemplaza "ActividadDestino" con la clase de destino
                }
            })

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter






        return view
    }



    fun irActividad(
        clase: Class<*>,
        etapa: String,
        fechaInicio: String,
        fechaFin: String,
        idTorneo: Int,
        idCategoria: Int,
        grupo: String,
        numEquiposGrupos: Int
    ) {
        val intent = Intent(requireContext(), clase)

        // Agregar los datos necesarios como extras al intent
        intent.putExtra("etapa", etapa)
        intent.putExtra("fecha_inicio", fechaInicio)
        intent.putExtra("fecha_fin", fechaFin)
        intent.putExtra("id_torneo", idTorneo)
        intent.putExtra("id_categoria", idCategoria)
        intent.putExtra("grupo", grupo)
        intent.putExtra("num_equipo_grupos", numEquiposGrupos)

        val fileName = "cache_t.txt"
        val filePath = File(requireContext().filesDir, fileName)
        val nuevosDatos = idTorneo.toString()
        filePath.writeText(nuevosDatos)

        // Iniciar la actividad con el intent
        startActivity(intent)
    }








}