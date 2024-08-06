package com.example.sgligas.Jugadores.Fragmento_Informacion_Jugador

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
import java.time.LocalDate
import java.time.Period

class Fragment_info_jugador : Fragment() {
    private lateinit var itemAdapterJugador: ItemAdapter
    private val itemList = mutableListOf<item>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_informacion_jugador, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_info_jugador)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val itemAdapter = ItemAdapter(itemList)
        recyclerView.adapter = itemAdapter
        val fechaNacimiento = arguments?.getString("fechaNacimiento") ?: "No disponible"
        val posicion = arguments?.getString("posicion") ?: "No disponible"
        val estatura = arguments?.getString("estatura") ?: "No disponible"
        val fechaDeNacimiento = LocalDate.parse(fechaNacimiento)
        // Se calcula la edad actual
        val edad = calcularEdad(fechaDeNacimiento)
        // Se limpia la lista itemList antes de agregar nuevos datos
        itemList.clear()
        itemList.add(item("EDAD", edad.toString()))
        itemList.add(item("POSICIÓN", posicion))
        itemList.add(item("ESTATURA", estatura))

        itemAdapter.notifyDataSetChanged()


        return view
    }

    fun calcularEdad(fechaNacimiento: LocalDate): Int {
        val fechaActual = LocalDate.now()
        val periodo = Period.between(fechaNacimiento, fechaActual)
        return periodo.years
    }
}