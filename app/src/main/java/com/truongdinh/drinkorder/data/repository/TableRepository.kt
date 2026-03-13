package com.truongdinh.drinkorder.data.repository

import com.truongdinh.drinkorder.data.dao.TableDao
import com.truongdinh.drinkorder.data.local.Prefs
import com.truongdinh.drinkorder.data.model.Table
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class TableRepository(
    private val dao: TableDao,
    private val prefs: Prefs  // Thêm Prefs để kiểm tra đã khởi tạo chưa
) {

    fun getAllTables(): Flow<List<Table>> =
        dao.getAllTables()
            .flowOn(Dispatchers.IO)

    suspend fun insertData() = withContext(Dispatchers.IO) {
        dao.deleteAllTables()
        dao.resetTableAutoIncrement()
        val sampleTables = (1..20).map { index ->
            Table(name = "Bàn $index", status = "Trống")
        }
        sampleTables.forEach { dao.insertTable(it) }
        prefs.isTableInitialized = true  // Đánh dấu đã khởi tạo
    }

    // Hàm tự động khởi tạo nếu chưa có dữ liệu
    suspend fun ensureDataInitialized() = withContext(Dispatchers.IO) {
        val tables = dao.getAllTables().first()
        // Kiểm tra xem có bàn nào không, bỏ qua flag vì có thể không đồng bộ
        if (tables.isEmpty()) {
            insertData()
        }
    }

    suspend fun update(table: Table) = withContext(Dispatchers.IO) {
        dao.updateTable(table)
    }

    fun searchTables(query: String): Flow<List<Table>> =
        dao.searchTables(query)
            .flowOn(Dispatchers.IO)

    suspend fun getTableById(id: Int): Table? = withContext(Dispatchers.IO) {
        dao.getTableById(id)
    }

    suspend fun updateTableStatus(tableId: Int, status: String) = withContext(Dispatchers.IO) {
        dao.updateTableStatus(tableId, status)
    }

    suspend fun resetAllTablesToEmpty() = withContext(Dispatchers.IO) {
        val allTables = dao.getAllTables().first()
        allTables.forEach { table ->
            dao.updateTableStatus(table.id, "Trống")
        }
    }

    fun getTablesByStatus(status: String): Flow<List<Table>> =
        dao.getTablesByStatus(status)
            .flowOn(Dispatchers.IO)
}