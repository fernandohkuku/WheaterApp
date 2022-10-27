package com.fernando.wheaterapp.data.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.fernando.wheaterapp.data.models.WeatherDataDto
import com.fernando.wheaterapp.data.models.WeatherDto
import com.fernando.wheaterapp.domain.entities.weather.WeatherDataEntity
import com.fernando.wheaterapp.domain.entities.weather.WeatherInfoEntity
import com.fernando.wheaterapp.domain.entities.weather.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class IndexedWeatherDataEntity(
    val index: Int,
    val data: WeatherDataEntity,
)

@RequiresApi(Build.VERSION_CODES.O)
fun WeatherDataDto.toWeatherDataMap(): Map<Int, List<WeatherDataEntity>> {
    return time.mapIndexed { index, time ->
        val temperature = temperatures[index]
        val weatherCode = weatherCodes[index]
        val pressure = pressures[index]
        val windSpeed = windSpeeds[index]
        val humidity = humidities[index]
        IndexedWeatherDataEntity(
            index = index,
            data = WeatherDataEntity(
                time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
                temperatureCelsius = temperature,
                windSpeed = windSpeed,
                pressure = pressure,
                humidity = humidity,
                weatherType = WeatherType.fromWMO(weatherCode)
            )
        )
    }.groupBy { it.index / 24 }.mapValues { it.value.map { it.data } }
}

@RequiresApi(Build.VERSION_CODES.O)
fun WeatherDto.toWeatherInfo() :WeatherInfoEntity{
    val weatherDataMap = weatherDataDto.toWeatherDataMap()
    val now = LocalDateTime.now()
    val currentWeather = weatherDataMap[0]?.find {
        val hour = if (now.minute < 30) now.hour else now.hour + 1
        it.time.hour == hour
    }

    return WeatherInfoEntity(
        weatherDataPerDay = weatherDataMap,
        currentWeatherData = currentWeather
    )
}