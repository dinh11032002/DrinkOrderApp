package com.truongdinh.drinkorder.data.model

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

data class OrderFirestore(
    val id: String = "",
    val tableDetailId: Int = 0,
    val tableName: String = "",
    val username: String = "",
    val drinks: List<Drink> = emptyList(),
    val totalPrice: Int = 0,
    val status: String = "Đang xử lý",
    val createdAt: Timestamp? = null
) {
    fun getFormattedDate(): String {
        return createdAt?.toDate()?.let {
            SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(it)
        } ?: ""
    }

    fun getStatusColor(): Long {
        return when (status) {
            "Đang xử lý" -> 0xFFFFA726 // màu cam
            "Hoàn thành" -> 0xFF66BB6A // xanh lá
            "Đã hủy" -> 0xFFEF5350     // đỏ
            else -> 0xFF9E9E9E         // xám
        }
    }
}
