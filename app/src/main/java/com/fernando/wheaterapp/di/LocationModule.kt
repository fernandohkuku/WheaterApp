package com.fernando.wheaterapp.di

import com.fernando.wheaterapp.domain.managers.LocationManager
import com.fernando.wheaterapp.infrastructure.managers.LocationManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {
    @Binds
    internal abstract fun bindLocationManager(
        locationManagerImpl: LocationManagerImpl,
    ): LocationManager

}