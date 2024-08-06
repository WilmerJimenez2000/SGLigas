package com.example.sgligas.Ligas

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.Categorias.InformacionCategoria
import com.example.sgligas.Categorias.Categoria
import com.example.sgligas.R
import com.google.gson.JsonArray
import com.google.gson.JsonParser

class Adaptador_ligas(
    private val ligas: List<Liga>,
    private val context: Context,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<Adaptador_ligas.LigaViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(liga: Liga)
    }

    private var openedPosition: Int = 0  // Inicialmente abierto el primer elemento
    private val listaDeCategorias = mutableListOf<Categoria>()

    class LigaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val linearLayoutNombreLiga: LinearLayout =
            itemView.findViewById(R.id.LinearLayout_NombreLiga)
        val nombreLiga: TextView = itemView.findViewById(R.id.nombre_liga)
        val listaCategorias: RecyclerView = itemView.findViewById(R.id.recycler_view_categorias)
        val btnMostrarCategorias: ImageButton =
            itemView.findViewById(R.id.imageButton_mostrar_categorias)
        val textoCategoria: TextView = itemView.findViewById(R.id.textView_categorias)
        fun bind(liga: Liga, itemClickListener: OnItemClickListener, isExpanded: Boolean) {
            nombreLiga.text = liga.nombre_liga

            listaCategorias.visibility = if (isExpanded) View.VISIBLE else View.GONE
            textoCategoria.visibility = if (isExpanded) View.VISIBLE else View.GONE

            itemView.setOnClickListener {
                itemClickListener.onItemClick(liga)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LigaViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_liga, parent, false)
        return LigaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LigaViewHolder, position: Int) {
        val liga = ligas[position]
        val isExpanded = position == openedPosition
        holder.bind(liga, itemClickListener, isExpanded)


        holder.btnMostrarCategorias.setOnClickListener {
            val previousOpenedPosition = openedPosition
            openedPosition = if (isExpanded) {
                RecyclerView.NO_POSITION
            } else {
                position
            }

            notifyItemChanged(previousOpenedPosition)
            notifyItemChanged(openedPosition)
        }

        listaDeCategorias.clear()
        val jsonParser = JsonParser()
        val jsonArray: JsonArray = jsonParser.parse(liga.categorias) as JsonArray

        for (element in jsonArray) {
            val idCategoria = element.asJsonObject.get("id_categoria").asInt
            val nombreCategoria = element.asJsonObject.get("nombre").asString
            val numEquipos = element.asJsonObject.get("num_equipos").asInt
            val categoria = Categoria(idCategoria, nombreCategoria, numEquipos)
            listaDeCategorias.add(categoria)
        }
        // Se itera sobre la lista de categorías
        for (categoria in listaDeCategorias) {
            println("ID Categoría: ${categoria.id_categoria}")
            println("Nombre: ${categoria.nombre_categoria}")
            println("Número de Equipos: ${categoria.num_equipos}")
        }

        val adapter = Adaptador_categorias_liga(
            listaDeCategorias,
            object : Adaptador_categorias_liga.OnItemClickListener {
                override fun onItemClick(categoria: Categoria) {
                    irActividad(
                        InformacionCategoria::class.java,
                        categoria.id_categoria,
                        categoria.nombre_categoria,
                        categoria.num_equipos
                    )
                }
            }
        )
// Se configura el RecyclerView con el adaptador
        holder.listaCategorias.adapter = adapter
        holder.listaCategorias.layoutManager = LinearLayoutManager(holder.listaCategorias.context)
    }

    override fun getItemCount(): Int {
        return ligas.size
    }

    fun irActividad(
        clase: Class<*>,
        id: Int,
        nombre_categoria: String,
        num_equipos: Int
    ) {
        val intent = Intent(context, clase)
        intent.putExtra("ID", id)
        intent.putExtra("nombre_categoria", nombre_categoria)
        intent.putExtra("num_equipos", num_equipos)


        context.startActivity(intent)
    }
}
