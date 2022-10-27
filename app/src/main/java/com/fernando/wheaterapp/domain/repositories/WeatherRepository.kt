package com.fernando.wheaterapp.domain.repositories

import com.fernando.wheaterapp.domain.entities.weather.WeatherInfoEntity

interface WeatherRepository {
    suspend fun getWeatherData(
        latitude:Double,
        longitude:Double
    ): WeatherInfoEntity
}