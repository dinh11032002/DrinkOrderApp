package com.truongdinh.drinkorder.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.truongdinh.drinkorder.data.model.Table
import com.truongdinh.drinkorder.ui.components.AppBackground
import com.truongdinh.drinkorder.ui.components.BaseFilterRow
import com.truongdinh.drinkorder.ui.components.SearchTextField
import com.truongdinh.drinkorder.ui.components.TableGrid
import com.truongdinh.drinkorder.ui.theme.AppSpacing

@Composable
fun HomeContent(
    username: String,
    tables: List<Table>,
    tableOwners: Map<Int, String>,
    selectedStatus: String,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onFilterChange: (String) -> Unit,
    onTableItemClick: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    AppBackground {

        Scaffold { innerPadding ->

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues = innerPadding)
                    .padding(all = AppSpacing.md)
            ) {
                Text(
                    text = "Xin chào, $username",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = AppSpacing.xxxl, start = AppSpacing.lg)
                )

                Spacer(modifier = Modifier.height(height = AppSpacing.md))

                SearchTextField(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChange,
                    placeholderText = "Tìm kiếm bàn...",
                    modifier = Modifier.fillMaxWidth()
                )

                val options = listOf("Đang phục vụ", "Trống")

                // Dùng BaseFilterRow thay thế hoàn toàn TableStatusFilter
                BaseFilterRow(
                    items = options,
                    selectedItem = if (selectedStatus == "Tất cả") null else selectedStatus,
                    onItemClick = { status ->
                        // Truyền ngược lại cho HomeScreen xử lý qua lambda
                        onFilterChange(status ?: "Tất cả")
                    },
                    labelProvider = { it }
                )

                TableGrid(
                    tableList = tables,
                    currentUsername = username,
                    tableOwners = tableOwners,
                    onClickTable = onTableItemClick
                )
            }
        }
    }
}