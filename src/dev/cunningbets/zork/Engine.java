package dev.cunningbets.zork;

import javacard.framework.*;

/**
 * Mini-Zork I game engine.
 * Contains: output buffer, parser, world engine, game state, action handlers.
 */
public class Engine {

    // ---------------------------------------------------------------
    // Output buffer (transient - RAM)
    // ---------------------------------------------------------------
    private static final short OUT_BUF_SIZE = 1024;
    private byte[] outBuf;
    private short[] outState; // [0]=outLen, [1]=outPos (read cursor for GET RESPONSE)
    private static final short OUT_LEN = 0;
    private static final short OUT_POS = 1;

    // ---------------------------------------------------------------
    // Game state (persistent - EEPROM)
    // ---------------------------------------------------------------
    // Layout of the state array:
    private static final short ST_ROOM = 0;        // 1 byte: current room ID
    private static final short ST_SCORE = 1;        // 1 byte: score
    private static final short ST_TURNS_HI = 2;     // 2 bytes: turn counter
    private static final short ST_TURNS_LO = 3;
    private static final short ST_LAMP_FUEL = 4;    // 1 byte: lamp fuel (turns remaining / 2)
    private static final short ST_FLAGS_HI = 5;     // 2 bytes: game flags
    private static final short ST_FLAGS_LO = 6;
    private static final short ST_DESC_MODE = 7;    // 1 byte: description mode
    private static final short ST_INITIALIZED = 8;  // 1 byte: 0x5A if game initialized
    private static final short ST_SIZE = 9;

    private byte[] state;

    // Object locations: 1 byte per object (room ID, LOC_PLAYER, LOC_INSIDE+objId)
    private byte[] objLoc;

    // Object mutable flags: 1 byte per object
    private byte[] objState;

    // Room visited bitfield: 1 bit per room (4 bytes = 32 rooms)
    private byte[] roomVisited;

    // ---------------------------------------------------------------
    // Parse state (transient)
    // ---------------------------------------------------------------
    private static final short PARSE_SIZE = 8;
    private short[] parseResult; // transient
    private static final short PR_VERB = 0;
    private static final short PR_DOBJ = 1;     // direct object ID
    private static final short PR_IOBJ = 2;     // indirect object ID
    private static final short PR_PREP = 3;     // preposition ID
    private static final short PR_VALID = 4;    // 1 if valid parse
    private static final short PR_DIR = 5;      // direction if movement command
    private static final short PR_ADJ = 6;      // adjective from input
    private static final short PR_IADJ = 7;     // indirect adjective

    // Tokenizer workspace (transient)
    private byte[] tokenBuf; // working buffer for input processing
    private static final short TOKEN_BUF_SIZE = 82;

    // Previous command for AGAIN
    private byte[] prevCommand;

    private static final byte MAGIC_INITIALIZED = 0x5A;

    /**
     * Constructor - allocates all arrays.
     * Called once during applet install.
     */
    Engine() {
        // Transient arrays (RAM, cleared on reset/deselect)
        outBuf = JCSystem.makeTransientByteArray(OUT_BUF_SIZE, JCSystem.CLEAR_ON_DESELECT);
        outState = JCSystem.makeTransientShortArray((short) 2, JCSystem.CLEAR_ON_DESELECT);
        parseResult = JCSystem.makeTransientShortArray(PARSE_SIZE, JCSystem.CLEAR_ON_DESELECT);
        tokenBuf = JCSystem.makeTransientByteArray(TOKEN_BUF_SIZE, JCSystem.CLEAR_ON_DESELECT);

        // Persistent arrays (EEPROM)
        state = new byte[ST_SIZE];
        objLoc = new byte[Data.NUM_OBJECTS];
        objState = new byte[Data.NUM_OBJECTS];
        roomVisited = new byte[4]; // 32 bits = 32 rooms
        prevCommand = new byte[TOKEN_BUF_SIZE];
    }

    /**
     * Initialize game to starting state.
     */
    void initGame() {
        // Set starting room
        state[ST_ROOM] = Data.R_WEST_OF_HOUSE;
        state[ST_SCORE] = 0;
        state[ST_TURNS_HI] = 0;
        state[ST_TURNS_LO] = 0;
        state[ST_LAMP_FUEL] = (byte) 100; // 200 turns encoded as /2
        state[ST_FLAGS_HI] = 0;
        state[ST_FLAGS_LO] = 0;
        state[ST_DESC_MODE] = Data.MODE_VERBOSE;
        state[ST_INITIALIZED] = MAGIC_INITIALIZED;

        // Set initial object locations from static data
        for (byte i = 1; i <= Data.NUM_OBJECTS; i++) {
            byte initLoc = Data.getObjInitialLocation(i);
            // Convert from static encoding to runtime encoding
            // Static: room IDs as-is, LOC_INSIDE_OBJ_BASE offset
            // In the static data, "inside object N" is stored as (LOC_INSIDE_OBJ_BASE + N - 201) = N
            // We need to detect this. If initLoc > Data.NUM_ROOMS && initLoc < (byte)Data.LOC_PLAYER:
            // Actually the static data stores raw values. Let me handle the encoding:
            // If the initial location value > NUM_ROOMS, it's an "inside object" reference
            // stored as the raw byte from LOC_INSIDE_OBJ_BASE + objId - 201
            // Due to byte overflow issues, let me just map them directly
            objLoc[(short)(i - 1)] = initLoc;
            objState[(short)(i - 1)] = 0;
        }

        // Fix up "inside container" locations
        // Leaflet is inside mailbox
        objLoc[(short)(Data.O_LEAFLET - 1)] = (byte)(Data.LOC_INSIDE_OBJ_BASE + Data.O_MAILBOX - 1);
        // Lunch and garlic inside brown sack
        objLoc[(short)(Data.O_LUNCH - 1)] = (byte)(Data.LOC_INSIDE_OBJ_BASE + Data.O_BROWN_SACK - 1);
        objLoc[(short)(Data.O_GARLIC - 1)] = (byte)(Data.LOC_INSIDE_OBJ_BASE + Data.O_BROWN_SACK - 1);
        // Axe carried by troll (inside troll object)
        objLoc[(short)(Data.O_AXE - 1)] = (byte)(Data.LOC_INSIDE_OBJ_BASE + Data.O_TROLL - 1);

        // Mailbox starts closed, sack starts closed
        // (containers start closed by default - no OS_OPEN flag)

        // Clear room visited
        roomVisited[0] = 0;
        roomVisited[1] = 0;
        roomVisited[2] = 0;
        roomVisited[3] = 0;

        // Clear prev command
        Util.arrayFillNonAtomic(prevCommand, (short) 0, TOKEN_BUF_SIZE, (byte) 0);
    }

    boolean isInitialized() {
        return state[ST_INITIALIZED] == MAGIC_INITIALIZED;
    }

