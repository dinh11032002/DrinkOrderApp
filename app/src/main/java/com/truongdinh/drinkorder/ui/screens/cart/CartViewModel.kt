package com.truongdinh.drinkorder.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.truongdinh.drinkorder.data.model.OrderFirestore
import com.truongdinh.drinkorder.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    private val repository: OrderRepository,
    private val username: String
) : ViewModel() {
    private val _orders = MutableStateFlow<List<OrderFirestore>>(emptyList())
    val orders: StateFlow<List<OrderFirestore>> = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadOrders()
    }

    private fun loadOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getOrdersByUsername(username).collect { orderList ->
                    _orders.value = orderList
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun updateOrderStatus(orderId: String, newStatus: String) {
        viewModelScope.launch {
            repository.updateOrderStatusFirestore(orderId, newStatus)
        }
    }

    fun deleteOrder(orderId: String) {
        viewModelScope.launch {
            repository.deleteOrder(orderId)
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}