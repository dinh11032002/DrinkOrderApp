package com.truongdinh.drinkorder.ui.screens.auth.signup

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val confirmPassword: String = "",

    val nameError: String? = null,
    val emailError: String? = null,
    val phoneError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,

    val isLoading: Boolean = false,
    val registerError: String? = null
)
