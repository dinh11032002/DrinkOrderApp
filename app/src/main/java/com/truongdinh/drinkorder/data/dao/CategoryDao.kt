package com.truongdinh.drinkorder.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.truongdinh.drinkorder.data.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query(value = "SELECT * FROM categories")
    fun getAllCategories(): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<Category>)

    @Delete
    suspend fun deleteCategory(category: Category)
}