package com.truongdinh.drinkorder.ui.screens.notifcation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.truongdinh.drinkorder.data.model.Notification
import com.truongdinh.drinkorder.ui.components.AppBackground
import com.truongdinh.drinkorder.ui.components.NotificationHeader
import com.truongdinh.drinkorder.ui.components.NotificationItem
import com.truongdinh.drinkorder.ui.theme.AppSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationContent(
    notifications: List<Notification>,
    unreadCount: Int,
    onMarkAllAsRead: () -> Unit,
    onMarkAsRead: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    AppBackground {
        Scaffold(
            topBar = {
                NotificationHeader(
                    unreadCount = unreadCount,
                    onMarkAllAsRead = onMarkAllAsRead
                )
            },
            containerColor = Color.Transparent,
            contentWindowInsets = ScaffoldDefaults.contentWindowInsets
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(AppSpacing.md),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
            ) {
                if (notifications.isEmpty()) {
                    item {
                        Text(
                            "Chưa có thông báo nào",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = AppSpacing.lg),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    items(
                        items = notifications,
                        key = { it.id }
                    ) { item ->
                        NotificationItem(
                            notification = item,
                            onMarkAsRead = { onMarkAsRead(item.id) },
                            onDelete = { onDelete(item.id) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}