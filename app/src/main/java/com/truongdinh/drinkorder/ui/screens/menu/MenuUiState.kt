package com.truongdinh.drinkorder.ui.screens.menu

import com.truongdinh.drinkorder.data.model.Category
import com.truongdinh.drinkorder.data.model.Drink

data class MenuUiState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val drinks: List<Drink> = emptyList(),
    val selectedCategoryId: Int? = null,
    val query: String = "",
    val error: String? = null
)
