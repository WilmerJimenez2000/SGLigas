package com.example.sgligas.Torneos.Fragmento_Posiciones_Torneo


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.R
import com.squareup.picasso.Picasso

class AdaptadorPosiciones(private val tablaPosiciones: List<EquipoPosiciones>, private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<AdaptadorPosiciones.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(equipo: EquipoPosiciones)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreEquipo: TextView = itemView.findViewById(R.id.textView_nombre_equipo_clasificacion)
        val escudo: ImageView= itemView.findViewById(R.id.imageView_equipo_clasificacion)
        val pg: TextView= itemView.findViewById(R.id.textView_clasificacion_PG)
        val pe: TextView= itemView.findViewById(R.id.textView_clasificacion_PE)
        val gf: TextView= itemView.findViewById(R.id.textView_clasificacion_GF)
        val gc: TextView= itemView.findViewById(R.id.textView_clasificacion_GC)
        val puntos: TextView=itemView.findViewById(R.id.textView_clasificacion_Pts)
        val gol_diferencia: TextView=itemView.findViewById(R.id.textView_clasificacion_DG)
        val posicion: TextView = itemView.findViewById(R.id.textView_numero_equipo)






        fun bind(equipo: EquipoPosiciones, itemClickListener: OnItemClickListener) {
            nombreEquipo.text = equipo.nombre_equipo
            pg.text=equipo.pg.toString()
            pe.text=equipo.pe.toString()
            gf.text=equipo.gf.toString()
            gc.text=equipo.gc.toString()
            puntos.text=equipo.puntos.toString()
            gol_diferencia.text=equipo.gol_diferencia.toString()

            posicion.text = (position + 1).toString() // La posición comienza desde 1





            Picasso.get().load(equipo.escudo).into(escudo)




            itemView.setOnClickListener {
                itemClickListener.onItemClick(equipo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_equipo_clasificacion, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val equipo = tablaPosiciones[position]
        holder.bind(equipo, itemClickListener)
    }

    override fun getItemCount(): Int {
        return tablaPosiciones.size
    }
}
