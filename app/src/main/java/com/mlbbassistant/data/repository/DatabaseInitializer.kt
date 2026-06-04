```kotlin
package com.mlbbassistant.data.repository

import android.util.Log
import com.mlbbassistant.data.api.dto.toEntity
import com.mlbbassistant.data.db.dao.HeroDao
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseInitializer @Inject constructor(
    private val heroDao: HeroDao,
    private val assetDataSource: AssetHeroDataSource
) {
    companion object {
        private const val TAG = "DatabaseInitializer"
    }

    private val handler = CoroutineExceptionHandler { _, throwable ->
        Log.e(TAG, "Seed coroutine crashed", throwable)
    }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO + handler)

    fun seedIfEmpty() {
        scope.launch {
            if (heroDao.getAll().isNotEmpty()) {
                Log.d(TAG, "DB already populated — skipping seed")
                return@launch
            }

            val entities = try {
                assetDataSource.load()?.heroes?.map { it.toEntity() }?.takeIf { it.isNotEmpty() }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading asset data source", e)
                null
            } ?: run {
                Log.w(TAG, "Asset load failed — using SeedDataProvider")
                SeedDataProvider.heroes()
            }

            try {
                heroDao.upsertAll(entities)
                Log.d(TAG, "Seeded ${entities.size} heroes")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to upsert heroes into database", e)
            }
        }
    }
}
```