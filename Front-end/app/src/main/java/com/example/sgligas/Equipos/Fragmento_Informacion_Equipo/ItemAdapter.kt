package com.example.sgligas.Equipos.Fragmento_Informacion_Equipo

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.R

class ItemAdapter(private val itemList: List<item>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_info, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.nombreTextView.text = item.nombre
        if(item.nombre=="COLORES") {
            holder.linearLayoutCamisetas.visibility = View.VISIBLE

           val colores = item.descripcion.split(",") // Dividir la cadena en una lista de colores

            if (colores.size == 2) {
                val color1 = colores[0]
                val color2 = colores[1]


                val color1_camiseta =
                    Color.parseColor(color1) // Convierte el color hexadecimal a un valor de color
                holder.color1.setColorFilter(color1_camiseta)

                val color2_camiseta =
                    Color.parseColor(color2) // Convierte el color hexadecimal a un valor de color
                holder.color2.setColorFilter(color2_camiseta)
                holder.descripcionTextView.visibility = View.GONE
            }else{

                val color1_ = colores[0]
                val color1_camisetas =
                    Color.parseColor(color1_) // Convierte el color hexadecimal a un valor de color
                holder.color1.setColorFilter(color1_camisetas)
                holder.color2.visibility=View.GONE
                holder.descripcionTextView.visibility = View.GONE


            }
        }else{
            holder.descripcionTextView.text = item.descripcion

        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.textNombre)
        val descripcionTextView: TextView = itemView.findViewById(R.id.textDescripcion)
        val linearLayoutCamisetas = itemView.findViewById<LinearLayout>(R.id.linear_camisetas)
        val color1:ImageView=itemView.findViewById<ImageView>(R.id.color1_camiseta)
        val color2:ImageView=itemView.findViewById<ImageView>(R.id.color2_camiseta)




    }
}
