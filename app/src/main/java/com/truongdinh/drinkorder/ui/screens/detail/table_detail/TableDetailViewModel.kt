package com.truongdinh.drinkorder.ui.screens.detail.table_detail

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.truongdinh.drinkorder.data.enum.OrderStatus
import com.truongdinh.drinkorder.data.enum.TableStatus
import com.truongdinh.drinkorder.data.model.Drink
import com.truongdinh.drinkorder.data.model.OrderFirestore
import com.truongdinh.drinkorder.data.repository.TableRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime

class TableDetailViewModel(
    private val tableRepository: TableRepository
) : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    private val _orderDrinks = MutableStateFlow<List<Drink>>(value = emptyList())
    val orderDrinks: StateFlow<List<Drink>> = _orderDrinks.asStateFlow()

    private val _createdAt = MutableStateFlow<LocalDateTime?>(value = null)
    val createdAt: StateFlow<LocalDateTime?> = _createdAt.asStateFlow()

    private val _orderStatus = MutableStateFlow<String?>(null)
    val orderStatus: StateFlow<String?> = _orderStatus.asStateFlow()

    fun addDrink(drink: Drink) {
        val currentList = _orderDrinks.value.toMutableList()
        if (currentList.none { it.id == drink.id }) {
            currentList.add(drink)
            _orderDrinks.value = currentList
        }
        setCreatedAtIfNeeded()
    }

    fun replacedDrink(oldDrinkId: Int, newDrink: Drink) {
        val currentList = _orderDrinks.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == oldDrinkId }

        if (index != -1) {
            currentList[index] = newDrink
            _orderDrinks.value = currentList
        } else {
            currentList.add(newDrink)
            _orderDrinks.value = currentList
        }
    }

    fun removeDrink(drink: Drink) {
        _orderDrinks.value = _orderDrinks.value.filterNot { it.id == drink.id }
    }

    fun clearOrder() {
        _orderDrinks.value = emptyList()
        _createdAt.value = null
    }

    @SuppressLint("NewApi")
    private fun setCreatedAtIfNeeded() {
        if (_createdAt.value == null) {
            _createdAt.value = LocalDateTime.now()
            viewModelScope.launch {

            }
        }
    }

    fun placeOrderToFirestore(
        tableId: Int,
        username: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val drinks = _orderDrinks.value
        if (drinks.isEmpty()) {
            onFailure(Exception("Chưa chọn món"))
            return
        }

        viewModelScope.launch {
            try {
                val totalPrice = drinks.sumOf { it.price * it.quantity }
                val order = OrderFirestore(
                    tableDetailId = tableId,
                    tableName = "Bàn $tableId",
                    username = username,
                    drinks = drinks,
                    totalPrice = totalPrice,
                    status = OrderStatus.PROCESSING.value,
                    createdAt = Timestamp.now()
                )
                firestore.collection("orders").add(order).await()

                // Cập nhật trạng thái bàn
                updateTableStatus(tableId, TableStatus.OCCUPIED)

                onSuccess()
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun updateTableStatus(tableId: Int, newStatus: TableStatus) {
        viewModelScope.launch {
            val table = tableRepository.getTableById(tableId) ?: return@launch
            tableRepository.update(table.copy(status = newStatus.value))
        }
    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.O)
    fun loadOrderFromFirestore(tableId: Int) {
        viewModelScope.launch {
            try {
                // Load cả Đang xử lý lẫn Hoàn thành
                var snapshot = firestore.collection("orders")
                    .whereEqualTo("tableDetailId", tableId)
                    .whereEqualTo("status", OrderStatus.PROCESSING.value)
                    .limit(1)
                    .get()
                    .await()

                // Nếu không có Đang xử lý thì tìm Hoàn thành mới nhất
                if (snapshot.isEmpty) {
                    snapshot = firestore.collection("orders")
                        .whereEqualTo("tableDetailId", tableId)
                        .whereEqualTo("status", OrderStatus.COMPLETE.value)
                        .limit(1)
                        .get()
                        .await()
                }

                if (!snapshot.isEmpty) {
                    val order = snapshot.documents[0]
                    _orderStatus.value = order.getString("status") // Lưu status lại

                    val drinks = order.get("drinks") as? List<Map<String, Any>> ?: emptyList()
                    _orderDrinks.value = drinks.map { d ->
                        Drink(
                            id = (d["id"] as Long).toInt(),
                            name = d["name"] as String,
                            quantity = (d["quantity"] as Long).toInt(),
                            size = d["size"] as String,
                            note = d["note"] as String,
                            price = (d["price"] as Long).toInt(),
                            image = (d["image"] as? String) ?: "",
                            idCategory = 0
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}