package com.truongdinh.drinkorder.ui.screens.detail.table_detail

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.truongdinh.drinkorder.data.enum.OrderStatus
import com.truongdinh.drinkorder.data.enum.TableStatus
import com.truongdinh.drinkorder.data.model.Drink
import com.truongdinh.drinkorder.di.AppModule
import com.truongdinh.drinkorder.ui.components.AppBackground
import com.truongdinh.drinkorder.ui.components.AppHeader
import com.truongdinh.drinkorder.ui.components.DrinkItemRow
import com.truongdinh.drinkorder.ui.theme.AppSpacing
import com.truongdinh.drinkorder.ui.theme.DrinkOrderTheme
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
@Composable
fun TableDetailScreen(
    navController: NavController,
    tableId: Int = 1,
    username: String = "",
    status: String = "Trống",
    onBackClick: () -> Unit = {},
    onNavigateToMenu: (onDrinkSelected: (Drink) -> Unit) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val viewModel: TableDetailViewModel = viewModel(
        key = "table_$tableId",
        factory = TableDetailViewModelFactory(
            tableRepository = AppModule.provideRepository(context)
        )
    )

    val currentDateTime = remember {
        LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        )
    }

    val orderDrinks by viewModel.orderDrinks.collectAsState()

    LaunchedEffect(tableId) {
        viewModel.loadOrderFromFirestore(tableId)
    }

    val orderStatus by viewModel.orderStatus.collectAsState()
    val isCompleted = orderStatus == OrderStatus.COMPLETE.value

    // Nhận lại món đã chọn từ màn Menu
    val savedDrinkFlow = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<Drink?>(key = "selectedDrink", initialValue = null)

    LaunchedEffect(Unit) {
        savedDrinkFlow?.collectLatest { drink ->
            drink?.let {
                viewModel.addDrink(it)
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.remove<Drink>("selectedDrink")
            }
        }
    }

    // Nhận lại món đã thay thế
    val replaceDrinkFlow = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<Pair<Int, Drink>?>(key = "replaceDrink", initialValue = null)

    LaunchedEffect(Unit) {
        replaceDrinkFlow?.collectLatest { pair ->
            pair?.let { (oldId, newDrink) ->
                viewModel.replacedDrink(oldId, newDrink)
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.remove<Pair<Int, Drink>>("replaceDrink")
            }
        }
    }

    AppBackground {
        Scaffold(
            topBar = {
                AppHeader(
                    title = "Thông tin bàn",
                    onBackClick = onBackClick
                )
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues = innerPadding)
                    .padding(all = AppSpacing.md)
            ) {
                // Thông tin chung
                Text(
                    text = "Bàn $tableId",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Trạng thái bàn: $status",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Nhân viên: $username",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Ngày: $currentDateTime",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(
                    Modifier.height(
                        height = AppSpacing.sm
                    )
                )

                // Danh sách món
                LazyColumn(
                    modifier = Modifier.weight(weight = 1f)
                ) {
                    items(orderDrinks, key = { it.id }) { drink ->
                        DrinkItemRow(
                            item = drink,
                            onClick = {
                                navController.navigate(route = "menu/$tableId/$username/$status?editDrinkId=${drink.id}")
                            },
                            onRemove = {
                                viewModel.removeDrink(drink)
                            }
                        )
                        Spacer(
                            Modifier.height(
                                height = AppSpacing.sm
                            )
                        )
                    }

                    item {
                        if (!isCompleted) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = AppSpacing.md),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        onNavigateToMenu { selectedDrink ->
                                            navController.navigate(route = "menu/$tableId/$username/$status")
                                        }
                                    },
                                    shape = CircleShape,
                                    colors = ButtonDefaults
                                        .buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.onPrimary
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Thêm món"
                                    )
                                    Spacer(
                                        Modifier.width(
                                            width = AppSpacing.xxs
                                        )
                                    )
                                    Text(
                                        "Thêm món"
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(
                    Modifier.height(
                        height = AppSpacing.sm
                    )
                )

                // Tổng tiền
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Tổng: ",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        "${orderDrinks.sumOf { it.price * it.quantity }} VNĐ",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(
                    Modifier.height(
                        height = AppSpacing.md
                    )
                )

                // Nút Đặt / Hủy
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            viewModel.placeOrderToFirestore(
                                tableId = tableId,
                                username = username,
                                onSuccess = {
                                    Toast.makeText(context, "Đặt món thành công!", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                },
                                onFailure = { e ->
                                    Toast.makeText(context, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                        enabled = orderDrinks.isNotEmpty() && !isCompleted,
                        modifier = Modifier
                            .weight(weight = 1f)
                            .padding(start = AppSpacing.sm),
                        colors = ButtonDefaults
                            .buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                    ) {
                        Text(
                            "Đặt"
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            // 1. Xóa đơn hàng
                            viewModel.clearOrder()

                            // 2. Cập nhật trạng thái bàn về "Trống"
                            viewModel.updateTableStatus(tableId, TableStatus.EMPTY)

                            // 3. Quay lại HomeScreen
                            navController.popBackStack()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onBackground
                        ),
                        modifier = Modifier
                            .weight(weight = 1f)
                            .padding(start = AppSpacing.sm)
                    ) {
                        Text(
                            "Hủy"
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TableDetailPreview() {
    DrinkOrderTheme {
        TableDetailScreen(
            navController = rememberNavController(),
            tableId = 1,
            username = "Nhân viên Demo",
            onBackClick = {},
            onNavigateToMenu = { onDrinkSelected ->
                onDrinkSelected(
                    Drink(
                        id = 2,
                        name = "Latte (Preview)",
                        price = 30000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://cdn-icons-png.flaticon.com/512/2935/2935458.png",
                        idCategory = 1
                    )
                )
            }
        )
    }
}