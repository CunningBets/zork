# Mini-Zork JavaCard Implementation Plan

## Overview
Native reimplementation of Mini-Zork I (1987 Infocom sampler) as a JavaCard applet.
Target: JCOP 2.4.1, 81 KB EEPROM. No Z-machine interpreter - game logic in Java.

## Architecture (3 source files + tests)
- **Zork.java** - Applet entry point, APDU protocol handler
- **Data.java** - All constants, prose text, room/object/vocab tables
- **Engine.java** - Parser, world engine, output buffer, game state

## AID
- Package: `A0:00:00:06:47:2E:5A:4F:52:4B` (encodes "ZORK")
- Applet: `A0:00:00:06:47:2E:5A:4F:52:4B:01`

## APDU Protocol
| CLA  | INS  | Purpose                    |
|------|------|----------------------------|
| 0x80 | 0x10 | Submit player command       |
| 0x80 | 0x12 | Re-render room (LOOK)      |
| 0x80 | 0x14 | Restart game               |
| 0x00 | 0xC0 | GET RESPONSE (chunking)    |

## Game Content (First Pass)

### Rooms (~28)
Surface: West/North/South of House, Behind House, Forest Path, Forest West/East/South,
         Clearing, Up a Tree, Grating Clearing
House: Kitchen, Living Room, Attic
Underground: Cellar, Troll Room, East of Chasm, Gallery, N-S Passage, Round Room,
             Narrow Passage, Treasure Room, Loud Room, Maze 1-3, Dead End, Grating Room

### Objects (~30)
Scenery: Mailbox, Trophy Case, Front Door, Kitchen Window, Rug, Trap Door, Grating, Table
Takeable: Leaflet, Lamp, Sword, Brown Sack, Lunch, Garlic, Bottle, Nasty Knife, Rope
Creatures: Troll, Axe
Maze: Skeleton Key, Bag of Coins, Rusty Knife, Bones
Treasures: Egg, Nest, Torch, Painting, Chalice, Bar, Bracelet

### Verbs (~25)
LOOK, EXAMINE, TAKE, DROP, PUT, INVENTORY, OPEN, CLOSE, GO (+ directions),
ATTACK, LIGHT, EXTINGUISH, READ, SCORE, WAIT, AGAIN, RESTART, QUIT,
BRIEF, VERBOSE, MOVE, ENTER, CLIMB

## Key Design Decisions
1. **No Huffman compression in first pass** - plain ASCII strings via String[] in Data.java.
   Will convert to byte[] arrays and add Huffman for real card deployment.
2. **Simple vocabulary**: 4-char truncation, linear scan lookup
3. **Single STATE byte array** for all mutable game state (~100 bytes)
4. **1024-byte transient output buffer**
5. **Transaction wrapping** on each turn for atomicity
6. **Object location encoding**: 0=nowhere, 1-199=room IDs, 200=inventory, 201+=inside object

## Implementation Phases

### Phase 1: Infrastructure [COMPLETE - 40/40 tests passing]
- [x] Create plan file
- [x] Change AID (build.xml, build.gradle, BaseTest.java)
- [x] Remove old project references (passport)
- [x] Data.java - constants and static data (28 rooms, 30 objects, ~120 vocab entries)
- [x] Engine.java - game engine (parser, world engine, output buffer, 15+ action handlers)
- [x] Zork.java - APDU handler (SELECT, COMMAND, LOOK, RESTART, GET RESPONSE)
- [x] Tests (40 tests: navigation, items, combat, scoring, darkness, doors, etc.)
- [x] All tests passing

### Phase 2: Content Expansion (future)
- More room descriptions refined from ZIL source
- Disambiguation flow
- Multi-command input (split on period/comma)
- More special handlers
- Brief/verbose/superbrief modes

### Phase 3: Card Deployment (future)
- Convert String-based prose to byte[] arrays
- Huffman compression for prose
- Abbreviation substitution
- Size optimization
- CAP file build and on-card testing

## Room Map (exits)
```
Forest-W --- Forest Path --- Clearing --- Forest-E
   |              |              |
   |         West of House      |
   |          N |     | S       |
   |    N-of-House  S-of-House  |
   |              |              |
Forest-S    Behind House ------+
                  |
   Grating   [window]
   Clearing     |
      |      Kitchen --- Living Room
      |         |            |
   Grating   Attic      [trap door]
   Room                     |
      |                  Cellar
      |                     |
      +---- Maze ----  Troll Room
                            |
                      E-of-Chasm
                            |
                        Gallery
                            |
                      N-S Passage
                            |
                       Round Room
                       /        \
               Narrow Pass   Loud Room
                  |
             Treasure Room
```

## Scoring
Treasures placed in trophy case earn points:
- Egg: 5, Bag of Coins: 10, Torch: 6, Painting: 6
- Chalice: 5, Bar: 5, Bracelet: 5
- Max score: ~50 points (adjustable)
