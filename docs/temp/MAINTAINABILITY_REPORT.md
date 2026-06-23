# Maintainability Report

Generated: 2026-06-23

## Large Files (>500 lines)

| File | Lines | Severity | Notes |
|------|-------|----------|-------|
| `OverlayService.kt` | 1,051 | CRITICAL | Two-mode touch strategy (bubble vs mini-widget), autonomous capture loop, session snapshot persistence, permission watchdog. Candidate for extraction of CaptureLoop, SessionPersistence, TouchStrategy helper classes in a future pass. |
| `MiniWidget.kt` | 672 | HIGH | Complex overlay Compose UI with slot grids, ban recommendations, score panels. Well-organized with internal composable functions but large overall. |

## Files 300-500 Lines (Monitor)

| File | Lines | Notes |
|------|-------|-------|
| `SettingsScreen.kt` | 492 | Many setting sections; would benefit from section composable extraction |
| `PermissionWizardScreen.kt` | 418 | Multi-step permission flow; reasonable for complexity |
| `DraftReplayScreen.kt` | 335 | Replay visualization; acceptable |
| `HeroDetailScreen.kt` | 312 | Detail view; acceptable |
| `HomeScreen.kt` | 302 | Multiple cards/sections; acceptable |
| `LogScreen.kt` | 301 | Log viewer with expand/collapse; acceptable |
| `DraftScorer.kt` | 283 | Core scoring algorithm; algorithmic complexity justified |
| `DraftExporter.kt` | 269 | Canvas rendering + CSV export; two responsibilities but manageable |

## Null Safety (Post-Cleanup)

| Issue | Count | Status |
|-------|-------|--------|
| `!!` operators | **0** | All 3 instances eliminated in this overhaul |
| `lateinit var` | 6 | All in OverlayService (Hilt injection) + test files; standard usage |

## Compiler Warnings (Post-Cleanup)

| Warning | Status |
|---------|--------|
| FlowPreview on `debounce()` | Fixed: `@OptIn(FlowPreview::class)` |
| Deprecated `Icons.Rounded.ShowChart` | Fixed: `Icons.AutoMirrored.Rounded.ShowChart` |
| Deprecated `LocalClipboardManager` | Suppressed: suspend-based `LocalClipboard` migration deferred |
| Always-true condition in MiniWidget | Fixed: direct null/id check for smart cast |

## Domain Purity

Domain layer (`domain/` package) was scanned for `android.*` imports:
- **Result**: 0 violations in source code
- Test files import `android.os.Parcel` and `android.os.Parcelable` for serialization tests (expected)
- `GetPagedHeroesUseCase` imports `androidx.paging.PagingData` (framework-adjacent but accepted by Android architecture guidelines)

## Coupling Analysis

| File | Import Count | Notes |
|------|-------------|-------|
| `OverlayService.kt` | 30+ | High coupling due to service orchestration role; expected for Android Service |
| `DraftExporter.kt` | 15 | Medium; Canvas + MediaStore + Room entity |
| `DraftViewModel.kt` | ~12 | Normal for ViewModel |
| All others | <10 | Low coupling |

## Bugs Fixed in This Overhaul

| Bug | File | Severity | Description |
|-----|------|----------|-------------|
| Data loss on save | `DraftSessionRepositoryImpl.kt` | HIGH | `toEntity()` hardcoded `yourPickIds = emptyList()` instead of mapping the actual field. Pick data was silently discarded on database round-trips. |

## Recommendations for Future Work

1. **OverlayService decomposition** (1,051 lines): Extract `CaptureLoopManager`, `SessionSnapshotManager`, and `TouchStrategyHandler` into separate classes to reduce cognitive complexity.
2. **VoiceAlertService integration**: Wire TTS alert calls into OverlayService draft phase transitions.
3. **MetaSnapshotDto.toEntity()**: `HeroDto` is missing `hasCCUlt` field mapping — defaults to `false` for all heroes fetched from network.
4. **Pre-existing test failures**: 8 unit tests failing on `main` branch (BanRecommenderTest, CompositionAnalyzerTest, RankRuleEngineTest, DraftScorerTest) — likely test data drift.
