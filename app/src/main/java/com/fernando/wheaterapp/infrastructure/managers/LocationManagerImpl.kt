package com.fernando.wheaterapp.infrastructure.managers

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.fernando.wheaterapp.data.mappers.toEntity
import com.fernando.wheaterapp.data.mappers.toLocationException
import com.fernando.wheaterapp.domain.entities.location.LocationEntity
import com.fernando.wheaterapp.domain.managers.LocationManager
import com.fernando.wheaterapp.infrastructure.exceptions.PermissionException
import com.fernando.wheaterapp.uiktx.hasPermission
import com.fernando.wheaterapp.uiktx.isNotNull
import com.fernando.wheaterapp.uiktx.isNull
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@SuppressLint("MissingPermission")
class LocationManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val locationProvider: FusedLocationProviderClient,
) : LocationManager {
    companion object {
        private const val TIMEOUT_MILLIS = 20 * 1000
    }

    private object Permission {
        const val LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    }

    private object Threshold {
        const val ANTIQUITY_MILLIS = 15 * 60 * 1000
        const val TIME_DIFFERENCE_MILLIS = 5 * 60 * 1000
    }

    private val locationRequest = LocationRequest.create().apply {
        interval = 0
        priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
    }

    private lateinit var locationCallback: LocationCallback

    override suspend fun requestEnable() {
        val settingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()
        try {
            LocationServices.getSettingsClient(context)
                .checkLocationSettings(settingsRequest)
                .await()
        } catch (ex: ApiException) {
            throw ex.toLocationException()
        }
    }

    override suspend fun getLatestLocation(): LocationEntity = validate().run {
        val lastLocationDto = locationProvider.lastLocation.await()
        if (lastLocationDto.hasRequiredAntiquity()) {
            return lastLocationDto.toEntity()
        }
        return getBetterLocation(lastLocationDto)
    }

    private fun validate() {
        if (context.hasPermission(Permission.LOCATION)) {
            throw PermissionException("Location permission required")
        }
    }

    private fun Location?.hasRequiredAntiquity(): Boolean {
        if (isNull()) {
            return false
        }

        val currentTimeMills = System.currentTimeMillis()
        return currentTimeMills - this!!.time < Threshold.ANTIQUITY_MILLIS
    }

    private suspend fun getBetterLocation(lastLocation: Location?): LocationEntity =
        suspendCoroutine { continuation ->
            val requestTimeMillis = System.currentTimeMillis()
            startLocationUpdates { locationResult ->
                var locationDto = locationResult.getBetterLocationThan(lastLocation)

                if (locationDto.isNull() && hasTimeoutPassed(requestTimeMillis)) {
                    locationDto = lastLocation
                }

                if (locationDto.isNotNull()) {
                    stopLocationUpdates()
                    continuation.resume(locationDto.toEntity())
                    return@startLocationUpdates
                }
            }
        }


    private fun startLocationUpdates(callback: (LocationResult) -> Unit) {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                callback(locationResult)
            }
        }
        locationProvider.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }


    private fun LocationResult.getBetterLocationThan(lastLocation: Location?): Location? {
        for (location in locations) {
            if (location.isBetterLocation(lastLocation)) {
                return location
            }
        }

        return null
    }

    private fun Location.isBetterLocation(oldLocationDto: Location?): Boolean {
        if (oldLocationDto.isNull()) return true

        val timeDeltaMillis = time - oldLocationDto!!.time
        val isSignificantlyNewer = timeDeltaMillis > Threshold.TIME_DIFFERENCE_MILLIS
        val isSignificantlyOlder = timeDeltaMillis < -Threshold.TIME_DIFFERENCE_MILLIS
        val isNewer = timeDeltaMillis > 0

        if (isSignificantlyNewer)
            return true
        else if (isSignificantlyOlder)
            return false

        val accuracyDelta = (accuracy - oldLocationDto.accuracy).toInt()
        val isLessAccurate = accuracyDelta > 0
        val isMoreAccurate = accuracyDelta < 0
        val isSignificantlyLessAccurate = accuracyDelta > 200

        return if (isMoreAccurate) true
        else if (isNewer && !isLessAccurate) true
        else isNewer && !isSignificantlyLessAccurate
    }

    private fun hasTimeoutPassed(requestTimeMillis: Long): Boolean {
        val currentTimeMillis = System.currentTimeMillis()
        return requestTimeMillis - currentTimeMillis > TIMEOUT_MILLIS
    }

    private fun stopLocationUpdates() = locationProvider.removeLocationUpdates(locationCallback)

}