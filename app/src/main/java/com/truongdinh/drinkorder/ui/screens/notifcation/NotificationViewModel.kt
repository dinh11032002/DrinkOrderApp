package com.truongdinh.drinkorder.ui.screens.notifcation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.truongdinh.drinkorder.data.model.Notification
import com.truongdinh.drinkorder.data.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val username: String,
    private val repository: NotificationRepository = NotificationRepository()
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount

    private var isUpdating = false // Flag để tránh override

    init {
        listenToNotifications()
    }

    private fun listenToNotifications() {
        repository.listenNotifications(username) { list ->
            if (!isUpdating) { // ✅ Chỉ update khi không đang xử lý
                _notifications.value = list
                _unreadCount.value = list.count { !it.isRead }
            }
        }
    }

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            try {
                isUpdating = true
                repository.markAsRead(notificationId)
                _notifications.value = _notifications.value.map {
                    if (it.id == notificationId) it.copy(isRead = true) else it
                }
                _unreadCount.value = _notifications.value.count { !it.isRead }
                isUpdating = false
            } catch (e: Exception) {
                isUpdating = false
                Log.e("NotificationVM", "Error marking as read: ${e.message}")
            }
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            try {
                isUpdating = true
                repository.markAllAsRead(username)
                _notifications.value = _notifications.value.map { it.copy(isRead = true) }
                _unreadCount.value = 0
                isUpdating = false
            } catch (e: Exception) {
                isUpdating = false
                Log.e("NotificationVM", "Error marking all as read: ${e.message}")
            }
        }
    }

    fun deleteNotification(notificationId: String) {
        viewModelScope.launch {
            try {
                repository.deleteNotification(notificationId)
            } catch (e: Exception) {
                Log.e("NotificationVM", "Error deleting: ${e.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.stopListening()
        Log.d("NotificationVM", "ViewModel cleared for $username")
    }
}