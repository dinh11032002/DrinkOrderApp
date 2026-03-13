package com.truongdinh.drinkorder.di

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.truongdinh.drinkorder.data.local.AppDatabase
import com.truongdinh.drinkorder.data.local.Prefs
import com.truongdinh.drinkorder.data.repository.CategoryRepository
import com.truongdinh.drinkorder.data.repository.DrinkRepository
import com.truongdinh.drinkorder.data.repository.NotificationRepository
import com.truongdinh.drinkorder.data.repository.OrderRepository
import com.truongdinh.drinkorder.data.repository.TableRepository
import com.truongdinh.drinkorder.data.repository.UserRepository
import com.truongdinh.drinkorder.ui.screens.auth.sigin.RememberAccountStore
import com.truongdinh.drinkorder.ui.screens.profile.SessionManager

object AppModule {
    private var db: AppDatabase? = null
    private var tableRepository: TableRepository? = null
    private var userRepository: UserRepository? = null
    private var drinkRepository: DrinkRepository? = null
    private var categoryRepository: CategoryRepository? = null
    private var orderRepository: OrderRepository? = null

    private var prefs: Prefs? = null

    @SuppressLint("StaticFieldLeak")
    private var firestore: FirebaseFirestore? = null

    fun provideDatabase(context: Context): AppDatabase {
        return db ?: Room.databaseBuilder(
            context.applicationContext,
            klass = AppDatabase::class.java,
            name = "drink_order_db"
        ).fallbackToDestructiveMigration().build().also { db = it }
    }

    fun providePrefs(context: Context): Prefs {
        return prefs ?: synchronized(lock = this) {
            prefs ?: Prefs(context.applicationContext).also { prefs = it }
        }
    }

    fun provideRepository(context: Context): TableRepository {
        return tableRepository ?: synchronized(lock = this) {
            tableRepository ?: TableRepository(
                dao = provideDatabase(context).tableDao(),
                prefs = providePrefs(context)  // ← Thêm prefs
            ).also { tableRepository = it }
        }
    }

    fun provideUserRepository(context: Context): UserRepository {
        return userRepository ?: synchronized(lock = this) {
            userRepository ?: UserRepository(
                userDao = provideDatabase(context).userDao()
            ).also { userRepository = it }
        }
    }

    fun provideCategoryRepository(context: Context): CategoryRepository {
        return categoryRepository ?: synchronized(lock = this) {
            categoryRepository ?: CategoryRepository(
                categoryDao = provideDatabase(context).categoryDao()
            ).also { categoryRepository = it }
        }
    }

    fun provideDrinkRepository(context: Context): DrinkRepository {
        return drinkRepository ?: synchronized(lock = this) {
            drinkRepository ?: DrinkRepository(
                drinkDao = provideDatabase(context).drinkDao()
            ).also { drinkRepository = it }
        }
    }

    fun provideOrderRepository(context: Context): OrderRepository {
        return orderRepository ?: synchronized(lock = this) {
            orderRepository ?: OrderRepository(
                orderDao = provideDatabase(context).orderDao(),
                firestore = provideFirebaseFirestore()
            ).also { orderRepository = it }
        }
    }

    fun provideFirebaseFirestore(): FirebaseFirestore {
        return firestore ?: synchronized(this) {
            firestore ?: FirebaseFirestore.getInstance().also { fs ->
                val settings = FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build()
                fs.firestoreSettings = settings
                firestore = fs
            }
        }
    }

    fun provideSessionManager(context: Context): SessionManager {
        return SessionManager(context)
    }

    fun provideRememberAccount(context: Context): RememberAccountStore {
        return RememberAccountStore(context)
    }
}