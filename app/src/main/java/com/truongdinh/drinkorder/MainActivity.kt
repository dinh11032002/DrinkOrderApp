package com.truongdinh.drinkorder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.FirebaseApp
import com.truongdinh.drinkorder.ui.navigation.AppNavigation
import com.truongdinh.drinkorder.ui.theme.DrinkOrderTheme
import com.truongdinh.drinkorder.ui.screens.detail.table_detail.OrderQueueViewModel

val LocalOrderQueueViewModel = staticCompositionLocalOf<OrderQueueViewModel> {
    error("OrderQueueViewModel not provided")
}
class MainActivity : ComponentActivity() {
    private lateinit var orderQueueViewModel: OrderQueueViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderQueueViewModel = ViewModelProvider(this)[OrderQueueViewModel::class.java]
        FirebaseApp.initializeApp(this)
        setContent {
            DrinkOrderTheme {
                CompositionLocalProvider(LocalOrderQueueViewModel provides orderQueueViewModel) {
                    AppNavigation()
                }
            }
        }
    }
}