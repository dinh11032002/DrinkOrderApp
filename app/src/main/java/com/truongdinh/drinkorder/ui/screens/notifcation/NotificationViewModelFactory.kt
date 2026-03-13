package com.truongdinh.drinkorder.ui.screens.notifcation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NotificationViewModelFactory(
    private val username: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            return NotificationViewModel(username) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}