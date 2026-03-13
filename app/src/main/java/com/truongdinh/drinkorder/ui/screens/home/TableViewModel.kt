package com.truongdinh.drinkorder.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.truongdinh.drinkorder.data.enum.OrderStatus
import com.truongdinh.drinkorder.data.enum.TableStatus
import com.truongdinh.drinkorder.data.local.Prefs
import com.truongdinh.drinkorder.data.model.Table
import com.truongdinh.drinkorder.data.repository.TableRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TableViewModel(
    private val repository: TableRepository
) : ViewModel() {

    private val _selectedStatus = MutableStateFlow("Tất cả")
    val selectedStatus = _selectedStatus.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _tableOwners = MutableStateFlow<Map<Int, String>>(emptyMap())
    val tableOwners: StateFlow<Map<Int, String>> = _tableOwners.asStateFlow()

    // Tự động khởi tạo dữ liệu khi ViewModel được tạo
    init {
        viewModelScope.launch {
            repository.ensureDataInitialized()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val tables: StateFlow<List<Table>> = combine(
        _selectedStatus.flatMapLatest { status ->
            when (status) {
                "Tất cả" -> repository.getAllTables()
                else -> repository.getTablesByStatus(status)
            }
        },
        _searchQuery
    ) { tables, query ->
        if (query.isBlank()) tables
        else tables.filter { it.name.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setFilter(status: String) {
        _selectedStatus.value = status
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun insertData() {
        viewModelScope.launch {
            repository.insertData()
        }
    }

    fun updateTableStatus(tableId: Int, status: String) {
        viewModelScope.launch {
            repository.updateTableStatus(tableId, status)
        }
    }

    // Giữ lại hàm này cho backward compatibility
    fun initializeTablesIfNeeded(prefs: Prefs) {
        viewModelScope.launch {
            // Kiểm tra database thực tế thay vì dựa vào prefs
            repository.ensureDataInitialized()
        }
    }

    fun resetAllTables() {
        viewModelScope.launch {
            repository.resetAllTablesToEmpty()
        }
    }

    fun syncTablesWithFirestore() {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("orders")
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener

                if (snapshot != null) {
                    val ownerMap = snapshot.documents
                        .groupBy { it.getLong("tableDetailId")?.toInt() }
                        .mapNotNull { (tableId, docs) ->
                            if (tableId == null) return@mapNotNull null
                            // Lấy doc mới nhất của từng bàn
                            val latestDoc = docs.maxByOrNull {
                                it.getTimestamp("createdAt")?.seconds ?: 0
                            }
                            val username = latestDoc?.getString("username")
                            if (username != null) tableId to username
                            else null
                        }.toMap()

                    _tableOwners.value = ownerMap

                    viewModelScope.launch {
                        repository.getAllTables().collect { currentTables ->
                            currentTables.forEach { table ->
                                if (ownerMap.containsKey(table.id) && table.status != TableStatus.OCCUPIED.value) {
                                    repository.updateTableStatus(table.id, TableStatus.OCCUPIED.value)
                                }
                            }
                            return@collect
                        }
                    }
                }
            }
    }
}