package com.truongdinh.drinkorder.ui.screens.detail.drink_detail

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.truongdinh.drinkorder.data.model.Drink
import com.truongdinh.drinkorder.ui.components.AppHeader
import com.truongdinh.drinkorder.ui.theme.AppRadius
import com.truongdinh.drinkorder.ui.theme.AppSize
import com.truongdinh.drinkorder.ui.theme.AppSpacing
import com.truongdinh.drinkorder.ui.theme.DrinkOrderTheme

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrinkDetailScreen(
    drink: Drink,
    onAddTable: (Drink) -> Unit,
    onCancel: () -> Unit,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedSize by remember(drink.id) { mutableStateOf(drink.size) }
    var quantity by remember(drink.id) { mutableIntStateOf(drink.quantity) }
    var note by remember(drink.id) { mutableStateOf(drink.note) }
    var expanded by remember(drink.id) { mutableStateOf(false) }
    val sizeOptions = remember(drink.id) { drink.sizePrices.keys.toList() }

    val pricePerUnit by derivedStateOf {
        drink.sizePrices[selectedSize] ?: drink.price
    }

    val totalPrice by derivedStateOf { pricePerUnit * quantity }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Thông tin đồ uống",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(all = AppSpacing.md)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(AppSpacing.lg))

            AsyncImage(
                model = drink.image,
                contentDescription = drink.name,
                modifier = Modifier
                    .height(AppSize.heightHero)
                    .clip(RoundedCornerShape(AppRadius.card)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(AppSize.imageDetailLg / 2 + AppSpacing.lg))

            Text(
                text = drink.name,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(height = AppSpacing.sm))

            Text(
                text = "Giá: $pricePerUnit VNĐ",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )

            if (quantity > 1) {
                Text(
                    text = "Tổng: $totalPrice VNĐ",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(Modifier.height(height = AppSpacing.lg + AppSpacing.xs))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedSize,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Chọn kích thước") },
                    trailingIcon = {
                        CompositionLocalProvider(
                            LocalContentColor provides MaterialTheme.colorScheme.onBackground
                        ) {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                        focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    ),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    sizeOptions.forEach { size ->
                        val sizePrice = drink.sizePrices[size] ?: drink.price
                        DropdownMenuItem(
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Size $size")
                                    Text(
                                        "$sizePrice VNĐ",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            },
                            onClick = {
                                selectedSize = size
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(height = AppSpacing.lg + AppSpacing.xs))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colorScheme.onBackground
                ) {
                    Text(
                        "Số lượng:",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { if (quantity > 1) quantity-- }) {
                            Icon(Icons.Default.Remove, contentDescription = "Giảm")
                        }
                        Text(
                            "$quantity",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        IconButton(onClick = { quantity++ }) {
                            Icon(Icons.Default.Add, contentDescription = "Tăng")
                        }
                    }
                }
            }

            Spacer(Modifier.height(height = AppSpacing.lg + AppSpacing.xs))

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Ghi chú") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                    focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = modifier.fillMaxWidth()
            )

            Spacer(
                Modifier.height(
                    height = AppSpacing.xl
                )
            )

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = AppSpacing.md
                )
            ) {
                OutlinedButton(
                    onClick = {
                        onAddTable(
                            drink.copy(
                                size = selectedSize,
                                quantity = quantity,
                                note = note,
                                price = pricePerUnit,
                                sizePrices = drink.sizePrices
                            )
                        )
                    },
                    modifier = modifier.weight(
                        weight = 1f
                    ),
                    shape = RoundedCornerShape(
                        size = AppRadius.button
                    ),
                    colors = ButtonDefaults
                        .buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                ) {
                    Text("Thêm vào bàn", style = MaterialTheme.typography.titleMedium)
                }

                OutlinedButton(
                    onClick = onCancel,
                    modifier = modifier.weight(1f),
                    shape = RoundedCornerShape(AppRadius.button),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outline
                    )
                ) {
                    Text("Huỷ", style = MaterialTheme.typography.titleMedium)
                }
            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DrinkDetailPreview() {
    DrinkOrderTheme {
        DrinkDetailScreen(
            drink = Drink(
                id = 1,
                name = "Cappuccino",
                price = 35000,
                size = "S",
                quantity = 1,
                note = "Nhiều sữa",
                image = "https://cdn-icons-png.flaticon.com/512/924/924514.png",
                idCategory = 1,
                sizePrices = mapOf(
                    "S" to 35000,
                    "M" to 40000,
                    "L" to 45000
                )
            ),
            onBackClick = {},
            onCancel = {},
            onAddTable = {}
        )
    }
}