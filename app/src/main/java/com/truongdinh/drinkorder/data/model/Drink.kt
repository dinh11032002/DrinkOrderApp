package com.truongdinh.drinkorder.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "drinks",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["idCategory"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["idCategory"])]
)
data class Drink(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val price: Int = 0,
    val size: String = "S",
    val quantity: Int = 1,
    val note: String = "",
    val image: String? = null,
    val idCategory: Int = 0,
    val sizePrices: Map<String, Int> = mapOf(
        "S" to price,
        "M" to price + 5000,
        "L" to price + 10000
    )
) : Parcelable