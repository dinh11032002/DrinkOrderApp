package com.truongdinh.drinkorder.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tables")
data class Table(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val status: String = "Trống"
)