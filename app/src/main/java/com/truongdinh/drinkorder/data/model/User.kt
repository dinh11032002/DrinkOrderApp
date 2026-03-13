package com.truongdinh.drinkorder.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val gender: String = "",
    val dob: String = "",
    val phone: String,
    val address: String = "",
    val email: String,
    val password: String
)