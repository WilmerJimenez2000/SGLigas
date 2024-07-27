package com.example.sgligas.Jugadores.Fragmento_Estadisticas_Jugador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.Partidos.Fragmento_Estadisticas_Partido.EstadisticaJugador
import com.example.sgligas.R


class Fragment_estadisticas_jugador : Fragment() {

    //private val listaDeTorneosJugador= mutableListOf<EstadisticaJugador>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_estadisticas_jugador, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)



        val fechaNacimiento = arguments?.getString("fechaNacimiento") ?: "No disponible"
        val posicion = arguments?.getString("posicion") ?: "No disponible"
        val estatura = arguments?.getString("estatura") ?: "No disponible"
        val CI = arguments?.getString("CI") ?: "No disponible"
        val nombreJugador = arguments?.getString("nombre_jugador") ?: "No disponible"
        val foto = arguments?.getString("URL_imagen") ?: ""
        val numeroCamiseta = arguments?.getString("numero") ?: "No disponible"
        val pj = arguments?.getString("pj") ?: "No disponible"
        val rojas = arguments?.getString("rojas") ?: "No disponible"
        val amarillas = arguments?.getString("amarillas") ?: "No disponible"
        val goles = arguments?.getString("goles") ?: "No disponible"
        val golesRecibidos = arguments?.getString("goles_recibidos") ?: "No disponible"
        val autogoles = arguments?.getString("autogoles") ?: "No disponible"



        val listaDeTorneosJugador = listOf(
            EstadisticaJugador(nombreJugador, CI.toInt(), pj.toInt(), rojas.toInt(), amarillas.toInt(),goles.toInt(), golesRecibidos.toInt(), autogoles.toInt(), posicion, foto, numeroCamiseta.toInt()),


        )





        val adapter= Adaptador_torneos_estadisticas(listaDeTorneosJugador, object : Adaptador_torneos_estadisticas.OnItemClickListener{
            override fun onItemClick(estadisticaJugador: EstadisticaJugador){

                // Llamar a la función irActividad con la clase adecuada


                //Toast.makeText(context, "${estadisticaJugador.id_torneo}", Toast.LENGTH_SHORT).show()


            }
            })

        val recyclerView: RecyclerView= view.findViewById(R.id.recycler_torneos_jugador)

        recyclerView.adapter= adapter
        recyclerView.layoutManager= LinearLayoutManager(requireContext())






    }





}