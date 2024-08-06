package com.example.sgligas.Categorias.Fragmento_Equipos_Categoria

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.Equipos.Equipo
import com.example.sgligas.R
import com.squareup.picasso.Picasso

class Adaptador_equipos(
    private val equipos: List<Equipo>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<Adaptador_equipos.EquipoViewHolder>() {
    //codigo para ejecutar un funcion al precionar un item
    interface OnItemClickListener {
        fun onItemClick(equipo: Equipo)
    }

    class EquipoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.textView_nombre_equipo)
        val logoImageView: ImageView = itemView.findViewById(R.id.imgView_equipo)
        fun bind(equipo: Equipo, itemClickListener: OnItemClickListener) {
            nombreTextView.text = " ${equipo.nombre_equipo}"
            // Se carga la imagen desde la URL usando Picasso
            Picasso.get().load(equipo.escudo).into(logoImageView)

            itemView.setOnClickListener {
                itemClickListener.onItemClick(equipo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipoViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_equipo, parent, false)
        return EquipoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EquipoViewHolder, position: Int) {
        val equipo = equipos[position]
        holder.bind(equipo, itemClickListener)
    }

    override fun getItemCount(): Int {
        return equipos.size
    }
}
