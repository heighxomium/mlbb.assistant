```kotlin
package com.mlbbassistant.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.mlbbassistant.data.api.dto.MetaSnapshotDto
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Reads hero data from the bundled [assets/heroes.json].
 * Never throws — returns null if the file is missing or malformed.
 */
@Singleton
class AssetHeroDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    companion object {
        private const val FILE = "heroes.json"
        private const val TAG = "AssetHeroDataSource"
    }

    fun load(): MetaSnapshotDto? {
        return runCatching {
            context.assets.open(FILE).bufferedReader().use { reader ->
                gson.fromJson(reader, MetaSnapshotDto::class.java)
            }
        }.onFailure { exception ->
            when (exception) {
                is JsonSyntaxException -> Log.e(TAG, "heroes.json is malformed", exception)
                is IOException -> Log.e(TAG, "Failed to read heroes.json from assets", exception)
                else -> Log.e(TAG, "Unexpected error occurred while reading heroes.json", exception)
            }
        }.getOrNull()
    }
}
```