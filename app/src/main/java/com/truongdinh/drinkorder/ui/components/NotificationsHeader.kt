package com.truongdinh.drinkorder.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationHeader(
    unreadCount: Int,
    onMarkAllAsRead: () -> Unit,

) {
    TopAppBar(
        title = { Text("Thông báo ($unreadCount)") },
        actions = {
            if (unreadCount > 0) {
                TextButton(onClick = onMarkAllAsRead) {
                    Text("Đánh dấu tất cả")
                }
            }
        }
    )
}