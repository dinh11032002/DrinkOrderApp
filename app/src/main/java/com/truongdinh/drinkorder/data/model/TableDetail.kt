package com.truongdinh.drinkorder.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "table_details",
    foreignKeys = [
        ForeignKey(
            entity = Table::class,
            parentColumns = ["id"],
            childColumns = ["tableId"],
            onDelete = ForeignKey.CASCADE // Nếu bàn bị xóa → chi tiết bàn cũng bị xóa
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.SET_NULL // Nếu nhân viên bị xóa → vẫn giữ lại lịch sử bàn
        ),
        ForeignKey(
            entity = Drink::class,
            parentColumns = ["id"],
            childColumns = ["drinkId"],
            onDelete = ForeignKey.RESTRICT // Không cho xóa món nếu đang có trong chi tiết bàn
        )
    ],
    indices = [
        Index("tableId"),
        Index("userId"),
        Index("drinkId")
    ]
)
data class TableDetail(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tableId: Int,
    val userId: Int?,
    val drinkId: Int,
    val status: String,
    val size: String,
    val quantity: Int,
    val note: String,
    val total: Int,
    val createdAt: LocalDateTime
)