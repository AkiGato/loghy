package com.example.data.db

import androidx.room.TypeConverter
import org.json.JSONArray

class Converters {
    @TypeConverter
    fun fromStringList(list: List<String>?): String {
        if (list == null) return "[]"
        val array = JSONArray()
        for (item in list) {
            array.put(item)
        }
        return array.toString()
    }

    @TypeConverter
    fun toStringList(data: String?): List<String> {
        if (data.isNullOrEmpty() || data == "[]") return emptyList()
        val result = mutableListOf<String>()
        try {
            val array = JSONArray(data)
            for (i in 0 until array.length()) {
                result.add(array.getString(i))
            }
        } catch (_: Exception) {
            // Fallback for empty or plain string
        }
        return result
    }
}
