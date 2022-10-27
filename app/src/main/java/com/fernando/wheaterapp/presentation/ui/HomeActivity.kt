package com.fernando.wheaterapp.presentation.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fernando.wheaterapp.presentation.components.PermissionRequest
import com.fernando.wheaterapp.presentation.components.PermissionRequester
import com.fernando.wheaterapp.presentation.ui.theme.WheaterAppTheme
import com.fernando.wheaterapp.presentation.ui.weather.WeatherCard
import com.fernando.wheaterapp.presentation.ui.weather.WeatherForestCast
import com.fernando.wheaterapp.presentation.ui.weather.WeatherViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    private val viewModel: WeatherViewModel by viewModels()

    private lateinit var permissionRequester: ActivityResultLauncher<Array<String>>


    private object Permission {
        const val ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION"
        const val ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionRequester = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            viewModel.loadWeatherInfo()
        }
        permissionRequester.launch(arrayOf(
            Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION
        ))
        setContent {
            WheaterAppTheme {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.DarkGray)

                    ) {
                        WeatherCard(
                            state = viewModel.state,
                            backgroundColor = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        WeatherForestCast(
                            state = viewModel.state,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.DarkGray)
                        )
                    }
                    if (viewModel.state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    viewModel.state.error?.let { error ->
                        Text(
                            text = error,
                            color = Color.Red,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

