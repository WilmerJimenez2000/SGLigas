package com.example.sgligas.Ligas.Fragmento_Informacion_Liga

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


class Fragment_info_liga : Fragment() {

    private lateinit var itemAdapterLiga: ItemAdapter
    private val itemList = mutableListOf<item>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_info_liga, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_info_liga)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val itemAdapter = ItemAdapter(itemList)
        recyclerView.adapter = itemAdapter

        val direccion = arguments?.getString("direccion") ?: "No disponible"
        val fechaFundacion = arguments?.getString("fechaFundacion") ?: "No disponible"


        // Limpia la lista itemList antes de agregar nuevos datos
        itemList.clear()
        itemList.add(item("FECHA DE FUNDACIÓN", fechaFundacion))
        itemList.add(item("DIRECCION", direccion))

        itemAdapter.notifyDataSetChanged()


        return view
    }

}









    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var itemList = mutableListOf<item>(

            item("FECHA DE FUNDACIÓN", "2023-10-10"),
            item("DIRECCION","Jose Puentes")
            // Agrega más elementos si es necesario
        )

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_info_liga)
        itemAdapterLiga = ItemAdapter(itemList)
        recyclerView.adapter = itemAdapterLiga
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }*/








