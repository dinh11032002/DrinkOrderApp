package com.truongdinh.drinkorder.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.truongdinh.drinkorder.data.model.Drink
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinkDao {
    @Query(value = "SELECT * FROM drinks")
    fun getAllDrinks(): Flow<List<Drink>>

    @Query(value = "SELECT * FROM drinks WHERE idCategory = :categoryId")
    fun getDrinksByCategory(categoryId: Int): Flow<List<Drink>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrinks(drinks: List<Drink>)

    @Delete
    suspend fun deleteDrink(drink: Drink)
}