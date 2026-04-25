# 🏰 Mini-Zork on JavaCard 💳

> *You are standing in an open field west of a white house, with a boarded front door.*
>
> *There is a small mailbox here.* 📬

A faithful reimplementation of **Mini-Zork I** (Infocom, 1987) running entirely on a JavaCard smart card. No Z-machine interpreter — the game logic is native JavaCard Java. The card *is* the computer. 🧠✨

## 🎮 What Is This?

A text adventure game that lives on a contactless smart card. Tap the card to a reader, send commands via APDU, and explore the Great Underground Empire — all powered by a tiny chip with 81KB of storage. ⚡

WARNING: This was produced using LLM, though it has been tested against a real card.

```
> open mailbox
Opened.
The small mailbox contains:
  leaflet

> take leaflet
Taken.

> read leaflet
WELCOME TO ZORK!
```

## 🗺️ Game Features

- 🏠 **28 rooms** — surface, house interior, and underground caverns
- 🗡️ **30 objects** — lamp, sword, troll, treasures, and more
- 🧌 **Combat** — defeat the troll to access the underground
- 🔦 **Light/dark model** — don't forget your lantern!
- 🗝️ **Puzzles** — kitchen window, rug/trap door, grating + key, maze
- 🏆 **7 treasures** to collect and deposit in the trophy case
- 💯 **Scoring** — max 50 points, with victory message
- 💾 **Auto-save** — every turn is atomic (transaction-wrapped)
- 🔄 **Restart** — reset to fresh game state anytime

## 🏗️ Architecture

Three Java files. That's it.

| File | Purpose | 📏 |
|------|---------|-----|
| `Zork.java` | 📡 APDU protocol handler | ~100 lines |
| `Engine.java` | 🧠 Parser + world engine + output buffer | ~600 lines |
| `Data.java` | 📖 All game content (rooms, objects, prose, vocab) | ~1400 lines |

### 📡 APDU Protocol

| CLA | INS | What it does |
|-----|-----|-------------|
| `80` | `10` | 📝 Send player command (ASCII text) |
| `80` | `12` | 👀 Re-describe current room (LOOK) |
| `80` | `14` | 🔄 Restart game |
| `00` | `C0` | 📨 GET RESPONSE (for long outputs) |

Responses are raw ASCII with `\n` newlines. Long responses chain via `SW 61XX` → GET RESPONSE.

## 🛠️ Building

### 📋 Prerequisites

- ☕ Java 8 (Zulu/Temurin) for CAP file build
- ☕ Java 18+ for running simulator tests
- 🐘 Gradle 8+ (wrapper included)
- 🐜 Ant (for CAP file build)
- 🃏 JavaCard 2.2.2 SDK (included in `ext/`)

### 🧪 Run Simulator Tests (51 tests)

```bash
make test
```

### 📦 Build CAP File

```bash
make build
```

Produces `build/zork.cap` (~21KB) 🎉

### 💳 Install on Card

```bash
make install
```

Uses [GlobalPlatformPro](https://github.com/martinpaljak/GlobalPlatformPro) to install via your reader. Default reader: ACR1555.

## 🎮 Go Client

A terminal client for playing the game on a real card lives in `client/`.

### 🔨 Build

```bash
cd client
go build -o zork-client .
```

### 🕹️ Interactive Play

```bash
./zork-client
```

```
Connected to ACS ACR1555 1S CL Reader(1)

Mini-ZORK I: The Great Underground Empire
A JavaCard interactive fiction by Infocom (reimplemented)
Release 1 / Serial number 260424

West of House
You are standing in an open field west of a white house,
with a boarded front door.
There is a small mailbox here.

> _
```

**Client commands:**
- 🎯 Type game commands directly: `north`, `take lamp`, `open mailbox`
- 👀 `/look` — re-describe room
- 🔄 `/restart` — restart game
- 🐛 `/debug` — toggle APDU hex dumps
- 🚪 `/quit` — exit

### 🧪 Automated Test Walkthrough (98 steps)

```bash
./zork-client -test
```

Runs a complete playthrough on real hardware: navigates the full map, solves all puzzles, collects all 7 treasures, achieves victory with score 50/50, and tests error handling. ✅

```
  ok  select applet
  ok  restart
  ok  look at starting room
  ok  open mailbox
  ok  take leaflet
  ...
  ok  kill troll with sword
  ...
  ok  deposit egg - victory!
  ok  final score is 50
  ok  RESTART APDU

98 passed, 0 failed
```

## 🗺️ Map

```
                     🌲 Forest Path 🌲
                      |          |
  🌲 Forest West --- Clearing --- Forest East 🌲
       |              |
  West of House 🏠  (paths)
    N |     | S
  North    South
  of House  of House
       |         |
      Behind House 🪟──── Kitchen 🍳
                            |
                        Living Room 🏆
                            |
                        [trap door]
                            |
                  ⬇️  Cellar (dark!) 🔦
                            |
                      Troll Room 🧌
                            |
                     East of Chasm
                            |
                        Gallery 🖼️
                            |
                      N-S Passage
                            |
                       Round Room 💎
                       /        \
                Narrow Pass   Loud Room 📢
                   |
              Treasure Room 🏆
```

## 📊 Size Budget

| Component | Size |
|-----------|------|
| 📦 CAP file (total) | **~21 KB** |
| 💾 EEPROM available | 81 KB |
| 📝 Headroom remaining | **~60 KB** 🎉 |

## 🎯 Applet AID

```
A0:00:00:06:47:2E:5A:4F:52:4B:01
              G  .  Z  O  R  K
```

## 📜 Credits

- 🕹️ Original **Zork I** by Infocom (Marc Blank, Dave Lebling, et al.)
- 📋 **Mini-Zork I** (1987) — promotional sampler edition
- 💳 JavaCard reimplementation — a love letter to interactive fiction and embedded systems

## 📄 License

The game content is based on Infocom's Mini-Zork I. This is a non-commercial educational reimplementation. The JavaCard engine code is original work.

---

*It is pitch black. You are likely to be eaten by a grue.* 😱🦷
