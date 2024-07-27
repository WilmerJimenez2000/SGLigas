package com.example.sgligas.Equipos.Fragmento_Partidos_Equipo

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.Partidos.InformacionPartido
import com.example.sgligas.Partidos.Partido
import com.example.sgligas.R
import org.json.JSONObject
import java.io.File

class AdaptadorPrincipalPartidos(private val tarjetasMesPartido: List<Tarjeta_mes_partido>,
                                 private val context: Context, private val idTorneo: String
) : RecyclerView.Adapter<AdaptadorPrincipalPartidos.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tarjeta_partidos_info_equipo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tarjetaMesPartido = tarjetasMesPartido[position]
        val jornada= tarjetaMesPartido.numeroJornada.toString()
        holder.numeroJornada.text = "JORNADA $jornada"

        // Configura el RecyclerView secundario (recyclerPartidos) utilizando Adaptador 2
        val adaptador2 = AdaptadorSecundarioPartidos(tarjetaMesPartido.partidos,

            object : AdaptadorSecundarioPartidos.OnItemClickListener {
                override fun onItemClick(partido: Partido) {


                    irActividad(
                        InformacionPartido::class.java, idTorneo


                    )




                    guardarObjetoPartido(

                        jornada,
                        partido.id_partidos,//numeroPartido: Int,
                       partido.nombre_equipo_local,// nombreEquipoLocal: String,
                        partido.escudo_equipo_local,//escudoEquipoLocal: String,
                        partido.id_equipo_local,//idEquipoLocal: Int,
                        partido.goles_equipo_local,//golesEquipoLocal: Int,
                        partido.goles_equipo_visitante,//golesEquipoVisitante: Int,
                        partido.id_equipo_visitante,//idEquipoVisitante: Int,
                        partido.nombre_equipo_visitante,//nombreEquipoVisitante: String,
                        partido.escudo_equipo_visitante,//escudoEquipoVisitante: String,
                        partido.fecha,//fecha: String,
                        partido.hora,//hora: String,
                        partido.vocal,//vocal: String,
                        partido.veedor,//veedor: String,
                        partido.cancha,//cancha: String
                       partido.idTorneo,
                        partido.estado


                    )

                    //irActividad(InformacionPartido::class.java)*/



                }
            }




            )
        holder.recyclerPartidos.adapter = adaptador2
        holder.recyclerPartidos.layoutManager = LinearLayoutManager(holder.recyclerPartidos.context)
    }

    override fun getItemCount(): Int {
        return tarjetasMesPartido.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val numeroJornada: TextView = itemView.findViewById(R.id.textView_n_jornada)
        val recyclerPartidos: RecyclerView = itemView.findViewById(R.id.recycler_partidos)
    }




    fun irActividad(clase: Class<*>, id: String) {

        val intent = Intent(context, clase)
        intent.putExtra("idTorneo", id)
        context.startActivity(intent)
    }






    fun guardarObjetoPartido(

        numeroJornada: String,


        idPartidos: Int,
        nombreEquipoLocal: String,
        escudoEquipoLocal: String,
        idEquipoLocal: Int,
        golesEquipoLocal: Int,
        golesEquipoVisitante: Int,
        idEquipoVisitante: Int,
        nombreEquipoVisitante: String,
        escudoEquipoVisitante: String,
        fecha: String,
        hora: String,
        vocal: String,
        veedor: String,
        cancha: String,
        idTorneo: Int,
        estado: String
    ){


        val json = JSONObject().apply {
            put("numero_jornada", numeroJornada)
            put("id_partidos", idPartidos)
            put("nombre_equipo_local", nombreEquipoLocal)
            put("escudo_equipo_local", escudoEquipoLocal)
            put("id_equipo_local", idEquipoLocal)
            put("goles_equipo_local", golesEquipoLocal)
            put("goles_equipo_visitante", golesEquipoVisitante)
            put("id_equipo_visitante", idEquipoVisitante)
            put("nombre_equipo_visitante", nombreEquipoVisitante)
            put("escudo_equipo_visitante", escudoEquipoVisitante)
            put("fecha", fecha)
            put("hora", hora)
            put("vocal", vocal)
            put("veedor", veedor)
            put("cancha", cancha)
            put("idTorneo", idTorneo)
            put("estado",estado)
        }


        val jsonData= json.toString()


        // Nombre del archivo en caché
        val fileName = "cache_file.txt"

// Ruta completa del archivo
        val filePath = File(context.filesDir, fileName)

// Datos nuevos para almacenar en caché
        val nuevosDatos = jsonData

// Escribir los nuevos datos en el archivo (reemplazando los datos existentes)
        filePath.writeText(nuevosDatos)

// Recuperar todos los datos de la caché
        val cachedData = filePath.readText()

        ////





    }

}