    // ---------------------------------------------------------------
    // Output buffer methods
    // ---------------------------------------------------------------

    void clearOutput() {
        outState[OUT_LEN] = 0;
        outState[OUT_POS] = 0;
    }

    private void emit(short stringId) {
        short len = Data.copyProse(stringId, outBuf, outState[OUT_LEN]);
        outState[OUT_LEN] += len;
    }

    private void emitNL() {
        if (outState[OUT_LEN] < OUT_BUF_SIZE) {
            outBuf[outState[OUT_LEN]] = 0x0A;
            outState[OUT_LEN]++;
        }
    }

    private void emitChar(byte c) {
        if (outState[OUT_LEN] < OUT_BUF_SIZE) {
            outBuf[outState[OUT_LEN]] = c;
            outState[OUT_LEN]++;
        }
    }

    private void emitString(byte[] str, short off, short len) {
        short space = (short)(OUT_BUF_SIZE - outState[OUT_LEN]);
        if (len > space) len = space;
        Util.arrayCopyNonAtomic(str, off, outBuf, outState[OUT_LEN], len);
        outState[OUT_LEN] += len;
    }

    private void emitNumber(short num) {
        if (num < 0) {
            emitChar((byte) '-');
            num = (short) -num;
        }
        if (num >= 100) {
            emitChar((byte) ('0' + num / 100));
            num = (short)(num % 100);
            emitChar((byte) ('0' + num / 10));
            emitChar((byte) ('0' + num % 10));
        } else if (num >= 10) {
            emitChar((byte) ('0' + num / 10));
            emitChar((byte) ('0' + num % 10));
        } else {
            emitChar((byte) ('0' + num));
        }
    }

    private void emitObjName(byte objId) {
        emit(Data.objName(objId));
    }

    /**
     * Returns how many bytes of output are available.
     */
    short outputAvailable() {
        return (short)(outState[OUT_LEN] - outState[OUT_POS]);
    }

    /**
     * Copy output to destination buffer. Returns bytes copied.
     * Advances read position.
     */
    short getOutput(byte[] dest, short destOff, short maxLen) {
        short avail = outputAvailable();
        if (avail <= 0) return 0;
        short toCopy = (avail < maxLen) ? avail : maxLen;
        Util.arrayCopyNonAtomic(outBuf, outState[OUT_POS], dest, destOff, toCopy);
        outState[OUT_POS] += toCopy;
        return toCopy;
    }

    // ---------------------------------------------------------------
    // Selection handler
    // ---------------------------------------------------------------

    void onSelect() {
        clearOutput();
        if (!isInitialized()) {
            // First ever selection - show intro
            initGame();
            emit(Data.S_INTRO);
            emitNL();
            describeRoom(true);
        } else {
            // Resuming
            emit(Data.S_RESUMING);
            emitNL();
            emitNL();
            describeRoom(false);
        }
    }

    // ---------------------------------------------------------------
    // Command processing
    // ---------------------------------------------------------------

    void processCommand(byte[] input, short offset, short length) {
        clearOutput();

        // Increment turn counter
        short turns = (short)(((state[ST_TURNS_HI] & 0xFF) << 8) | (state[ST_TURNS_LO] & 0xFF));
        turns++;
        state[ST_TURNS_HI] = (byte)(turns >> 8);
        state[ST_TURNS_LO] = (byte)(turns & 0xFF);

        // Copy input to token buffer, normalize to lowercase
        short len = (length > (short)(TOKEN_BUF_SIZE - 1)) ? (short)(TOKEN_BUF_SIZE - 1) : length;
        Util.arrayCopyNonAtomic(input, offset, tokenBuf, (short) 0, len);
        tokenBuf[len] = 0; // null terminate

        for (short i = 0; i < len; i++) {
            byte c = tokenBuf[i];
            if (c >= 'A' && c <= 'Z') {
                tokenBuf[i] = (byte)(c + 32); // lowercase
            }
        }

        // Parse and execute
        parse(tokenBuf, (short) 0, len);

        if (parseResult[PR_VALID] == 1) {
            execute();
        }

        // Per-turn housekeeping
        housekeeping();
    }

    void processLook() {
        clearOutput();
        describeRoom(true);
    }

    void processRestart() {
        clearOutput();
        initGame();
        emit(Data.S_RESTART_MSG);
        emitNL();
        emitNL();
        emit(Data.S_INTRO);
        emitNL();
        describeRoom(true);
    }

    // ---------------------------------------------------------------
    // Parser
    // ---------------------------------------------------------------

