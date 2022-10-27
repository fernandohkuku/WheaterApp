package com.fernando.wheaterapp.data.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.fernando.wheaterapp.data.mappers.toWeatherInfo
import com.fernando.wheaterapp.data.models.WeatherDto
import com.fernando.wheaterapp.domain.entities.weather.WeatherInfoEntity
import com.fernando.wheaterapp.domain.repositories.WeatherRepository
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
internal class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource,
) : WeatherRepository {

    override suspend fun getWeatherData(latitude: Double, longitude: Double): WeatherInfoEntity =
        remoteDataSource.getWeatherData(
            latitude,
            longitude
        ).toWeatherInfo()
}


internal interface WeatherRemoteDataSource {
    suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): WeatherDto
}

