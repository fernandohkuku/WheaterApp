package com.fernando.wheaterapp.domain.entities.weather

data class WeatherInfoEntity(
    val weatherDataPerDay: Map<Int, List<WeatherDataEntity>>,
    val currentWeatherData: WeatherDataEntity?,
)
