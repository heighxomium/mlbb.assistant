```kotlin
package com.mlbbassistant.data.api.dto

import com.mlbbassistant.data.db.entity.HeroEntity
import com.mlbbassistant.data.db.entity.MetaSnapshotEntity

fun HeroDto.toEntity(): HeroEntity = HeroEntity(
    id = id,
    name = name.trim().ifBlank { "Hero $id" },
    role = role.trim().ifBlank { "UNKNOWN" },
    secondaryRole = secondaryRole?.trim()?.takeIf { it.isNotBlank() },
    winRate = winRate.coerceIn(0f, 1f),
    pickRate = pickRate.coerceIn(0f, 1f),
    banRate = banRate.coerceIn(0f, 1f),
    counters = counters.orEmpty(),
    counteredBy = counteredBy.orEmpty(),
    synergies = synergies.orEmpty(),
    lane = lane.trim().ifBlank { "JUNGLE" },
    imageUrl = imageUrl.trim()
)

fun MetaSnapshotDto.toEntity(): MetaSnapshotEntity = MetaSnapshotEntity(
    patch = patch.trim().ifBlank { "unknown" },
    updatedAt = updatedAt ?: System.currentTimeMillis(),
    fetchedAt = System.currentTimeMillis()
)
```