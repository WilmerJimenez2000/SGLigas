package com.example.sgligas.Login
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sgligas.R
import java.util.concurrent.TimeUnit

class VerificarCodigo : AppCompatActivity() {

    private lateinit var edtCodigo: EditText
    private lateinit var btnVerificar: Button
    private lateinit var textView7: TextView

    private lateinit var numeroIntentos: TextView

    private var correoUsuario: String = ""
    private var codigoVerificacion: String = ""
    private var intentosRestantes: Int = 3

    private lateinit var countDownTimer: CountDownTimer
    private var timeLeftInMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verificar_codigo)

        edtCodigo = findViewById(R.id.edtCodigoVer)
        btnVerificar = findViewById(R.id.btnverificarcode)
        textView7 = findViewById(R.id.textView7)
        numeroIntentos = findViewById(R.id.textNumeroIntentos)

        val imgreturn: ImageView = findViewById(R.id.imgreturn)
        imgreturn.setOnClickListener {
            irActividad(RecuperarContrasena::class.java)
        }

        correoUsuario = intent.getStringExtra("correoUsuario") ?: ""
        codigoVerificacion = intent.getStringExtra("codigoVerificacion") ?: ""

        textView7.text = "Introduce el código de verificación enviado al correo $correoUsuario ."
        numeroIntentos.text = "Intentos restantes: $intentosRestantes"

        timeLeftInMillis = TimeUnit.MINUTES.toMillis(3)
        startTimer()

        btnVerificar.setOnClickListener {
            verificarCodigo()
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimer()
            }

            override fun onFinish() {
                irActividad(RecuperarContrasena::class.java)
                Toast.makeText(this@VerificarCodigo, "Tiempo agotado", Toast.LENGTH_SHORT).show()

            }
        }.start()
    }

    private fun updateTimer() {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeftInMillis) % 60
        val formattedTime = String.format("%02d:%02d", minutes, seconds)
        textView7.text = "Introduce el código de verificación enviado al correo $correoUsuario. Tiempo de validez de código ($formattedTime)"
    }

    private fun verificarCodigo() {
        val codigoIngresado = edtCodigo.text.toString()

        if (codigoIngresado.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa el código de verificación", Toast.LENGTH_SHORT).show()
            return
        }

        if (codigoIngresado == codigoVerificacion) {
            countDownTimer.cancel()
            val intent = Intent(this, CambiarContrasena::class.java).apply {
                putExtra("correoUsuario", correoUsuario)
            }
            startActivity(intent)
        } else {
            intentosRestantes--
            numeroIntentos.text = "Intentos restantes: $intentosRestantes"
            limpicarCampo()

            if (intentosRestantes == 0) {
                Toast.makeText(this, "Has agotado los intentos. Vuelve a iniciar el proceso de recuperación.", Toast.LENGTH_SHORT).show()
                irActividad(RecuperarContrasena::class.java)
            } else {
                Toast.makeText(this, "Código incorrecto. Intentos restantes: $intentosRestantes", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun irActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }

    private fun limpicarCampo(){
        edtCodigo.text.clear()
    }
}
