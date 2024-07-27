package com.example.sgligas
import Fragment_Ligas
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button

import androidx.fragment.app.Fragment


import androidx.appcompat.app.AppCompatActivity
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.sgligas.Inicio.Fragment_item
import com.example.sgligas.Inicio.Fragment_perfil
import com.example.sgligas.Ligas.Liga
import com.example.sgligas.databinding.ActivityMainBinding
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

import android.os.Handler
import android.os.Looper



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Fragment_Ligas())











        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){

                R.id.menu_ligas -> replaceFragment(Fragment_Ligas())
                //R.id.menu_item -> replaceFragment(Fragment_item())
                R.id.menu_perfil -> replaceFragment(Fragment_perfil())

                else ->{



                }

            }

            true

        }




    }

    override fun onBackPressed() {
        // No hacer nada cuando se presiona el botón de retroceso
        // Esto desactivará la funcionalidad del botón de retroceso
    }



    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()


    }

    private fun myFunction() {
        // Tu lógica aquí
        Log.e("Función ejecutada","en este momento")
    }


    override fun onResume() {
        super.onResume()
        val intent = Intent(this, MyForegroundService::class.java)
        startService(intent)
    }

    /*override fun onPause() {
        super.onPause()
        val intent = Intent(this, MyForegroundService::class.java)
        stopService(intent)
    }*/








    fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }









}
