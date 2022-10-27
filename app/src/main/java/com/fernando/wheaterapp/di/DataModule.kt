package com.fernando.wheaterapp.di

import com.fernando.wheaterapp.data.remote.source.WeatherRemoteDataSourceImpl
import com.fernando.wheaterapp.data.repositories.WeatherRemoteDataSource
import com.fernando.wheaterapp.data.repositories.WeatherRepositoryImpl
import com.fernando.wheaterapp.domain.repositories.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {
    @Binds
    fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl,
    ): WeatherRepository

    @Binds
    fun bindWeatherRemoteDataSource(
        weatherRemoteDataSourceImpl: WeatherRemoteDataSourceImpl,
    ): WeatherRemoteDataSource
}