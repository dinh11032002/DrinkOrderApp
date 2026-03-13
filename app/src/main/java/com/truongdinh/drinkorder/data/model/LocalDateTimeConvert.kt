package com.truongdinh.drinkorder.data.model

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class LocalDateTimeConvert {
    @SuppressLint("NewApi")
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): Long? {
        return value?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    }

    @SuppressLint("NewApi")
    @TypeConverter
    fun toLocalDateTime(value: Long?): LocalDateTime? {
        return value?.let {
            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDateTime()
        }
    }
}