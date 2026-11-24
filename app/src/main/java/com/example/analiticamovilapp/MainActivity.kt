package com.example.analiticamovilapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

        btnSincronizar.setOnClickListener {
            Toast.makeText(
                this,
                "Preparando entorno para consumir API...",
                Toast.LENGTH_SHORT
            ).show()
            tvEstadoModelo.text = "Estado del Modelo: Listo para conectar"
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