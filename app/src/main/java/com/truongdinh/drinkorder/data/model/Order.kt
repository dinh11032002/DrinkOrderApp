package com.truongdinh.drinkorder.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(
            entity = TableDetail::class,
            parentColumns = ["id"],
            childColumns = ["tableDetailId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["tableDetailId"])]
)
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val tableDetailId: Int,
    val totalPrice: Int,
    val status: String = "Đang xử lý",
    val createdAt: LocalDateTime
)
