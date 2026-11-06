package com.pes.weatherandcitytracker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pes.weatherandcitytracker.data.local.dao.CityDao
import com.pes.weatherandcitytracker.data.local.dao.WeatherDao
import com.pes.weatherandcitytracker.data.local.entity.FavoriteCityEntity
import com.pes.weatherandcitytracker.data.local.entity.WeatherHistoryEntity

@Database(
    entities = [
        FavoriteCityEntity::class,
        WeatherHistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDao
    abstract fun weatherDao(): WeatherDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "weather_tracker_db"
                ).fallbackToDestructiveMigration(dropAllTables = true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
