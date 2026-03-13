package com.truongdinh.drinkorder.ui.screens.detail.table_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.truongdinh.drinkorder.data.model.Order
import com.truongdinh.drinkorder.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)

    fun loadOrdersByTable(tableDetailId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = orderRepository.getOrdersByTable(tableDetailId)
                _orders.value = result
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
