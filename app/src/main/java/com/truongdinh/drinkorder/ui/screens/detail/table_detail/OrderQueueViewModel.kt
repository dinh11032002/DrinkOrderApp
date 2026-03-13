package com.truongdinh.drinkorder.ui.screens.detail.table_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.truongdinh.drinkorder.data.enum.OrderStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

/**
 * ViewModel xử lý đơn hàng TUẦN TỰ như quầy pha chế có 1 nhân viên
 * Đơn sau phải đợi đơn trước hoàn thành
 */
class OrderQueueViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val ordersCollection = firestore.collection("orders")

    // Thời gian chế biến mỗi đơn (có thể thay đổi)
    companion object {
        // const val PREPARATION_TIME = 3 * 60 * 1000L  // 3 phút (production)
        const val PREPARATION_TIME = 2 * 60 * 1000L  // 2 phút (testing)
        // const val PREPARATION_TIME = 30 * 1000L  // 30 giây (testing nhanh)
    }

    // Đơn đang được xử lý
    private var currentProcessingOrder: String? = null
    private var isProcessing = false

    init {
        startQueueProcessor()
    }

    /**
     * Bắt đầu xử lý hàng đợi đơn hàng
     */
    private fun startQueueProcessor() {
        Log.d("OrderQueue", "Queue processor started")

        // Lắng nghe đơn hàng "Đang xử lý" theo thứ tự thời gian tạo
        ordersCollection
            .whereEqualTo("status", OrderStatus.PROCESSING.value)
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("OrderQueue", "Error: ${error.message}")
                    return@addSnapshotListener
                }

                val pendingOrders = snapshot?.documents ?: emptyList()
                val orderIds = pendingOrders.map { it.id.take(8) }

                Log.d("OrderQueue", "Queue has ${pendingOrders.size} orders: $orderIds")
                Log.d("OrderQueue", "isProcessing=$isProcessing, currentOrder=${currentProcessingOrder?.take(8)}")

                if (pendingOrders.isEmpty()) {
                    if (isProcessing) {
                        Log.d("OrderQueue", "Queue is empty, stopping processor")
                        isProcessing = false
                        currentProcessingOrder = null
                    }
                    return@addSnapshotListener
                }

                // Chỉ xử lý đơn đầu tiên NẾU chưa có đơn nào đang xử lý
                if (!isProcessing) {
                    val firstOrder = pendingOrders.first()
                    Log.d("OrderQueue", "Starting to process order ${firstOrder.id.take(8)}")
                    processOrder(firstOrder.id, firstOrder.getTimestamp("createdAt"))
                } else {
                    Log.d("OrderQueue", "Already processing ${currentProcessingOrder?.take(8)}, waiting...")
                }
            }
    }

    /**
     * Xử lý một đơn hàng
     */
    private fun processOrder(orderId: String, createdAt: Timestamp?) {
        // Check duplicate
        if (currentProcessingOrder == orderId) {
            Log.d("OrderQueue", "Order $orderId ALREADY PROCESSING, abort!")
            return
        }

        if (createdAt == null) {
            Log.e("OrderQueue", "Order $orderId has no createdAt")
            return
        }

        // Mark processing
        isProcessing = true
        currentProcessingOrder = orderId

        Log.d("OrderQueue", "START processing order $orderId")  // ← Log này

        viewModelScope.launch {
            try {
                // Check status before processing
                val currentDoc = ordersCollection.document(orderId).get().await()
                val currentStatus = currentDoc.getString("status")
                val notificationSent = currentDoc.getBoolean("notificationSent") ?: false

                Log.d("OrderQueue", "Pre-check: status='$currentStatus', notificationSent=$notificationSent")

                if (currentStatus != OrderStatus.PROCESSING.value) {
                    Log.d("OrderQueue", "Order $orderId status changed, skipping...")
                    isProcessing = false
                    currentProcessingOrder = null
                    return@launch
                }

                if (notificationSent) {
                    Log.d("OrderQueue", "Notification already sent, skipping...")
                    isProcessing = false
                    currentProcessingOrder = null
                    return@launch
                }

                val elapsedTime = Date().time - createdAt.toDate().time
                val remainingTime = PREPARATION_TIME - elapsedTime

                if (remainingTime > 0) {
                    Log.d("OrderQueue", "Waiting ${remainingTime / 1000}s for order $orderId")
                    delay(remainingTime)
                }

                // Final check before completing
                val finalDoc = ordersCollection.document(orderId).get().await()
                val finalStatus = finalDoc.getString("status")
                val finalNotificationSent = finalDoc.getBoolean("notificationSent") ?: false

                Log.d("OrderQueue", "Final check: status='$finalStatus', notificationSent=$finalNotificationSent")

                if (finalStatus == OrderStatus.PROCESSING.value && !finalNotificationSent) {
                    Log.d("OrderQueue", "CALLING completeOrder for $orderId")
                    completeOrder(orderId)
                    Log.d("OrderQueue", "completeOrder finished for $orderId")
                } else {
                    Log.d("OrderQueue", "Order $orderId no longer eligible (status=$finalStatus, notifSent=$finalNotificationSent)")
                }

            } catch (e: Exception) {
                Log.e("OrderQueue", "Error processing order $orderId: ${e.message}", e)
            } finally {
                isProcessing = false
                currentProcessingOrder = null
                Log.d("OrderQueue", "FINISHED processing order $orderId")
            }
        }
    }

    /**
     * Hoàn thành đơn hàng (chuyển status thành "Hoàn thành")
     */
    private suspend fun completeOrder(orderId: String) {
        try {
            // STEP 1: Get order data
            val orderDoc = ordersCollection.document(orderId).get().await()
            val tableNumber = orderDoc.getLong("tableDetailId")?.toInt() ?: 0
            val username = orderDoc.getString("username") ?: ""
            val currentStatus = orderDoc.getString("status") ?: ""
            val notificationSent = orderDoc.getBoolean("notificationSent") ?: false

            // CRITICAL LOG
            Log.d("OrderQueue", """
                completeOrder called for: $orderId
            - tableNumber: $tableNumber
            - username: $username
            - status: $currentStatus
            - notificationSent: $notificationSent
        """.trimIndent())

            // STEP 2: Validation checks
            if (currentStatus != OrderStatus.PROCESSING.value) {
                Log.d("OrderQueue", "Skip: Status is '$currentStatus'")
                return
            }

            if (notificationSent) {
                Log.d("OrderQueue", "Skip: Notification already sent")
                return
            }

            if (username.isEmpty()) {
                Log.e("OrderQueue", "Skip: No username")
                return
            }

            if (tableNumber == 0) {
                Log.e("OrderQueue", "Skip: Invalid table number")
                return
            }

            // STEP 3: Update order with notificationSent flag FIRST
            ordersCollection.document(orderId)
                .update(
                    mapOf(
                        "status" to OrderStatus.COMPLETE.value,
                        "completedAt" to Timestamp.now(),
                        "notificationSent" to true  // ← CRITICAL!
                    )
                )
                .await()

            Log.d("OrderQueue", "Order $orderId updated to 'Hoàn thành'")

            // STEP 4: Double-check notification doesn't exist
            val existingNotifs = firestore.collection("notifications")
                .whereEqualTo("orderId", orderId)
                .get()
                .await()

            if (!existingNotifs.isEmpty) {
                Log.d("OrderQueue", "Notification for order $orderId already exists! Count: ${existingNotifs.size()}")
                return
            }

            // STEP 5: Create notification
            val notificationData = mapOf(
                "username" to username,
                "tableNumber" to tableNumber,
                "tableName" to "Bàn $tableNumber",
                "orderId" to orderId,
                "message" to "Đơn hàng bàn $tableNumber đã hoàn thành!",
                "createdAt" to Timestamp.now(),
                "isRead" to false
            )

            firestore.collection("notifications")
                .add(notificationData)
                .await()

            Log.d("OrderQueue", "Notification created for user '$username', order $orderId")

        } catch (e: Exception) {
            Log.e("OrderQueue", "Error in completeOrder: ${e.message}", e)
        }
    }

    /**
     * Lấy vị trí của đơn hàng trong hàng đợi (cho UI hiển thị)
     */
    fun getOrderQueuePosition(orderId: String, callback: (Int) -> Unit) {
        ordersCollection
            .whereEqualTo("status", OrderStatus.PROCESSING.value)
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                val position = snapshot.documents.indexOfFirst { it.id == orderId }
                if (position >= 0) {
                    callback(position + 1)  // Vị trí bắt đầu từ 1
                } else {
                    callback(0)  // Không tìm thấy
                }
            }
            .addOnFailureListener { e ->
                Log.e("OrderQueue", "Error getting position: ${e.message}")
                callback(0)
            }
    }
}