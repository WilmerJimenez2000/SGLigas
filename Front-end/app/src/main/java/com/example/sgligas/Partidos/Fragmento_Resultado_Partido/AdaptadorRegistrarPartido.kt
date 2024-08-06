package com.example.sgligas.PartidosLiga.Registro_Estadisticas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.Jugadores.Jugador
import com.example.sgligas.R

class AdaptadorRegistrarPartido(
    private val jugadores: MutableList<Jugador>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<AdaptadorRegistrarPartido.JugadorViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(jugador: Jugador)
        fun onItemDeleteClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JugadorViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_jugador_registro_partido, parent, false)
        return JugadorViewHolder(view)
    }

    override fun onBindViewHolder(holder: JugadorViewHolder, position: Int) {
        val jugador = jugadores[position]
        holder.bind(jugador, itemClickListener)
    }

    override fun getItemCount(): Int {
        return jugadores.size
    }

    fun eliminarJugador(position: Int) {
        jugadores.removeAt(position)
        notifyItemRemoved(position)
    }

    fun obtenerListaJugadoresSeleccionados(): MutableList<Jugador> {
        return jugadores
    }

    class JugadorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.textView_nombreJT)
        val btn_eliminar_J_Selec: ImageView =
            itemView.findViewById(R.id.imageView_eliminarJ_estadistica)
        val visto: ImageView = itemView.findViewById(R.id.imageView_visto)
        fun bind(jugador: Jugador, itemClickListener: OnItemClickListener) {
            nombre.text = jugador.nombre

            itemView.setOnClickListener {
                itemClickListener.onItemClick(jugador)
                visto.visibility = View.VISIBLE
            }

            btn_eliminar_J_Selec.setOnClickListener {
                itemClickListener.onItemDeleteClick(adapterPosition)
            }
        }
    }
}
