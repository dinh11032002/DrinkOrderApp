package com.truongdinh.drinkorder.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.truongdinh.drinkorder.ui.theme.AppSpacing

@Composable
fun <T> BaseFilterRow(
    items: List<T>,
    selectedItem: T?,
    onItemClick: (T?) -> Unit,
    labelProvider: (T) -> String,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(vertical = AppSpacing.sm),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterChipItem(
            text = "Tất cả",
            isSelected = selectedItem == null,
            onClick = { onItemClick(null) }
        )

        items.forEach { item ->
            FilterChipItem(
                text = labelProvider(item),
                isSelected = item == selectedItem,
                onClick = { onItemClick(item) }
            )
        }
    }
}