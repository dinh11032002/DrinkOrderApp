package com.truongdinh.drinkorder.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converters {
    private val gson = Gson()

    @TypeConverter
    @JvmStatic
    fun mapToString(map: Map<String, Int>?): String {
        return gson.toJson(map ?: emptyMap<String, Int>())
    }

    @TypeConverter
    @JvmStatic
    fun stringToMap(value: String?): Map<String, Int> {
        if (value.isNullOrEmpty()) {
            return emptyMap()
        }
        val type = object : TypeToken<Map<String, Int>>() {}.type
        return gson.fromJson(value, type)
    }
}