package com.truongdinh.drinkorder.ui.screens.detail.table_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.truongdinh.drinkorder.data.repository.OrderRepository

class OrderViewModelFactory(
    private val repository: OrderRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            return OrderViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
