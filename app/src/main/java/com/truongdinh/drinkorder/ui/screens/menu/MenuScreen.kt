package com.truongdinh.drinkorder.ui.screens.menu

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.truongdinh.drinkorder.di.AppModule
import com.truongdinh.drinkorder.ui.theme.DrinkOrderTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    editDrinkId: Int?,
    onBackClick: () -> Unit = {},
    onDrinkClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Khởi tạo ViewModel ổn định
    val viewModel: MenuViewModel = remember {
        val categoryRepo = AppModule.provideCategoryRepository(context)
        val drinkRepo = AppModule.provideDrinkRepository(context)
        MenuViewModel(categoryRepository = categoryRepo, drinkRepository = drinkRepo)
    }

    val categories by viewModel.categories.collectAsState()
    val drinks by viewModel.drinks.collectAsState()
    val selectedCategoryId by viewModel.selectedCategoryId.collectAsState()
    var query by rememberSaveable { mutableStateOf("") }

    // Dùng derivedStateOf để tránh recompose thừa
    val filteredDrinks by remember {
        derivedStateOf {
            when {
                query.isNotBlank() ->
                    drinks.filter { it.name.contains(query, ignoreCase = true) }

                selectedCategoryId == null ->
                    drinks

                else ->
                    drinks.filter { it.idCategory == selectedCategoryId }
            }
        }
    }

    val uiState = MenuUiState(
        categories = categories,
        drinks = filteredDrinks,
        selectedCategoryId = selectedCategoryId,
        query = query
    )

    MenuContent(
        state = uiState,
        onBackClick = onBackClick,
        onQueryChange = { query = it },
        onCategoryClick = { category ->
            viewModel.selectCategory(category?.id)
        },
        onClickItem = { drink ->
            onDrinkClick(drink.id)
        },
        modifier = modifier
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MenuPreview() {
    DrinkOrderTheme {
        MenuScreen(editDrinkId = null, onDrinkClick = { })
    }
}