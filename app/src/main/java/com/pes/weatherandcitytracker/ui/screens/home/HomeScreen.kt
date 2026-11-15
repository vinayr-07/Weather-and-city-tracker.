package com.pes.weatherandcitytracker.ui.screens.home

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.pes.weatherandcitytracker.ui.components.CurrentWeatherCard
import com.pes.weatherandcitytracker.ui.components.DailyForecastRow
import com.pes.weatherandcitytracker.ui.components.SearchBarWidget
import com.pes.weatherandcitytracker.ui.components.WeatherStatsGrid
import com.pes.weatherandcitytracker.ui.viewmodel.WeatherViewModel
import com.pes.weatherandcitytracker.util.ImplicitIntentHelper

@Composable
fun HomeScreen(
    viewModel: WeatherViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Search Bar at Top
            SearchBarWidget(
                query = state.searchQuery,
                onQueryChanged = viewModel::onSearchQueryChanged,
                searchResults = state.searchResults,
                isSearching = state.isSearching,
                onCitySelected = viewModel::selectCity
            )

            // Loading state
            if (state.isLoading && state.weatherData == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.weatherData != null) {
                val weather = state.weatherData!!

                // Main Weather Card
                CurrentWeatherCard(
                    weatherData = weather,
                    tempUnit = state.tempUnit,
                    windUnit = state.windUnit,
                    isFavorite = state.isFavorite,
                    onToggleFavorite = viewModel::toggleFavorite,
                    onViewOnMap = {
                        ImplicitIntentHelper.openCityOnMap(
                            context = context,
                            latitude = weather.city.latitude,
                            longitude = weather.city.longitude,
                            cityName = weather.city.name
                        )
                    },
                    onShare = {
                        ImplicitIntentHelper.shareWeatherReport(
                            context = context,
                            cityName = weather.city.name,
                            temperature = weather.current.temperatureCelsius,
                            tempUnit = state.tempUnit,
                            conditionDescription = weather.current.conditionDescription,
                            humidityPercentage = weather.current.humidityPercentage,
                            windSpeed = weather.current.windSpeedKmh,
                            windUnit = state.windUnit
                        )
                    },
                    onRefresh = viewModel::refreshWeather
                )

                // Key Weather Statistics Grid
                WeatherStatsGrid(
                    current = weather.current,
                    windUnit = state.windUnit
                )

                // 7-Day Forecast Row
                DailyForecastRow(
                    dailyList = weather.daily,
                    tempUnit = state.tempUnit
                )

                Spacer(modifier = Modifier.height(16.dp))
            } else if (state.errorMessage != null && state.weatherData == null) {
                // Error State with Retry
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.errorMessage ?: "Failed to load weather data",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = viewModel::refreshWeather) {
                        Text("Retry")
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
