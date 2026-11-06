package com.pes.weatherandcitytracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pes.weatherandcitytracker.data.local.entity.FavoriteCityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Query("SELECT * FROM favorite_cities ORDER BY savedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteCityEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_cities WHERE id = :cityId)")
    fun isFavorite(cityId: Int): Flow<Boolean>

    @Query("SELECT * FROM favorite_cities WHERE id = :cityId LIMIT 1")
    suspend fun getFavoriteById(cityId: Int): FavoriteCityEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(city: FavoriteCityEntity)

    @Delete
    suspend fun deleteFavorite(city: FavoriteCityEntity)

    @Query("DELETE FROM favorite_cities WHERE id = :cityId")
    suspend fun deleteFavoriteById(cityId: Int)
}

