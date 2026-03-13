package com.truongdinh.drinkorder.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.truongdinh.drinkorder.data.model.Order

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Query(value = "SELECT * FROM orders WHERE tableDetailId = :tableDetailId")
    suspend fun getOrdersByTable(tableDetailId: Int): List<Order>

    @Query(value = "UPDATE orders SET status = :status WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Int, status: String)
}