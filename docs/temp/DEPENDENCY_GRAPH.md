# Dependency Graph

Generated: 2026-06-23

## Architecture Layers

```
┌─────────────────────────────────────────────────────────┐
│  Presentation Layer                                      │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌────────────┐ │
│  │ Screens  │ │ViewModels│ │Components│ │  Overlay   │ │
│  │(Compose) │ │ (Hilt)   │ │ (shared) │ │  Service   │ │
│  └────┬─────┘ └────┬─────┘ └──────────┘ └──────┬─────┘ │
│       │            │                            │       │
├───────┼────────────┼────────────────────────────┼───────┤
│  Domain Layer      │                            │       │
│  ┌─────────┐ ┌─────┴─────┐ ┌──────────┐ ┌─────┴─────┐ │
│  │  Models │ │ Use Cases │ │ Scoring  │ │  Engine   │ │
│  │  (pure) │ │  (inject) │ │  (pure)  │ │  (pure)   │ │
│  └─────────┘ └─────┬─────┘ └──────────┘ └───────────┘ │
│                    │ (interfaces)                        │
├────────────────────┼────────────────────────────────────┤
│  Data Layer        │                                    │
│  ┌─────────┐ ┌─────┴─────┐ ┌──────────┐ ┌───────────┐ │
│  │  Room   │ │  Repos    │ │   API    │ │DataStore  │ │
│  │(DB/DAO) │ │  (impl)   │ │(Retrofit)│ │(prefs)    │ │
│  └─────────┘ └───────────┘ └──────────┘ └───────────┘ │
│                                                         │
├─────────────────────────────────────────────────────────┤
│  DI Layer (Hilt Modules)                                │
│  AppModule │ DatabaseModule │ NetworkModule │ OverlayModule │ RepositoryModule │
└─────────────────────────────────────────────────────────┘
```

## Key Dependency Flows

### ViewModel → UseCase → Repository

```
HomeViewModel
  ├── GetHeroesUseCase → HeroRepository(interface) → HeroRepositoryImpl → HeroDao
  └── GetDraftHistoryUseCase → DraftSessionRepository(interface) → DraftSessionRepositoryImpl → DraftSessionDao

DraftViewModel
  ├── GetSuggestionsUseCase → DraftScorer (pure computation)
  ├── SaveDraftSessionUseCase → DraftSessionRepository
  ├── DraftSessionManager (StateFlow<DraftSession>)
  └── GetHeroesUseCase → HeroRepository

HeroListViewModel
  ├── GetHeroesUseCase → HeroRepository
  └── SyncHeroesUseCase → HeroRepository → MetaApi

SettingsViewModel
  ├── ToggleOverlayUseCase → OverlayController
  └── PreferencesDataStore → DataStore<Preferences>
```

### Overlay Service Dependencies

```
OverlayService (1,051 lines)
  ├── @Inject DraftSessionManager
  ├── @Inject GetHeroesUseCase
  ├── @Inject DataStore<Preferences>
  ├── PhaseDetector (capture)
  ├── PortraitMatcher (capture)
  ├── SlotRegions (capture)
  ├── ScreenCaptureManager (service)
  └── MiniWidget / FloatingBubble (overlay composables)
```

### Capture Pipeline

```
ScreenCaptureManager
  └── MediaProjection → VirtualDisplay → ImageReader → Bitmap
        ↓
  PhaseDetector.detect(frame) → DetectedPhase
  SlotRegions.cropSlot(frame, region) → Bitmap
  PortraitMatcher.match(crop, heroes) → MatchResult
  FirstPickDetector.detect(frame) → Boolean
```

## Fan-In / Fan-Out Analysis

### High Fan-In (most depended-upon)
| Module | Fan-In | Dependents |
|--------|--------|------------|
| `Hero` (domain model) | 25+ | Almost every layer |
| `DraftSessionManager` | 8 | ViewModels, OverlayService, tests |
| `HeroRepository` | 6 | 4 use cases, 2 repos |
| `DraftScorer` | 5 | GetSuggestionsUseCase, DraftViewModel, tests |
| `PhaseDetectionConfig` | 3 | PhaseDetector, PortraitMatcher, OverlayService |

### High Fan-Out (most dependencies)
| Module | Fan-Out | Dependencies |
|--------|---------|-------------|
| `OverlayService.kt` | 12+ | DraftSessionManager, GetHeroesUseCase, DataStore, PhaseDetector, PortraitMatcher, SlotRegions, ScreenCaptureManager, ... |
| `DraftExporter.kt` | 8 | Canvas, MediaStore, DraftSessionEntity, DraftOutcome, DateFormatter, ... |
| `DatabaseModule.kt` | 6 | AppDatabase, HeroDao, DraftSessionDao, HeroPoolDao, Migrations |
| `NetworkModule.kt` | 5 | OkHttp, Retrofit, Gson, MetaApi, Interceptors |

## Hilt Module Bindings

```kotlin
// AppModule: DataStore, DraftSessionManager, VoiceAlertService
// DatabaseModule: AppDatabase, HeroDao, DraftSessionDao, HeroPoolDao, MIGRATION_1_2, MIGRATION_2_3
// NetworkModule: OkHttpClient, Retrofit, MetaApi, Gson
// OverlayModule: OverlayController (singleton)
// RepositoryModule: @Binds HeroRepository, @Binds DraftSessionRepository
```
