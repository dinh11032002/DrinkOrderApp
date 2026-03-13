package com.truongdinh.drinkorder.ui.screens.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.truongdinh.drinkorder.data.model.Category
import com.truongdinh.drinkorder.data.model.Drink
import com.truongdinh.drinkorder.ui.components.AppBackground
import com.truongdinh.drinkorder.ui.components.AppHeader
import com.truongdinh.drinkorder.ui.components.BaseFilterRow
import com.truongdinh.drinkorder.ui.components.DrinkCard
import com.truongdinh.drinkorder.ui.components.SearchTextField
import com.truongdinh.drinkorder.ui.theme.AppSpacing

@Composable
fun MenuContent(
    state: MenuUiState,
    onBackClick: () -> Unit,
    onQueryChange: (String) -> Unit,
    onCategoryClick: (Category?) -> Unit,
    onClickItem: (Drink) -> Unit,
    modifier: Modifier
) {
    AppBackground {
        Scaffold(
            topBar = {
                AppHeader(
                    title = "Menu",
                    onBackClick = onBackClick
                )
            }
        ) { innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues = innerPadding)
                    .padding(all = AppSpacing.md)
            ) {
                SearchTextField(
                    query = state.query,
                    onQueryChange = onQueryChange,
                    placeholderText = "Tìm kiếm món...",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(height = AppSpacing.md))

                if (state.categories.isNotEmpty()) {
                    BaseFilterRow(
                        items = state.categories,
                        selectedItem = state.categories.find { it.id == state.selectedCategoryId },
                        onItemClick = onCategoryClick,
                        labelProvider = { it.name } // Lấy field name từ đối tượng Category
                    )
                }

                Spacer(Modifier.height(height = AppSpacing.lg))

                if (state.drinks.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Không có đồ uống nào",
                            color = Color.LightGray,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 160.dp),
                        horizontalArrangement = Arrangement.spacedBy(AppSpacing.md),
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.md),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.drinks, key = { it.id }) { drink ->
                            DrinkCard(
                                drink = drink,
                                onClick = { onClickItem(drink) }
                            )
                        }
                    }
                }
            }
        }
    }
}