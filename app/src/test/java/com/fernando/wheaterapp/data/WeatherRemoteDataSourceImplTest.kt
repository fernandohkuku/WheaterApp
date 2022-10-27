package com.fernando.wheaterapp.data

import com.fernando.wheaterapp.data.remote.api.WeatherService
import org.junit.Rule
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock


@RunWith(JUnit4::class)
class WeatherRemoteDataSourceImplTest {

    @Rule
    @JvmField
    var expectedException = ExpectedException.none()

    @Mock
    private lateinit var service: WeatherService



}