package com.example.analiticamovilapp.network

import com.example.analiticamovilapp.models.ClimaResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v1/forecast")
    suspend fun obtenerTelemetriaClima(
        @Query("latitude") latitud: Double = 19.43,
        @Query("longitude") longitud: Double = -99.13,
        @Query("current_weather") climaActual: Boolean = true
    ): Response<ClimaResponse>
}