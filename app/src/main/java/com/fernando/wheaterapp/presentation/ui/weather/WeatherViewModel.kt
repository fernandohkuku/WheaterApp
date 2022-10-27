package com.fernando.wheaterapp.presentation.ui.weather

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fernando.wheaterapp.domain.entities.location.LocationEntity
import com.fernando.wheaterapp.domain.usecases.location.GetLocationUseCase
import com.fernando.wheaterapp.domain.usecases.weather.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class WeatherViewModel @Inject constructor(
    private val getLocationUseCase: GetLocationUseCase,
    private val getWeatherUseCase: GetWeatherUseCase,
) : ViewModel() {

    var state by mutableStateOf(WeatherState())
        private set

    fun loadWeatherInfo() = viewModelScope.launch(Main) {
        state = state.copy(
            isLoading = true,
            error = null
        )
        getLocationUseCase().subscribe(
            ::onSuccessLocation
        ) { error ->
            state = state.copy(
                isLoading = false,
                error = error.message
            )
            onError(error)
        }
    }

    private suspend fun onSuccessLocation(location: LocationEntity) {
        getWeatherUseCase(location).fold({ weatherInfo ->
            state = state.copy(
                weatherInfoEntity = weatherInfo,
                isLoading = false,
                error = null
            )
        }, { error ->
            state = state.copy(
                weatherInfoEntity = null,
                isLoading = false,
                error = error.message
            )
            onError(error)
        })
    }

    private fun onError(error: Exception) {
        Timber.e(error)
    }

}