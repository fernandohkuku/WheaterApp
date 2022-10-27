package com.fernando.wheaterapp.domain.managers

import com.fernando.wheaterapp.domain.entities.location.LocationEntity

internal interface LocationManager {
    suspend fun requestEnable()
    suspend fun getLatestLocation():LocationEntity
}