package com.truongdinh.drinkorder.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.truongdinh.drinkorder.data.repository.TableRepository

class TableViewModelFactory(
    private val repository: TableRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TableViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TableViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}