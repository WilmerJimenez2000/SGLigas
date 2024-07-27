package com.example.sgligas.Jugadores

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.example.sgligas.FragmentAdapter
import com.example.sgligas.Jugadores.Fragmento_Estadisticas_Jugador.Fragment_estadisticas_jugador
import com.example.sgligas.Jugadores.Fragmento_Informacion_Jugador.Fragment_info_jugador
import com.example.sgligas.R
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso

class InformacionJugador : AppCompatActivity() {
    private lateinit var boton_voler: ImageButton
    private lateinit var  nombre_jugador: TextView
    private lateinit var fragmentAdapter: FragmentAdapter

    private lateinit var imagenJugador: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacion_jugador)

        boton_voler = findViewById(R.id.btn_regresar)
        boton_voler.setOnClickListener {
            //irActividad(InformacionEquipo::class.java)
            onBackPressed()
        }




        // En tu actividad InformacionJugador




        val CI = intent.getIntExtra("CI", -1)
        val nombreJugador = intent.getStringExtra("nombre_jugador")
        val fechaNacimiento = intent.getStringExtra("fecha_nacimiento")
        val urlImagen = intent.getStringExtra("URL_imagen")
        val numero = intent.getIntExtra("numero", -1)
        val posicion = intent.getStringExtra("posicion")
        val estado = intent.getStringExtra("estado")
        val estatura = intent.getIntExtra("estatura", -1)
        val idEquipo = intent.getIntExtra("idEquipo", -1)
        val pj = intent.getIntExtra("pj", -1)
        val rojas = intent.getIntExtra("rojas", -1)
        val amarillas = intent.getIntExtra("amarillas", -1)
        val goles = intent.getIntExtra("goles", -1)
        val golesRecibidos = intent.getIntExtra("goles_recibidos", -1)
        val autogoles = intent.getIntExtra("autogoles", -1)




        nombre_jugador=findViewById(R.id.nombre_jugador)
        nombre_jugador.text=nombreJugador
        imagenJugador=findViewById(R.id.imagen_jugador)

        Picasso.get().load(urlImagen).into(imagenJugador)

        cargarDatos(fechaNacimiento.toString(), posicion.toString(),"${estatura} cm",
            nombreJugador.toString(),
            CI.toString(),
            pj.toString(),
            rojas.toString(),
            amarillas.toString(),
            goles.toString(),
            golesRecibidos.toString(),
            autogoles.toString(),
            urlImagen.toString(),
            numero.toString()
        )





    }



    fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }


    fun cargarDatos(fechaNacimiento:String, posicion: String, estatura: String,
                    nombreJ: String,
                    CI: String,
                    pj: String,
                    rojas: String,
                    amarillas: String,
                    goles: String,
                    goles_recibidos: String,
                    autogoles: String,
                    foto: String,
                    numeroCamiseta: String
    ){
        var viewPager = findViewById (R.id.viewPager_jugador) as ViewPager
        var tablayout = findViewById(R.id.tablayout_jugador) as TabLayout


        val bundle = Bundle()

        bundle.putString("fechaNacimiento",fechaNacimiento)
        bundle.putString("posicion", posicion)
        bundle.putString("estatura", estatura)
        bundle.putString("CI", CI)
        bundle.putString("nombre_jugador", nombreJ)
        bundle.putString("URL_imagen", foto)
        bundle.putString("numero", numeroCamiseta)
        bundle.putString("pj", pj)
        bundle.putString("rojas", rojas)
        bundle.putString("amarillas", amarillas)
        bundle.putString("goles", goles)
        bundle.putString("goles", goles)
        bundle.putString( "goles_recibidos", goles_recibidos)
        bundle.putString("autogoles", autogoles)


        Log.e("se cargo los datosggggg pj: ", "$pj")






        val fragmentInfoJugador = Fragment_info_jugador()
        fragmentInfoJugador.arguments = bundle

        val fragmentEstadisticasJugador= Fragment_estadisticas_jugador()
        fragmentEstadisticasJugador.arguments=bundle





        fragmentAdapter = FragmentAdapter(supportFragmentManager)

        fragmentAdapter.addFragment(fragmentInfoJugador, "INFO")
        fragmentAdapter.addFragment(fragmentEstadisticasJugador, "ESTADISTICAS")


        viewPager.adapter = fragmentAdapter
        tablayout.setupWithViewPager(viewPager)
    }
}