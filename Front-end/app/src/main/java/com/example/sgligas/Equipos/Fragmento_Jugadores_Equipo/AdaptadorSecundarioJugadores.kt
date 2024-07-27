package com.example.sgligas.Equipos.Fragmento_Jugadores_Equipo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.Partidos.Fragmento_Estadisticas_Partido.EstadisticaJugador
import com.example.sgligas.R
import com.squareup.picasso.Picasso

class AdaptadorSecundarioJugadores(
    private val jugadores: List<EstadisticaJugador>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<AdaptadorSecundarioJugadores.JugadorViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(jugador: EstadisticaJugador)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JugadorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_jugador, parent, false)
        return JugadorViewHolder(view)
    }

    override fun onBindViewHolder(holder: JugadorViewHolder, position: Int) {
        val jugador = jugadores[position]
        holder.bind(jugador, itemClickListener)

    }

    override fun getItemCount(): Int {
        return jugadores.size
    }

    class JugadorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val numeroTextView: TextView = itemView.findViewById(R.id.textView_numero)
        val nombreTextView: TextView = itemView.findViewById(R.id.textView_nombre)
        val fotojugadorImageView: ImageView = itemView.findViewById(R.id.imageView_jugador)
        val pjTextView: TextView = itemView.findViewById(R.id.textView_pj)
        val tarjetasAmarillasTextView: TextView = itemView.findViewById(R.id.textView_tarjetas_r)
        val golesTextView: TextView = itemView.findViewById(R.id.textView_goles)
        val tarjetasRojasTextView: TextView = itemView.findViewById(R.id.textView_tarjetas_a)

        fun bind(jugador: EstadisticaJugador, itemClickListener: OnItemClickListener) {

            numeroTextView.text= jugador.numeroCamiseta.toString()

            nombreTextView.text = jugador.nombreJ
            pjTextView.text = jugador.pj.toString()


            tarjetasAmarillasTextView.text = jugador.amarillas.toString()

            if (jugador.posicion == "Portero") {
                golesTextView.text = jugador.goles_recibidos.toString()

            } else {
                golesTextView.text = jugador.goles.toString()
            }





            Picasso.get().load(jugador.foto).into(fotojugadorImageView)



            tarjetasRojasTextView.text = jugador.rojas.toString()

            itemView.setOnClickListener {
                itemClickListener.onItemClick(jugador)
            }

        }
    }

    ///


}
