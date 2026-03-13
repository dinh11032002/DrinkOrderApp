package com.truongdinh.drinkorder.ui.screens.auth.sigin

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onForgotPassword: () -> Unit,
    onLoginSuccess: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(
            repository = AppModule.provideUserRepository(context),
            sessionManager = AppModule.provideSessionManager(context),
            rememberAccountStore = AppModule.provideRememberAccount(context)
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
                text = "Đăng nhập",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = modifier.height(height = AppSpacing.xxl))

            OutlinedTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text(text = "Email", style = MaterialTheme.typography.labelLarge) },
                modifier = modifier
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
                Spacer(modifier = modifier.height(height = AppSpacing.xs))
                Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.titleSmall)
            }

            Spacer(modifier = modifier.height(height = AppSpacing.sm))

            PasswordTextField(
                value = state.password,
                onValueChange = viewModel::onPasswordChange,
                label = "Mật khẩu",
                error = state.passwordError,
                modifier = modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = modifier.height(height = AppSpacing.sm))

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = state.rememberMe,
                        onCheckedChange = viewModel::onRememberMeChange
                    )
                    Text(
                        text = "Ghi nhớ tài khoản",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                TextButton(
                    onClick = {
                        onForgotPassword()
                    }
                ) {
                    Text(text = "Quên mật khẩu?", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme        .onBackground)
                }

            }

            Spacer(modifier = modifier.height(height = AppSpacing.md))

            Button(
                onClick = {
                    viewModel.login { user ->
                        Toast.makeText(
                            context,
                            "Xin chào ${user.name}!",
                            Toast.LENGTH_SHORT
                        ).show()

                        onLoginSuccess(user.name)
                    }
                },
                enabled = !state.isLoading,
                colors = ButtonDefaults
                    .buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                modifier = modifier
                    .fillMaxWidth()
                    .height(AppSize.buttonMdHeight)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = modifier
                            .size(size = 20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = modifier.width(width = AppSpacing.sm))
                    Text(text = "Đang đăng nhập...")
                } else {
                    Text(text = "Đăng nhập", style = MaterialTheme.typography.headlineSmall)
                }
            }

            Spacer(modifier = modifier.height(height = AppSpacing.lg))

            TextButton(
                onClick = {
                    onNavigateToRegister()
                }
            ) {
                Text(
                    text = "Chưa có tài khoản? Đăng ký ngay",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    DrinkOrderTheme {
        LoginScreen(
            onLoginSuccess = {},
            onNavigateToRegister = {},
            onForgotPassword = {}
        )
    }
}