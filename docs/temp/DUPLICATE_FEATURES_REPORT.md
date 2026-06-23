# Duplicate Features Report

Generated: 2026-06-23

## Activities / Fragments
| Class | File | Purpose | Duplicate? |
|-------|------|---------|------------|
| `MainActivity` | `presentation/main/` | App entry point, navigation host | No |
| `OverlayPermissionActivity` | `presentation/overlay/` | Overlay permission request flow | No |

**Result**: No duplicate activities. Each serves a distinct purpose.

## Use Cases (7 total)
| Use Case | Dependencies | Duplicate? |
|----------|-------------|------------|
| `GetHeroesUseCase` | HeroRepository | No |
| `GetPagedHeroesUseCase` | HeroRepository (Paging3) | No (distinct from GetHeroes: paged vs flow) |
| `GetSuggestionsUseCase` | DraftScorer | No |
| `SaveDraftSessionUseCase` | DraftSessionRepository | No |
| `GetDraftHistoryUseCase` | DraftSessionRepository | No |
| `SyncHeroesUseCase` | HeroRepository | No |
| `ToggleOverlayUseCase` | OverlayController | No |

**Result**: No duplicate use cases. `GetHeroesUseCase` returns a `Flow<List<Hero>>` for reactive UI, while `GetPagedHeroesUseCase` returns `Flow<PagingData<Hero>>` for large-list performance.

## Singleton Objects (29 total, post-cleanup)
Key objects audited:
- `PhaseDetectionConfig` — single config source for capture thresholds
- `PhaseDetector` — pixel-based phase classification
- `PortraitMatcher` — hero portrait matching via dHash + histogram
- `SlotRegions` — normalised screen regions for slot detection
- `PerceptualHash` — dHash computation utility
- `FirstPickDetector` — first-pick side detection
- `DateFormatter` — thread-safe date formatting
- `JsonParser` — GSON-based JSON parsing
- `NetworkMonitor` — connectivity monitoring
- `WizardPreference` — onboarding flag DataStore accessor

**Result**: No duplicate singletons. Each serves a unique purpose.

## Data Models (~40 data classes)
Key models audited:
- `Hero` (domain) ↔ `HeroEntity` (data) — proper domain/entity separation
- `DraftHistoryItem` (domain) ↔ `DraftSessionEntity` (data) — proper separation
- `HeroPoolEntity` — standalone, no domain mirror needed
- `MetaSnapshotDto` / `HeroDto` — network DTOs, distinct from entities

**Result**: No redundant parallel models. Domain ↔ Entity mapping follows Clean Architecture correctly.

## Navigation
- Single `AppNavGraph.kt` with `AppRoute` sealed class
- No duplicate navigation logic

## Strings / Colors / Dimens
- Theme colors defined once in `Color.kt`
- Strings properly localized in 6 language files (default + fil/in/ms/th/vi)
- No duplicate string keys detected across value files

## Asset Files
- `draft_ui_map.json` and `default_heroes.json` — unique purposes, no duplicates
- MD5 check: both files are unique

## Dead Code Eliminated in This Overhaul

| File | Lines | Reason |
|------|-------|--------|
| `FrameProcessor.kt` | 220 | Never instantiated or referenced by any caller |
| `PhaseOcrDetector.kt` | 106 | Never called outside its own file |
| `RankDetector.kt` | 128 | Never called outside its own file |
| `AppConstants.kt` | 12 | Never imported; OverlayService uses own inline constants |
| `Extensions.kt` | 71 | All 8 extension functions never imported or called |
| **Total** | **537** | |

## Partially-Used Code (Flagged, Not Removed)

| Item | Status | Notes |
|------|--------|-------|
| `VoiceAlertService` | Wired but idle | Injected in MainActivity, only `shutdown()` called. speak/alert methods never invoked. Feature stub for future TTS alerts. |
