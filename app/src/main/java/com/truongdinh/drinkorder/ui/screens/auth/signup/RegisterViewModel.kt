package com.truongdinh.drinkorder.ui.screens.auth.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.truongdinh.drinkorder.data.model.User
import com.truongdinh.drinkorder.data.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    var uiState by mutableStateOf(RegisterUiState())
        private set

    fun onNameChange(value: String) {
        uiState = uiState.copy(name = value, nameError = null)
    }

    fun onEmailChange(value: String) {
        uiState = uiState.copy(email = value, emailError = null)
    }

    fun onPhoneChange(value: String) {
        uiState = uiState.copy(phone = value, phoneError = null)
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value, passwordError = null)
    }

    fun onConfirmPasswordChange(value: String) {
        uiState = uiState.copy(
            confirmPassword = value,
            confirmPasswordError = null
        )
    }

    fun register(onSuccess: () -> Unit) {
        if (!validate()) return

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, registerError = null)

            val user = User(
                id = 0,
                name = uiState.name,
                phone = uiState.phone,
                email = uiState.email,
                password = uiState.password
            )

            val result = userRepository.register(user)

            uiState = if (result.isSuccess) {
                onSuccess()
                RegisterUiState()
            } else {
                uiState.copy(
                    isLoading = false,
                    registerError = result.exceptionOrNull()?.message ?: "Đăng ký thất bại"
                )
            }
        }
    }

    private fun validate(): Boolean {
        var valid = true
        var state = uiState

        if (state.name.isBlank()) {
            state = state.copy(nameError = "Vui lòng nhập họ tên")
            valid = false
        }

        if (!state.email.contains("@")) {
            state = state.copy(emailError = "Email không hợp lệ")
            valid = false
        }

        if (state.phone.length < 10) {
            state = state.copy(phoneError = "Số điện thoại không hợp lệ")
            valid = false
        }

        if (state.password.length < 6) {
            state = state.copy(passwordError = "Mật khẩu ít nhất 6 ký tự")
            valid = false
        }

        if (state.confirmPassword != state.password) {
            state = state.copy(confirmPasswordError = "Mật khẩu không khớp")
            valid = false
        }

        uiState = state
        return valid
    }
}