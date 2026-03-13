package com.truongdinh.drinkorder.ui.screens.auth.sigin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.truongdinh.drinkorder.data.repository.UserRepository
import com.truongdinh.drinkorder.ui.screens.profile.SessionManager

class LoginViewModelFactory(
    private val repository: UserRepository,
    private val sessionManager: SessionManager,
    private val rememberAccountStore: RememberAccountStore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository, sessionManager, rememberAccountStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}