package com.truongdinh.drinkorder.ui.screens.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.truongdinh.drinkorder.data.repository.UserRepository

class RegisterViewModelFactory(
    private val repository: UserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}