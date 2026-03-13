package com.truongdinh.drinkorder.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.truongdinh.drinkorder.data.model.Notification
import kotlinx.coroutines.tasks.await

class NotificationRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val reference = db.collection("notifications")
    private var listenerRegistration: ListenerRegistration? = null

    fun listenNotifications(username: String, onChange: (List<Notification>) -> Unit) {
        // ✅ Hủy listener cũ trước khi tạo mới
        stopListening()

        Log.d("NotificationRepo", "Starting to listen for user: $username")

        listenerRegistration = reference
            .whereEqualTo("username", username) // ✅ Lọc theo username
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("NotificationRepo", "Error listening: ${error.message}", error)
                    onChange(emptyList())
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val data = snapshot.documents.mapNotNull { doc ->
                        try {
                            val notification = doc.toObject(Notification::class.java)?.copy(id = doc.id) // ✅ Thêm val notification
                            Log.d("NotificationRepo", "id: ${doc.id} | isRead: ${doc.getBoolean("isRead")} | parsed isRead: ${notification?.isRead}")
                            notification
                        } catch (e: Exception) {
                            Log.e("NotificationRepo", "Failed to parse doc ${doc.id}: ${e.message}")
                            null
                        }
                    }
                    Log.d("NotificationRepo", "Loaded ${data.size} notifications for $username")
                    onChange(data)
                } else {
                    onChange(emptyList())
                }
            }
    }

    // ✅ Hủy listener khi không cần nữa (logout)
    fun stopListening() {
        listenerRegistration?.remove()
        listenerRegistration = null
        Log.d("NotificationRepo", "Stopped listening")
    }

    suspend fun markAsRead(id: String) {
        try {
            reference.document(id).update("isRead", true).await()
            Log.d("NotificationRepo", "Successfully marked $id as read on Firestore")

            // Verify lại xem Firestore có lưu không
            val doc = reference.document(id).get().await()
            Log.d("NotificationRepo", "After update - isRead: ${doc.getBoolean("isRead")}")
        } catch (e: Exception) {
            Log.e("NotificationRepo", "Error marking as read: ${e.message}")
        }
    }

    suspend fun markAllAsRead(username: String) {
        try {
            val snapshot = reference
                .whereEqualTo("username", username)
                .whereEqualTo("isRead", false) // ✅ Filter thẳng trên Firestore
                .get().await()

            if (snapshot.documents.isEmpty()) return

            val batch = db.batch()
            snapshot.documents.forEach { doc ->
                batch.update(doc.reference, "isRead", true)
            }
            batch.commit().await()
            Log.d("NotificationRepo", "Marked all as read for $username")
        } catch (e: Exception) {
            Log.e("NotificationRepo", "Error marking all as read: ${e.message}")
        }
    }

    suspend fun deleteNotification(id: String) {
        try {
            reference.document(id).delete().await()
        } catch (e: Exception) {
            Log.e("NotificationRepo", "Error deleting: ${e.message}")
        }
    }
}