# Mini-Zork on JavaCard: Software Specification

**Target platform:** JCOP 2.4.1, 81 KB EEPROM
**Approach:** Native reimplementation of Mini-Zork I (Infocom, 1987) as a JavaCard applet. No Z-machine interpreter.
**Scope:** Software only. Hardware (reader IC, MCU, display, keyboard) is assumed and not covered here.

---

## 1. Goals and non-goals

### Goals
- Ship a single JavaCard applet that plays Mini-Zork I end to end, faithful to the 1987 demo's content and feel.
- Fit comfortably in 81 KB of EEPROM, including applet bytecode, prose, world data, and persistent save state.
- Expose the game through a simple APDU protocol suitable for a minimal, non-programmable-looking terminal.
- Make each turn atomic: a pulled card mid-turn leaves the game in a consistent prior state.
- Keep the code auditable. A reviewer should be able to read the applet and confirm the card is where the thinking happens.

### Non-goals
- Z-machine compatibility. No `.z3` story file is loaded or parsed. The game logic is written directly in JavaCard Java.
- Running any Infocom game other than Mini-Zork I. Reuse of this codebase for other titles is out of scope.
- Multi-player, networking, or cross-card save portability.
- Graphics. Output is ASCII text only.

---

## 2. Source reference

The spec assumes the implementer has access to a Mini-Zork I source or story file to use as the canonical reference for:

- Room list, room descriptions (long and brief), and exit graph
- Object list, object descriptions, properties, and initial placements
- Vocabulary (verbs, nouns, adjectives, prepositions, directions)
- Action handlers and puzzle logic
- Prose text for standard responses ("Taken.", "You can't go that way.", etc.)

The implementer will transcribe this content into the data structures defined in Section 5 rather than parsing it at runtime. The Mini-Zork ZIL source or a decompiled v3 story file both serve as adequate references.

---

## 3. System architecture

The applet has four subsystems:

1. **APDU handler** — entry point. Decodes command APDUs, invokes the game loop, returns response APDUs.
2. **Parser** — tokenizes input, resolves vocabulary, matches sentence patterns, produces a parsed command structure.
3. **World engine** — maintains game state (player location, object positions, flags, turn counter, score) and executes action handlers.
4. **Output buffer** — accumulates text produced during a turn, formats it, and hands it to the APDU handler for return.

Data flows strictly in one direction per turn: APDU in → parser → world engine → output buffer → APDU out. There is no interrupt handling, no concurrency, and no background processing between turns. The card is idle until the next command APDU arrives.

---

## 4. APDU protocol

### 4.1 Applet selection

Standard ISO 7816-4 SELECT by AID. AID to be assigned during personalization; `0xA0 00 00 06 47 2E 5A 4F 52 4B` ("…ZORK") is suggested.

On SELECT, the applet:
- Initializes the output buffer.
- If this is the first selection after installation, runs the game's intro sequence (banner, starting room description) and places its text in the output buffer.
- If a saved game exists (it always will after the first turn), emits a brief "Resuming." line followed by a re-render of the current room's brief description.
- Returns the buffered text in the response.

### 4.2 Commands

| CLA  | INS  | P1 | P2 | Lc     | Data             | Le   | Purpose                                |
|------|------|----|----|--------|------------------|------|----------------------------------------|
| 0x80 | 0x10 | 00 | 00 | 1..80  | ASCII command    | 00   | Submit player input                    |
| 0x80 | 0x12 | 00 | 00 | 00     | —                | 00   | Re-render current room (LOOK shortcut) |
| 0x80 | 0x14 | 00 | 00 | 00     | —                | 00   | Restart game (requires confirmation)   |
| 0x00 | 0xC0 | 00 | 00 | —      | —                | xx   | GET RESPONSE (standard)                |

All other CLA/INS combinations return `0x6D00` (instruction not supported).

### 4.3 Response format

