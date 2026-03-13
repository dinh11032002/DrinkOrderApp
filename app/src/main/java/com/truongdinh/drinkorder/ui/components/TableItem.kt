package com.truongdinh.drinkorder.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chair
import androidx.compose.material.icons.filled.TableBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.truongdinh.drinkorder.ui.theme.AppSize
import com.truongdinh.drinkorder.ui.theme.AppSpacing

@Composable
fun TableItem(
    tableName: String,
    isDisabled: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDisabled)
                Color.Gray
            else
                MaterialTheme.colorScheme.primary
        ),
        modifier = modifier
            .height(110.dp)
            .fillMaxWidth()
            .clickable(enabled = !isDisabled) { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(AppSpacing.sm)
        ) {
            Icon(
                imageVector = Icons.Filled.TableBar,
                contentDescription = "Table Icon",
                tint = if (isDisabled) Color.White else MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(AppSize.iconLg)
            )
            Text(
                text = tableName,
                style = MaterialTheme.typography.labelLarge,
                color = if (isDisabled) Color.White else MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}