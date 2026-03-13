package com.truongdinh.drinkorder.ui.screens.auth.signup

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.truongdinh.drinkorder.di.AppModule
import com.truongdinh.drinkorder.ui.components.AuthBackground
import com.truongdinh.drinkorder.ui.components.PasswordTextField
import com.truongdinh.drinkorder.ui.theme.AppSize
import com.truongdinh.drinkorder.ui.theme.AppSpacing
import com.truongdinh.drinkorder.ui.theme.DrinkOrderTheme
import com.truongdinh.drinkorder.ui.theme.OutlineColor

import com.truongdinh.drinkorder.ui.theme.TextSecondary

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val viewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModelFactory(
            repository = AppModule.provideUserRepository(context)
        )
    )

    val state = viewModel.uiState

    AuthBackground {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(all = AppSpacing.md),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Đăng ký",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(height = AppSpacing.xl))

            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::onNameChange,
                label = { Text(
                        text = "Họ và tên",
                        style = MaterialTheme.typography.labelLarge
                ) },
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge,
                singleLine = true,
                shape = CircleShape,
                isError = state.nameError != null,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = OutlineColor,
                    unfocusedContainerColor = OutlineColor,

                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = OutlineColor,
                    errorIndicatorColor = MaterialTheme.colorScheme.error,

                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = TextSecondary,
                    errorLabelColor = MaterialTheme.colorScheme.error,

                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    errorTextColor = MaterialTheme.colorScheme.error
                )
            )
            state.nameError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Spacer(Modifier.height(height = AppSpacing.sm))

            OutlinedTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text(text = "Email", style = MaterialTheme.typography.labelLarge) },
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge,
                singleLine = true,
                shape = CircleShape,
                isError = state.emailError != null,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = OutlineColor,
                    unfocusedContainerColor = OutlineColor,

                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = OutlineColor,
                    errorIndicatorColor = MaterialTheme.colorScheme.error,

                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = TextSecondary,
                    errorLabelColor = MaterialTheme.colorScheme.error,

                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    errorTextColor = MaterialTheme.colorScheme.error
                )
            )
            state.emailError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Spacer(Modifier.height(height = AppSpacing.sm))

            OutlinedTextField(
                value = state.phone,
                onValueChange = viewModel::onPhoneChange,
                label = { Text(text = "Số điện thoại", style = MaterialTheme.typography.labelLarge) },
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge,
                singleLine = true,
                shape = CircleShape,
                isError = state.phoneError != null,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = OutlineColor,
                    unfocusedContainerColor = OutlineColor,

                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = OutlineColor,
                    errorIndicatorColor = MaterialTheme.colorScheme.error,

                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = TextSecondary,
                    errorLabelColor = MaterialTheme.colorScheme.error,

                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    errorTextColor = MaterialTheme.colorScheme.error
                )
            )
            state.phoneError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Spacer(Modifier.height(height = AppSpacing.sm))

            PasswordTextField(
                value = state.password,
                onValueChange = viewModel::onPasswordChange,
                label = "Mật khẩu",
                error = state.passwordError,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(Modifier.height(height = AppSpacing.sm))

            PasswordTextField(
                value = state.confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                label = "Xác nhận mật khẩu",
                error = state.confirmPasswordError,
                modifier = Modifier
                    .fillMaxWidth()
            )

            state.registerError?.let {
                Spacer(Modifier.height(height = AppSpacing.sm))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.height(AppSpacing.xxl))

            Button(
                enabled = !state.isLoading,
                onClick = {
                    viewModel.register {
                        Toast.makeText(
                            context,
                            "Đăng ký thành công!",
                            Toast.LENGTH_SHORT
                        ).show()
                        onNavigateToLogin()
                    }
                },
                colors = ButtonDefaults
                    .buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppSize.buttonMdHeight)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(size = 20.dp)
                    )
                    Spacer(Modifier.width(width = AppSpacing.sm))
                    Text(text = "Đang đăng ký...")
                } else {
                    Text(text = "Đăng ký", style = MaterialTheme.typography.headlineSmall)
                }
            }

            Spacer(Modifier.height(height = AppSpacing.md))

            TextButton(onClick = onNavigateToLogin) {
                Text(
                    text = "Đã có tài khoản? Đăng nhập ngay",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    DrinkOrderTheme {
        RegisterScreen(
            onNavigateToLogin = {}
        )
    }
}