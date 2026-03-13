package com.truongdinh.drinkorder.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.truongdinh.drinkorder.data.model.Drink

import coil.compose.AsyncImage
import com.truongdinh.drinkorder.ui.theme.AppSize
import com.truongdinh.drinkorder.ui.theme.AppSpacing

@Composable
fun DrinkItemRow(
    item: Drink,
    onRemove: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = AppSpacing.xxs)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .padding(AppSpacing.md)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                model = item.image,
                contentDescription = item.name,
                modifier = Modifier
                    .size(AppSize.imageDetailMd)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(AppSpacing.md))

            Column(Modifier.weight(1f)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onRemove) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Xóa món",
                            tint = Color.Gray
                        )
                    }
                }

                Text(
                    text = "${item.price} VNĐ",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
                Text("Kích thước: ${item.size}", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
                Text("Số lượng: ${item.quantity}", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
                Text("Ghi chú: ${item.note}", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}