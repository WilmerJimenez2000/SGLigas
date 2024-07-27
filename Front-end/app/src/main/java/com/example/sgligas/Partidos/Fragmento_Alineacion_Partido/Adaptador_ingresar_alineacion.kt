package com.example.sgligas.Partidos.Fragmento_Alineacion_Partido

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.R
import com.squareup.picasso.Picasso

class Adaptador_ingresar_alineacion(
    private val jugadores: List<JugadorAlineacion>,
    private val itemClickListener: OnItemClickListener


) : RecyclerView.Adapter<Adaptador_ingresar_alineacion.JugadorViewHolder>() {



    // Código para ejecutar una función al presionar un ítem
    interface OnItemClickListener {
        fun onItemClick(position: Int, jugador: JugadorAlineacion)


    }

    fun obtenerListaDeJugadores(): List<JugadorAlineacion> {
        return jugadores
    }




    class JugadorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.textView_nombre_jugador)
        val logoImageView: ImageView = itemView.findViewById(R.id.imgView_jugador)

        fun bind(position: Int, jugador: JugadorAlineacion, itemClickListener: OnItemClickListener) {

            val nombreCompleto = jugador.nombre
            val nombresApellidos = nombreCompleto.split(" ")  // Dividir el nombre completo en una lista de palabras

            val primerNombre = nombresApellidos.first()  // Obtener el primer elemento de la lista como el primer nombre
            val primerApellido = nombresApellidos.last().first()


            nombreTextView.text = " $primerNombre $primerApellido."

            if (jugador.estado=="Visible") {

                nombreTextView.visibility = View.VISIBLE
                logoImageView.visibility = View.VISIBLE

                Picasso.get().load(jugador.escudo).into(logoImageView)

            }

            if(jugador.fondo=="No"){
                itemView.background = null


            }

            // Cargar la imagen desde la URL usando Picasso


            itemView.setOnClickListener {
                itemClickListener.onItemClick(position,jugador)


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JugadorViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_jugador_alineacion, parent, false)
        return JugadorViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: JugadorViewHolder, position: Int) {
        val jugador = jugadores[position]
        holder.bind(position, jugador, itemClickListener)
    }
    override fun getItemCount(): Int {
        return jugadores.size
    }


}
