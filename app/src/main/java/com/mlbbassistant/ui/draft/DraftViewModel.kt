```kotlin
package com.mlbbassistant.ui.draft

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mlbbassistant.core.DraftEngine
import com.mlbbassistant.data.model.DraftState
import com.mlbbassistant.data.model.DraftSuggestion
import com.mlbbassistant.data.model.Hero
import com.mlbbassistant.data.repository.HeroRepository
import com.mlbbassistant.data.repository.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DraftViewModel @Inject constructor(
    private val repo: HeroRepository,
    private val engine: DraftEngine,
    private val prefs: UserPreferences
) : ViewModel() {

    private val _draftState = MutableStateFlow(DraftState())
    val draftState: StateFlow<DraftState> = _draftState.asStateFlow()

    private val allHeroes: StateFlow<List<Hero>> = repo.observeHeroes()
        .catch { emit(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private data class ScoringConfig(
        val topN: Int,
        val meta: Float,
        val counter: Float,
        val synergy: Float
    )

    private val scoringConfig: Flow<ScoringConfig> = combine(
        prefs.suggestionCount,
        prefs.weightMeta,
        prefs.weightCounter,
        prefs.weightSynergy
    ) { topN, meta, counter, synergy ->
        ScoringConfig(topN, meta, counter, synergy)
    }.catch { emit(ScoringConfig(5, 0.35f, 0.40f, 0.25f)) }

    val suggestions: StateFlow<List<DraftSuggestion>> = combine(
        _draftState,
        allHeroes,
        scoringConfig
    ) { state, heroes, cfg ->
        val pool = heroes.filter { it.id !in state.unavailableIds }
        engine.suggest(pool, state, cfg.topN, DraftEngine.Weights(cfg.meta, cfg.counter, cfg.synergy))
    }.catch { emit(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addAllyPick(hero: Hero) = updateDraftState { copy(allyPicks = (allyPicks + hero).take(5)) }
    fun addEnemyPick(hero: Hero) = updateDraftState { copy(enemyPicks = (enemyPicks + hero).take(5)) }
    fun addBan(hero: Hero) = updateDraftState { copy(bans = (bans + hero).take(10)) }
    fun removeAllyPick(hero: Hero) = updateDraftState { copy(allyPicks = allyPicks - hero) }
    fun removeEnemyPick(hero: Hero) = updateDraftState { copy(enemyPicks = enemyPicks - hero) }
    fun removeBan(hero: Hero) = updateDraftState { copy(bans = bans - hero) }
    fun resetDraft() = updateDraftState { DraftState() }

    private fun updateDraftState(transform: DraftState.() -> DraftState) {
        _draftState.update { currentState ->
            runCatching { currentState.transform() }.getOrDefault(currentState)
        }
    }
}
```