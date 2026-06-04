```kotlin
package com.mlbbassistant.data.model

enum class HeroRole(val displayName: String) {
    TANK("Tank"),
    FIGHTER("Fighter"),
    ASSASSIN("Assassin"),
    MAGE("Mage"),
    MARKSMAN("Marksman"),
    SUPPORT("Support"),
    UNKNOWN("Unknown");

    companion object {
        fun fromString(value: String?): HeroRole {
            return value?.let { 
                values().find { it.name.equals(it, ignoreCase = true) } 
            } ?: UNKNOWN
        }
    }
}
```