    private void parse(byte[] buf, short offset, short length) {
        // Clear parse result
        for (short i = 0; i < PARSE_SIZE; i++) parseResult[i] = 0;

        // Skip leading whitespace
        short pos = offset;
        short end = (short)(offset + length);
        while (pos < end && buf[pos] == ' ') pos++;

        if (pos >= end) {
            emit(Data.S_HUH);
            emitNL();
            return;
        }

        // Extract first word (up to 4 chars)
        byte[] word = new byte[4];
        short wordLen = extractWord(buf, pos, end, word);
        if (wordLen == 0) {
            emit(Data.S_HUH);
            emitNL();
            return;
        }
        pos = skipToNextWord(buf, (short)(pos + wordLen), end);

        // Look up first word in vocabulary
        short vocabIdx = lookupVocab(word);
        if (vocabIdx < 0) {
            emitUnknownWord(word, wordLen);
            return;
        }

        byte wordType = Data.VOCAB[(short)(vocabIdx * Data.VOCAB_ENTRY_SIZE + 4)];
        byte wordValue = Data.VOCAB[(short)(vocabIdx * Data.VOCAB_ENTRY_SIZE + 5)];

        // Direction as a bare command → GO direction
        if (wordType == Data.VT_DIRECTION) {
            parseResult[PR_VERB] = wordValue; // direction ID acts as verb
            parseResult[PR_DIR] = wordValue;
            parseResult[PR_VALID] = 1;
            return;
        }

        if (wordType != Data.VT_VERB) {
            emit(Data.S_HUH);
            emitNL();
            return;
        }

        byte verb = wordValue;
        parseResult[PR_VERB] = verb;

        // Handle verbs that take no object
        if (verb == Data.VERB_LOOK || verb == Data.VERB_INVENTORY ||
            verb == Data.VERB_SCORE || verb == Data.VERB_WAIT ||
            verb == Data.VERB_RESTART || verb == Data.VERB_QUIT ||
            verb == Data.VERB_BRIEF || verb == Data.VERB_VERBOSE ||
            verb == Data.VERB_DIAGNOSE || verb == Data.VERB_SAVE) {

            // But check if next word is a direction for "go north" etc.
            if (verb == Data.VERB_LOOK && pos < end) {
                // "look" alone is fine, but "look at X" → examine
                // For now, treat bare "look" as room description
            }
            parseResult[PR_VALID] = 1;
            return;
        }

        // Handle AGAIN
        if (verb == Data.VERB_AGAIN) {
            // Re-parse the previous command
            short prevLen = 0;
            while (prevLen < TOKEN_BUF_SIZE && prevCommand[prevLen] != 0) prevLen++;
            if (prevLen > 0) {
                parse(prevCommand, (short) 0, prevLen);
            } else {
                emit(Data.S_HUH);
                emitNL();
            }
            return;
        }

        // Parse direct object (with optional adjective)
        if (pos >= end) {
            // Some verbs need an object
            if (verb == Data.VERB_TAKE || verb == Data.VERB_DROP || verb == Data.VERB_EXAMINE ||
                verb == Data.VERB_OPEN || verb == Data.VERB_CLOSE || verb == Data.VERB_ATTACK ||
                verb == Data.VERB_READ || verb == Data.VERB_PUT || verb == Data.VERB_LIGHT ||
                verb == Data.VERB_EXTINGUISH || verb == Data.VERB_MOVE || verb == Data.VERB_CLIMB ||
                verb == Data.VERB_TURN) {
                emit(Data.S_HUH);
                emitNL();
                return;
            }
            parseResult[PR_VALID] = 1;
            return;
        }

        // Try to parse: [adjective] noun [preposition [adjective] noun]
        byte adj = Data.A_NONE;
        byte nounId = Data.N_NONE;

        // Read next word
        wordLen = extractWord(buf, pos, end, word);
        pos = skipToNextWord(buf, (short)(pos + wordLen), end);
        vocabIdx = lookupVocab(word);

        if (vocabIdx < 0) {
            emitUnknownWord(word, wordLen);
            return;
        }

        wordType = Data.VOCAB[(short)(vocabIdx * Data.VOCAB_ENTRY_SIZE + 4)];
        wordValue = Data.VOCAB[(short)(vocabIdx * Data.VOCAB_ENTRY_SIZE + 5)];

        // Check if "go <direction>"
        if (wordType == Data.VT_DIRECTION) {
            // "go north" etc.
            parseResult[PR_VERB] = wordValue;
            parseResult[PR_DIR] = wordValue;
            parseResult[PR_VALID] = 1;
            return;
        }

        if (wordType == Data.VT_ADJECTIVE) {
            adj = wordValue;
            parseResult[PR_ADJ] = adj;
            // Next word should be noun
            if (pos >= end) {
                emit(Data.S_HUH);
                emitNL();
                return;
            }
            wordLen = extractWord(buf, pos, end, word);
            pos = skipToNextWord(buf, (short)(pos + wordLen), end);
            vocabIdx = lookupVocab(word);
            if (vocabIdx < 0) {
                emitUnknownWord(word, wordLen);
                return;
            }
            wordType = Data.VOCAB[(short)(vocabIdx * Data.VOCAB_ENTRY_SIZE + 4)];
            wordValue = Data.VOCAB[(short)(vocabIdx * Data.VOCAB_ENTRY_SIZE + 5)];
        }

        if (wordType == Data.VT_NOUN) {
            nounId = wordValue;
        } else {
            emit(Data.S_HUH);
            emitNL();
            return;
        }

        // Resolve direct object
        byte dobj = resolveObject(nounId, adj);
        if (dobj == Data.O_NONE && nounId != Data.N_ALL) {
            // Resolution failed - message already emitted
            return;
        }
        parseResult[PR_DOBJ] = dobj;

        // Check for preposition + indirect object (e.g., "put X in Y")
        if (pos < end) {
            wordLen = extractWord(buf, pos, end, word);
            pos = skipToNextWord(buf, (short)(pos + wordLen), end);

            // Try preposition lookup first (handles "in" ambiguity with direction)
            vocabIdx = lookupVocabByType(word, Data.VT_PREPOSITION);
            if (vocabIdx < 0) {
                vocabIdx = lookupVocab(word);
            }

            if (vocabIdx >= 0) {
                wordType = Data.VOCAB[(short)(vocabIdx * Data.VOCAB_ENTRY_SIZE + 4)];
                wordValue = Data.VOCAB[(short)(vocabIdx * Data.VOCAB_ENTRY_SIZE + 5)];

                if (wordType == Data.VT_PREPOSITION && pos < end) {
                    parseResult[PR_PREP] = wordValue;

                    // Parse indirect object
                    byte iadj = Data.A_NONE;
                    wordLen = extractWord(buf, pos, end, word);
                    pos = skipToNextWord(buf, (short)(pos + wordLen), end);
                    vocabIdx = lookupVocab(word);

                    if (vocabIdx >= 0) {
                        wordType = Data.VOCAB[(short)(vocabIdx * Data.VOCAB_ENTRY_SIZE + 4)];
                        wordValue = Data.VOCAB[(short)(vocabIdx * Data.VOCAB_ENTRY_SIZE + 5)];

                        if (wordType == Data.VT_ADJECTIVE) {
                            iadj = wordValue;
                            parseResult[PR_IADJ] = iadj;
                            if (pos < end) {
                                wordLen = extractWord(buf, pos, end, word);
                                vocabIdx = lookupVocab(word);
                                if (vocabIdx >= 0) {
                                    wordType = Data.VOCAB[(short)(vocabIdx * Data.VOCAB_ENTRY_SIZE + 4)];
                                    wordValue = Data.VOCAB[(short)(vocabIdx * Data.VOCAB_ENTRY_SIZE + 5)];
                                }
                            }
                        }

                        if (wordType == Data.VT_NOUN) {
                            byte iobj = resolveObject(wordValue, iadj);
                            if (iobj != Data.O_NONE) {
                                parseResult[PR_IOBJ] = iobj;
                            }
                        }
                    }
                }
            }
        }

        // "turn on lamp" → LIGHT lamp
        if (verb == Data.VERB_TURN && parseResult[PR_DOBJ] != 0) {
            // Check if "on" was the object word - actually this is handled by
            // "turn on X" parsing. For now, TURN without ON/OFF is just TURN.
        }

        parseResult[PR_VALID] = 1;

        // Save command for AGAIN (but not AGAIN itself)
        Util.arrayCopyNonAtomic(tokenBuf, (short) 0, prevCommand, (short) 0, TOKEN_BUF_SIZE);
    }

    /**
     * Extract a word from buffer, truncate to 4 chars, lowercase.
     * Returns length of original word consumed from buffer.
     */
    private short extractWord(byte[] buf, short pos, short end, byte[] word) {
        // Clear word
        word[0] = ' '; word[1] = ' '; word[2] = ' '; word[3] = ' ';

        short start = pos;
        short wi = 0;
        while (pos < end && buf[pos] != ' ' && buf[pos] != 0 && buf[pos] != ',' && buf[pos] != '.') {
            if (wi < 4) {
                word[wi] = buf[pos];
            }
            wi++;
            pos++;
        }
        return (short)(pos - start);
    }

