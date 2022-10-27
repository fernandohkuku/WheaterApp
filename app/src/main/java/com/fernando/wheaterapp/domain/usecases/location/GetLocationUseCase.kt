package com.fernando.wheaterapp.domain.usecases.location

import com.fernando.wheaterapp.domain.entities.location.LocationEntity
import com.fernando.wheaterapp.domain.managers.LocationManager
import com.fernando.wheaterapp.domain.usecases.base.UseCase
import kotlinx.coroutines.CoroutineDispatcher

internal class GetLocationUseCase(
    private val manager: LocationManager,
    background: CoroutineDispatcher,
) : UseCase<LocationEntity, Void>(background) {
    override suspend fun run(input: Void?): LocationEntity {
        manager.requestEnable()
        return manager.getLatestLocation()
    }

}