Response data is raw ASCII. Newline is `0x0A`. No control codes other than newline are emitted; any formatting (clear-screen, cursor positioning, status line rendering) is the responsibility of the terminal. Output is designed to flow cleanly to a dumb scrolling display.

If the accumulated output exceeds 256 bytes, the applet returns the first chunk with SW `0x61XX` where `XX` is the number of remaining bytes (capped at 0xFF; if more than 255 bytes remain, `XX` is 0x00 meaning "255 or more"). The terminal issues GET RESPONSE to retrieve subsequent chunks until SW is `0x9000`.

Typical turns produce 100–400 bytes of output. The buffer is sized at 1024 bytes to accommodate verbose responses (full room descriptions with object lists, or multi-paragraph special events).

### 4.4 Status words

| SW     | Meaning                                       |
|--------|-----------------------------------------------|
| 0x9000 | Success, no more data                         |
| 0x61XX | Success, XX bytes pending (0x00 = ≥255)       |
| 0x6700 | Wrong length (Lc out of range)                |
| 0x6982 | Security status not satisfied (reserved)      |
| 0x6A86 | Wrong P1/P2                                   |
| 0x6D00 | Instruction not supported                     |
| 0x6F00 | Internal error (unrecoverable; game halted)   |

### 4.5 Error handling

Parser errors (unknown word, ungrammatical input) are *not* APDU errors. They produce normal game text ("I don't know the word 'xyzzy'.") and return SW 0x9000. APDU-level errors are reserved for protocol violations, not gameplay events.

---

## 5. Data structures

All game data lives in persistent `byte[]` arrays allocated at install time. No dynamic allocation occurs during gameplay.

### 5.1 Prose table

All text strings are stored in a single large `byte[]` (`PROSE`) as concatenated, Huffman-compressed ASCII. A parallel `short[]` (`PROSE_INDEX`) maps string IDs to (offset, length) pairs.

Strings are accessed by ID throughout the rest of the data. There are no inline string literals in the action handlers — every piece of output is a reference into `PROSE`.

**Compression:** static Huffman table built from Mini-Zork's actual prose frequencies. Expected ratio: 55–65% of original ASCII size. The decode routine is ~80 lines of JavaCard and writes directly into the output buffer.

**Abbreviation substitution:** before Huffman encoding, common phrases ("the", " you ", "is ", "are ", "The ", ". ", etc., plus Mini-Zork-specific repeats like "mailbox", "trophy case", "white house") are replaced with single-byte tokens drawn from a 64-entry dictionary. This captures much of what Infocom's own abbreviation table did and is simpler to implement.

Estimated prose budget: ~20 KB compressed, covering all room descriptions (long + brief), object descriptions, dictionary words, and stock responses.

### 5.2 Room table

Each room is a fixed-size record (16 bytes) in `ROOMS[]`:

| Offset | Size | Field                                           |
|--------|------|-------------------------------------------------|
| 0      | 2    | Long description string ID                      |
| 2      | 2    | Brief description string ID                     |
| 4      | 2    | Name string ID (for status line)                |
| 6      | 8    | Exit table: 8 bytes, one per direction          |
| 14     | 1    | Flags (lit, visited, special-handler, etc.)     |
| 15     | 1    | Special action handler ID (0 = none)            |

**Exit encoding:** one byte per direction in the order N, S, E, W, NE, NW, SE, SW (U, D, IN, OUT require a secondary table for rooms that use them — roughly 6 rooms in Mini-Zork). A value of 0 means no exit. Values 1–199 are room IDs. Values 200–255 are indices into a "conditional exit" table containing locked doors, required objects, or messages ("The door is boarded.").

Mini-Zork has ~30 rooms. `ROOMS[]` is therefore ~480 bytes plus a conditional-exit table of ~40 bytes.

### 5.3 Object table

Each object is a fixed-size record (12 bytes) in `OBJECTS[]`:

