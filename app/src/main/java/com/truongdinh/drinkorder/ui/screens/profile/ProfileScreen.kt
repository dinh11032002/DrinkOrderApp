package com.truongdinh.drinkorder.ui.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.truongdinh.drinkorder.data.model.User
import com.truongdinh.drinkorder.ui.components.AppBackground
import com.truongdinh.drinkorder.ui.theme.DrinkOrderTheme


@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateToLogin: () -> Unit
) {
    val user by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    when {
        isLoading -> {
            AppBackground {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Đang tải thông tin...")
                }
            }
        }

        user == null -> {
            AppBackground {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Không có thông tin người dùng")
                }
            }
        }

        else -> {
            ProfileContent(
                user = user!!,
                onLogout = {
                    viewModel.logout()
                    onNavigateToLogin()
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileContentPreview() {
    DrinkOrderTheme {
        ProfileContent(
            user = User(
                id = 1,
                name = "Trương Đình",
                email = "truongdinh@gmail.com",
                phone = "0909123456",
                address = "Quận 7, TP.HCM",
                gender = "Nam",
                dob = "01/01/2002",
                password = "123456"
            ),
            onLogout = {}
        )
    }
}