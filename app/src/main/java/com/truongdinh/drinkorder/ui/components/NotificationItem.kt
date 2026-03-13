package com.truongdinh.drinkorder.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.truongdinh.drinkorder.data.model.Notification
import com.truongdinh.drinkorder.ui.theme.AppSpacing

@Composable
fun NotificationItem(
    notification: Notification,
    onMarkAsRead: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (notification.isRead) {
        Color.Transparent
    } else {
        MaterialTheme.colorScheme.surface
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(bgColor)
            .clickable { // ✅ Bấm thẳng vào item để đánh dấu đọc
                if (!notification.isRead) onMarkAsRead()
            }
            .padding(horizontal = AppSpacing.md, vertical = AppSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = notification.message,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )

        // ❌ Bỏ IconButton Check đi

        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}