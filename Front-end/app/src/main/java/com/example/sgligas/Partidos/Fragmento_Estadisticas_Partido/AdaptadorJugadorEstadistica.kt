package com.example.sgligas.Partidos.Fragmento_Estadisticas_Partido

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.R

class AdaptadorJugadorEstadistica(
    private val jugadores: MutableList<EstadisticaJugador>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<AdaptadorJugadorEstadistica.JugadorViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(estadisticaJugador: EstadisticaJugador)
        fun onItemDeleteClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JugadorViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_jugador_registro_estadistica, parent, false)
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

    fun obtenerListaJugadoresSeleccionados(): MutableList<EstadisticaJugador> {
        return jugadores
    }



    class JugadorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.textView_nombreJ)
        val TA: TextView = itemView.findViewById(R.id.textView_TA)
        val TR: TextView = itemView.findViewById(R.id.textView_TR)
        val gol: TextView = itemView.findViewById(R.id.textView_gol)
        val agol: TextView = itemView.findViewById(R.id.textView_agol)
        val btn_eliminar_J_Selec: ImageView = itemView.findViewById(R.id.imageView_eliminarJ_estadistica)



        fun bind(estadisticaJugador: EstadisticaJugador, itemClickListener: OnItemClickListener) {
            nombre.text=estadisticaJugador.nombreJ
            TA.text = estadisticaJugador.amarillas.toString()
            TR.text = estadisticaJugador.rojas.toString()

            if(estadisticaJugador.posicion=="Portero"){

                gol.text = estadisticaJugador.goles_recibidos.toString()



            }else{

                gol.text = estadisticaJugador.goles.toString()


            }
            agol.text = estadisticaJugador.autogoles.toString()

            itemView.setOnClickListener {
                itemClickListener.onItemClick(estadisticaJugador)
            }

            btn_eliminar_J_Selec.setOnClickListener {

               itemClickListener.onItemDeleteClick(adapterPosition)
            }


        }
    }
}
