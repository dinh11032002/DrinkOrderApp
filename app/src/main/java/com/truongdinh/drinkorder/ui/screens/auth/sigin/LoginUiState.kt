package com.truongdinh.drinkorder.ui.screens.auth.sigin

import com.truongdinh.drinkorder.data.model.User

data class LoginUiState(
    val email: String = "",
    val password: String = "",

    val emailError: String? = null,
    val passwordError: String? = null,

    val rememberMe: Boolean = false,
    val isLoading: Boolean = false,
    val loginError: String? = null,

    val user: User? = null
)
