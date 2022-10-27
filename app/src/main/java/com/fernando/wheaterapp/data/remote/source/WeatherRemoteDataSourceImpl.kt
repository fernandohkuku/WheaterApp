package com.fernando.wheaterapp.data.remote.source

import com.fernando.wheaterapp.data.models.WeatherDto
import com.fernando.wheaterapp.data.remote.api.WeatherService
import com.fernando.wheaterapp.data.repositories.WeatherRemoteDataSource
import javax.inject.Inject

internal class WeatherRemoteDataSourceImpl @Inject constructor(
    private val weatherService: WeatherService,
) : WeatherRemoteDataSource {

    override suspend fun getWeatherData(latitude: Double, longitude: Double): WeatherDto =
        weatherService.getWeatherData(
            latitude,
            longitude
        )
}