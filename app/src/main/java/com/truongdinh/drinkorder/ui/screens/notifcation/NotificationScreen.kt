package com.truongdinh.drinkorder.ui.screens.notifcation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel
) {
    val notifications by viewModel.notifications.collectAsState()
    val unreadCount by viewModel.unreadCount.collectAsState()

    NotificationContent(
        notifications = notifications,
        unreadCount = unreadCount,
        onMarkAllAsRead = viewModel::markAllAsRead,
        onMarkAsRead = viewModel::markAsRead,
        onDelete = viewModel::deleteNotification
    )
}

@Preview(showBackground = true)
@Composable
fun NotificationPreview() {
    val context = LocalContext.current
    val fakeViewModel: NotificationViewModel = viewModel(
        factory = NotificationViewModelFactory("preview_user")
    )
    NotificationScreen(viewModel = fakeViewModel)
}