| Offset | Size | Field                                           |
|--------|------|-------------------------------------------------|
| 0      | 2    | Short name string ID                            |
| 2      | 2    | Long description string ID (when examined)      |
| 4      | 2    | Flags (takeable, container, openable, light, readable, weapon, worn, etc.) |
| 6      | 1    | Initial location (room ID, or parent object ID) |
| 8      | 1    | Adjective set ID (indexes into adjective table) |
| 9      | 1    | Noun set ID (indexes into noun table)           |
| 10     | 1    | Capacity (for containers) / damage (for weapons)|
| 11     | 1    | Special handler ID (0 = none)                   |

Mini-Zork has ~40 objects. `OBJECTS[]` is ~480 bytes.

Static object data is in code space. Dynamic per-object state (current location, open/closed, lit/unlit, moved flags) lives in a separate `OBJECT_STATE[]` array so that only mutable data is written to EEPROM each turn (Section 8).

### 5.4 Vocabulary

Three tables:

- `VERBS[]` — verb tokens with their canonical form ID and action handler ID.
- `NOUNS[]` — noun tokens with the bitfield of object IDs they can refer to.
- `ADJECTIVES[]` — adjective tokens with their bitfield.

Words are stored as 6-byte packed entries (4-byte truncated word + 2 bytes of metadata), matching Infocom's own approach for compactness and fast lookup. Mini-Zork's vocabulary is ~300 words, giving ~1.8 KB.

### 5.5 Dynamic state

Persistent game state is a single `byte[]` (`STATE`) of about 200 bytes:

- Player current room (1 byte)
- Player inventory list (16 bytes, object IDs)
- Score (1 byte), moves/turns (2 bytes)
- Global flags (16 bytes, 128 bits for game-wide state: has-been-warned, troll-defeated, lantern-fuel-remaining, etc.)
- Per-object state (40 objects × 2 bytes each = 80 bytes: current location + per-object flags)
- Lantern turns remaining (1 byte; Mini-Zork uses a simplified lantern timer)
- RNG seed (2 bytes)

`STATE` is the only array written to EEPROM during normal play. All other data is read-only after install.

---

## 6. Parser

### 6.1 Tokenization

