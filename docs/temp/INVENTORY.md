# File Inventory

Generated: 2026-06-23

## Summary
- **Total Kotlin files**: 125 (source + test)
- **Total lines**: 15,389
- **Source files**: 118
- **Test files**: 7

## Source Files by Layer

### App Root (2 files)
| File | Lines |
|------|-------|
| `AppDataStore.kt` | 17 |
| `MLBBApplication.kt` | ~20 |

### Capture Layer (6 files)
| File | Lines |
|------|-------|
| `capture/FirstPickDetector.kt` | ~50 |
| `capture/PerceptualHash.kt` | ~80 |
| `capture/PhaseDetectionConfig.kt` | 85 |
| `capture/PhaseDetector.kt` | ~80 |
| `capture/PortraitMatcher.kt` | 178 |
| `capture/SlotRegions.kt` | ~120 |

### Data Layer (14 files)
| File | Lines |
|------|-------|
| `data/export/DraftExporter.kt` | 269 |
| `data/local/crashlog/AppLogTree.kt` | ~50 |
| `data/local/crashlog/CrashLogStore.kt` | ~60 |
| `data/local/database/AppDatabase.kt` | 37 |
| `data/local/database/Converters.kt` | 38 |
| `data/local/database/DraftSessionDao.kt` | ~40 |
| `data/local/database/DraftSessionEntity.kt` | 35 |
| `data/local/database/HeroDao.kt` | 113 |
| `data/local/database/HeroEntity.kt` | 59 |
| `data/local/database/HeroPoolDao.kt` | 37 |
| `data/local/database/HeroPoolEntity.kt` | 26 |
| `data/local/datastore/PreferencesDataStore.kt` | 37 |
| `data/local/preferences/WizardPreference.kt` | 35 |
| `data/remote/api/MetaApi.kt` | ~15 |
| `data/remote/dto/MetaSnapshotDto.kt` | 38 |
| `data/repository/DraftSessionRepositoryImpl.kt` | 67 |
| `data/repository/HeroRepositoryImpl.kt` | 105 |

### DI Layer (5 files)
| File | Lines |
|------|-------|
| `di/AppModule.kt` | 30 |
| `di/DatabaseModule.kt` | 98 |
| `di/NetworkModule.kt` | 92 |
| `di/OverlayModule.kt` | 33 |
| `di/RepositoryModule.kt` | 22 |

### Domain Layer (18 files)
| File | Lines |
|------|-------|
| `domain/OverlayController.kt` | ~10 |
| `domain/advisor/BanRecommender.kt` | 167 |
| `domain/advisor/BuildAdvisor.kt` | 200 |
| `domain/advisor/CompositionAnalyzer.kt` | 146 |
| `domain/advisor/CompositionArchetype.kt` | 90 |
| `domain/advisor/DraftScoreCalculator.kt` | 127 |
| `domain/advisor/EnemyIntentAnalyzer.kt` | 59 |
| `domain/advisor/WinConditionGenerator.kt` | 108 |
| `domain/engine/DraftPatternAnalyzer.kt` | 93 |
| `domain/engine/DraftSessionManager.kt` | 234 |
| `domain/engine/PickSequenceEngine.kt` | 53 |
| `domain/engine/RankRuleEngine.kt` | 94 |
| `domain/engine/WeightCalibrator.kt` | 93 |
| `domain/model/DraftHistoryItem.kt` | 28 |
| `domain/model/DraftOutcome.kt` | ~15 |
| `domain/model/Hero.kt` | ~30 |
| `domain/model/Proficiency.kt` | ~25 |
| `domain/repository/*.kt` | ~30 |
| `domain/scoring/DraftScorer.kt` | 283 |
| `domain/scoring/ScoreWeights.kt` | 26 |
| `domain/usecase/*.kt (7)` | ~230 |

### Presentation Layer (38 files)
| File | Lines |
|------|-------|
| `presentation/common/components/*.kt (7)` | ~650 |
| `presentation/common/theme/*.kt (3)` | ~150 |
| `presentation/draft/*.kt (4)` | ~700 |
| `presentation/herodetail/HeroDetailScreen.kt` | 312 |
| `presentation/herolist/*.kt (3)` | ~350 |
| `presentation/heropool/*.kt (2)` | ~300 |
| `presentation/history/*.kt (3)` | ~550 |
| `presentation/home/*.kt (2)` | ~420 |
| `presentation/log/*.kt (2)` | ~380 |
| `presentation/main/MainActivity.kt` | 78 |
| `presentation/metaboard/MetaBoardScreen.kt` | 223 |
| `presentation/navigation/*.kt (2)` | ~100 |
| `presentation/overlay/*.kt (9)` | ~3,300 |
| `presentation/settings/*.kt (5)` | ~1,000 |
| `presentation/shell/AppShell.kt` | ~50 |
| `presentation/welcome/PermissionWizardScreen.kt` | 418 |

### Services (3 files)
| File | Lines |
|------|-------|
| `service/MLBBAccessibilityService.kt` | 53 |
| `service/ScreenCaptureManager.kt` | 93 |
| `service/VoiceAlertService.kt` | 39 |

### Utils (4 files)
| File | Lines |
|------|-------|
| `utils/DateFormatter.kt` | 81 |
| `utils/JsonParser.kt` | 24 |
| `utils/NetworkMonitor.kt` | 40 |
| `utils/NetworkResult.kt` | 94 |

### Resources
| File | Type |
|------|------|
| `AndroidManifest.xml` | Manifest |
| `res/drawable/ic_launcher_foreground.xml` | Vector |
| `res/drawable/ic_launcher_background.xml` | Vector |
| `res/mipmap-anydpi-v26/ic_launcher.xml` | Adaptive icon |
| `res/mipmap-anydpi-v26/ic_launcher_round.xml` | Adaptive icon |
| `res/values/colors.xml` | Colors |
| `res/values/strings.xml` | Strings (default) |
| `res/values/themes.xml` | Themes |
| `res/values-fil/strings.xml` | Filipino |
| `res/values-in/strings.xml` | Indonesian |
| `res/values-ms/strings.xml` | Malay |
| `res/values-th/strings.xml` | Thai |
| `res/values-vi/strings.xml` | Vietnamese |
| `res/xml/accessibility_service_config.xml` | Service config |
| `res/xml/file_paths.xml` | File provider |
| `res/raw/default_heroes.json` | Hero seed data (132 heroes, 74KB) |
| `assets/draft_ui_map.json` | UI region map (7KB) |

### Tests (7 files)
| File | Lines |
|------|-------|
| `PerceptualHashTest.kt` | ~100 |
| `BanRecommenderTest.kt` | 231 |
| `CompositionAnalyzerTest.kt` | 220 |
| `DraftSessionManagerTest.kt` | 250 |
| `DraftSessionSerializationTest.kt` | 242 |
| `PickSequenceEngineTest.kt` | ~80 |
| `RankRuleEngineTest.kt` | 212 |
| `DraftScorerTest.kt` | 219 |
