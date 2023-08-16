package com.example.grocerystore.data.source.local.utilsData

import androidx.room.TypeConverter

class ListTypeConverter {
    @TypeConverter
    fun fromStringToStringList(value: String): List<String> {
        return value.split(",").map { it }
    }

    @TypeConverter
    fun fromStringListToString(value: List<String>): String {
        return value.joinToString(separator = ",")
    }
}