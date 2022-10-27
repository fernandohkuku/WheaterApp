package com.fernando.wheaterapp.domain.usecases.weather

import com.fernando.wheaterapp.domain.entities.location.LocationEntity
import com.fernando.wheaterapp.domain.entities.weather.WeatherInfoEntity
import com.fernando.wheaterapp.domain.repositories.WeatherRepository
import com.fernando.wheaterapp.domain.usecases.base.UseCase
import kotlinx.coroutines.CoroutineDispatcher

class GetWeatherUseCase(
    private val repository: WeatherRepository,
    background: CoroutineDispatcher,
) : UseCase<WeatherInfoEntity, LocationEntity>(background) {
    override suspend fun run(input: LocationEntity?): WeatherInfoEntity {
        requireNotNull(input) { "location can't be null" }
        return repository.getWeatherData(
            latitude = input.latitude,
            longitude = input.longitude
        )
    }
}