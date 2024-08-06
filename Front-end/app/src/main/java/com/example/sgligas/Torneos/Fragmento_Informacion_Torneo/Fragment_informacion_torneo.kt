package com.example.sgligas.Torneos.Fragmento_Informacion_Torneo

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

class Fragment_informacion_torneo : Fragment() {
    private val itemList = mutableListOf<item>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_informacion_torneo, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_informacion_torneo)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val itemAdapter = ItemAdapter(itemList)
        recyclerView.adapter = itemAdapter
        val fechaInicio = arguments?.getString("fechaInicio") ?: "No disponible"
        val fechaFin = arguments?.getString("fechaFin") ?: "No disponible"

        itemList.clear()
        itemList.add(item("FECHA INICIO", fechaInicio))
        itemList.add(item("FECHA FIN", fechaFin))

        itemAdapter.notifyDataSetChanged()








        return view
    }
}