package com.example.sgligas.Equipos.Fragmento_Partidos_Equipo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.Partidos.Partido
import com.example.sgligas.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class AdaptadorSecundarioPartidos(
    private val partidos: List<Partido>,
    private val itemClickListener: AdaptadorSecundarioPartidos.OnItemClickListener
) : RecyclerView.Adapter<AdaptadorSecundarioPartidos.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(partido: Partido)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_partido, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val partido = partidos[position]


        holder.bind(partido, itemClickListener)
    }

    override fun getItemCount(): Int {
        return partidos.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val equipoVisitanteTextView: TextView = itemView.findViewById(R.id.text_equipo_visitante)
        val equipoLocalTextView: TextView = itemView.findViewById(R.id.text_equipo_local)
        val resultadoTextView: TextView = itemView.findViewById(R.id.text_resultado)
        val fechaTextView: TextView = itemView.findViewById(R.id.text_fecha)
        val horaTextView: TextView = itemView.findViewById(R.id.text_hora)
        val escudoEquipoLocal: ImageView = itemView.findViewById(R.id.imageView_escudo_local)
        val escudoEquipoVisitante: ImageView =
            itemView.findViewById(R.id.imageView_escudo_visitante)

        //paso 3
        fun bind(partido: Partido, itemClickListener: OnItemClickListener) {
            equipoVisitanteTextView.text = partido.nombre_equipo_visitante
            equipoLocalTextView.text = partido.nombre_equipo_local

            fechaTextView.text = partido.fecha
            val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formatoHora = SimpleDateFormat("HH", Locale.getDefault())
            val fechaPartido = partido.fecha
            val horaPartido = partido.hora
            val fechaActual = formatoFecha.format(Date())
            val horaActual = formatoHora.format(Date())




            if (partido.estado == "jugado") {
                resultadoTextView.text =
                    "${partido.goles_equipo_local} - ${partido.goles_equipo_visitante}"
            } else {
                resultadoTextView.text = " vs "
            }


            horaTextView.text = partido.hora




            Picasso.get().load(partido.escudo_equipo_local).into(escudoEquipoLocal)
            Picasso.get().load(partido.escudo_equipo_visitante).into(escudoEquipoVisitante)


            itemView.setOnClickListener {
                itemClickListener.onItemClick(partido)
            }
        }

        // Se obtiene la fecha actual en el formato "año-mes-día"
        private fun obtenerFechaActual(): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = Date()
            return dateFormat.format(currentDate)
        }

        // Se compara la fecha del partido con la fecha actual
        private fun compararFechas(partidoDate: String): Boolean {
            val currentDate = obtenerFechaActual()
            return currentDate == partidoDate
        }

        private fun obtenerHoraActual(): String {
            val formatoHora = SimpleDateFormat("HH:mm", Locale.getDefault())
            val currentDate = Date()
            return formatoHora.format(currentDate)
        }

    }
}
