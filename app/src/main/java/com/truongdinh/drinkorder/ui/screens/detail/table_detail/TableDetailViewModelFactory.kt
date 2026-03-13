package com.truongdinh.drinkorder.ui.screens.detail.table_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.truongdinh.drinkorder.data.repository.TableRepository

class TableDetailViewModelFactory(
    private val tableRepository: TableRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TableDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TableDetailViewModel(tableRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}