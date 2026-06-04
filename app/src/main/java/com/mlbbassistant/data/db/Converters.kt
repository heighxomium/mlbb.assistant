```kotlin
package com.mlbbassistant.data.db

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson: Gson by lazy { Gson() }
    private val listType = object : TypeToken<List<Int>>() {}.type

    @TypeConverter
    fun fromIntList(value: List<Int>?): String {
        return gson.toJson(value.orEmpty())
    }

    @TypeConverter
    fun toIntList(value: String?): List<Int> {
        if (value.isNullOrBlank()) return emptyList()
        return try {
            gson.fromJson(value, listType) ?: emptyList()
        } catch (e: JsonSyntaxException) {
            Log.w("Converters", "Invalid JSON for int list: $value", e)
            emptyList()
        } catch (e: Exception) {
            Log.e("Converters", "Unexpected error while parsing JSON: $value", e)
            emptyList()
        }
    }
}
```