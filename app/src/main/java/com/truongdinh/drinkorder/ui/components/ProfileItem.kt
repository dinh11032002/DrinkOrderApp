package com.truongdinh.drinkorder.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.truongdinh.drinkorder.ui.theme.AppSpacing

@Composable
fun ProfileItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    text: String,
    ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppSpacing.md)
            )
        }
    }
}