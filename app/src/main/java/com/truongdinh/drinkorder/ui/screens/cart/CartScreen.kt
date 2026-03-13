package com.truongdinh.drinkorder.ui.screens.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.truongdinh.drinkorder.LocalOrderQueueViewModel
import com.truongdinh.drinkorder.di.AppModule.provideOrderRepository
import com.truongdinh.drinkorder.ui.components.AppBackground
import com.truongdinh.drinkorder.ui.components.OrderItemCard
import com.truongdinh.drinkorder.ui.theme.AppSpacing
import com.truongdinh.drinkorder.ui.theme.DrinkOrderTheme

@Composable
fun CartScreen(
    username: String = "",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Provide OrderRepository
    val repository = remember {
        provideOrderRepository(context)
    }

    // Provide OrderQueueViewModel để hiển thị vị trí trong hàng đợi
    val queueViewModel = LocalOrderQueueViewModel.current

    val viewModel: CartViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CartViewModel(repository, username) as T
            }
        }
    )

    val orders by viewModel.orders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    AppBackground {
        Scaffold(
            topBar = {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Transparent
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = AppSpacing.md)
                    ) {
                        Text(
                            text = "Đơn hàng của tôi",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Danh sách các đơn hàng đã đặt",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.White
                        )
                    }

                    errorMessage != null -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Lỗi: $errorMessage",
                                color = Color.White
                            )
                            Button(
                                onClick = { viewModel.clearError() },
                                modifier = Modifier.padding(top = AppSpacing.sm)
                            ) {
                                Text("Thử lại")
                            }
                        }
                    }

                    orders.isEmpty() -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(all = AppSpacing.xl),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "📋",
                                fontSize = 64.sp
                            )
                            Spacer(Modifier.height(AppSpacing.lg))
                            Text(
                                text = "Chưa có đơn hàng nào",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Hãy đặt món để xem lịch sử đơn hàng",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.padding(top = AppSpacing.sm)
                            )
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = AppSpacing.lg),
                            contentPadding = PaddingValues(vertical = AppSpacing.sm)
                        ) {

                            items(orders, key = { it.id }) { order ->
                                OrderItemCard(
                                    order = order,
                                    queueViewModel = queueViewModel  // Thêm dòng này
                                )
                            }

                            // Summary
                            item {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = AppSpacing.md),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(AppSpacing.md)
                                    ) {
                                        Text(
                                            text = "Tổng quan",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(Modifier.height(AppSpacing.sm))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("Tổng số đơn:")
                                            Text(
                                                text = "${orders.size}",
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("Tổng chi tiêu:")
                                            Text(
                                                text = "${orders.sumOf { it.totalPrice }} VNĐ",
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CartScreenPreview() {
    DrinkOrderTheme {
        CartScreen(username = "Demo User")
    }
}