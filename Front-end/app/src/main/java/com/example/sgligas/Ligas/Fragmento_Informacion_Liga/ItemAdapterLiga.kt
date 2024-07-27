package com.example.sgligas.Ligas.Fragmento_Informacion_Liga




import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.Equipos.Fragmento_Informacion_Equipo.item
import com.example.sgligas.R


class ItemAdapterLiga (private val itemList:List<item>):RecyclerView.Adapter<ItemAdapterLiga.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):ViewHolder{

        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_info, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nombreTextView: TextView = itemView.findViewById(R.id.textNombre)
        val descripcionTextView: TextView= itemView.findViewById(R.id.textDescripcion)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val item= itemList[position]
        holder.nombreTextView.text=item.nombre
        holder.descripcionTextView.text=item.descripcion
    }


    override fun getItemCount(): Int {
        return itemList.size
    }

}