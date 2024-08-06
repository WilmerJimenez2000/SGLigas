package com.example.sgligas.Ligas.Fragmento_Categoria_Liga

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.Categorias.Categoria
import com.example.sgligas.R

class Adaptador_categorias(
    private val categorias: List<Categoria>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<Adaptador_categorias.CategoriaViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(categoria: Categoria)
    }


    class CategoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreCategoria: TextView = itemView.findViewById(R.id.nombre_liga)
        fun bind(categoria: Categoria, itemClickListener: OnItemClickListener) {
            nombreCategoria.text = "${categoria.nombre_categoria}"
            itemView.setOnClickListener {
                itemClickListener.onItemClick(categoria)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_categoria, parent, false)
        return CategoriaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val categoria = categorias[position]
        holder.bind(categoria, itemClickListener)
    }

    override fun getItemCount(): Int {
        return categorias.size
    }
}