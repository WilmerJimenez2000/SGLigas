import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.Ligas.InformacionLiga
import com.example.sgligas.Ligas.Adaptador_ligas
import com.example.sgligas.Ligas.Liga
import com.example.sgligas.R
import com.example.sgligas.consultaBaseDeDatos
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException

class Fragment_Ligas : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var callsCompleted = 0
    private var totalCalls = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ligas, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewListaLigas)


        cargarLigas()


        return view
    }

    private fun leerDesdeArchivo(nombreArchivo: String): String? {
        val archivo = File(requireContext().filesDir, nombreArchivo)
        return try {
            if (archivo.exists()) archivo.readText() else null
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("Archivo", "Error al leer el archivo: ${e.message}")
            null
        }
    }

    fun cargarLigas() {
        // Leer jsonData desde cache_cat_ligas.txt
        val jsonData = leerDesdeArchivo("cache_cat_ligas.txt")

        if (jsonData.isNullOrBlank()) {
            // Se maneja el caso donde jsonData es null o vacío
            Log.e("Error", "El archivo está vacío o el contenido no es válido.")
            return
        }

        try {
            val jsonObject = JSONObject(jsonData)

            if (jsonObject.has("ligas")) {
                val datosArray = jsonObject.getJSONArray("ligas")
                val ligas = ArrayList<Liga>()
                totalCalls = datosArray.length()

                for (i in 0 until datosArray.length()) {
                    val ligaJson = datosArray.getJSONObject(i)
                    val nombreLiga = ligaJson.optString("nombre_liga", "Desconocido")
                    val fechaFundacion = ligaJson.optString("fecha_fundacion", "Desconocida")
                    val direccion = ligaJson.optString("direccion", "Desconocida")
                    val id_liga = ligaJson.optInt("id_liga", -1)
                    val estado = ligaJson.optString("estado", "inactivo")
                    val categorias = ligaJson.optString("categorias", "N/A")

                    Log.e("categorias", "$categorias")

                    if (estado == "activo") {
                        val nuevaLiga =
                            Liga(direccion, fechaFundacion, nombreLiga, id_liga, categorias)
                        ligas.add(nuevaLiga)
                    }
                }
                val adapter = Adaptador_ligas(
                    ligas,
                    requireContext(),
                    object : Adaptador_ligas.OnItemClickListener {
                        override fun onItemClick(liga: Liga) {
                            // Llamar a la función irActividad con la clase adecuada
                            irActividad(
                                InformacionLiga::class.java,
                                liga.id_liga,
                                liga.direccion,
                                liga.fecha_fundacion,
                                liga.nombre_liga
                            )
                        }
                    })

                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
            } else {
                // Se maneja el caso donde "ligas" no está presente en la respuesta
                Log.e("Error", "\"ligas\" no está presente en la respuesta JSON.")
            }
        } catch (e: JSONException) {
            Log.e("JSON Error", "Error al analizar el JSON: ${e.message}")
            e.printStackTrace()
        }
    }



    fun irActividad(
        clase: Class<*>,
        id: Int,
        direccion: String,
        fecha_fundacion: String,
        nombre_liga: String
    ) {
        val intent = Intent(requireContext(), clase)
        intent.putExtra("ID", id)
        intent.putExtra("direccion", direccion)
        intent.putExtra("fecha_fundacion", fecha_fundacion)
        intent.putExtra("nombre_liga", nombre_liga)
        startActivity(intent)
    }

}
