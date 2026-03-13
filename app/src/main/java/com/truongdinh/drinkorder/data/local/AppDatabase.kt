package com.truongdinh.drinkorder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.truongdinh.drinkorder.data.dao.CategoryDao
import com.truongdinh.drinkorder.data.dao.DrinkDao
import com.truongdinh.drinkorder.data.dao.OrderDao
import com.truongdinh.drinkorder.data.dao.TableDao
import com.truongdinh.drinkorder.data.dao.UserDao
import com.truongdinh.drinkorder.data.model.Category
import com.truongdinh.drinkorder.data.model.Drink
import com.truongdinh.drinkorder.data.model.LocalDateTimeConvert
import com.truongdinh.drinkorder.data.model.Order
import com.truongdinh.drinkorder.data.model.Table
import com.truongdinh.drinkorder.data.model.TableDetail
import com.truongdinh.drinkorder.data.model.User

@Database(
    entities = [
    Table::class, User::class, TableDetail::class, Drink::class, Category::class, Order::class], version = 10, exportSchema = true)
@TypeConverters(LocalDateTimeConvert::class, Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tableDao(): TableDao
    abstract fun userDao(): UserDao
    abstract fun categoryDao() : CategoryDao
    abstract fun drinkDao() : DrinkDao

    abstract fun orderDao() : OrderDao
}