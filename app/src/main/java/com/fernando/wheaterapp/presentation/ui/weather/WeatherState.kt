package com.fernando.wheaterapp.presentation.ui.weather

import com.fernando.wheaterapp.domain.entities.weather.WeatherInfoEntity

data class WeatherState(
    val weatherInfoEntity: WeatherInfoEntity? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)