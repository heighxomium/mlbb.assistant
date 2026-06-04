```kotlin
package com.mlbbassistant.data.model

/**
 * Snapshot of an ongoing draft session.
 *
 * @property allyPicks   Heroes already locked in by the player's team (max 5).
 * @property enemyPicks  Heroes already locked in by the enemy team (max 5).
 * @property bans        All banned heroes regardless of team (max 10).
 */
data class DraftState(
    val allyPicks: List<Hero> = emptyList(),
    val enemyPicks: List<Hero> = emptyList(),
    val bans: List<Hero> = emptyList()
) {
    val allPicked: List<Hero> by lazy { allyPicks + enemyPicks }
    val allUnavailable: List<Hero> by lazy { allPicked + bans }
    val unavailableIds: Set<Int> by lazy { allUnavailable.mapNotNull { it.id }.toSet() }
    val isComplete: Boolean get() = allyPicks.size == 5 && enemyPicks.size == 5
}

data class Hero(
    val id: Int?,
    val name: String
)
```