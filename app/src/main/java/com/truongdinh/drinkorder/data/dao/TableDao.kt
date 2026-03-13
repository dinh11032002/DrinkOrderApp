package com.truongdinh.drinkorder.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.truongdinh.drinkorder.data.model.Table
import kotlinx.coroutines.flow.Flow

@Dao
interface TableDao {
    @Query("SELECT * FROM tables")
    fun getAllTables(): Flow<List<Table>>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertTable(table: Table)

    @Update
    suspend fun updateTable(table: Table)

    @Delete
    suspend fun deleteTable(table: Table)

    @Query("DELETE FROM sqlite_sequence WHERE name='tables'")
    suspend fun resetTableAutoIncrement()

    @Query("DELETE FROM tables")
    suspend fun deleteAllTables()

    @Query("SELECT * FROM tables WHERE name LIKE '%' || :query || '%'")
    fun searchTables(query: String): Flow<List<Table>>

    @Query("SELECT * FROM tables WHERE id = :id")
    suspend fun getTableById(id: Int): Table?

    @Query("UPDATE tables SET status = :status WHERE id = :tableId")
    suspend fun updateTableStatus(tableId: Int, status: String)

    @Query("SELECT * FROM tables WHERE status = :status")
    fun getTablesByStatus(status: String): Flow<List<Table>>
}