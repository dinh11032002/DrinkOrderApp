package com.truongdinh.drinkorder.ui.components

import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.PaddingValues
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.lazy.grid.GridCells
    import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
    import androidx.compose.foundation.lazy.grid.items
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import com.truongdinh.drinkorder.data.model.Table
    import com.truongdinh.drinkorder.ui.theme.AppSpacing

@Composable
fun TableGrid(
    tableList: List<Table>,
    currentUsername: String,
    tableOwners: Map<Int, String>,
    onClickTable: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (tableList.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(top = AppSpacing.md),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Chưa có dữ liệu bàn",
                color = Color.Gray,
                style = MaterialTheme.typography.labelLarge
            )
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.lg),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.lg),
            contentPadding = PaddingValues(AppSpacing.lg),
            modifier = modifier.fillMaxWidth()
        ) {
            items(items = tableList) { table ->
                val owner = tableOwners[table.id]
                val isDisabled = table.status == "Đang phục vụ" && owner != currentUsername
                TableItem(
                    tableName = table.name,
                    isDisabled = isDisabled,
                    onClick = { onClickTable(table.id, table.status) }
                )
            }
        }
    }
}