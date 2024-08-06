package com.example.sgligas.Ligas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.example.sgligas.FragmentAdapter
import com.example.sgligas.Ligas.Fragmento_Categoria_Liga.Fragment_categorias_liga
import com.example.sgligas.Ligas.Fragmento_Informacion_Liga.Fragment_info_liga
import com.example.sgligas.Login.Login
import com.example.sgligas.R
import com.example.sgligas.consultaBaseDeDatos
import com.google.android.material.tabs.TabLayout
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException

class InformacionLiga : AppCompatActivity() {
    private lateinit var nombre_liga: TextView
    private lateinit var boton_voler: ImageButton
    private lateinit var fragmentAdapter: FragmentAdapter
    private lateinit var direccionLiga: String
    private lateinit var fecha_fundacion: String
    private lateinit var nombreLiga: String
    private lateinit var cerrar_sesion_presidente: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacion_liga)
        val id = intent.getIntExtra("ID", -1)

        obtenerCategoriasLiga(id.toString())


        nombreLiga = intent.getStringExtra("nombre_liga").toString()
        direccionLiga = intent.getStringExtra("direccion").toString()
        fecha_fundacion = intent.getStringExtra("fecha_fundacion").toString()

        nombre_liga = findViewById(R.id.nombre_liga)

        nombre_liga.text = nombreLiga.toUpperCase()


        boton_voler = findViewById(R.id.btn_regresar)
        boton_voler.setOnClickListener {
            onBackPressed()
        }

        cerrar_sesion_presidente = findViewById(R.id.btn_cerrar_sesion_presidente)
        val archivoUsuario = File(this@InformacionLiga.filesDir, "cache_user.txt")

        if (archivoUsuario.exists()) {
            val UsuarioLogin = archivoUsuario.readText()
            val json = JSONObject(UsuarioLogin)
            val jsonUsuario = json.getString("tipo_usuario")
            if (jsonUsuario == "presidente") {
                cerrar_sesion_presidente.visibility = View.VISIBLE
                boton_voler.visibility = View.INVISIBLE
                cerrar_sesion_presidente.setOnClickListener {
                    val archivoUsuario = File(this@InformacionLiga.filesDir, "cache_user.txt")

                    if (archivoUsuario.exists()) {
                        // El archivo existe, intenta borrarlo
                        if (archivoUsuario.delete()) {
                            // Borrado exitoso
                            Log.i(
                                "Borrado",
                                "El archivo cache_user.txt ha sido borrado exitosamente."
                            )
                        } else {
                            // Fallo al borrar el archivo
                            Log.e(
                                "Error de borrado",
                                "Fallo al intentar borrar el archivo cache_user.txt."
                            )
                        }
                    } else {
                        // El archivo no existe
                        Log.i("No existe", "El archivo cache_user.txt no existe en el directorio.")
                    }
                    irActividad(Login::class.java)
                }
            } else {
            }
        }
    }

    fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }

    fun cargarDatos(direccionLiga: String, fecha_fundacion: String, lista_categorias: String) {
        var viewPager = findViewById(R.id.viewPager_liga) as ViewPager
        var tablayout = findViewById(R.id.tablayout_liga) as TabLayout
        val bundle = Bundle()

        bundle.putString("direccion", direccionLiga)
        bundle.putString("fechaFundacion", fecha_fundacion)
        bundle.putString("listaCategorias", lista_categorias)
        val fragmentInfoLiga = Fragment_info_liga()
        fragmentInfoLiga.arguments = bundle
        val fragmentCategoriasLiga = Fragment_categorias_liga()
        fragmentCategoriasLiga.arguments = bundle





        fragmentAdapter = FragmentAdapter(supportFragmentManager)
        val archivoUsuario = File(this.filesDir, "cache_user.txt")

        if (archivoUsuario.exists()) {
            val UsuarioLogin = archivoUsuario.readText()
            val json = JSONObject(UsuarioLogin)
            val jsonUsuario = json.getString("tipo_usuario")



            if (jsonUsuario == "presidente") {
                fragmentAdapter.addFragment(fragmentCategoriasLiga, "CATEGORÍAS")
            } else {
                fragmentAdapter.addFragment(fragmentInfoLiga, "INFO")
                fragmentAdapter.addFragment(fragmentCategoriasLiga, "CATEGORÍAS")
            }
        }



        viewPager.adapter = fragmentAdapter
        tablayout.setupWithViewPager(viewPager)
    }

    private fun obtenerCategoriasLiga(idLiga: String) {
        val url = consultaBaseDeDatos.obtenerURLConsulta("3_mostrar_categoria.php")
        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("id_liga", idLiga)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonData = response.body?.string()
                    try {
                        val jsonObject = JSONObject(jsonData)
                        if (jsonObject.has("datos")) {
                            runOnUiThread {
                                cargarDatos(direccionLiga, fecha_fundacion, jsonData ?: "")
                            }
                        } else {
                            // Se maneja el caso donde "datos" no está presente en la respuesta
                            Log.e("", "No hay datos en la respuesta")
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.e("", "Error al parsear el JSON")

                    }
                } else {
                    // Se maneja los errores en la respuesta HTTP
                    Log.e("Error HTTP", "Código: ${response.code}")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                // Se maneja los errores de conexión
                Log.e("Error de conexión", "Falló la conexión: ${e.message}")


            }
        })
    }
}