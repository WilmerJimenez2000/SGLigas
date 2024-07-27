package com.example.sgligas

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

object  mostrarMensaje {

    fun mensaje(context: Context, message: String, iconResId: Int) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.toast_layout, null)

        // Configura el mensaje en el diseño personalizado
        val text = layout.findViewById<TextView>(R.id.custom_toast_message)
        text.text = message

        // Configura el icono en el diseño personalizado
        val icon = layout.findViewById<ImageView>(R.id.custom_toast_icon)
        icon.setImageResource(iconResId)

        // Crea el Toast con el diseño personalizado
        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout

        // Muestra el Toast
        toast.show()
    }
}