package com.example.sgligas.Equipos.Fragmento_Jugadores_Equipo

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.Jugadores.InformacionJugador
import com.example.sgligas.Partidos.Fragmento_Estadisticas_Partido.EstadisticaJugador
import com.example.sgligas.R
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import org.json.JSONObject
import java.io.File

class AdaptadorPrincipalJugadores(
    private val tarjetasPosicionJugador: List<Tarjeta_posicion_jugador>,
    private val context: Context
) : RecyclerView.Adapter<AdaptadorPrincipalJugadores.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_tarjeta_jugadores_info_equipo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tarjetaPosicionJugador = tarjetasPosicionJugador[position]
        holder.posicionTextView.text = tarjetaPosicionJugador.posicion

        Log.e("este es la posicion de la tarjeta", "${tarjetaPosicionJugador.posicion}")

        if (tarjetaPosicionJugador.posicion == "PORTERO") {

            holder.imageView_Goles.setImageResource(R.drawable.icono_goles_recibidos)


        }


        val adaptador2 = AdaptadorSecundarioJugadores(
            tarjetaPosicionJugador.jugadores,
            object : AdaptadorSecundarioJugadores.OnItemClickListener {
                override fun onItemClick(jugador: EstadisticaJugador) {


                    val infoJugador = File(context.filesDir, "cache_JEquipo.txt").readText()


                    try {
                        val jsonParser = JsonParser()
                        val json: JsonObject = jsonParser.parse(infoJugador).asJsonObject

                        if (json.has("datos")) {
                            val jugadoresElement = json.getAsJsonArray("datos")

                            for (element in jugadoresElement) {


                                val json = JSONObject(element.toString())

                                val CI = json.getInt("CI")

                                val nombre = json.getString("nombre")
                                val posicion = json.getString("posicion")
                                val fechaNacimiento = json.getString("fecha_nacimiento")
                                val idEquipo = json.getInt("id_equipo")
                                val estatura = json.getInt("estatura")
                                val estado = json.getString("estado")
                                val foto = json.getString("foto")


                                if (CI == jugador.CI) {


                                    irActividad(
                                        InformacionJugador::class.java,

                                        CI,
                                        nombre,
                                        fechaNacimiento,
                                        foto,
                                        5,
                                        posicion,
                                        estado,
                                        estatura,
                                        idEquipo,
                                        jugador.pj,
                                        jugador.rojas,
                                        jugador.amarillas,
                                        jugador.goles,
                                        jugador.goles_recibidos,
                                        jugador.autogoles


                                    )


                                }


                            }


// Aquí puedes hacer lo que necesites con listaPartidos
                        } else {
                            Log.e("fragmento", "'partidos' no está presente en la respuesta")
                        }

                    } catch (e: JsonSyntaxException) {
                        e.printStackTrace()
                        Log.e(
                            "fragmento",
                            "Error al parsear el JSON de informacion Jugador: $infoJugador"
                        )
                    }


                }
            }
        )

        holder.recyclerJugadores.adapter = adaptador2
        holder.recyclerJugadores.layoutManager =
            LinearLayoutManager(holder.recyclerJugadores.context)
    }

    override fun getItemCount(): Int {
        return tarjetasPosicionJugador.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val posicionTextView: TextView = itemView.findViewById(R.id.posicion_jugador)
        val imageView_Goles: ImageView = itemView.findViewById(R.id.imageView_Goles)
        val recyclerJugadores: RecyclerView = itemView.findViewById(R.id.recycler_jugadores)
    }

    fun irActividad(
        clase: Class<*>,
        CI: Int,
        nombreJugador: String,
        fechaNacimiento: String,
        urlImagen: String,
        numero: Int,
        posicion: String,
        estado: String,
        estatura: Int,
        idEquipo: Int,

        pj: Int,
        rojas: Int,
        amarillas: Int,
        goles: Int,
        goles_recibidos: Int,
        autogoles: Int


    ) {


        val intent = Intent(context, clase)
        intent.putExtra("CI", CI)
        intent.putExtra("nombre_jugador", nombreJugador)
        intent.putExtra("fecha_nacimiento", fechaNacimiento)
        intent.putExtra("URL_imagen", urlImagen)
        intent.putExtra("numero", numero)
        intent.putExtra("posicion", posicion)
        intent.putExtra("estado", estado)
        intent.putExtra("estatura", estatura)
        intent.putExtra("idEquipo", idEquipo)
        intent.putExtra("pj", pj)
        intent.putExtra("rojas", rojas)
        intent.putExtra("amarillas", amarillas)
        intent.putExtra("goles", goles)
        intent.putExtra("goles_recibidos", goles_recibidos)
        intent.putExtra("autogoles", autogoles)

        context.startActivity(intent)
    }


}
