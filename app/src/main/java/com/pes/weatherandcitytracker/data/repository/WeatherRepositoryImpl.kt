package com.pes.weatherandcitytracker.data.repository

import com.pes.weatherandcitytracker.data.local.PreferencesManager
import com.pes.weatherandcitytracker.data.local.dao.CityDao
import com.pes.weatherandcitytracker.data.local.dao.WeatherDao
import com.pes.weatherandcitytracker.data.local.entity.FavoriteCityEntity
import com.pes.weatherandcitytracker.data.local.entity.WeatherHistoryEntity
import com.pes.weatherandcitytracker.data.model.City
import com.pes.weatherandcitytracker.data.model.CurrentWeather
import com.pes.weatherandcitytracker.data.model.DailyForecast
import com.pes.weatherandcitytracker.data.model.WeatherData
import com.pes.weatherandcitytracker.data.remote.OpenMeteoApiService
import com.pes.weatherandcitytracker.util.Resource
import com.pes.weatherandcitytracker.util.WeatherCodeMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.io.IOException

class WeatherRepositoryImpl(
    private val apiService: OpenMeteoApiService,
    private val cityDao: CityDao,
    private val weatherDao: WeatherDao,
    private val preferencesManager: PreferencesManager
) : WeatherRepository {

    override fun searchCities(query: String): Flow<Resource<List<City>>> = flow {
        if (query.trim().length < 2) {
            emit(Resource.Success(emptyList()))
            return@flow
        }
        emit(Resource.Loading())
        try {
            val response = apiService.searchCities(name = query.trim())
            if (response.isSuccessful) {
                val results = response.body()?.results.orEmpty()
                val cities = results.map { dto ->
                    City(
                        id = dto.id,
                        name = dto.name,
                        country = dto.country.orEmpty(),
                        state = dto.admin1,
                        latitude = dto.latitude,
                        longitude = dto.longitude
                    )
                }
                emit(Resource.Success(cities))
            } else {
                emit(Resource.Error("Search failed with code: ${response.code()}"))
            }
        } catch (e: IOException) {
            emit(Resource.Error("Network error: Please check your internet connection."))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error: ${e.localizedMessage ?: "Unknown"}"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getWeather(city: City): Flow<Resource<WeatherData>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getForecast(
                latitude = city.latitude,
                longitude = city.longitude
            )
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val currentDto = body.current ?: throw IllegalStateException("Current weather unavailable")
                val condition = WeatherCodeMapper.map(currentDto.weatherCode, currentDto.isDay)

                val currentWeather = CurrentWeather(
                    temperatureCelsius = currentDto.temperature2m,
                    apparentTemperatureCelsius = currentDto.apparentTemperature,
                    humidityPercentage = currentDto.relativeHumidity2m,
                    windSpeedKmh = currentDto.windSpeed10m,
                    precipitationMm = currentDto.precipitation,
                    weatherCode = currentDto.weatherCode,
                    conditionDescription = condition.description,
                    iconUrl = condition.iconUrl,
                    isDay = currentDto.isDay == 1
                )

                val dailyList = mutableListOf<DailyForecast>()
                val dailyDto = body.daily
                if (dailyDto?.time != null && dailyDto.weatherCode != null) {
                    val count = minOf(
                        dailyDto.time.size,
                        dailyDto.weatherCode.size,
                        dailyDto.temperature2mMax?.size ?: 0,
                        dailyDto.temperature2mMin?.size ?: 0
                    )
                    for (i in 0 until count) {
                        val code = dailyDto.weatherCode[i]
                        val dayCondition = WeatherCodeMapper.map(code, 1)
                        dailyList.add(
                            DailyForecast(
                                date = dailyDto.time[i],
                                maxTemperatureCelsius = dailyDto.temperature2mMax?.get(i) ?: 0.0,
                                minTemperatureCelsius = dailyDto.temperature2mMin?.get(i) ?: 0.0,
                                weatherCode = code,
                                conditionDescription = dayCondition.description,
                                iconUrl = dayCondition.iconUrl
                            )
                        )
                    }
                }

                // Cache in Room DB for offline resilience
                val historyEntity = WeatherHistoryEntity(
                    cityId = city.id,
                    cityName = city.name,
                    country = city.country,
                    state = city.state,
                    latitude = city.latitude,
                    longitude = city.longitude,
                    temperatureCelsius = currentWeather.temperatureCelsius,
                    apparentTemperatureCelsius = currentWeather.apparentTemperatureCelsius,
                    weatherCode = currentWeather.weatherCode,
                    conditionDescription = currentWeather.conditionDescription,
                    humidityPercentage = currentWeather.humidityPercentage,
                    windSpeedKmh = currentWeather.windSpeedKmh,
                    iconUrl = currentWeather.iconUrl,
                    isDay = currentWeather.isDay,
                    timestamp = System.currentTimeMillis()
                )
                weatherDao.insertHistory(historyEntity)
                preferencesManager.setLastCity(city.name)

                emit(
                    Resource.Success(
                        WeatherData(
                            city = city,
                            current = currentWeather,
                            daily = dailyList,
                            isFromCache = false
                        )
                    )
                )
            } else {
                // Fallback to cache if server error
                fallbackToCache(city, "Server error (${response.code()})")
            }
        } catch (e: Exception) {
            // Fallback to cache on network drop / timeout
            fallbackToCache(city, e.localizedMessage ?: "Network connection failed")
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun kotlinx.coroutines.flow.FlowCollector<Resource<WeatherData>>.fallbackToCache(
        city: City,
        errorMessage: String
    ) {
        val cached = weatherDao.getLatestByCityId(city.id)
            ?: weatherDao.getLatestByCityName(city.name)

        if (cached != null) {
            val currentWeather = CurrentWeather(
                temperatureCelsius = cached.temperatureCelsius,
                apparentTemperatureCelsius = cached.apparentTemperatureCelsius,
                humidityPercentage = cached.humidityPercentage,
                windSpeedKmh = cached.windSpeedKmh,
                precipitationMm = 0.0,
                weatherCode = cached.weatherCode,
                conditionDescription = cached.conditionDescription,
                iconUrl = cached.iconUrl,
                isDay = cached.isDay
            )
            emit(
                Resource.Success(
                    WeatherData(
                        city = city,
                        current = currentWeather,
                        daily = emptyList(),
                        isFromCache = true,
                        lastUpdatedTimestamp = cached.timestamp
                    )
                )
            )
        } else {
            emit(Resource.Error("Offline: No cached data available for ${city.name} ($errorMessage)"))
        }
    }

    override fun getFavoriteCities(): Flow<List<City>> {
        return cityDao.getAllFavorites().map { entities ->
            entities.map { entity ->
                City(
                    id = entity.id,
                    name = entity.name,
                    country = entity.country,
                    state = entity.state,
                    latitude = entity.latitude,
                    longitude = entity.longitude,
                    isFavorite = true
                )
            }
        }
    }

    override fun isFavorite(cityId: Int): Flow<Boolean> {
        return cityDao.isFavorite(cityId)
    }

    override suspend fun toggleFavorite(city: City) {
        val existing = cityDao.getFavoriteById(city.id)
        if (existing != null) {
            cityDao.deleteFavorite(existing)
        } else {
            cityDao.insertFavorite(
                FavoriteCityEntity(
                    id = city.id,
                    name = city.name,
                    country = city.country,
                    state = city.state,
                    latitude = city.latitude,
                    longitude = city.longitude
                )
            )
        }
    }

    override fun getSearchHistory(): Flow<List<WeatherHistoryEntity>> {
        return weatherDao.getAllHistory()
    }

    override suspend fun clearSearchHistory() {
        weatherDao.clearAllHistory()
    }

    override fun getPreferencesManager(): PreferencesManager {
        return preferencesManager
    }
}

