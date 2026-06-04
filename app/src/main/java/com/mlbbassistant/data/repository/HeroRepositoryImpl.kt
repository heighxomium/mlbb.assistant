```kotlin
package com.mlbbassistant.data.repository

import android.util.Log
import com.google.gson.Gson
import com.mlbbassistant.core.Resource
import com.mlbbassistant.data.api.dto.toEntity
import com.mlbbassistant.data.db.dao.HeroDao
import com.mlbbassistant.data.db.dao.MetaSnapshotDao
import com.mlbbassistant.data.model.Hero
import com.mlbbassistant.data.model.HeroRole
import com.mlbbassistant.di.NetworkModule
import kotlinx.coroutines.flow.*
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeroRepositoryImpl @Inject constructor(
    private val heroDao: HeroDao,
    private val metaSnapshotDao: MetaSnapshotDao,
    private val assetDataSource: AssetHeroDataSource,
    private val userPreferences: UserPreferences,
    private val okHttpClient: OkHttpClient,
    private val gson: Gson
) : HeroRepository {

    companion object {
        private const val TAG = "HeroRepository"
    }

    override fun observeHeroes(): Flow<List<Hero>> =
        heroDao.observeAll()
            .map { list -> list.map { it.toDomain() } }
            .catch { e ->
                Log.e(TAG, "observeHeroes error", e)
                emit(emptyList())
            }

    override fun observeByRole(role: HeroRole): Flow<List<Hero>> =
        heroDao.observeByRole(role.name)
            .map { list -> list.map { it.toDomain() } }
            .catch { e ->
                Log.e(TAG, "observeByRole error", e)
                emit(emptyList())
            }

    override fun searchHeroes(query: String): Flow<List<Hero>> =
        heroDao.search(query.trim())
            .map { list -> list.map { it.toDomain() } }
            .catch { e ->
                Log.e(TAG, "searchHeroes error", e)
                emit(emptyList())
            }

    override suspend fun getHeroById(id: Int): Hero? = runCatching {
        heroDao.getById(id)?.toDomain()
    }.onFailure { e ->
        Log.e(TAG, "getHeroById error", e)
    }.getOrNull()

    override suspend fun refreshHeroes(patch: String?): Resource<Unit> {
        val apiUrl = runCatching { userPreferences.apiUrl.first() }.getOrDefault("")
        if (apiUrl.isNotBlank()) {
            val remoteResult = tryRemoteRefresh(apiUrl, patch)
            if (remoteResult is Resource.Success) return remoteResult
            Log.w(TAG, "Remote refresh failed — falling back to bundled assets")
        }
        return tryAssetRefresh()
    }

    private suspend fun tryRemoteRefresh(baseUrl: String, patch: String?): Resource<Unit> = runCatching {
        val service = NetworkModule.buildApiService(baseUrl, okHttpClient, gson)
            ?: return Resource.Error("Invalid API URL: $baseUrl")
        val snapshot = service.getMetaSnapshot(patch)
        heroDao.upsertAll(snapshot.heroes.map { it.toEntity() })
        metaSnapshotDao.upsert(snapshot.toEntity())
        Log.d(TAG, "Remote refresh OK — ${snapshot.heroes.size} heroes")
        Resource.Success(Unit)
    }.getOrElse { e ->
        Log.w(TAG, "Remote refresh error", e)
        Resource.Error("Network error: ${e.message}", e)
    }

    private suspend fun tryAssetRefresh(): Resource<Unit> = runCatching {
        val snapshot = assetDataSource.load()
            ?: return Resource.Error("Bundled heroes.json could not be parsed")
        val entities = snapshot.heroes.map { it.toEntity() }
        if (entities.isEmpty()) return Resource.Error("heroes.json contains no heroes")
        heroDao.upsertAll(entities)
        metaSnapshotDao.upsert(snapshot.toEntity())
        Log.d(TAG, "Asset refresh OK — ${entities.size} heroes")
        Resource.Success(Unit)
    }.getOrElse { e ->
        Log.e(TAG, "Asset refresh error", e)
        Resource.Error("Failed to load bundled data: ${e.message}", e)
    }

    override suspend fun getLastSyncedPatch(): String? = runCatching {
        metaSnapshotDao.getLatest()?.patch
    }.onFailure { e ->
        Log.e(TAG, "getLastSyncedPatch error", e)
    }.getOrNull()
}
```