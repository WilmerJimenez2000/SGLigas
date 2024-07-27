package com.example.sgligas.Partidos.Fragmento_Alineacion_Partido

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.R
import com.example.sgligas.consultaBaseDeDatos
import com.google.gson.JsonSyntaxException
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import kotlin.properties.Delegates


class Fragment_alineacion_partido : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val listaDeJugadoresAlineacion: MutableList<JugadorAlineacion> = mutableListOf()
    private var idPartidos by Delegates.notNull<Int>()
    lateinit var adapter: Adaptador_ingresar_alineacion

    private lateinit var LinearLayoutPrimario: LinearLayout
    private lateinit var LinearLayoutSecundario: LinearLayout




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_alineacion_partido, container, false)

        recyclerView= view.findViewById(R.id.recyclerView_alineacion)

        LinearLayoutPrimario=view.findViewById(R.id.LinearLayout_Principal_alineacion)
        LinearLayoutSecundario=view.findViewById(R.id.LinearLayout_Secundario_alineacion)


        val informacionPartido = File(requireContext().filesDir, "cache_file.txt").readText()



        try {
            val json = JSONObject(informacionPartido)


            idPartidos = json.getInt("id_partidos")



        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            Log.e("fragmento", "Error al parsear el JSON de partidos: $informacionPartido")
        }

        recuperarAlineacionPartido(idPartidos.toString())






        return view
    }

    fun recuperarAlineacionPartido(idPartido: String){


        val url = consultaBaseDeDatos.obtenerURLConsulta("7_mostrar_alineacion.php")

        val client = OkHttpClient()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("id_partido", idPartido)






        val request = Request.Builder()
            .url(url)
            .post(requestBody.build())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                // Manejar la respuesta del servidor
                val jsonData = response.body?.string()


                Log.e("esto es lo que se tiene la alinacion", "$jsonData")


                try {
                    val jsonObject = JSONObject(jsonData.toString())



                    if (jsonObject.has("datos")) {


                        // Suponiendo que tienes el JSON almacenado en un String llamado jsonString
                        val jsonObject = JSONObject(jsonData)
                        val jsonString = jsonObject.getString("datos") // Obtiene el JSON de la alineación
                        val jsonObjectDatos= JSONObject(jsonString)
                        val alineacionJson=jsonObjectDatos.getString("alineacion")
                        val alineacionArray = JSONArray(alineacionJson) // Convertir el string a JSONArray

                        val listaDeJugadoresAlineacionRecuperada = mutableListOf<JugadorAlineacion>()


                        for (i in 0 until alineacionArray.length()) {
                            val jugadorJson = alineacionArray.getJSONObject(i)
                            val jugador = JugadorAlineacion(
                                nombre = jugadorJson.getString("nombre"),
                                escudo = jugadorJson.getString("escudo"),
                                equipo = jugadorJson.getString("equipo"),
                                nombreEquipo= "NombreEquipo",
                                estado = jugadorJson.getString("estado"),
                                fondo = jugadorJson.getString("fondo")
                            )
                            listaDeJugadoresAlineacionRecuperada.add(jugador)
                        }







                        activity?.runOnUiThread {

                            adapter = Adaptador_ingresar_alineacion(
                                listaDeJugadoresAlineacionRecuperada,
                                object : Adaptador_ingresar_alineacion.OnItemClickListener {
                                    override fun onItemClick(
                                        position: Int,
                                        jugador: JugadorAlineacion
                                    ) {
                                        // Lógica para manejar clics en elementos del adaptador
                                    }
                                })

                            recyclerView.layoutManager =
                                GridLayoutManager(requireContext(), 7) // 4 columnas
                            recyclerView.adapter = adapter


                        }





                    } else {
                        Log.e("No ", "tiene datos")

                        activity?.runOnUiThread {


                            LinearLayoutPrimario.visibility = View.GONE
                            LinearLayoutSecundario.visibility = View.VISIBLE

                        }


                    }
                } catch (e: JSONException) {
                    e.printStackTrace()

                }




                /*try {
                    val jsonObject = JSONObject(jsonData.toString())



                    if (jsonObject.has("datos")) {


                        // Suponiendo que tienes el JSON almacenado en un String llamado jsonString
                        val jsonObject = JSONObject(jsonData)
                        val alineacionJson = jsonObject.getString("datos") // Obtiene el JSON de la alineación

                        Log.e("esto es lo que se tiene la alineacciooooooooooooooon", "$alineacionJson")


                        // Deserializa la cadena JSON de la alineación a una lista de objetos JugadorAlineacion
                        val listaDeJugadoresAlineacionRecuperada: MutableList<JugadorAlineacion> = Gson().fromJson(
                            alineacionJson, object : TypeToken<List<JugadorAlineacion>>() {}.type
                        )




                        activity?.runOnUiThread {

                            adapter = Adaptador_ingresar_alineacion(
                                listaDeJugadoresAlineacionRecuperada,
                                object : Adaptador_ingresar_alineacion.OnItemClickListener {
                                    override fun onItemClick(
                                        position: Int,
                                        jugador: JugadorAlineacion
                                    ) {
                                        // Lógica para manejar clics en elementos del adaptador
                                    }
                                })

                            recyclerView.layoutManager =
                                GridLayoutManager(requireContext(), 7) // 4 columnas
                            recyclerView.adapter = adapter


                        }





                    } else {
                        Log.e("No ", "tiene datos")

                        activity?.runOnUiThread {


                            LinearLayoutPrimario.visibility = View.GONE
                            LinearLayoutSecundario.visibility = View.VISIBLE

                        }


                    }
                } catch (e: JSONException) {
                    e.printStackTrace()

                }*/








            }

            override fun onFailure(call: Call, e: IOException) {
                // Manejar errores de conexión
            }
        })






    }


}