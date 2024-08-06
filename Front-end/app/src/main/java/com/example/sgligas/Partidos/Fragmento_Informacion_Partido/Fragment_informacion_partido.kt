package com.example.sgligas.Partidos.Fragmento_Informacion_Partido

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.Equipos.Fragmento_Informacion_Equipo.ItemAdapter
import com.example.sgligas.Equipos.Fragmento_Informacion_Equipo.item
import com.example.sgligas.R

class Fragment_informacion_partido : Fragment() {
    private lateinit var itemAdapterPartido: ItemAdapter
    private val itemList = mutableListOf<item>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_informacion_partido, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_info_partido)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val itemAdapter = ItemAdapter(itemList)
        recyclerView.adapter = itemAdapter
        val vocal = arguments?.getString("vocal") ?: "No disponible"
        val veedor = arguments?.getString("veedor") ?: "No disponible"
        val cancha = arguments?.getString("cancha") ?: "No disponible"
        itemList.clear()
        itemList.add(item("VOCAL", vocal))
        itemList.add(item("VEEDOR", veedor))
        itemList.add(item("CANCHA", cancha))

        itemAdapter.notifyDataSetChanged()



        return view
    }
}