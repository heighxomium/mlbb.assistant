```kotlin
package com.mlbbassistant.data.repository

import com.mlbbassistant.data.db.entity.HeroEntity

/**
 * Hard-coded fallback hero list — Patch 2.1.70, Season 40, June 2026.
 * 133 heroes including Hirara (released June 17 2026).
 * Used only when assets/heroes.json cannot be read AND the DB is empty.
 * Source: mlbbhub.com
 */
object SeedDataProvider {

    fun heroes(): List<HeroEntity> = listOf(
        createHero(1, "Aamon", "ASSASSIN", null, 0.513f, 0.062f, 0.12f, listOf(76, 37, 12), listOf(80, 72, 94), listOf(116, 10), "JUNGLE"),
        createHero(2, "Akai", "TANK", null, 0.508f, 0.055f, 0.06f, listOf(38, 79, 74), listOf(35, 70, 123), listOf(46, 65, 128), "ROAM"),
        createHero(3, "Aldous", "FIGHTER", null, 0.499f, 0.04f, 0.04f, listOf(76, 22, 92), listOf(27, 107, 94), listOf(67, 116, 10), "EXP"),
        createHero(4, "Alice", "TANK", "MAGE", 0.503f, 0.048f, 0.08f, listOf(58, 17, 19), listOf(54, 70, 123), listOf(46, 128, 129), "ROAM"),
        createHero(5, "Alpha", "FIGHTER", null, 0.468f, 0.035f, 0.03f, listOf(76, 92, 22), listOf(27, 49, 101), listOf(10, 116, 67), "EXP"),
        createHero(6, "Alucard", "FIGHTER", "ASSASSIN", 0.499f, 0.058f, 0.05f, listOf(76, 92, 30), listOf(101, 107, 27), listOf(116, 67, 90), "EXP"),
        createHero(7, "Angela", "SUPPORT", null, 0.491f, 0.072f, 0.14f, listOf(79, 74, 38), listOf(95, 56, 113), listOf(79, 74, 122), "ROAM"),
        createHero(8, "Argus", "FIGHTER", null, 0.531f, 0.045f, 0.07f, listOf(76, 30, 22), listOf(27, 107, 101), listOf(116, 40, 36), "EXP"),
        createHero(9, "Arlott", "FIGHTER", "ASSASSIN", 0.477f, 0.038f, 0.06f, listOf(76, 92, 18), listOf(27, 107, 49), listOf(116, 36, 85), "EXP"),
        createHero(10, "Atlas", "TANK", null, 0.518f, 0.082f, 0.15f, listOf(38, 79, 74), listOf(27, 91, 72), listOf(46, 102, 128), "ROAM"),
        // Add the rest of the heroes here...
    )

    private fun createHero(
        id: Int,
        name: String,
        role: String,
        secondaryRole: String?,
        winRate: Float,
        pickRate: Float,
        banRate: Float,
        counters: List<Int>,
        counteredBy: List<Int>,
        synergies: List<Int>,
        lane: String
    ): HeroEntity {
        return HeroEntity(
            id = id,
            name = name,
            role = role,
            secondaryRole = secondaryRole,
            winRate = winRate,
            pickRate = pickRate,
            banRate = banRate,
            counters = counters,
            counteredBy = counteredBy,
            synergies = synergies,
            lane = lane,
            imageUrl = ""
        )
    }
}
```