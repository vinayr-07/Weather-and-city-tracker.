package com.pes.weatherandcitytracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.pes.weatherandcitytracker.data.local.AppDatabase
import com.pes.weatherandcitytracker.data.local.PreferencesManager
import com.pes.weatherandcitytracker.data.remote.RetrofitClient
import com.pes.weatherandcitytracker.data.repository.WeatherRepositoryImpl
import com.pes.weatherandcitytracker.ui.navigation.AppNavGraph
import com.pes.weatherandcitytracker.ui.navigation.BottomNavBar
import com.pes.weatherandcitytracker.ui.theme.WeatherAndCityTrackerTheme
import com.pes.weatherandcitytracker.ui.viewmodel.FavoritesViewModel
import com.pes.weatherandcitytracker.ui.viewmodel.SettingsViewModel
import com.pes.weatherandcitytracker.ui.viewmodel.ViewModelFactory
import com.pes.weatherandcitytracker.ui.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Initialize Singletons & Repository
        val database = AppDatabase.getInstance(applicationContext)
        val apiService = RetrofitClient.apiService
        val preferencesManager = PreferencesManager(applicationContext)
        val repository = WeatherRepositoryImpl(
            apiService = apiService,
            cityDao = database.cityDao(),
            weatherDao = database.weatherDao(),
            preferencesManager = preferencesManager
        )

        // 2. Initialize ViewModels via Factory
        val factory = ViewModelFactory(repository)
        val weatherViewModel = ViewModelProvider(this, factory)[WeatherViewModel::class.java]
        val favoritesViewModel = ViewModelProvider(this, factory)[FavoritesViewModel::class.java]
        val settingsViewModel = ViewModelProvider(this, factory)[SettingsViewModel::class.java]

        setContent {
            WeatherAndCityTrackerTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavBar(navController = navController)
                    }
                ) { innerPadding ->
                    AppNavGraph(
                        navController = navController,
                        weatherViewModel = weatherViewModel,
                        favoritesViewModel = favoritesViewModel,
                        settingsViewModel = settingsViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