    private short skipToNextWord(byte[] buf, short pos, short end) {
        while (pos < end && (buf[pos] == ' ' || buf[pos] == ',' || buf[pos] == '.')) {
            pos++;
        }
        return pos;
    }

    /**
     * Look up a 4-char word in the vocabulary table.
     * Returns the entry index, or -1 if not found.
     */
    private short lookupVocab(byte[] word) {
        short numEntries = Data.getVocabSize();
        for (short i = 0; i < numEntries; i++) {
            short base = (short)(i * Data.VOCAB_ENTRY_SIZE);
            if (Data.VOCAB[base] == word[0] &&
                Data.VOCAB[(short)(base + 1)] == word[1] &&
                Data.VOCAB[(short)(base + 2)] == word[2] &&
                Data.VOCAB[(short)(base + 3)] == word[3]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Look up a word with type filter.
     */
    private short lookupVocabByType(byte[] word, byte typeFilter) {
        short numEntries = Data.getVocabSize();
        for (short i = 0; i < numEntries; i++) {
            short base = (short)(i * Data.VOCAB_ENTRY_SIZE);
            if (Data.VOCAB[(short)(base + 4)] == typeFilter &&
                Data.VOCAB[base] == word[0] &&
                Data.VOCAB[(short)(base + 1)] == word[1] &&
                Data.VOCAB[(short)(base + 2)] == word[2] &&
                Data.VOCAB[(short)(base + 3)] == word[3]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Resolve a noun ID + adjective to an object ID.
     * Checks visibility (in current room, in inventory, in open containers).
     * Emits error message if unresolvable.
     */
    private byte resolveObject(byte nounId, byte adjId) {
        byte room = state[ST_ROOM];
        byte match = Data.O_NONE;
        byte matchCount = 0;
        byte secondMatch = Data.O_NONE;

        for (byte i = 1; i <= Data.NUM_OBJECTS; i++) {
            if (Data.getObjNoun(i) != nounId) continue;

            // Check adjective filter
            if (adjId != Data.A_NONE && Data.getObjAdjective(i) != adjId) continue;

            // Check visibility
            if (!isObjectVisible(i, room)) continue;

            matchCount++;
            if (matchCount == 1) {
                match = i;
            } else if (matchCount == 2) {
                secondMatch = i;
            }
        }

        if (matchCount == 0) {
            emit(Data.S_CANT_SEE);
            emitNL();
            return Data.O_NONE;
        }

        if (matchCount == 1) {
            return match;
        }

        // Ambiguous - ask for clarification
        emit(Data.S_WHICH_DO_YOU_MEAN);
        emitObjName(match);
        emitString(new byte[]{' ', 'o', 'r', ' ', 't', 'h', 'e', ' '}, (short) 0, (short) 8);
        emitObjName(secondMatch);
        emitChar((byte) '?');
        emitNL();
        return Data.O_NONE;
    }

    /**
     * Check if an object is visible to the player.
     * Visible means: in current room, in inventory, or inside an open container
     * that is itself visible.
     */
    private boolean isObjectVisible(byte objId, byte room) {
        if (objId < 1 || objId > Data.NUM_OBJECTS) return false;

        // Check if hidden
        if ((objState[(short)(objId - 1)] & Data.OS_HIDDEN) != 0) return false;

        // Check if invisible and not yet revealed
        if (Data.objHasFlag(objId, Data.OF_INVISIBLE)) {
            // Trap door: visible only if rug moved
            if (objId == Data.O_TRAP_DOOR && !hasGameFlag(Data.GF_RUG_MOVED)) return false;
        }

        byte loc = objLoc[(short)(objId - 1)];

        // In current room
        if (loc == room) return true;

        // In inventory
        if (loc == (byte) Data.LOC_PLAYER) return true;

        // Inside a container that is visible and open
        if ((loc & 0xFF) >= (Data.LOC_INSIDE_OBJ_BASE & 0xFF)) {
            byte parentId = (byte)((loc & 0xFF) - (Data.LOC_INSIDE_OBJ_BASE & 0xFF) + 1);
            if (isObjectVisible(parentId, room)) {
                // If parent is a container, check if it's open
                if (Data.objHasFlag(parentId, Data.OF_CONTAINER)) {
                    return (objState[(short)(parentId - 1)] & Data.OS_OPEN) != 0;
                }
                // If parent is a surface, always visible
                if (Data.objHasFlag(parentId, Data.OF_SURFACE)) return true;
            }
        }

        return false;
    }

    private boolean isObjectInRoom(byte objId, byte room) {
        if (objId < 1 || objId > Data.NUM_OBJECTS) return false;
        return objLoc[(short)(objId - 1)] == room;
    }

    private boolean isObjectHeld(byte objId) {
        if (objId < 1 || objId > Data.NUM_OBJECTS) return false;
        return objLoc[(short)(objId - 1)] == (byte) Data.LOC_PLAYER;
    }

    private void emitUnknownWord(byte[] word, short wordLen) {
        emit(Data.S_UNKNOWN_WORD);
        for (short i = 0; i < 4 && word[i] != ' '; i++) {
            emitChar(word[i]);
        }
        emitChar((byte) '"');
        emitChar((byte) '.');
        emitNL();
    }

    // ---------------------------------------------------------------
    // World engine - command execution
    // ---------------------------------------------------------------

    private void execute() {
        short verb = parseResult[PR_VERB];
        byte dobj = (byte) parseResult[PR_DOBJ];

        // Directions (verb IDs 0-11 are directions)
        if (verb >= 0 && verb < Data.NUM_ALL_DIRS) {
            handleGo((byte) verb);
            return;
        }

        // Check if in dark room
        if (!isCurrentRoomLit() && verb != Data.VERB_LIGHT) {
            // Allow some verbs in the dark
            if (verb != Data.VERB_INVENTORY && verb != Data.VERB_SCORE &&
                verb != Data.VERB_QUIT && verb != Data.VERB_RESTART &&
                verb != Data.VERB_BRIEF && verb != Data.VERB_VERBOSE &&
                verb != Data.VERB_WAIT && verb != Data.VERB_SAVE &&
                verb != Data.VERB_DIAGNOSE) {
                emit(Data.S_DARK);
                emitNL();
                return;
            }
        }

        switch (verb) {
            case Data.VERB_LOOK:
                describeRoom(true);
                break;
            case Data.VERB_EXAMINE:
                handleExamine(dobj);
                break;
            case Data.VERB_TAKE:
                handleTake(dobj);
                break;
            case Data.VERB_DROP:
                handleDrop(dobj);
                break;
            case Data.VERB_INVENTORY:
                handleInventory();
                break;
            case Data.VERB_OPEN:
                handleOpen(dobj);
                break;
            case Data.VERB_CLOSE:
                handleClose(dobj);
                break;
            case Data.VERB_ATTACK:
                handleAttack(dobj);
                break;
            case Data.VERB_LIGHT:
                handleLight(dobj);
                break;
            case Data.VERB_EXTINGUISH:
                handleExtinguish(dobj);
                break;
            case Data.VERB_READ:
                handleRead(dobj);
                break;
            case Data.VERB_PUT:
                handlePut(dobj, (byte) parseResult[PR_IOBJ]);
                break;
            case Data.VERB_SCORE:
                handleScore();
                break;
            case Data.VERB_WAIT:
                emit(Data.S_TIME_PASSES);
                emitNL();
                break;
            case Data.VERB_RESTART:
                processRestart();
                break;
            case Data.VERB_BRIEF:
                state[ST_DESC_MODE] = Data.MODE_BRIEF;
                emit(Data.S_BRIEF_MODE);
                emitNL();
                break;
            case Data.VERB_VERBOSE:
                state[ST_DESC_MODE] = Data.MODE_VERBOSE;
                emit(Data.S_VERBOSE_MODE);
                emitNL();
                break;
            case Data.VERB_SAVE:
                emit(Data.S_SAVE_MSG);
                emitNL();
                break;
            case Data.VERB_MOVE:
                handleMove(dobj);
                break;
            case Data.VERB_CLIMB:
                handleClimb(dobj);
                break;
            case Data.VERB_DIAGNOSE:
                handleDiagnose();
                break;
            default:
                emit(Data.S_HUH);
                emitNL();
                break;
        }
    }

    // ---------------------------------------------------------------
    // Action handlers
    // ---------------------------------------------------------------

    private void handleGo(byte direction) {
        byte room = state[ST_ROOM];

        // Special movement cases
        byte dest = Data.R_NONE;

        // Window entry/exit
        if (room == Data.R_BEHIND_HOUSE && (direction == Data.DIR_WEST || direction == Data.DIR_IN)) {
            if (hasGameFlag(Data.GF_WINDOW_OPEN)) {
                dest = Data.R_KITCHEN;
            } else {
                emitString(new byte[]{'T','h','e',' ','w','i','n','d','o','w',' ','i','s',' ','c','l','o','s','e','d','.'}, (short)0, (short)21);
                emitNL();
                return;
            }
        } else if (room == Data.R_KITCHEN && (direction == Data.DIR_WEST || direction == Data.DIR_OUT)) {
            if (hasGameFlag(Data.GF_WINDOW_OPEN)) {
                dest = Data.R_BEHIND_HOUSE;
            } else {
                emitString(new byte[]{'T','h','e',' ','w','i','n','d','o','w',' ','i','s',' ','c','l','o','s','e','d','.'}, (short)0, (short)21);
                emitNL();
                return;
            }
        }
        // Trap door
        else if (room == Data.R_LIVING_ROOM && direction == Data.DIR_DOWN) {
            if (hasGameFlag(Data.GF_TRAP_OPEN)) {
                dest = Data.R_CELLAR;
            } else {
                emitString(new byte[]{'Y','o','u',' ','c','a','n','\'','t',' ','g','o',' ','t','h','a','t',' ','w','a','y','.'}, (short)0, (short)22);
                emitNL();
                return;
            }
        } else if (room == Data.R_CELLAR && direction == Data.DIR_UP) {
            if (hasGameFlag(Data.GF_TRAP_OPEN)) {
                dest = Data.R_LIVING_ROOM;
            } else {
                emitString(new byte[]{'T','h','e',' ','t','r','a','p',' ','d','o','o','r',' ','i','s',' ','c','l','o','s','e','d','.'}, (short)0, (short)24);
                emitNL();
                return;
            }
        }
        // Troll blocks east
        else if (room == Data.R_TROLL_ROOM && direction == Data.DIR_EAST) {
            if (!hasGameFlag(Data.GF_TROLL_DEAD)) {
                emit(Data.S_TROLL_BLOCKS);
                emitNL();
                return;
            }
            dest = Data.R_EAST_OF_CHASM;
        }
        // Grating
        else if (room == Data.R_GRATING_CLEARING && direction == Data.DIR_DOWN) {
            if (hasGameFlag(Data.GF_GRATING_OPEN)) {
                dest = Data.R_GRATING_ROOM;
            } else {
                emit(Data.S_GRATING_LOCKED);
                emitNL();
                return;
            }
        } else if (room == Data.R_GRATING_ROOM && direction == Data.DIR_UP) {
            if (hasGameFlag(Data.GF_GRATING_OPEN)) {
                dest = Data.R_GRATING_CLEARING;
            } else {
                emit(Data.S_GRATING_LOCKED);
                emitNL();
                return;
            }
        }

        // Standard exit lookup
        if (dest == Data.R_NONE) {
            if (direction < Data.NUM_BASIC_DIRS) {
                dest = Data.getRoomExit(room, direction);
            } else if (direction == Data.DIR_UP) {
                dest = Data.getRoomExit(room, (byte) 8);
            } else if (direction == Data.DIR_DOWN) {
                dest = Data.getRoomExit(room, (byte) 9);
            } else if (direction == Data.DIR_IN) {
                // Try various entrances
                dest = Data.R_NONE;
            } else if (direction == Data.DIR_OUT) {
                dest = Data.R_NONE;
            }
        }

        if (dest == Data.R_NONE) {
            emit(Data.S_CANT_GO);
            emitNL();
            return;
        }

        // Check darkness in destination
        byte oldRoom = room;
        state[ST_ROOM] = dest;

        // Special: entering cellar first time closes trap door
        if (dest == Data.R_CELLAR && oldRoom == Data.R_LIVING_ROOM) {
            if (!hasGameFlag(Data.GF_CELLAR_VISITED)) {
                setGameFlag(Data.GF_CELLAR_VISITED);
                clearGameFlag(Data.GF_TRAP_OPEN);
                objState[(short)(Data.O_TRAP_DOOR - 1)] &= ~Data.OS_OPEN;
                emit(Data.S_TRAP_CLOSED_BEHIND);
                emitNL();
                emitNL();
            }
        }

        // Describe new room
        describeRoom(false);
    }

    private void handleTake(byte objId) {
        if (objId == Data.O_NONE) return;

        // Check if takeable
        if (!Data.objHasFlag(objId, Data.OF_TAKEABLE)) {
            emit(Data.S_CANT_TAKE);
            emitNL();
            return;
        }

        // Check if already held
        if (isObjectHeld(objId)) {
            emit(Data.S_ALREADY_HAVE);
            emitNL();
            return;
        }

        // Check inventory capacity
        short invCount = countInventory();
        if (invCount >= Data.MAX_INVENTORY) {
            emit(Data.S_HANDS_FULL);
            emitNL();
            return;
        }

        // Move to inventory
        objLoc[(short)(objId - 1)] = (byte) Data.LOC_PLAYER;
        objState[(short)(objId - 1)] |= Data.OS_MOVED;
        emit(Data.S_TAKEN);
        emitNL();
    }

    private void handleDrop(byte objId) {
        if (objId == Data.O_NONE) return;

        if (!isObjectHeld(objId)) {
            emit(Data.S_DONT_HAVE);
            emitNL();
            return;
        }

        objLoc[(short)(objId - 1)] = state[ST_ROOM];
        emit(Data.S_DROPPED);
        emitNL();
    }

    private void handleExamine(byte objId) {
        if (objId == Data.O_NONE) return;

        short descId = Data.objDesc(objId);
        short len = Data.proseLength(descId);
        if (len > 0) {
            emit(descId);
            emitNL();
        } else {
            emit(Data.S_NOTHING_SPECIAL);
            emitNL();
        }

        // If container and open, list contents
        if (Data.objHasFlag(objId, Data.OF_CONTAINER) && (objState[(short)(objId - 1)] & Data.OS_OPEN) != 0) {
            listContainerContents(objId);
        }
    }

    private void handleInventory() {
        short count = 0;
        for (byte i = 1; i <= Data.NUM_OBJECTS; i++) {
            if (isObjectHeld(i)) count++;
        }

        if (count == 0) {
            emit(Data.S_EMPTY_HANDED);
            emitNL();
            return;
        }

        emit(Data.S_CARRYING);
        emitNL();
        for (byte i = 1; i <= Data.NUM_OBJECTS; i++) {
            if (isObjectHeld(i)) {
                emitString(new byte[]{' ', ' '}, (short) 0, (short) 2);
                emitObjName(i);
                emitNL();
            }
        }
    }

    private void handleOpen(byte objId) {
        if (objId == Data.O_NONE) return;

        // Special handlers
        byte handler = Data.getObjHandler(objId);
        if (handler == Data.SH_WINDOW) {
            if (hasGameFlag(Data.GF_WINDOW_OPEN)) {
                emit(Data.S_ALREADY_OPEN);
            } else {
                setGameFlag(Data.GF_WINDOW_OPEN);
                objState[(short)(Data.O_KITCHEN_WINDOW - 1)] |= Data.OS_OPEN;
                emit(Data.S_WINDOW_OPEN);
            }
            emitNL();
            return;
        }

        if (handler == Data.SH_GRATING) {
            if (hasGameFlag(Data.GF_GRATING_OPEN)) {
                emit(Data.S_ALREADY_OPEN);
            } else if (isObjectHeld(Data.O_SKELETON_KEY)) {
                setGameFlag(Data.GF_GRATING_OPEN);
                objState[(short)(Data.O_GRATING - 1)] |= Data.OS_OPEN;
                emit(Data.S_OPENED);
            } else {
                emit(Data.S_GRATING_LOCKED);
            }
            emitNL();
            return;
        }

        if (handler == Data.SH_TRAP_DOOR) {
            if (hasGameFlag(Data.GF_TRAP_OPEN)) {
                emit(Data.S_ALREADY_OPEN);
            } else {
                if (!hasGameFlag(Data.GF_RUG_MOVED)) {
                    emit(Data.S_CANT_SEE);
                } else {
                    setGameFlag(Data.GF_TRAP_OPEN);
                    objState[(short)(Data.O_TRAP_DOOR - 1)] |= Data.OS_OPEN;
                    emit(Data.S_OPENED);
                }
            }
            emitNL();
            return;
        }

        if (!Data.objHasFlag(objId, Data.OF_OPENABLE) && !Data.objHasFlag(objId, Data.OF_CONTAINER)) {
            emit(Data.S_HUH);
            emitNL();
            return;
        }

        if ((objState[(short)(objId - 1)] & Data.OS_OPEN) != 0) {
            emit(Data.S_ALREADY_OPEN);
            emitNL();
            return;
        }

        objState[(short)(objId - 1)] |= Data.OS_OPEN;
        emit(Data.S_OPENED);
        emitNL();

        // List contents of container
        if (Data.objHasFlag(objId, Data.OF_CONTAINER)) {
            listContainerContents(objId);
        }
    }

    private void handleClose(byte objId) {
        if (objId == Data.O_NONE) return;

        // Special: window
        if (objId == Data.O_KITCHEN_WINDOW) {
            if (!hasGameFlag(Data.GF_WINDOW_OPEN)) {
                emit(Data.S_ALREADY_CLOSED);
            } else {
                clearGameFlag(Data.GF_WINDOW_OPEN);
                objState[(short)(Data.O_KITCHEN_WINDOW - 1)] &= ~Data.OS_OPEN;
                emit(Data.S_WINDOW_CLOSED);
            }
            emitNL();
            return;
        }

        if (!Data.objHasFlag(objId, Data.OF_OPENABLE) && !Data.objHasFlag(objId, Data.OF_CONTAINER)) {
            emit(Data.S_NOT_CLOSEABLE);
            emitNL();
            return;
        }

        if ((objState[(short)(objId - 1)] & Data.OS_OPEN) == 0) {
            emit(Data.S_ALREADY_CLOSED);
            emitNL();
            return;
        }

        objState[(short)(objId - 1)] &= ~Data.OS_OPEN;
        emit(Data.S_CLOSED);
        emitNL();
    }

    private void handleAttack(byte objId) {
        if (objId == Data.O_NONE) return;

        if (!Data.objHasFlag(objId, Data.OF_ACTOR)) {
            emit(Data.S_CANT_ATTACK);
            emitNL();
            return;
        }

        // Troll combat
        if (objId == Data.O_TROLL) {
            // Check if holding a weapon
            boolean hasWeapon = false;
            for (byte i = 1; i <= Data.NUM_OBJECTS; i++) {
                if (isObjectHeld(i) && Data.objHasFlag(i, Data.OF_WEAPON)) {
                    hasWeapon = true;
                    break;
                }
            }

            if (hasWeapon) {
                // Kill the troll
                setGameFlag(Data.GF_TROLL_DEAD);
                objState[(short)(Data.O_TROLL - 1)] |= Data.OS_DEAD;
                objLoc[(short)(Data.O_TROLL - 1)] = (byte) Data.LOC_NOWHERE;
                // Drop the axe in the room
                objLoc[(short)(Data.O_AXE - 1)] = state[ST_ROOM];
                emit(Data.S_TROLL_DIES);
                emitNL();
                // Award points
                addScore((byte) 8);
            } else {
                emitString(new byte[]{
                    'T','h','e',' ','t','r','o','l','l',' ','l','a','u','g','h','s',
                    ' ','a','t',' ','y','o','u','r',' ','f','e','e','b','l','e',
                    ' ','a','t','t','e','m','p','t','.'
                }, (short) 0, (short) 39);
                emitNL();
            }
            return;
        }

        emit(Data.S_CANT_ATTACK);
        emitNL();
    }

    private void handleLight(byte objId) {
        if (objId == Data.O_NONE) {
            // If holding lamp, light it
            if (isObjectHeld(Data.O_LAMP)) {
                objId = Data.O_LAMP;
            } else {
                emit(Data.S_HUH);
                emitNL();
                return;
            }
        }

        if (objId == Data.O_LAMP) {
            if ((objState[(short)(Data.O_LAMP - 1)] & Data.OS_ON) != 0) {
                emitString(new byte[]{'I','t',' ','i','s',' ','a','l','r','e','a','d','y',' ','o','n','.'}, (short)0, (short)17);
                emitNL();
            } else if (state[ST_LAMP_FUEL] <= 0) {
                emit(Data.S_LAMP_DEAD);
                emitNL();
            } else {
                objState[(short)(Data.O_LAMP - 1)] |= Data.OS_ON;
                emit(Data.S_LAMP_ON);
                emitNL();
                // If we just illuminated a dark room, describe it
                if (!Data.isRoomLit(state[ST_ROOM])) {
                    describeRoom(true);
                }
            }
            return;
        }

        emit(Data.S_NOTHING_SPECIAL);
        emitNL();
    }

    private void handleExtinguish(byte objId) {
        if (objId == Data.O_NONE) {
            if (isObjectHeld(Data.O_LAMP)) objId = Data.O_LAMP;
        }

        if (objId == Data.O_LAMP) {
            if ((objState[(short)(Data.O_LAMP - 1)] & Data.OS_ON) == 0) {
                emitString(new byte[]{'I','t',' ','i','s',' ','a','l','r','e','a','d','y',' ','o','f','f','.'}, (short)0, (short)18);
                emitNL();
            } else {
                objState[(short)(Data.O_LAMP - 1)] &= ~Data.OS_ON;
                emit(Data.S_LAMP_OFF);
                emitNL();
            }
            return;
        }

        emit(Data.S_NOTHING_SPECIAL);
        emitNL();
    }

    private void handleRead(byte objId) {
        if (objId == Data.O_NONE) return;

        if (!Data.objHasFlag(objId, Data.OF_READABLE)) {
            emit(Data.S_NOT_READABLE);
            emitNL();
            return;
        }

        // For readable objects, the examine description IS the read text
        emit(Data.objDesc(objId));
        emitNL();
    }

    private void handlePut(byte dobj, byte iobj) {
        if (dobj == Data.O_NONE) return;

        if (!isObjectHeld(dobj)) {
            emit(Data.S_DONT_HAVE);
            emitNL();
            return;
        }

        if (iobj == Data.O_NONE) {
            emit(Data.S_HUH);
            emitNL();
            return;
        }

        // Check if target is a container or surface
        if (!Data.objHasFlag(iobj, Data.OF_CONTAINER) && !Data.objHasFlag(iobj, Data.OF_SURFACE)) {
            emit(Data.S_NOT_CONTAINER);
            emitNL();
            return;
        }

        // Container must be open
        if (Data.objHasFlag(iobj, Data.OF_CONTAINER) && (objState[(short)(iobj - 1)] & Data.OS_OPEN) == 0) {
            emit(Data.S_NOT_OPEN);
            emitNL();
            return;
        }

        // Place object inside container
        objLoc[(short)(dobj - 1)] = (byte)((Data.LOC_INSIDE_OBJ_BASE & 0xFF) + iobj - 1);

        // Trophy case scoring
        if (iobj == Data.O_TROPHY_CASE && Data.objHasFlag(dobj, Data.OF_TREASURE)) {
            byte pts = Data.getTreasureValue(dobj);
            addScore(pts);
        }

        emit(Data.S_PUT_IN);
        emitNL();

        // Check victory
        checkVictory();
    }

    private void handleScore() {
        emit(Data.S_SCORE_MSG);
        emitNumber((short)(state[ST_SCORE] & 0xFF));
        emitString(new byte[]{' ','o','u','t',' ','o','f',' '}, (short) 0, (short) 8);
        emitNumber((short) Data.MAX_SCORE);
        emitString(new byte[]{',',' ','i','n',' '}, (short) 0, (short) 5);
        short turns = (short)(((state[ST_TURNS_HI] & 0xFF) << 8) | (state[ST_TURNS_LO] & 0xFF));
        emitNumber(turns);
        emitString(new byte[]{' ','t','u','r','n','s','.'}, (short) 0, (short) 7);
        emitNL();
    }

    private void handleMove(byte objId) {
        if (objId == Data.O_NONE) return;

        // Special: move rug reveals trap door
        if (objId == Data.O_RUG) {
            if (!hasGameFlag(Data.GF_RUG_MOVED)) {
                setGameFlag(Data.GF_RUG_MOVED);
                emit(Data.S_RUG_MOVED);
                emitNL();
            } else {
                emit(Data.S_NOTHING_SPECIAL);
                emitNL();
            }
            return;
        }

        if (Data.objHasFlag(objId, Data.OF_TAKEABLE)) {
            emitString(new byte[]{'Y','o','u',' ','c','a','n','\'','t',' ','m','o','v','e',' ','t','h','a','t','.'}, (short)0, (short)20);
        } else {
            emit(Data.S_NOTHING_SPECIAL);
        }
        emitNL();
    }

    private void handleClimb(byte objId) {
        byte room = state[ST_ROOM];

        // Climb tree
        if (room == Data.R_FOREST_PATH || room == Data.R_FOREST_EAST) {
            handleGo(Data.DIR_UP);
            return;
        }

        // Climb down from tree
        if (room == Data.R_UP_A_TREE) {
            handleGo(Data.DIR_DOWN);
            return;
        }

        emit(Data.S_NOTHING_SPECIAL);
        emitNL();
    }

    private void handleDiagnose() {
        short turns = (short)(((state[ST_TURNS_HI] & 0xFF) << 8) | (state[ST_TURNS_LO] & 0xFF));
        emitString(new byte[]{'T','u','r','n','s',':',' '}, (short)0, (short)7);
        emitNumber(turns);
        emitNL();
        emitString(new byte[]{'S','c','o','r','e',':',' '}, (short)0, (short)7);
        emitNumber((short)(state[ST_SCORE] & 0xFF));
        emitNL();
        short fuel = (short)((state[ST_LAMP_FUEL] & 0xFF) * 2);
        emitString(new byte[]{'L','a','m','p',':',' '}, (short)0, (short)6);
        emitNumber(fuel);
        emitString(new byte[]{' ','t','u','r','n','s',' ','o','f',' ','f','u','e','l'}, (short)0, (short)14);
        emitNL();
    }

    // ---------------------------------------------------------------
    // Room description
    // ---------------------------------------------------------------

    private void describeRoom(boolean forceLong) {
        byte room = state[ST_ROOM];

        if (!isCurrentRoomLit()) {
            emit(Data.S_DARK);
            emitNL();
            return;
        }

        // Room name (always shown)
        emit(Data.roomName(room));
        emitNL();

        // Long description: show if verbose mode, or first visit, or forced
        boolean visited = isRoomVisited(room);
        boolean showLong = forceLong ||
            state[ST_DESC_MODE] == Data.MODE_VERBOSE ||
            !visited;

        if (showLong && state[ST_DESC_MODE] != Data.MODE_SUPERBRIEF) {
            emit(Data.roomLongDesc(room));
            emitNL();
        }

        // Mark as visited
        setRoomVisited(room);

        // List objects in room
        for (byte i = 1; i <= Data.NUM_OBJECTS; i++) {
            if (objLoc[(short)(i - 1)] == room) {
                // Skip invisible objects
                if (Data.objHasFlag(i, Data.OF_INVISIBLE) && !isObjectRevealed(i)) continue;
                // Skip actors that are dead
                if (Data.objHasFlag(i, Data.OF_ACTOR) && (objState[(short)(i - 1)] & Data.OS_DEAD) != 0) continue;

                if (Data.objHasFlag(i, Data.OF_ACTOR)) {
                    emitString(new byte[]{'A',' '}, (short) 0, (short) 2);
                    emitObjName(i);
                    emitString(new byte[]{' ','i','s',' ','h','e','r','e','.'}, (short)0, (short)9);
                } else {
                    emitString(new byte[]{'T','h','e','r','e',' ','i','s',' ','a',' '}, (short) 0, (short) 11);
                    emitObjName(i);
                    emitString(new byte[]{' ','h','e','r','e','.'}, (short) 0, (short) 6);
                }
                emitNL();
            }
        }
    }

    // ---------------------------------------------------------------
    // Per-turn housekeeping
    // ---------------------------------------------------------------

    private void housekeeping() {
        // Lamp fuel
        if ((objState[(short)(Data.O_LAMP - 1)] & Data.OS_ON) != 0) {
            if (state[ST_LAMP_FUEL] > 0) {
                state[ST_LAMP_FUEL]--;

                if (state[ST_LAMP_FUEL] == 10) {
                    emit(Data.S_LAMP_GETTING_DIM);
                    emitNL();
                }
                if (state[ST_LAMP_FUEL] == 0) {
                    objState[(short)(Data.O_LAMP - 1)] &= ~Data.OS_ON;
                    emit(Data.S_LAMP_DEAD);
                    emitNL();
                }
            }
        }
    }

    // ---------------------------------------------------------------
    // Helper methods
    // ---------------------------------------------------------------

    private boolean isCurrentRoomLit() {
        byte room = state[ST_ROOM];
        // Room is naturally lit
        if (Data.isRoomLit(room)) return true;
        // Player has a lit light source
        if (isObjectHeld(Data.O_LAMP) && (objState[(short)(Data.O_LAMP - 1)] & Data.OS_ON) != 0) return true;
        if (isObjectHeld(Data.O_TORCH) && Data.objHasFlag(Data.O_TORCH, Data.OF_LIGHT)) return true;
        // Lamp or torch in the room
        if (isObjectInRoom(Data.O_LAMP, room) && (objState[(short)(Data.O_LAMP - 1)] & Data.OS_ON) != 0) return true;
        if (isObjectInRoom(Data.O_TORCH, room)) return true; // torch is always lit
        return false;
    }

    private boolean hasGameFlag(short flag) {
        short flags = (short)(((state[ST_FLAGS_HI] & 0xFF) << 8) | (state[ST_FLAGS_LO] & 0xFF));
        return (flags & flag) != 0;
    }

    private void setGameFlag(short flag) {
        short flags = (short)(((state[ST_FLAGS_HI] & 0xFF) << 8) | (state[ST_FLAGS_LO] & 0xFF));
        flags |= flag;
        state[ST_FLAGS_HI] = (byte)(flags >> 8);
        state[ST_FLAGS_LO] = (byte)(flags & 0xFF);
    }

    private void clearGameFlag(short flag) {
        short flags = (short)(((state[ST_FLAGS_HI] & 0xFF) << 8) | (state[ST_FLAGS_LO] & 0xFF));
        flags &= ~flag;
        state[ST_FLAGS_HI] = (byte)(flags >> 8);
        state[ST_FLAGS_LO] = (byte)(flags & 0xFF);
    }

    private boolean isRoomVisited(byte roomId) {
        if (roomId < 1 || roomId > 32) return false;
        short byteIdx = (short)((roomId - 1) / 8);
        short bitMask = (short)(1 << ((roomId - 1) % 8));
        return (roomVisited[byteIdx] & bitMask) != 0;
    }

    private void setRoomVisited(byte roomId) {
        if (roomId < 1 || roomId > 32) return;
        short byteIdx = (short)((roomId - 1) / 8);
        roomVisited[byteIdx] |= (byte)(1 << ((roomId - 1) % 8));
    }

    private short countInventory() {
        short count = 0;
        for (byte i = 1; i <= Data.NUM_OBJECTS; i++) {
            if (objLoc[(short)(i - 1)] == (byte) Data.LOC_PLAYER) count++;
        }
        return count;
    }

    private void addScore(byte points) {
        short current = (short)(state[ST_SCORE] & 0xFF);
        current += points;
        if (current > Data.MAX_SCORE) current = Data.MAX_SCORE;
        state[ST_SCORE] = (byte) current;
    }

    private void listContainerContents(byte containerId) {
        byte locCode = (byte)((Data.LOC_INSIDE_OBJ_BASE & 0xFF) + containerId - 1);
        boolean found = false;
        for (byte i = 1; i <= Data.NUM_OBJECTS; i++) {
            if (objLoc[(short)(i - 1)] == locCode) {
                if (!found) {
                    emitString(new byte[]{'T','h','e',' '}, (short)0, (short)4);
                    emitObjName(containerId);
                    emitString(new byte[]{' ','c','o','n','t','a','i','n','s',':'}, (short)0, (short)10);
                    emitNL();
                    found = true;
                }
                emitString(new byte[]{' ', ' '}, (short) 0, (short) 2);
                emitObjName(i);
                emitNL();
            }
        }
    }

    private boolean isObjectRevealed(byte objId) {
        if (objId == Data.O_TRAP_DOOR) return hasGameFlag(Data.GF_RUG_MOVED);
        return true;
    }

    private void checkVictory() {
        // Count treasures in trophy case
        byte locCode = (byte)((Data.LOC_INSIDE_OBJ_BASE & 0xFF) + Data.O_TROPHY_CASE - 1);
        short treasureCount = 0;
        for (byte i = 1; i <= Data.NUM_OBJECTS; i++) {
            if (objLoc[(short)(i - 1)] == locCode && Data.objHasFlag(i, Data.OF_TREASURE)) {
                treasureCount++;
            }
        }

        // All 7 treasures collected
        if (treasureCount >= 7) {
            emitNL();
            emit(Data.S_VICTORY);
            emitNL();
            setGameFlag(Data.GF_GAME_OVER);
        }
    }
}
