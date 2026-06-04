```kotlin
package com.mlbbassistant.data.model

/**
 * A ranked hero recommendation produced by the suggestion engine.
 *
 * @property hero The suggested hero.
 * @property score Composite score in [0, 1] used for ranking.
 * @property reason Human-readable explanation for the suggestion.
 * @property counterScore Contribution from counter-pick advantage.
 * @property synergyScore Contribution from team synergy.
 * @property metaScore Contribution from win/ban/pick rate meta weight.
 */
data class DraftSuggestion(
    val hero: Hero,
    val score: Float,
    val reason: String,
    val counterScore: Float = 0f,
    val synergyScore: Float = 0f,
    val metaScore: Float = 0f
) {
    init {
        require(score in 0f..1f) { "Score must be in the range [0, 1]." }
        require(counterScore >= 0f) { "Counter score must be non-negative." }
        require(synergyScore >= 0f) { "Synergy score must be non-negative." }
        require(metaScore >= 0f) { "Meta score must be non-negative." }
    }
}
```