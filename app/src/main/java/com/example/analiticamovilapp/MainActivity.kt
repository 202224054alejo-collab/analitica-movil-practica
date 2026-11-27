package com.example.analiticamovilapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.analiticamovilapp.network.RetrofitClient
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + v.paddingLeft,
                systemBars.top + v.paddingTop,
                systemBars.right + v.paddingRight,
                systemBars.bottom + v.paddingBottom
            )
            insets
        }

        val tvEstadoModelo = findViewById<TextView>(R.id.tvEstadoModelo)
        val btnSincronizar = findViewById<Button>(R.id.btnSincronizar)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val tvResultadosApi = findViewById<TextView>(R.id.tvResultadosApi)

        btnSincronizar.setOnClickListener {
            // 1. Estado de carga (UI Thread)
            progressBar.visibility = View.VISIBLE
            btnSincronizar.isEnabled = false
            tvEstadoModelo.text = "Estado: Consultando API REST..."

            Toast.makeText(
                this,
                "Preparando entorno para consumir API...",
                Toast.LENGTH_SHORT
            ).show()

            // 2. Ejecución asíncrona fuera del hilo principal
            lifecycleScope.launch {
                try {
                    val respuesta = RetrofitClient.apiService.obtenerTelemetriaClima()
                    if (respuesta.isSuccessful && respuesta.body() != null) {
                        val datos = respuesta.body()!!.climaActual
                        tvEstadoModelo.text = "Estado: Datos sincronizados (HTTP 200)"
                        tvResultadosApi.text = """
                            Temperatura actual: ${datos.temperatura} °C
                            Velocidad del viento: ${datos.velocidadViento} km/h
                            Última medición: ${datos.horaMedicion}
                        """.trimIndent()
                    } else {
                        tvEstadoModelo.text = "Error del servidor: Código ${respuesta.code()}"
                    }
                } catch (e: Exception) {
                    // 3. Manejo controlado de errores de red
                    tvEstadoModelo.text = "Error de conexión"
                    tvResultadosApi.text = "No se pudo conectar con la API: ${e.localizedMessage}"
                } finally {
                    // 4. Restaurar controles visuales
                    progressBar.visibility = View.GONE
                    btnSincronizar.isEnabled = true
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("CICLO_VIDA", "La aplicación entró en primer plano (onStart)")
    }

    override fun onPause() {
        super.onPause()
        Log.d("CICLO_VIDA", "La aplicación pasó a segundo plano (onPause)")
    }
}
