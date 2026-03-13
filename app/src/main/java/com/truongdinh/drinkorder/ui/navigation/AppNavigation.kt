package com.truongdinh.drinkorder.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.truongdinh.drinkorder.di.AppModule
import com.truongdinh.drinkorder.ui.components.AppBackground
import com.truongdinh.drinkorder.ui.components.BottomBar
import com.truongdinh.drinkorder.ui.screens.auth.sigin.LoginScreen
import com.truongdinh.drinkorder.ui.screens.auth.signup.RegisterScreen
import com.truongdinh.drinkorder.ui.screens.cart.CartScreen
import com.truongdinh.drinkorder.ui.screens.detail.drink_detail.DrinkDetailScreen
import com.truongdinh.drinkorder.ui.screens.detail.table_detail.TableDetailScreen
import com.truongdinh.drinkorder.ui.screens.home.HomeScreen
import com.truongdinh.drinkorder.ui.screens.menu.MenuScreen
import com.truongdinh.drinkorder.ui.screens.menu.MenuViewModel
import com.truongdinh.drinkorder.ui.screens.menu.MenuViewModelFactory
import com.truongdinh.drinkorder.ui.screens.notifcation.NotificationScreen
import com.truongdinh.drinkorder.ui.screens.notifcation.NotificationViewModel
import com.truongdinh.drinkorder.ui.screens.notifcation.NotificationViewModelFactory
import com.truongdinh.drinkorder.ui.screens.profile.ProfileScreen
import com.truongdinh.drinkorder.ui.theme.DrinkOrderTheme
import com.truongdinh.drinkorder.ui.screens.profile.ProfileViewModel
import com.truongdinh.drinkorder.ui.screens.profile.ProfileViewModelFactory
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sessionManager = AppModule.provideSessionManager(context)

    var username by remember { mutableStateOf(sessionManager.getUsername().orEmpty()) }

    val notificationViewModel: NotificationViewModel = viewModel(
        key = "notification_$username",
        factory = NotificationViewModelFactory(username)
    )
    val unreadCount by notificationViewModel.unreadCount.collectAsState()

    val startDestination = if (username.isBlank()) "login" else "home"

    AppBackground {
        Scaffold(
            bottomBar = {
                val route = navController.currentBackStackEntryAsState().value?.destination?.route
                if (
                    route != "login" &&
                    route != "register" &&
                    route != "menu" &&
                    !route.orEmpty().startsWith("table_detail") &&
                    !route.orEmpty().startsWith("drink_detail")
                ) {
                    BottomBar(
                        navController = navController,
                        unreadCount = unreadCount
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("login") {
                    LoginScreen(
                        onLoginSuccess = {
                            username = sessionManager.getUsername().orEmpty()
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        },
                        onNavigateToRegister = { navController.navigate("register") },
                        onForgotPassword = {}
                    )
                }

                composable("register") {
                    RegisterScreen(onNavigateToLogin = { navController.popBackStack() })
                }

                composable("home") {
                    val currentUsername = sessionManager.getUsername().orEmpty()
                    HomeScreen(
                        username = currentUsername,
                        onTableClick = { tableId, status ->
                            sessionManager.saveCurrentTableNumber(tableId)
                            navController.navigate("table_detail/$tableId/$currentUsername/$status")
                        }
                    )
                }

                composable(
                    route = "table_detail/{tableId}/{username}/{status}",
                    arguments = listOf(
                        navArgument("tableId") { type = NavType.IntType },
                        navArgument("username") { type = NavType.StringType },
                        navArgument("status") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val tableId = backStackEntry.arguments?.getInt("tableId") ?: 1
                    val usernameArg = backStackEntry.arguments?.getString("username") ?: username
                    val status = backStackEntry.arguments?.getString("status") ?: "Trống"
                    TableDetailScreen(
                        navController = navController,
                        tableId = tableId,
                        username = usernameArg,
                        status = status,
                        onBackClick = { navController.popBackStack() },
                        onNavigateToMenu = { navController.navigate("menu/$tableId/$usernameArg/$status") }
                    )
                }

                composable(
                    route = "menu/{tableId}/{username}/{status}?editDrinkId={editDrinkId}",
                    arguments = listOf(
                        navArgument("tableId") { type = NavType.IntType },
                        navArgument("username") { type = NavType.StringType },
                        navArgument("status") { type = NavType.StringType },
                        navArgument("editDrinkId") {
                            type = NavType.IntType
                            defaultValue = -1
                        }
                    )
                ) { backStackEntry ->
                    val tableId = backStackEntry.arguments?.getInt("tableId") ?: 1
                    val usernameArg = backStackEntry.arguments?.getString("username") ?: username
                    val status = backStackEntry.arguments?.getString("status") ?: "Trống"
                    val editDrinkId = backStackEntry.arguments?.getInt("editDrinkId") ?: -1
                    MenuScreen(
                        editDrinkId = editDrinkId,
                        onBackClick = { navController.popBackStack() },
                        onDrinkClick = { drinkId ->
                            navController.navigate("drink_detail/$drinkId/$tableId/$usernameArg/$status?editDrinkId=$editDrinkId")
                        }
                    )
                }

                composable(
                    route = "drink_detail/{drinkId}/{tableId}/{username}/{status}?editDrinkId={editDrinkId}",
                    arguments = listOf(
                        navArgument("drinkId") { type = NavType.IntType },
                        navArgument("tableId") { type = NavType.IntType },
                        navArgument("username") { type = NavType.StringType },
                        navArgument("status") { type = NavType.StringType },
                        navArgument("editDrinkId") { type = NavType.IntType; defaultValue = -1 }
                    )
                ) { backStackEntry ->
                    val drinkId = backStackEntry.arguments?.getInt("drinkId") ?: 0
                    val tableId = backStackEntry.arguments?.getInt("tableId") ?: 1
                    val usernameArg = backStackEntry.arguments?.getString("username") ?: username
                    val status = backStackEntry.arguments?.getString("status") ?: "Trống"
                    val editDrinkId = backStackEntry.arguments?.getInt("editDrinkId") ?: -1
                    val menuViewModel: MenuViewModel = viewModel(
                        factory = MenuViewModelFactory(
                            AppModule.provideCategoryRepository(context),
                            AppModule.provideDrinkRepository(context)
                        )
                    )
                    val drinks by menuViewModel.drinks.collectAsState()
                    val drink = remember(drinks) { drinks.firstOrNull { it.id == drinkId } }
                    drink?.let {
                        DrinkDetailScreen(
                            drink = it,
                            onAddTable = { selectedDrink ->
                                val backEntry = navController.getBackStackEntry("table_detail/$tableId/$usernameArg/$status")
                                if (editDrinkId != -1) backEntry.savedStateHandle["replaceDrink"] = Pair(editDrinkId, selectedDrink)
                                else backEntry.savedStateHandle["selectedDrink"] = selectedDrink
                                navController.popBackStack(route = "table_detail/$tableId/$usernameArg/$status", inclusive = false)
                            },
                            onCancel = { navController.popBackStack() },
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }

                composable("cart") {
                    CartScreen(username = sessionManager.getUsername().orEmpty())
                }

                composable("notification") {
                    NotificationScreen(viewModel = notificationViewModel)
                }

                composable("profile") {
                    val profileVM: ProfileViewModel = viewModel(
                        factory = ProfileViewModelFactory(
                            AppModule.provideUserRepository(context),
                            AppModule.provideSessionManager(context)
                        )
                    )
                    ProfileScreen(
                        viewModel = profileVM,
                        onNavigateToLogin = {
                            username = ""
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DrinkOrderPreview() {
    DrinkOrderTheme {
        AppNavigation()
    }
}