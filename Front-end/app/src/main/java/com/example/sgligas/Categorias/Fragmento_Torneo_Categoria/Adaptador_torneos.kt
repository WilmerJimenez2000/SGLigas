package com.example.sgligas.Categorias.Fragmento_Torneo_Categoria

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.R
import com.example.sgligas.Torneos.Torneo

class Adaptador_torneos (private val torneos: List<Torneo>,
                         private val itemClickListener: OnItemClickListener
): RecyclerView.Adapter<Adaptador_torneos.TorneoViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(torneo : Torneo)
    }

    class TorneoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val etapaTorneo: TextView = itemView.findViewById(R.id.textView_etapa_torneo)

        fun bind(torneo: Torneo, itemClickListener : OnItemClickListener){

            etapaTorneo.text="${torneo.etapa}"

            itemView.setOnClickListener{
                itemClickListener.onItemClick(torneo)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TorneoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_torneo, parent, false)
        return TorneoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TorneoViewHolder, position: Int) {
        val torneo = torneos[position]
        holder.bind(torneo, itemClickListener)
    }

    override fun getItemCount(): Int{
        return torneos.size
    }




}