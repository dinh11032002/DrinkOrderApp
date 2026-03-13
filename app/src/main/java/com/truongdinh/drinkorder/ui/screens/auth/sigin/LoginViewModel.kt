package com.truongdinh.drinkorder.ui.screens.auth.sigin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.truongdinh.drinkorder.data.model.User
import com.truongdinh.drinkorder.data.repository.UserRepository
import com.truongdinh.drinkorder.ui.screens.profile.SessionManager
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: UserRepository,
    private val sessionManager: SessionManager,
    private val rememberAccountStore: RememberAccountStore
) : ViewModel() {
    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(value: String) {
        uiState = uiState.copy(email = value, emailError = null)
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value, passwordError = null)
    }

    fun onRememberMeChange(value: Boolean) {
        uiState = uiState.copy(rememberMe = value)
    }

    init {
        rememberAccountStore.getEmail()?.let {
            uiState = uiState.copy(email = it, rememberMe = true)
        }
    }

    fun login(onSuccess: (User) -> Unit) {
        if (!validate()) return

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, loginError = null)

            val result = repository.login(
                email = uiState.email,
                password = uiState.password
            )

            uiState = if (result.isSuccess) {
                val user = result.getOrThrow()

                sessionManager.saveSession(user.email, user.name)

                if (uiState.rememberMe) {
                    rememberAccountStore.saveEmail(user.email)
                } else {
                    rememberAccountStore.clear()
                }

                onSuccess(user)

                LoginUiState()
            } else {
                uiState.copy(
                    isLoading = false,
                    loginError = result.exceptionOrNull()?.message
                )
            }
        }
    }

    private fun validate(): Boolean {
        var valid = true
        var state = uiState

        if (!state.email.contains("@")) {
            state = state.copy(emailError = "Email không hợp lệ")
            valid = false
        }

        if (state.password.length < 6) {
            state = state.copy(passwordError = "Mật khẩu không hợp lệ")
            valid = false
        }

        uiState = state
        return valid
    }
}