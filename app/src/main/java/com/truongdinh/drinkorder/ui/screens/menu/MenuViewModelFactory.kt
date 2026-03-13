package com.truongdinh.drinkorder.ui.screens.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.truongdinh.drinkorder.data.repository.CategoryRepository
import com.truongdinh.drinkorder.data.repository.DrinkRepository

class MenuViewModelFactory(
    private val categoryRepository: CategoryRepository,
    private val drinkRepository: DrinkRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MenuViewModel::class.java)) {
            return MenuViewModel(drinkRepository, categoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}