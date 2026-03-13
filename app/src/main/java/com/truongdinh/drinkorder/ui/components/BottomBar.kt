package com.truongdinh.drinkorder.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(
    navController: NavController,
    unreadCount: Int,
    modifier: Modifier = Modifier
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Trang chủ") },
            label = { Text("Trang chủ") },
            selected = currentRoute == "home",
            onClick = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                    launchSingleTop = true
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Giỏ hàng") },
            label = { Text("Giỏ hàng") },
            selected = currentRoute == "cart",
            onClick = { navController.navigate("cart") { launchSingleTop = true } }
        )

        NavigationBarItem(
            icon = {
                BadgedBox(
                    badge = {
                        if (unreadCount > 0) {
                            Badge(containerColor = Color.Red, contentColor = Color.White) {
                                Text(
                                    text = if (unreadCount > 9) "9+" else "$unreadCount",
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                ) {
                    Icon(Icons.Default.Notifications, contentDescription = "Thông báo")
                }
            },
            label = { Text("Thông báo") },
            selected = currentRoute == "notification",
            onClick = { navController.navigate("notification") { launchSingleTop = true } }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Cá nhân") },
            label = { Text("Cá nhân") },
            selected = currentRoute == "profile",
            onClick = { navController.navigate("profile") { launchSingleTop = true } }
        )
    }
}