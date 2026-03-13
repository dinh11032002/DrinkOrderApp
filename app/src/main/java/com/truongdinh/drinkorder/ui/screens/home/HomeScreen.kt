package com.truongdinh.drinkorder.ui.screens.home

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.truongdinh.drinkorder.data.enum.TableStatus
import com.truongdinh.drinkorder.data.local.Prefs
import com.truongdinh.drinkorder.di.AppModule
import com.truongdinh.drinkorder.ui.theme.DrinkOrderTheme

@Composable
fun HomeScreen(
    username: String = "",
    onTableClick: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: TableViewModel = viewModel(
        factory = TableViewModelFactory(repository = AppModule.provideRepository(context))
    )

    val tables by viewModel.tables.collectAsState()
    val selectedStatus by viewModel.selectedStatus.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val tableOwners by viewModel.tableOwners.collectAsState()

    LaunchedEffect(Unit) {
        val prefs = Prefs(context)
        viewModel.initializeTablesIfNeeded(prefs)
        viewModel.syncTablesWithFirestore()
    }

    LaunchedEffect(tableOwners) {
        Log.d("HomeScreen", "tableOwners: $tableOwners")
        Log.d("HomeScreen", "currentUsername: $username")
    }

    HomeContent(
        username = username,
        tables = tables,
        tableOwners = tableOwners,
        selectedStatus = selectedStatus,
        searchQuery = searchQuery,
        onSearchQueryChange = { viewModel.setSearchQuery(it) },
        onFilterChange = { viewModel.setFilter(it) },
        onTableItemClick = { tableId, currentStatus ->
            val owner = tableOwners[tableId]
            Log.d("HomeScreen", "tableId: $tableId | owner: $owner | username: $username | match: ${owner == username}")
            // Chỉ cho bấm nếu bàn trống hoặc do chính mình phụ trách
            if (currentStatus == TableStatus.EMPTY.value || owner == username) {
                val displayStatus = if (currentStatus == TableStatus.EMPTY.value)
                    TableStatus.OCCUPIED.value
                else currentStatus
                onTableClick(tableId, displayStatus)
            }
        },
        modifier = modifier
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomePreview() {
    DrinkOrderTheme {
        HomeScreen(onTableClick = { _, _ -> })
    }
}