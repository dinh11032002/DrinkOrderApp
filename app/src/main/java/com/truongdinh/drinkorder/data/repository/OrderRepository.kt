package com.truongdinh.drinkorder.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.truongdinh.drinkorder.data.dao.OrderDao
import com.truongdinh.drinkorder.data.enum.OrderStatus
import com.truongdinh.drinkorder.data.model.Order
import com.truongdinh.drinkorder.data.model.OrderFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class OrderRepository(
    private val orderDao: OrderDao,
    private val firestore: FirebaseFirestore
) {
    private val ordersCollection = firestore.collection("orders")
    private val notificationsCollection = firestore.collection("notifications")

    suspend fun createOrder(order: Order) = orderDao.insertOrder(order)
    suspend fun getOrdersByTable(tableDetailId: Int) = orderDao.getOrdersByTable(tableDetailId)
    suspend fun updateOrderStatus(orderId: Int, status: String) =
        orderDao.updateOrderStatus(orderId, status)

    fun getOrdersByUsername(username: String): Flow<List<OrderFirestore>> = callbackFlow {
        val listener = ordersCollection
            .whereEqualTo("username", username)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val orders = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(OrderFirestore::class.java)?.copy(id = doc.id)
                } ?: emptyList()

                trySend(orders)
            }

        awaitClose { listener.remove() }
    }

    /**
     * Update trạng thái đơn hàng trên Firestore
     * Nếu chuyển từ Đang xử lý → Hoàn thành → tạo notification
     */
    private suspend fun pushNotification(tableNumber: Int, orderId: String) {
        val data = mapOf(
            "tableNumber" to tableNumber,
            "orderId" to orderId,
            "message" to "Đơn hàng bàn $tableNumber đã hoàn thành",
            "createdAt" to com.google.firebase.Timestamp.now(),
            "isRead" to false
        )

        notificationsCollection.add(data).await()
    }

    suspend fun updateOrderStatusFirestore(orderId: String, newStatus: String) {
        val doc = ordersCollection.document(orderId).get().await()

        val oldStatus = doc.getString("status")
        val tableNumber = doc.getLong("tableDetailId")?.toInt()

        println("DEBUG: oldStatus=$oldStatus, newStatus=$newStatus")

        // Cập nhật trạng thái
        ordersCollection.document(orderId)
            .update("status", newStatus)
            .await()

        // Logic tạo thông báo an toàn
        if (!oldStatus.equals("Hoàn thành", ignoreCase = true)
            && newStatus.equals("Hoàn thành", ignoreCase = true)
            && tableNumber != null
        ) {
            pushNotification(tableNumber, orderId)
        }
    }

    suspend fun placeOrder(orderData: Map<String, Any>) {
        ordersCollection.add(orderData).await()
    }

    suspend fun getActiveOrderByTable(tableId: Int): QuerySnapshot {
        return ordersCollection
            .whereEqualTo("tableDetailId", tableId)
            .whereEqualTo("status", OrderStatus.PROCESSING.value)
            .limit(1)
            .get()
            .await()
    }


    suspend fun deleteOrder(orderId: String) {
        ordersCollection.document(orderId)
            .delete()
            .await()
    }
}