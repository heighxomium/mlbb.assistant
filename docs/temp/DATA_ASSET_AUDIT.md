# Data Asset Audit

Generated: 2026-06-23

## JSON Files

### `app/src/main/assets/draft_ui_map.json`
- **Size**: 7,287 bytes
- **Purpose**: Normalised screen region coordinates for draft UI slot detection
- **Structure**: Object with phase_banner, action_button, ban_slots, pick_slots
- **Minification status**: Already compact (7,287 bytes actual vs 7,362 bytes with json.dumps compact separators)
- **Used by**: `SlotRegions.kt` (parsed at runtime)
- **Action**: No optimization needed

### `app/src/main/res/raw/default_heroes.json`
- **Size**: 74,623 bytes (73 KB)
- **Purpose**: Seed data for 132 MLBB heroes with stats, roles, counters, synergies
- **Minification status**: Already fully minified (74,623 bytes matches compact output)
- **Used by**: `JsonParser.kt` → loaded into Room database on first launch
- **Action**: No optimization needed

### `app/schemas/com.mlbb.assistant.data.local.database.AppDatabase/3.json`
- **Size**: ~15 KB
- **Purpose**: Room schema export for version 3 (auto-generated)
- **Action**: Do not modify (generated file)

## CSV Files
**None found.**

## XML Data Files
No data XML files. All XML files are Android resource/config files.

## Proto Files
**None found.**

## Summary

| Asset | Size | Already Minified? | Action |
|-------|------|-------------------|--------|
| `draft_ui_map.json` | 7 KB | Yes | None |
| `default_heroes.json` | 73 KB | Yes | None |
| Room schema JSON | ~15 KB | N/A (generated) | Do not modify |

**Total data asset size**: ~95 KB
**Optimization savings**: 0 bytes (all assets already optimized)
