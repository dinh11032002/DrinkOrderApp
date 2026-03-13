package com.truongdinh.drinkorder.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.TableBar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.truongdinh.drinkorder.data.model.OrderFirestore
import com.truongdinh.drinkorder.ui.theme.AppSize
import com.truongdinh.drinkorder.ui.theme.AppSpacing
import com.truongdinh.drinkorder.ui.screens.detail.table_detail.OrderQueueViewModel
import kotlinx.coroutines.delay

@Composable
fun OrderItemCard(
    order: OrderFirestore,
    queueViewModel: OrderQueueViewModel? = null,
    modifier: Modifier = Modifier
) {
    var queuePosition by remember { mutableStateOf(0) }

    // Lấy vị trí trong hàng đợi nếu đơn đang xử lý
    LaunchedEffect(order.id, order.status) {
        if (order.status == "Đang xử lý" && queueViewModel != null) {
            while (true) {
                queueViewModel.getOrderQueuePosition(order.id) { position ->
                    queuePosition = position
                }
                delay(1000)  // Cập nhật mỗi giây
            }
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = AppSpacing.sm),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.md)
        ) {
            // Header với status badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Đơn hàng #${order.id.take(8)}",
                    style = MaterialTheme.typography.titleMedium
                )

                // Status badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(order.getStatusColor())
                ) {
                    Text(
                        text = order.status,
                        modifier = Modifier.padding(horizontal = AppSpacing.md, vertical = AppSpacing.xxs),
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            // Hiển thị vị trí trong hàng đợi nếu đang xử lý
            if (order.status == "Đang xử lý" && queuePosition > 0) {
                Spacer(Modifier.height(AppSpacing.sm))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = AppSpacing.xs)
                ) {
                    Icon(
                        imageVector = Icons.Default.Restaurant,
                        contentDescription = null,
                        modifier = Modifier.size(AppSize.iconSm),
                        tint = Color(0xFFFFA726)
                    )
                    Spacer(Modifier.width(AppSpacing.xs))
                    Text(
                        text = if (queuePosition == 1) {
                            "Đang pha chế..."
                        } else {
                            "Vị trí thứ $queuePosition trong hàng đợi"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFFFA726)
                    )
                }
            }

            Spacer(Modifier.height(AppSpacing.sm))

            // Thông tin bàn và thời gian
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.TableBar,
                    contentDescription = null,
                    modifier = Modifier.size(AppSize.iconSm),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(AppSpacing.xs))
                Text(
                    text = order.tableName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(Modifier.width(AppSpacing.lg))

                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    modifier = Modifier.size(AppSize.iconSm),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(AppSpacing.xs))
                Text(
                    text = order.getFormattedDate(),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Gray
                )
            }

            Spacer(Modifier.height(AppSpacing.md))
            HorizontalDivider()
            Spacer(Modifier.height(AppSpacing.md))

            // Danh sách món
            Text(
                text = "Món đã gọi:",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(AppSpacing.sm))

            order.drinks.forEach { drink ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = AppSpacing.xs),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${drink.quantity}x ${drink.name} (${drink.size})",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${drink.price * drink.quantity} VNĐ",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                if (drink.note.isNotEmpty()) {
                    Text(
                        text = " ${drink.note}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = AppSpacing.lg)
                    )
                }
            }

            Spacer(Modifier.height(AppSpacing.md))
            HorizontalDivider()
            Spacer(Modifier.height(AppSpacing.md))

            // Tổng tiền
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Tổng cộng:",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${order.totalPrice} VNĐ",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}