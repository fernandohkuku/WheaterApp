package com.fernando.wheaterapp.domain.entities.weather

import java.time.LocalDateTime

data class WeatherDataEntity(
    val time: LocalDateTime,
    val temperatureCelsius: Double,
    val pressure: Double,
    val windSpeed: Double,
    val humidity: Double,
    val weatherType: WeatherType
) {
}