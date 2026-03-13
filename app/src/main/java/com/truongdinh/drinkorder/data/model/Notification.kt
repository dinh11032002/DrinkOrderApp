package com.truongdinh.drinkorder.data.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Parcelize
data class Notification(
    val id: String = "",
    val username: String = "",
    val tableNumber: Int = 0,
    val tableName: String = "",
    val orderId: String = "",
    val message: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    @JvmField
    val isRead: Boolean = false
) : Parcelable {

    fun getFormattedTime(): String {
        val date = createdAt.toDate()
        val now = Date()
        val diff = now.time - date.time

        return when {
            diff < 60_000 -> "Vừa xong"
            diff < 3_600_000 -> "${diff / 60_000} phút trước"
            diff < 86_400_000 -> "${diff / 3_600_000} giờ trước"
            else -> {
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                sdf.format(date)
            }
        }
    }
}