The input buffer (up to 80 bytes of ASCII from the APDU) is normalized to lowercase in place, then split on whitespace and punctuation. Each token is truncated to 4 characters and looked up in the vocabulary tables (this matches Infocom's own truncation, meaning "inventory" and "inve" both match).

### 6.2 Sentence patterns

The parser recognizes a small set of patterns sufficient for Mini-Zork:

- `VERB`
- `VERB NOUN`
- `VERB ADJ* NOUN`
- `VERB NOUN PREP NOUN`
- `VERB ADJ* NOUN PREP ADJ* NOUN`
- `DIRECTION` (implicit "go")
- Bare `NOUN` after a disambiguation prompt

Multi-command input ("take lamp. light it. go north") is supported by splitting on periods and commas before tokenization, then running each fragment through the parser in sequence.

### 6.3 Object resolution

Given a noun token and optional adjectives, the parser produces the intersection of:
- Objects whose noun set includes the token
- Objects whose adjective set includes all supplied adjectives
- Objects currently visible to the player (in current room, in player inventory, or inside open containers at either location)

If the result is a single object, resolution succeeds. If empty, the parser emits "You can't see any such thing." If multiple, the parser emits "Which X do you mean, the A or the B?" and sets a pending-disambiguation flag in `STATE`. The next turn's input is interpreted as the answer.

### 6.4 Output

The parser produces a `ParsedCommand` structure (in transient memory, not persistent):

- Verb ID
- Direct object ID (or 0)
- Indirect object ID (or 0)
- Preposition ID (or 0)
- Valid flag

This structure is handed to the world engine. If `valid` is false, the parser has already emitted an explanation to the output buffer.

---

## 7. World engine

### 7.1 Turn loop

```
1. Clear output buffer.
2. If pending disambiguation, resolve it; else tokenize and parse input.
3. If parse failed, go to step 8.
4. Dispatch to the verb's action handler.
5. Action handler may modify STATE, emit text, or trigger special events.
6. If player moved, run the new room's arrival logic (describe room, list objects).
7. Run per-turn housekeeping: decrement lantern fuel, run the troll's one-shot logic if applicable, check win/loss conditions.
8. If STATE has changed, commit the transaction.
9. Render status line (location, score, moves) as the first line of output.
10. Return output buffer to APDU handler.
```

### 7.2 Action handlers

Each verb has a handler method. Handlers follow a uniform signature:

```java
private void handleTake(ParsedCommand cmd);
private void handleOpen(ParsedCommand cmd);
// ...
```

Mini-Zork needs roughly these verbs: look, examine, take, drop, put, open, close, go (+ all directions), inventory, light, extinguish, read, attack, kill, move, push, turn, tie, climb, enter, exit, wait, again, save (no-op; save is automatic), restore (no-op), restart, quit, score, brief, verbose, superbrief.

About 30 handlers total. Most are short (10–30 lines); a few (take, go, examine) are longer because they handle many edge cases.

### 7.3 Special handlers

Some rooms and objects have custom logic that can't be expressed in the generic tables:

- **White House:** board-up message if player tries to enter through boarded windows, unless the kitchen window is opened.
- **Kitchen:** sack of groceries logic.
- **Troll Room:** combat. Mini-Zork simplifies this to a deterministic sequence rather than the full combat table.
- **Cellar:** one-way trapdoor.
- **Lantern:** fuel countdown, warning messages at specific thresholds.
- **Trophy Case:** scoring when treasures are deposited.

Each is a small method invoked via the special-handler ID field in the room or object table. Roughly 10–15 special handlers, averaging 20 lines each.

### 7.4 Randomness

Mini-Zork uses randomness only for minor flavor (combat outcomes, occasional atmospheric messages). A 16-bit LFSR seeded at install from a card-specific value and re-seeded from the turn counter is sufficient.

---

## 8. Persistence and atomicity

### 8.1 Transaction model

Each turn is wrapped in a JavaCard transaction:

```java
JCSystem.beginTransaction();
try {
    executeTurn();
    JCSystem.commitTransaction();
} catch (Exception e) {
    JCSystem.abortTransaction();
    // emit internal error message, return SW 0x6F00
}
```

If the card is pulled mid-turn, the transaction aborts and `STATE` reverts to its pre-turn contents. The next selection resumes from the last committed turn.

### 8.2 EEPROM wear

The worst-case write pattern is `STATE` being rewritten once per turn. At 100,000 write cycles per cell (typical JCOP spec) and a generous estimate of 1,000 turns per playthrough, a single card supports ~100 full playthroughs before wear becomes a concern. For a demonstration piece this is ample.

Wear mitigation if needed: rotate the write location of the turn counter across a ring of 8 slots, reducing the hottest cell's rewrite rate by 8×. Not required for v1.

### 8.3 Save/restore semantics

There is no explicit save command. The game is always saved; every committed turn is the save point. `restart` (INS 0x14) resets `STATE` to its install-time contents after a confirmation prompt on the next turn. There is no undo.

---

## 9. Size budget

| Component                         | Estimated size |
|-----------------------------------|----------------|
| Applet bytecode (parser, engine, handlers, Huffman decoder) | 18–22 KB |
| Prose table (compressed)          | 18–22 KB       |
| Room table + exit tables          | ~0.5 KB        |
| Object table                      | ~0.5 KB        |
| Vocabulary tables                 | ~1.8 KB        |
| Abbreviation dictionary           | ~0.5 KB        |
| Huffman decode table              | ~0.5 KB        |
| Dynamic STATE                     | ~0.2 KB        |
| Transient buffers (output, parse) | ~1.5 KB RAM (not EEPROM) |
| **Total EEPROM**                  | **~42–48 KB**  |

Leaves 33–39 KB of headroom on an 81 KB card — comfortable margin for implementation overhead, unanticipated content, or a second story.

---

## 10. Build and install

### 10.1 Toolchain

- JavaCard SDK 3.0.4 or later (compatible with JCOP 2.4.1).
- `javacardx.framework` and `javacard.framework` only. No optional packages required.
- Standard `converter` tool to produce CAP file.

### 10.2 Install parameters

- AID: assigned at personalization.
- Install data: none required. The game's initial state is hardcoded in the applet's `install()` method, which populates `STATE` on first run.

### 10.3 Personalization

The prose table and Huffman decode table are compiled into the CAP file, so personalization is a standard applet install with no post-install data loading. This keeps the install path simple and makes every card identical.

---

## 11. Testing

### 11.1 Off-card simulation

A host-side JavaCard simulator (`jCardSim` or equivalent) runs the applet against a scripted sequence of APDUs. The test suite includes:

- A full walkthrough of Mini-Zork I, collecting all treasures and reaching the end state.
- Parser edge cases: unknown verbs, ambiguous nouns, multi-noun commands, truncation matches.
- Disambiguation flow (ambiguous noun → prompt → answer).
- Transaction abort: simulate power loss mid-turn, verify state reverts.
- APDU chaining: verify large responses retrieve correctly via GET RESPONSE.
- Restart: verify `STATE` returns to install-time contents.

### 11.2 On-card testing

A smaller subset of the above runs against real hardware to verify timing and EEPROM behavior. Focus on:

- End-to-end walkthrough performance (turn latency under 500ms target).
- Card-removal mid-turn recovery.
- Multiple install/uninstall cycles.

### 11.3 Prose fidelity

Golden-file comparison: the walkthrough's output is diffed against the output of a reference Z-machine running the original Mini-Zork I `.z3`. Minor differences are expected (status line formatting, trailing whitespace) and documented; substantive differences are bugs.

---

## 12. Open questions

- **Status line format.** Z-machine v3 puts score/moves on a separate top line. If the terminal's display supports a reserved status row, the applet should emit a control byte to position the cursor. If not, status goes inline as the first line of each response. Decision depends on final display choice; applet should be configurable at install.
- **Vocabulary coverage.** Mini-Zork's original vocabulary is smaller than full Zork but still has synonyms. Decide whether to preserve all synonyms (better player experience) or trim to canonical forms only (smaller table). Recommend preserving all.
- **Lantern model.** Mini-Zork uses a simplified lantern. Confirm whether to match its exact turn count or use a round number. No gameplay impact.
- **Brief/verbose/superbrief modes.** Standard Infocom toggles. Worth including; cost is small.

---

## 13. Development plan

Rough order of work:

1. APDU skeleton: select, command, response, GET RESPONSE. Echo input to verify plumbing.
2. Prose table: build compression tool host-side, embed compressed data, implement decoder. Smoke-test by printing the intro.
3. Room and object tables: transcribe from reference. Implement LOOK and GO. Player can walk the map.
4. Parser: tokenization, vocabulary lookup, basic sentence patterns.
5. Core verbs: TAKE, DROP, EXAMINE, INVENTORY, OPEN, CLOSE, PUT.
6. Light model, lantern, dark rooms.
7. Special handlers: one at a time, in the order the player encounters them.
8. Scoring, win condition, restart.
9. Disambiguation, multi-command input, abbreviations (`g` for again, `i` for inventory, etc.).
10. Transaction wrapping, save-state persistence, power-loss testing.
11. Walkthrough regression suite, prose diff against reference.
12. Size and performance optimization.

Estimated effort: 3–6 weeks for a developer comfortable with JavaCard; longer for a first-timer, largely due to the platform's quirks rather than Mini-Zork's complexity.
