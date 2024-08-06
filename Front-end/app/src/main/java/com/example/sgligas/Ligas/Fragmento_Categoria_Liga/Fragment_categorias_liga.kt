package com.example.sgligas.Ligas.Fragmento_Categoria_Liga

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.Categorias.Categoria
import com.example.sgligas.Categorias.InformacionCategoria
import com.example.sgligas.R
import com.google.gson.JsonObject
import com.google.gson.JsonParser

class Fragment_categorias_liga : Fragment() {
    private val listaDeCategorias = mutableListOf<Categoria>()
    private lateinit var recyclerViewMain: RecyclerView
    private lateinit var layoutMensajeNocategorias: LinearLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_categorias_liga, container, false)


        recyclerViewMain = view.findViewById(R.id.recycler_categorias_liga)
        layoutMensajeNocategorias = view.findViewById(R.id.layout_mensaje_no_categorias)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listaCategorias = arguments?.getString("listaCategorias") ?: "No disponible"
        val jsonParser = JsonParser()
        val json: JsonObject = jsonParser.parse(listaCategorias).asJsonObject


        if (json.has("datos")) {
            val datosArray = json.getAsJsonArray("datos")


            if (datosArray != null && !datosArray.isEmpty()) {
                for (element in datosArray) {
                    val idCategoria = element.asJsonObject.get("id_categoria").asInt
                    val nombreCategoria = element.asJsonObject.get("nombre").asString
                    val numEquipos = element.asJsonObject.get("num_equipos").asInt
                    val categoria = Categoria(idCategoria, nombreCategoria, numEquipos)
                    listaDeCategorias.add(categoria)
                }
                val adapter = Adaptador_categorias(
                    listaDeCategorias,
                    object : Adaptador_categorias.OnItemClickListener {
                        override fun onItemClick(categoria: Categoria) {
                            irActividad(
                                InformacionCategoria::class.java,
                                categoria.id_categoria,
                                categoria.nombre_categoria,
                                categoria.num_equipos
                            )
                        }
                    })



                recyclerViewMain.adapter = adapter
                recyclerViewMain.layoutManager = LinearLayoutManager(requireContext())
            } else {
                recyclerViewMain.visibility = View.GONE

                layoutMensajeNocategorias.visibility = View.VISIBLE
            }
        }
    }

    fun irActividad(clase: Class<*>, id: Int, nombre_categoria: String, num_equipos: Int) {
        val intent = Intent(requireContext(), clase)


        intent.putExtra("ID", id)
        intent.putExtra("nombre_categoria", nombre_categoria)
        intent.putExtra("num_equipos", num_equipos)
        startActivity(intent)
    }
}

