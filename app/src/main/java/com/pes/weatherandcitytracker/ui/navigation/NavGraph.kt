package com.pes.weatherandcitytracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pes.weatherandcitytracker.ui.screens.favorites.FavoritesScreen
import com.pes.weatherandcitytracker.ui.screens.home.HomeScreen
import com.pes.weatherandcitytracker.ui.screens.settings.SettingsScreen
import com.pes.weatherandcitytracker.ui.viewmodel.FavoritesViewModel
import com.pes.weatherandcitytracker.ui.viewmodel.SettingsViewModel
import com.pes.weatherandcitytracker.ui.viewmodel.WeatherViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    weatherViewModel: WeatherViewModel,
    favoritesViewModel: FavoritesViewModel,
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(viewModel = weatherViewModel)
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                viewModel = favoritesViewModel,
                onCitySelected = { city ->
                    weatherViewModel.selectCity(city)
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(viewModel = settingsViewModel)
        }
    }
}

