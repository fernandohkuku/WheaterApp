package com.fernando.wheaterapp.data.remote.api

import com.fernando.wheaterapp.data.models.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("v1/forecast?hourly=temperature_2m,relativehumidity_2m,windspeed_10m,weathercode,pressure_msl")
    suspend fun getWeatherData(
        @Query("latitude") latitude:Double,
        @Query("longitude") longitude:Double
    ):WeatherDto
}