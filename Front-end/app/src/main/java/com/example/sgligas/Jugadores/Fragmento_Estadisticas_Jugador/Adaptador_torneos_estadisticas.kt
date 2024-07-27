package com.example.sgligas.Jugadores.Fragmento_Estadisticas_Jugador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.Partidos.Fragmento_Estadisticas_Partido.EstadisticaJugador
import com.example.sgligas.R

class Adaptador_torneos_estadisticas(private var estadisticas: List<EstadisticaJugador>,
                                     private val itemClickListener: OnItemClickListener
):RecyclerView.Adapter<Adaptador_torneos_estadisticas.EstadisticaJugadorViewHolder>() {



    interface OnItemClickListener{
        fun onItemClick(estadisticaJugador: EstadisticaJugador)
    }



    class EstadisticaJugadorViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nombreTorneo: TextView = itemView.findViewById(R.id.nombre_torneo_estadistica)
        val descripcionEstadisticas: View = itemView.findViewById(R.id.layout_descripcion_estadisticas)
        val partidosJugados: TextView= itemView.findViewById(R.id.textView_partidos_jugados)
        val numeroGoles: TextView= itemView.findViewById(R.id.textView_numero_goles)
        val numeroTarjetas: TextView= itemView.findViewById(R.id.textView_numero_tarjetas)



        fun bind(estadisticaJugador: EstadisticaJugador, itemClickListener: OnItemClickListener){
            //nombreTorneo.text="${estadisticaJugador.} ETAPA"
            partidosJugados.text="${estadisticaJugador.pj}"
            numeroGoles.text="${estadisticaJugador.goles}"
            numeroTarjetas.text="${estadisticaJugador.amarillas}/${estadisticaJugador.rojas}"

            descripcionEstadisticas.visibility == View.VISIBLE
            itemView.setOnClickListener{
               /* descripcionEstadisticas.visibility =
                    if (descripcionEstadisticas.visibility == View.VISIBLE) View.GONE else View.VISIBLE*/

                itemClickListener.onItemClick(estadisticaJugador)
            }
        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EstadisticaJugadorViewHolder {

        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.item_torneo_estadistica, parent, false)
        return EstadisticaJugadorViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: EstadisticaJugadorViewHolder, position: Int){
        val estadisticaJugador=estadisticas[position]
        holder.bind(estadisticaJugador, itemClickListener)
    }


    override fun getItemCount(): Int {

        return estadisticas.size
    }
}