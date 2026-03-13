package com.truongdinh.drinkorder.ui.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.truongdinh.drinkorder.data.model.User
import com.truongdinh.drinkorder.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            _isLoading.value = true

            val email = sessionManager.getEmail()
            if (email.isNullOrBlank()) {
                Log.e("ProfileVM", "Session email is null")
                _user.value = null
                _isLoading.value = false
                return@launch
            }

            try {
                _user.value = userRepository.getUserByEmail(email)
            } catch (e: Exception) {
                Log.e("ProfileVM", "Load user failed", e)
                _user.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        sessionManager.clearSession()
        _user.value = null
    }
}