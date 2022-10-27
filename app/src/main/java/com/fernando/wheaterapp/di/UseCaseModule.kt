package com.fernando.wheaterapp.di

import com.fernando.wheaterapp.domain.managers.LocationManager
import com.fernando.wheaterapp.domain.repositories.WeatherRepository
import com.fernando.wheaterapp.domain.usecases.location.GetLocationUseCase
import com.fernando.wheaterapp.domain.usecases.weather.GetWeatherUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    internal fun provideGetLocationUseCase(
        manager: LocationManager,
        background: CoroutineDispatcher,
    ) = GetLocationUseCase(manager, background)

    @Provides
    @Singleton
    fun provideGetWeatherUseCase(
        repository: WeatherRepository,
        background: CoroutineDispatcher,
    ) = GetWeatherUseCase(repository, background)
}