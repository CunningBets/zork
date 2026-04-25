package dev.cunningbets.zork;

/**
 * Static game data for Mini-Zork I.
 * All constants, prose text, room/object/vocabulary tables.
 *
 * NOTE: String[] usage is for simulator development. For real JavaCard
 * deployment, convert to flat byte[] with Huffman compression.
 */
public class Data {

    // ---------------------------------------------------------------
    // APDU Constants
    // ---------------------------------------------------------------
    public static final byte CLA_ZORK = (byte) 0x80;
    public static final byte INS_COMMAND = (byte) 0x10;
    public static final byte INS_LOOK = (byte) 0x12;
    public static final byte INS_RESTART = (byte) 0x14;
    public static final byte INS_GET_RESPONSE = (byte) 0xC0;

    // ---------------------------------------------------------------
    // Direction Indices (0-11) - also used as verb IDs for movement
    // ---------------------------------------------------------------
    public static final byte DIR_NORTH = 0;
    public static final byte DIR_SOUTH = 1;
    public static final byte DIR_EAST = 2;
    public static final byte DIR_WEST = 3;
    public static final byte DIR_NORTHEAST = 4;
    public static final byte DIR_NORTHWEST = 5;
    public static final byte DIR_SOUTHEAST = 6;
    public static final byte DIR_SOUTHWEST = 7;
    public static final byte DIR_UP = 8;
    public static final byte DIR_DOWN = 9;
    public static final byte DIR_IN = 10;
    public static final byte DIR_OUT = 11;
    public static final byte NUM_BASIC_DIRS = 8;  // N,S,E,W,NE,NW,SE,SW stored in room record
    public static final byte NUM_ALL_DIRS = 12;

    // ---------------------------------------------------------------
    // Verb IDs (directions are 0-11, verbs start at 12)
    // ---------------------------------------------------------------
    public static final byte VERB_LOOK = 12;
    public static final byte VERB_EXAMINE = 13;
    public static final byte VERB_TAKE = 14;
    public static final byte VERB_DROP = 15;
    public static final byte VERB_INVENTORY = 16;
    public static final byte VERB_OPEN = 17;
    public static final byte VERB_CLOSE = 18;
    public static final byte VERB_ATTACK = 19;
    public static final byte VERB_LIGHT = 20;
    public static final byte VERB_EXTINGUISH = 21;
    public static final byte VERB_READ = 22;
    public static final byte VERB_PUT = 23;
    public static final byte VERB_SCORE = 24;
    public static final byte VERB_WAIT = 25;
    public static final byte VERB_AGAIN = 26;
    public static final byte VERB_RESTART = 27;
    public static final byte VERB_QUIT = 28;
    public static final byte VERB_BRIEF = 29;
    public static final byte VERB_VERBOSE = 30;
    public static final byte VERB_MOVE = 31;
    public static final byte VERB_CLIMB = 32;
    public static final byte VERB_ENTER = 33;
    public static final byte VERB_EXIT = 34;
    public static final byte VERB_DIAGNOSE = 35;
    public static final byte VERB_SAVE = 36;
    public static final byte VERB_RESTORE = 37;
    public static final byte VERB_TURN = 38;
    public static final byte VERB_TIE = 39;

    // ---------------------------------------------------------------
    // Room IDs (1-based, 0 = no room)
    // ---------------------------------------------------------------
    public static final byte R_NONE = 0;
    // Surface
    public static final byte R_WEST_OF_HOUSE = 1;
    public static final byte R_NORTH_OF_HOUSE = 2;
    public static final byte R_SOUTH_OF_HOUSE = 3;
    public static final byte R_BEHIND_HOUSE = 4;
    public static final byte R_KITCHEN = 5;
    public static final byte R_LIVING_ROOM = 6;
    public static final byte R_ATTIC = 7;
    public static final byte R_FOREST_PATH = 8;
    public static final byte R_FOREST_WEST = 9;
    public static final byte R_FOREST_EAST = 10;
    public static final byte R_FOREST_SOUTH = 11;
    public static final byte R_CLEARING = 12;
    public static final byte R_UP_A_TREE = 13;
    public static final byte R_GRATING_CLEARING = 14;
    // Underground
    public static final byte R_CELLAR = 15;
    public static final byte R_TROLL_ROOM = 16;
    public static final byte R_EAST_OF_CHASM = 17;
    public static final byte R_GALLERY = 18;
    public static final byte R_NS_PASSAGE = 19;
    public static final byte R_ROUND_ROOM = 20;
    public static final byte R_NARROW_PASSAGE = 21;
    public static final byte R_TREASURE_ROOM = 22;
    public static final byte R_LOUD_ROOM = 23;
    public static final byte R_MAZE_1 = 24;
    public static final byte R_MAZE_2 = 25;
    public static final byte R_MAZE_3 = 26;
    public static final byte R_DEAD_END = 27;
    public static final byte R_GRATING_ROOM = 28;
    public static final byte NUM_ROOMS = 28;

    // ---------------------------------------------------------------
    // Room flags
    // ---------------------------------------------------------------
    public static final byte RF_LIT = 0x01;         // Room is naturally lit
    public static final byte RF_OUTSIDE = 0x02;     // Outdoor room

    // ---------------------------------------------------------------
    // Object IDs (1-based, 0 = no object)
    // ---------------------------------------------------------------
    public static final byte O_NONE = 0;
    // Scenery / Fixed
    public static final byte O_MAILBOX = 1;
    public static final byte O_TROPHY_CASE = 2;
    public static final byte O_FRONT_DOOR = 3;
    public static final byte O_KITCHEN_WINDOW = 4;
    public static final byte O_RUG = 5;
    public static final byte O_TRAP_DOOR = 6;
    public static final byte O_GRATING = 7;
    public static final byte O_KITCHEN_TABLE = 8;
    // Takeable items
    public static final byte O_LEAFLET = 9;
    public static final byte O_LAMP = 10;
    public static final byte O_SWORD = 11;
    public static final byte O_BROWN_SACK = 12;
    public static final byte O_LUNCH = 13;
    public static final byte O_GARLIC = 14;
    public static final byte O_BOTTLE = 15;
    public static final byte O_NASTY_KNIFE = 16;
    public static final byte O_ROPE = 17;
    // Creatures
    public static final byte O_TROLL = 18;
    public static final byte O_AXE = 19;
    // Maze items
    public static final byte O_SKELETON_KEY = 20;
    public static final byte O_BAG_OF_COINS = 21;
    public static final byte O_RUSTY_KNIFE = 22;
    public static final byte O_BONES = 23;
    // Treasures and misc
    public static final byte O_EGG = 24;
    public static final byte O_NEST = 25;
    public static final byte O_TORCH = 26;
    public static final byte O_PAINTING = 27;
    public static final byte O_CHALICE = 28;
    public static final byte O_BAR = 29;
    public static final byte O_BRACELET = 30;
    public static final byte NUM_OBJECTS = 30;

    // ---------------------------------------------------------------
    // Object flags (bitmask in short)
    // ---------------------------------------------------------------
    public static final short OF_TAKEABLE = 0x0001;
    public static final short OF_CONTAINER = 0x0002;
    public static final short OF_OPENABLE = 0x0004;
    public static final short OF_LIGHT = 0x0008;
    public static final short OF_READABLE = 0x0010;
    public static final short OF_WEAPON = 0x0020;
    public static final short OF_TREASURE = 0x0040;
    public static final short OF_ACTOR = 0x0080;
    public static final short OF_SURFACE = 0x0100;  // objects can be placed ON it
    public static final short OF_DOOR = 0x0200;
    public static final short OF_INVISIBLE = 0x0400; // not listed in room
    public static final short OF_FLAMMABLE = 0x0800;

    // ---------------------------------------------------------------
    // Mutable object state flags (per-object byte in dynamic state)
    // ---------------------------------------------------------------
    public static final byte OS_OPEN = 0x01;
    public static final byte OS_ON = 0x02;     // light source is on
    public static final byte OS_MOVED = 0x04;  // object has been moved from initial position
    public static final byte OS_DEAD = 0x08;   // creature is dead
    public static final byte OS_HIDDEN = 0x10; // currently hidden/invisible

    // ---------------------------------------------------------------
    // Game state flags (2-byte bitfield in STATE)
    // ---------------------------------------------------------------
    public static final short GF_TROLL_DEAD = 0x0001;
    public static final short GF_TRAP_OPEN = 0x0002;
    public static final short GF_WINDOW_OPEN = 0x0004;
    public static final short GF_RUG_MOVED = 0x0008;
    public static final short GF_GRATING_OPEN = 0x0010;
    public static final short GF_GRATING_REVEALED = 0x0020;
    public static final short GF_GAME_OVER = 0x0040;
    public static final short GF_CELLAR_VISITED = 0x0080;

    // ---------------------------------------------------------------
    // Special location values for object placement
    // ---------------------------------------------------------------
    public static final short LOC_NOWHERE = 0;
    public static final short LOC_PLAYER = (short) 200;
    // LOC_INSIDE_OBJ_BASE + obj_id = inside that container
    public static final short LOC_INSIDE_OBJ_BASE = (short) 201;

    // ---------------------------------------------------------------
    // Noun IDs (for object resolution)
    // ---------------------------------------------------------------
    public static final byte N_NONE = 0;
    public static final byte N_MAILBOX = 1;
    public static final byte N_CASE = 2;
    public static final byte N_DOOR = 3;
    public static final byte N_WINDOW = 4;
    public static final byte N_RUG = 5;
    public static final byte N_TRAP = 6;
    public static final byte N_GRATING = 7;
    public static final byte N_TABLE = 8;
    public static final byte N_LEAFLET = 9;
    public static final byte N_LAMP = 10;
    public static final byte N_SWORD = 11;
    public static final byte N_SACK = 12;
    public static final byte N_LUNCH = 13;
    public static final byte N_GARLIC = 14;
    public static final byte N_BOTTLE = 15;
    public static final byte N_KNIFE = 16;
    public static final byte N_ROPE = 17;
    public static final byte N_TROLL = 18;
    public static final byte N_AXE = 19;
    public static final byte N_KEY = 20;
    public static final byte N_COINS = 21;
    public static final byte N_BONES = 22;
    public static final byte N_EGG = 23;
    public static final byte N_NEST = 24;
    public static final byte N_TORCH = 25;
    public static final byte N_PAINTING = 26;
    public static final byte N_CHALICE = 27;
    public static final byte N_BAR = 28;
    public static final byte N_BRACELET = 29;
    public static final byte N_ALL = 30;
    public static final byte N_SELF = 31;
    public static final byte N_HOUSE = 32;
    public static final byte N_TREE = 33;

    // ---------------------------------------------------------------
    // Adjective IDs
    // ---------------------------------------------------------------
    public static final byte A_NONE = 0;
    public static final byte A_SMALL = 1;
    public static final byte A_BRASS = 2;
    public static final byte A_ELVISH = 3;
    public static final byte A_BROWN = 4;
    public static final byte A_NASTY = 5;
    public static final byte A_RUSTY = 6;
    public static final byte A_SKELETON = 7;
    public static final byte A_JEWELED = 8;
    public static final byte A_IVORY = 9;
    public static final byte A_PLATINUM = 10;
    public static final byte A_SAPPHIRE = 11;
    public static final byte A_SILVER = 12;
    public static final byte A_FRONT = 13;
    public static final byte A_KITCHEN = 14;
    public static final byte A_TRAP = 15;

    // ---------------------------------------------------------------
    // Preposition IDs
    // ---------------------------------------------------------------
    public static final byte P_NONE = 0;
    public static final byte P_IN = 1;
    public static final byte P_ON = 2;
    public static final byte P_WITH = 3;
    public static final byte P_AT = 4;
    public static final byte P_TO = 5;
    public static final byte P_FROM = 6;
    public static final byte P_UNDER = 7;

    // ---------------------------------------------------------------
    // Vocab entry types
    // ---------------------------------------------------------------
    public static final byte VT_DIRECTION = 0;
    public static final byte VT_VERB = 1;
    public static final byte VT_NOUN = 2;
    public static final byte VT_ADJECTIVE = 3;
    public static final byte VT_PREPOSITION = 4;
    public static final byte VT_SPECIAL = 5;

    // ---------------------------------------------------------------
    // Special handler IDs
    // ---------------------------------------------------------------
    public static final byte SH_NONE = 0;
    public static final byte SH_TROLL = 1;
    public static final byte SH_TRAP_DOOR = 2;
    public static final byte SH_WINDOW = 3;
    public static final byte SH_GRATING = 4;
    public static final byte SH_TROPHY_CASE = 5;
    public static final byte SH_LAMP = 6;
    public static final byte SH_RUG = 7;
    public static final byte SH_LOUD_ROOM = 8;

    // ---------------------------------------------------------------
    // Description mode
    // ---------------------------------------------------------------
    public static final byte MODE_BRIEF = 0;
    public static final byte MODE_VERBOSE = 1;
    public static final byte MODE_SUPERBRIEF = 2;

    // ---------------------------------------------------------------
    // Treasure values (points when placed in trophy case)
    // Index by object ID - 1 (only for treasure objects)
    // ---------------------------------------------------------------
    public static byte getTreasureValue(byte objId) {
        switch (objId) {
            case O_EGG: return 5;
            case O_BAG_OF_COINS: return 10;
            case O_TORCH: return 6;
            case O_PAINTING: return 6;
            case O_CHALICE: return 5;
            case O_BAR: return 5;
            case O_BRACELET: return 5;
            default: return 0;
        }
    }

    public static final byte MAX_SCORE = 50; // 42 from treasures + 8 bonus
    public static final byte MAX_INVENTORY = 8; // max items player can carry
    public static final short LAMP_FUEL_INITIAL = 200; // turns of light

    // ===============================================================
    // PROSE TABLE
    // ===============================================================
    // String IDs for all game text.
    // NOTE: Uses String[] for simulator. Replace with byte[] for card.

    // String ID constants
    // Room descriptions: room_id * 2 = long desc, room_id * 2 + 1 = name
    // Offset by 0 since room IDs start at 1
    public static short roomLongDesc(byte roomId) { return (short)(roomId * 2); }
    public static short roomName(byte roomId) { return (short)(roomId * 2 + 1); }

    // Object strings start after rooms
    public static final short OBJ_STR_BASE = (short)((NUM_ROOMS + 1) * 2);
    public static short objName(byte objId) { return (short)(OBJ_STR_BASE + objId * 2); }
    public static short objDesc(byte objId) { return (short)(OBJ_STR_BASE + objId * 2 + 1); }

    // Stock response strings start after objects
    public static final short STOCK_STR_BASE = (short)(OBJ_STR_BASE + (NUM_OBJECTS + 1) * 2);
    public static final short S_TAKEN = (short)(STOCK_STR_BASE + 0);
    public static final short S_DROPPED = (short)(STOCK_STR_BASE + 1);
    public static final short S_CANT_GO = (short)(STOCK_STR_BASE + 2);
    public static final short S_DARK = (short)(STOCK_STR_BASE + 3);
    public static final short S_DARK_WARNING = (short)(STOCK_STR_BASE + 4);
    public static final short S_CANT_SEE = (short)(STOCK_STR_BASE + 5);
    public static final short S_DONT_HAVE = (short)(STOCK_STR_BASE + 6);
    public static final short S_ALREADY_HAVE = (short)(STOCK_STR_BASE + 7);
    public static final short S_CANT_TAKE = (short)(STOCK_STR_BASE + 8);
    public static final short S_HANDS_FULL = (short)(STOCK_STR_BASE + 9);
    public static final short S_NOT_OPEN = (short)(STOCK_STR_BASE + 10);
    public static final short S_ALREADY_OPEN = (short)(STOCK_STR_BASE + 11);
    public static final short S_NOT_CLOSEABLE = (short)(STOCK_STR_BASE + 12);
    public static final short S_ALREADY_CLOSED = (short)(STOCK_STR_BASE + 13);
    public static final short S_OPENED = (short)(STOCK_STR_BASE + 14);
    public static final short S_CLOSED = (short)(STOCK_STR_BASE + 15);
    public static final short S_EMPTY_HANDED = (short)(STOCK_STR_BASE + 16);
    public static final short S_CARRYING = (short)(STOCK_STR_BASE + 17);
    public static final short S_UNKNOWN_WORD = (short)(STOCK_STR_BASE + 18);
    public static final short S_HUH = (short)(STOCK_STR_BASE + 19);
    public static final short S_GRUE = (short)(STOCK_STR_BASE + 20);
    public static final short S_TROLL_BLOCKS = (short)(STOCK_STR_BASE + 21);
    public static final short S_TROLL_DIES = (short)(STOCK_STR_BASE + 22);
    public static final short S_INTRO = (short)(STOCK_STR_BASE + 23);
    public static final short S_RESUMING = (short)(STOCK_STR_BASE + 24);
    public static final short S_SCORE_MSG = (short)(STOCK_STR_BASE + 25);
    public static final short S_TIME_PASSES = (short)(STOCK_STR_BASE + 26);
    public static final short S_CANT_ATTACK = (short)(STOCK_STR_BASE + 27);
    public static final short S_LAMP_ON = (short)(STOCK_STR_BASE + 28);
    public static final short S_LAMP_OFF = (short)(STOCK_STR_BASE + 29);
    public static final short S_LAMP_DIM = (short)(STOCK_STR_BASE + 30);
    public static final short S_LAMP_DEAD = (short)(STOCK_STR_BASE + 31);
    public static final short S_NOTHING_SPECIAL = (short)(STOCK_STR_BASE + 32);
    public static final short S_NOT_READABLE = (short)(STOCK_STR_BASE + 33);
    public static final short S_BOARDED = (short)(STOCK_STR_BASE + 34);
    public static final short S_TRAP_CLOSED_BEHIND = (short)(STOCK_STR_BASE + 35);
    public static final short S_GAME_OVER = (short)(STOCK_STR_BASE + 36);
    public static final short S_RESTART_MSG = (short)(STOCK_STR_BASE + 37);
    public static final short S_SAVE_MSG = (short)(STOCK_STR_BASE + 38);
    public static final short S_BRIEF_MODE = (short)(STOCK_STR_BASE + 39);
    public static final short S_VERBOSE_MODE = (short)(STOCK_STR_BASE + 40);
    public static final short S_CANT_PUT = (short)(STOCK_STR_BASE + 41);
    public static final short S_PUT_IN = (short)(STOCK_STR_BASE + 42);
    public static final short S_WINDOW_OPEN = (short)(STOCK_STR_BASE + 43);
    public static final short S_WINDOW_CLOSED = (short)(STOCK_STR_BASE + 44);
    public static final short S_GRATING_LOCKED = (short)(STOCK_STR_BASE + 45);
    public static final short S_RUG_MOVED = (short)(STOCK_STR_BASE + 46);
    public static final short S_LOUD_ECHO = (short)(STOCK_STR_BASE + 47);
    public static final short S_WHICH_DO_YOU_MEAN = (short)(STOCK_STR_BASE + 48);
    public static final short S_NOT_CONTAINER = (short)(STOCK_STR_BASE + 49);
    public static final short S_VICTORY = (short)(STOCK_STR_BASE + 50);
    public static final short S_LAMP_GETTING_DIM = (short)(STOCK_STR_BASE + 51);
    public static final short S_TRAP_DOOR_CLOSED = (short)(STOCK_STR_BASE + 52);
    public static final short S_TROLL_LAUGHS = (short)(STOCK_STR_BASE + 53);
    public static final short S_ALREADY_ON = (short)(STOCK_STR_BASE + 54);
    public static final short S_ALREADY_OFF = (short)(STOCK_STR_BASE + 55);
    public static final short S_CANT_MOVE = (short)(STOCK_STR_BASE + 56);

    // Total number of strings
    public static final short NUM_STRINGS = (short)(STOCK_STR_BASE + 57);


    // ===============================================================
    // PROSE - flat byte array, 0x00 delimited. PROSE_IDX[n] = offset of string n.
    // Generated by GenerateCardData - do not edit by hand.
    // ===============================================================
    public static final byte[] PROSE = {
        0, // 0: (empty)
        0, // 1: (empty)
        // 2: "You are standing in an open field west o..."
        'Y','o','u',' ','a','r','e',' ','s','t','a','n','d','i','n','g',' ','i','n',' ','a','n',' ','o',
        'p','e','n',' ','f','i','e','l','d',' ','w','e','s','t',' ','o','f',' ','a',' ','w','h','i','t',
        'e',' ','h','o','u','s','e',',',' ','w','i','t','h',' ','a',' ','b','o','a','r','d','e','d',' ',
        'f','r','o','n','t',' ','d','o','o','r','.',0,
        // 3: "West of House"
        'W','e','s','t',' ','o','f',' ','H','o','u','s','e',0,
        // 4: "You are facing the north side of a white..."
        'Y','o','u',' ','a','r','e',' ','f','a','c','i','n','g',' ','t','h','e',' ','n','o','r','t','h',
        ' ','s','i','d','e',' ','o','f',' ','a',' ','w','h','i','t','e',' ','h','o','u','s','e','.',' ',
        'T','h','e','r','e',' ','i','s',' ','n','o',' ','d','o','o','r',' ','h','e','r','e',',',' ','a',
        'n','d',' ','a','l','l',' ','t','h','e',' ','w','i','n','d','o','w','s',' ','a','r','e',' ','b',
        'o','a','r','d','e','d',' ','u','p','.',' ','A',' ','n','a','r','r','o','w',' ','p','a','t','h',
        ' ','l','e','a','d','s',' ','r','o','u','n','d',' ','t','h','e',' ','h','o','u','s','e',' ','t',
        'o',' ','t','h','e',' ','e','a','s','t','.',0,
        // 5: "North of House"
        'N','o','r','t','h',' ','o','f',' ','H','o','u','s','e',0,
        // 6: "You are facing the south side of a white..."
        'Y','o','u',' ','a','r','e',' ','f','a','c','i','n','g',' ','t','h','e',' ','s','o','u','t','h',
        ' ','s','i','d','e',' ','o','f',' ','a',' ','w','h','i','t','e',' ','h','o','u','s','e','.',' ',
        'T','h','e','r','e',' ','i','s',' ','n','o',' ','d','o','o','r',' ','h','e','r','e',',',' ','a',
        'n','d',' ','a','l','l',' ','t','h','e',' ','w','i','n','d','o','w','s',' ','a','r','e',' ','b',
        'o','a','r','d','e','d','.',0,
        // 7: "South of House"
        'S','o','u','t','h',' ','o','f',' ','H','o','u','s','e',0,
        // 8: "You are behind the white house. A path l..."
        'Y','o','u',' ','a','r','e',' ','b','e','h','i','n','d',' ','t','h','e',' ','w','h','i','t','e',
        ' ','h','o','u','s','e','.',' ','A',' ','p','a','t','h',' ','l','e','a','d','s',' ','i','n','t',
        'o',' ','t','h','e',' ','f','o','r','e','s','t',' ','t','o',' ','t','h','e',' ','e','a','s','t',
        '.',' ','I','n',' ','o','n','e',' ','c','o','r','n','e','r',' ','o','f',' ','t','h','e',' ','h',
        'o','u','s','e',' ','t','h','e','r','e',' ','i','s',' ','a',' ','s','m','a','l','l',' ','w','i',
        'n','d','o','w',' ','w','h','i','c','h',' ','i','s',' ','s','l','i','g','h','t','l','y',' ','a',
        'j','a','r','.',0,
        // 9: "Behind House"
        'B','e','h','i','n','d',' ','H','o','u','s','e',0,
        // 10: "You are in the kitchen of the white hous..."
        'Y','o','u',' ','a','r','e',' ','i','n',' ','t','h','e',' ','k','i','t','c','h','e','n',' ','o',
        'f',' ','t','h','e',' ','w','h','i','t','e',' ','h','o','u','s','e','.',' ','A',' ','t','a','b',
        'l','e',' ','s','e','e','m','s',' ','t','o',' ','h','a','v','e',' ','b','e','e','n',' ','u','s',
        'e','d',' ','r','e','c','e','n','t','l','y',' ','f','o','r',' ','t','h','e',' ','p','r','e','p',
        'a','r','a','t','i','o','n',' ','o','f',' ','f','o','o','d','.',' ','A',' ','p','a','s','s','a',
        'g','e',' ','l','e','a','d','s',' ','t','o',' ','t','h','e',' ','e','a','s','t',' ','a','n','d',
        ' ','a',' ','s','t','a','i','r','c','a','s','e',' ','l','e','a','d','s',' ','u','p','.',0,
        // 11: "Kitchen"
        'K','i','t','c','h','e','n',0,
        // 12: "You are in the living room. There is a d..."
        'Y','o','u',' ','a','r','e',' ','i','n',' ','t','h','e',' ','l','i','v','i','n','g',' ','r','o',
        'o','m','.',' ','T','h','e','r','e',' ','i','s',' ','a',' ','d','o','o','r','w','a','y',' ','t',
        'o',' ','t','h','e',' ','e','a','s','t',',',' ','a','n','d',' ','a',' ','w','o','o','d','e','n',
        ' ','d','o','o','r',' ','w','i','t','h',' ','s','t','r','a','n','g','e',' ','g','o','t','h','i',
        'c',' ','l','e','t','t','e','r','i','n','g',' ','t','o',' ','t','h','e',' ','w','e','s','t',',',
        ' ','w','h','i','c','h',' ','a','p','p','e','a','r','s',' ','t','o',' ','b','e',' ','n','a','i',
        'l','e','d',' ','s','h','u','t','.',0,
        // 13: "Living Room"
        'L','i','v','i','n','g',' ','R','o','o','m',0,
        // 14: "This is the attic. The only exit is a st..."
        'T','h','i','s',' ','i','s',' ','t','h','e',' ','a','t','t','i','c','.',' ','T','h','e',' ','o',
        'n','l','y',' ','e','x','i','t',' ','i','s',' ','a',' ','s','t','a','i','r','w','a','y',' ','l',
        'e','a','d','i','n','g',' ','d','o','w','n','.',0,
        // 15: "Attic"
        'A','t','t','i','c',0,
        // 16: "This is a path winding through a dimly l..."
        'T','h','i','s',' ','i','s',' ','a',' ','p','a','t','h',' ','w','i','n','d','i','n','g',' ','t',
        'h','r','o','u','g','h',' ','a',' ','d','i','m','l','y',' ','l','i','t',' ','f','o','r','e','s',
        't','.',' ','T','h','e',' ','p','a','t','h',' ','h','e','a','d','s',' ','n','o','r','t','h','-',
        's','o','u','t','h',' ','h','e','r','e','.',' ','O','n','e',' ','p','a','r','t','i','c','u','l',
        'a','r','l','y',' ','l','a','r','g','e',' ','t','r','e','e',' ','w','i','t','h',' ','s','o','m',
        'e',' ','l','o','w',' ','b','r','a','n','c','h','e','s',' ','s','t','a','n','d','s',' ','a','t',
        ' ','t','h','e',' ','e','d','g','e',' ','o','f',' ','t','h','e',' ','p','a','t','h','.',0,
        // 17: "Forest Path"
        'F','o','r','e','s','t',' ','P','a','t','h',0,
        // 18: "This is a dimly lit forest, with large t..."
        'T','h','i','s',' ','i','s',' ','a',' ','d','i','m','l','y',' ','l','i','t',' ','f','o','r','e',
        's','t',',',' ','w','i','t','h',' ','l','a','r','g','e',' ','t','r','e','e','s',' ','a','l','l',
        ' ','a','r','o','u','n','d','.',0,
        // 19: "Forest"
        'F','o','r','e','s','t',0,
        // 20: "This is a dimly lit forest, with large t..."
        'T','h','i','s',' ','i','s',' ','a',' ','d','i','m','l','y',' ','l','i','t',' ','f','o','r','e',
        's','t',',',' ','w','i','t','h',' ','l','a','r','g','e',' ','t','r','e','e','s',' ','a','l','l',
        ' ','a','r','o','u','n','d','.',' ','O','n','e',' ','p','a','r','t','i','c','u','l','a','r','l',
        'y',' ','l','a','r','g','e',' ','t','r','e','e',' ','w','i','t','h',' ','s','o','m','e',' ','l',
        'o','w',' ','b','r','a','n','c','h','e','s',' ','s','t','a','n','d','s',' ','h','e','r','e','.',
        0,
        // 21: "Forest"
        'F','o','r','e','s','t',0,
        // 22: "This is a forest, with trees in all dire..."
        'T','h','i','s',' ','i','s',' ','a',' ','f','o','r','e','s','t',',',' ','w','i','t','h',' ','t',
        'r','e','e','s',' ','i','n',' ','a','l','l',' ','d','i','r','e','c','t','i','o','n','s','.',' ',
        'T','o',' ','t','h','e',' ','e','a','s','t',',',' ','t','h','e','r','e',' ','a','p','p','e','a',
        'r','s',' ','t','o',' ','b','e',' ','s','u','n','l','i','g','h','t','.',0,
        // 23: "Forest"
        'F','o','r','e','s','t',0,
        // 24: "You are in a clearing, with a forest sur..."
        'Y','o','u',' ','a','r','e',' ','i','n',' ','a',' ','c','l','e','a','r','i','n','g',',',' ','w',
        'i','t','h',' ','a',' ','f','o','r','e','s','t',' ','s','u','r','r','o','u','n','d','i','n','g',
        ' ','y','o','u',' ','o','n',' ','a','l','l',' ','s','i','d','e','s','.',' ','A',' ','p','a','t',
        'h',' ','l','e','a','d','s',' ','s','o','u','t','h','.',0,
        // 25: "Clearing"
        'C','l','e','a','r','i','n','g',0,
        // 26: "You are about 10 feet above the ground n..."
        'Y','o','u',' ','a','r','e',' ','a','b','o','u','t',' ','1','0',' ','f','e','e','t',' ','a','b',
        'o','v','e',' ','t','h','e',' ','g','r','o','u','n','d',' ','n','e','s','t','l','e','d',' ','a',
        'm','o','n','g',' ','s','o','m','e',' ','l','a','r','g','e',' ','b','r','a','n','c','h','e','s',
        '.',' ','T','h','e',' ','n','e','a','r','e','s','t',' ','b','r','a','n','c','h',' ','a','b','o',
        'v','e',' ','y','o','u',' ','i','s',' ','b','e','y','o','n','d',' ','y','o','u','r',' ','r','e',
        'a','c','h','.',' ','B','e','l','o','w',' ','y','o','u',' ','i','s',' ','a',' ','f','o','r','e',
        's','t',' ','p','a','t','h','.',0,
        // 27: "Up a Tree"
        'U','p',' ','a',' ','T','r','e','e',0,
        // 28: "You are in a small clearing in a well-ma..."
        'Y','o','u',' ','a','r','e',' ','i','n',' ','a',' ','s','m','a','l','l',' ','c','l','e','a','r',
        'i','n','g',' ','i','n',' ','a',' ','w','e','l','l','-','m','a','r','k','e','d',' ','f','o','r',
        'e','s','t',' ','p','a','t','h','.',0,
        // 29: "Grating Clearing"
        'G','r','a','t','i','n','g',' ','C','l','e','a','r','i','n','g',0,
        // 30: "You are in a dark and damp cellar with a..."
        'Y','o','u',' ','a','r','e',' ','i','n',' ','a',' ','d','a','r','k',' ','a','n','d',' ','d','a',
        'm','p',' ','c','e','l','l','a','r',' ','w','i','t','h',' ','a',' ','n','a','r','r','o','w',' ',
        'p','a','s','s','a','g','e','w','a','y',' ','l','e','a','d','i','n','g',' ','n','o','r','t','h',
        ',',' ','a','n','d',' ','a',' ','c','r','a','w','l','w','a','y',' ','t','o',' ','t','h','e',' ',
        's','o','u','t','h','.',' ','T','o',' ','t','h','e',' ','w','e','s','t',' ','i','s',' ','t','h',
        'e',' ','b','o','t','t','o','m',' ','o','f',' ','a',' ','s','t','e','e','p',' ','m','e','t','a',
        'l',' ','r','a','m','p',' ','w','h','i','c','h',' ','i','s',' ','u','n','c','l','i','m','b','a',
        'b','l','e','.',0,
        // 31: "Cellar"
        'C','e','l','l','a','r',0,
        // 32: "This is a small room with passages to th..."
        'T','h','i','s',' ','i','s',' ','a',' ','s','m','a','l','l',' ','r','o','o','m',' ','w','i','t',
        'h',' ','p','a','s','s','a','g','e','s',' ','t','o',' ','t','h','e',' ','e','a','s','t',' ','a',
        'n','d',' ','s','o','u','t','h',' ','a','n','d',' ','a',' ','f','o','r','b','i','d','d','i','n',
        'g',' ','h','o','l','e',' ','l','e','a','d','i','n','g',' ','w','e','s','t','.',' ','B','l','o',
        'o','d','s','t','a','i','n','s',' ','a','n','d',' ','d','e','e','p',' ','s','c','r','a','t','c',
        'h','e','s',' ','(','p','e','r','h','a','p','s',' ','m','a','d','e',' ','b','y',' ','s','t','r',
        'a','y','i','n','g',' ','a','d','v','e','n','t','u','r','e','r','s',')',' ','m','a','r',' ','t',
        'h','e',' ','w','a','l','l','s','.',0,
        // 33: "Troll Room"
        'T','r','o','l','l',' ','R','o','o','m',0,
        // 34: "You are on the east edge of a chasm, the..."
        'Y','o','u',' ','a','r','e',' ','o','n',' ','t','h','e',' ','e','a','s','t',' ','e','d','g','e',
        ' ','o','f',' ','a',' ','c','h','a','s','m',',',' ','t','h','e',' ','b','o','t','t','o','m',' ',
        'o','f',' ','w','h','i','c','h',' ','c','a','n','n','o','t',' ','b','e',' ','s','e','e','n','.',
        ' ','A',' ','n','a','r','r','o','w',' ','p','a','s','s','a','g','e',' ','l','e','a','d','s',' ',
        'n','o','r','t','h',',',' ','a','n','d',' ','t','h','e',' ','p','a','t','h',' ','y','o','u',' ',
        'a','r','e',' ','o','n',' ','c','o','n','t','i','n','u','e','s',' ','t','o',' ','t','h','e',' ',
        'e','a','s','t','.',0,
        // 35: "East of Chasm"
        'E','a','s','t',' ','o','f',' ','C','h','a','s','m',0,
        // 36: "This is an art gallery. Most of the pain..."
        'T','h','i','s',' ','i','s',' ','a','n',' ','a','r','t',' ','g','a','l','l','e','r','y','.',' ',
        'M','o','s','t',' ','o','f',' ','t','h','e',' ','p','a','i','n','t','i','n','g','s',' ','h','a',
        'v','e',' ','b','e','e','n',' ','s','t','o','l','e','n',' ','b','y',' ','v','a','n','d','a','l',
        's',' ','w','i','t','h',' ','e','x','c','e','p','t','i','o','n','a','l',' ','t','a','s','t','e',
        '.',' ','A',' ','p','a','s','s','a','g','e',' ','e','x','i','t','s',' ','t','o',' ','t','h','e',
        ' ','s','o','u','t','h','.',0,
        // 37: "Gallery"
        'G','a','l','l','e','r','y',0,
        // 38: "This is a long north-south passageway."
        'T','h','i','s',' ','i','s',' ','a',' ','l','o','n','g',' ','n','o','r','t','h','-','s','o','u',
        't','h',' ','p','a','s','s','a','g','e','w','a','y','.',0,
        // 39: "N-S Passage"
        'N','-','S',' ','P','a','s','s','a','g','e',0,
        // 40: "This is a circular stone room with passa..."
        'T','h','i','s',' ','i','s',' ','a',' ','c','i','r','c','u','l','a','r',' ','s','t','o','n','e',
        ' ','r','o','o','m',' ','w','i','t','h',' ','p','a','s','s','a','g','e','s',' ','i','n',' ','a',
        'l','l',' ','d','i','r','e','c','t','i','o','n','s','.',' ','S','e','v','e','r','a','l',' ','o',
        'f',' ','t','h','e','m',' ','h','a','v','e',' ','u','n','f','o','r','t','u','n','a','t','e','l',
        'y',' ','b','e','e','n',' ','b','l','o','c','k','e','d',' ','b','y',' ','c','a','v','e','-','i',
        'n','s','.',0,
        // 41: "Round Room"
        'R','o','u','n','d',' ','R','o','o','m',0,
        // 42: "This is a narrow passage. The passage tu..."
        'T','h','i','s',' ','i','s',' ','a',' ','n','a','r','r','o','w',' ','p','a','s','s','a','g','e',
        '.',' ','T','h','e',' ','p','a','s','s','a','g','e',' ','t','u','r','n','s',' ','f','r','o','m',
        ' ','n','o','r','t','h',' ','t','o',' ','s','o','u','t','h',' ','h','e','r','e','.',0,
        // 43: "Narrow Passage"
        'N','a','r','r','o','w',' ','P','a','s','s','a','g','e',0,
        // 44: "This is a large room with a distinctly G..."
        'T','h','i','s',' ','i','s',' ','a',' ','l','a','r','g','e',' ','r','o','o','m',' ','w','i','t',
        'h',' ','a',' ','d','i','s','t','i','n','c','t','l','y',' ','G','o','t','h','i','c',' ','f','e',
        'e','l',' ','t','o',' ','i','t','.',' ','O','n',' ','t','h','e',' ','w','a','l','l','s',' ','a',
        'r','e',' ','m','o','u','n','t','e','d',' ','t','h','e',' ','h','e','a','d','s',' ','o','f',' ',
        'm','a','n','y',' ','a','n','i','m','a','l','s','.',' ','A',' ','w','i','d','e',' ','p','a','s',
        's','a','g','e',' ','e','x','i','t','s',' ','t','o',' ','t','h','e',' ','s','o','u','t','h','.',
        0,
        // 45: "Treasure Room"
        'T','r','e','a','s','u','r','e',' ','R','o','o','m',0,
        // 46: "This is a large room with a ceiling whic..."
        'T','h','i','s',' ','i','s',' ','a',' ','l','a','r','g','e',' ','r','o','o','m',' ','w','i','t',
        'h',' ','a',' ','c','e','i','l','i','n','g',' ','w','h','i','c','h',' ','c','a','n','n','o','t',
        ' ','b','e',' ','d','e','t','e','c','t','e','d',' ','f','r','o','m',' ','t','h','e',' ','g','r',
        'o','u','n','d','.',' ','A',' ','n','a','r','r','o','w',' ','p','a','s','s','a','g','e',' ','e',
        'x','i','t','s',' ','t','o',' ','t','h','e',' ','w','e','s','t','.',' ','T','h','e',' ','r','o',
        'o','m',' ','i','s',' ','d','e','a','f','e','n','i','n','g','l','y',' ','l','o','u','d',' ','w',
        'i','t','h',' ','a','n',' ','u','n','i','d','e','n','t','i','f','i','e','d',',',' ','l','o','w',
        ' ','f','r','e','q','u','e','n','c','y',' ','v','i','b','r','a','t','i','o','n','.',0,
        // 47: "Loud Room"
        'L','o','u','d',' ','R','o','o','m',0,
        // 48: "You are in a maze of twisty little passa..."
        'Y','o','u',' ','a','r','e',' ','i','n',' ','a',' ','m','a','z','e',' ','o','f',' ','t','w','i',
        's','t','y',' ','l','i','t','t','l','e',' ','p','a','s','s','a','g','e','s',',',' ','a','l','l',
        ' ','a','l','i','k','e','.',0,
        // 49: "Maze"
        'M','a','z','e',0,
        // 50: "You are in a maze of twisty little passa..."
        'Y','o','u',' ','a','r','e',' ','i','n',' ','a',' ','m','a','z','e',' ','o','f',' ','t','w','i',
        's','t','y',' ','l','i','t','t','l','e',' ','p','a','s','s','a','g','e','s',',',' ','a','l','l',
        ' ','a','l','i','k','e','.',0,
        // 51: "Maze"
        'M','a','z','e',0,
        // 52: "You are in a maze of twisty little passa..."
        'Y','o','u',' ','a','r','e',' ','i','n',' ','a',' ','m','a','z','e',' ','o','f',' ','t','w','i',
        's','t','y',' ','l','i','t','t','l','e',' ','p','a','s','s','a','g','e','s',',',' ','a','l','l',
        ' ','a','l','i','k','e','.',0,
        // 53: "Maze"
        'M','a','z','e',0,
        // 54: "Dead end."
        'D','e','a','d',' ','e','n','d','.',0,
        // 55: "Dead End"
        'D','e','a','d',' ','E','n','d',0,
        // 56: "You are in a small room near the surface..."
        'Y','o','u',' ','a','r','e',' ','i','n',' ','a',' ','s','m','a','l','l',' ','r','o','o','m',' ',
        'n','e','a','r',' ','t','h','e',' ','s','u','r','f','a','c','e','.',' ','A',' ','n','a','r','r',
        'o','w',' ','p','a','s','s','a','g','e',' ','l','e','a','d','s',' ','s','o','u','t','h','.',' ',
        'A','b','o','v','e',' ','y','o','u',' ','i','s',' ','a',' ','g','r','a','t','i','n','g','.',0,
        // 57: "Grating Room"
        'G','r','a','t','i','n','g',' ','R','o','o','m',0,
        0, // 58: (empty)
        0, // 59: (empty)
        // 60: "small mailbox"
        's','m','a','l','l',' ','m','a','i','l','b','o','x',0,
        // 61: "It's a small mailbox."
        'I','t','\'','s',' ','a',' ','s','m','a','l','l',' ','m','a','i','l','b','o','x','.',0,
        // 62: "trophy case"
        't','r','o','p','h','y',' ','c','a','s','e',0,
        // 63: "It's a large trophy case, currently empt..."
        'I','t','\'','s',' ','a',' ','l','a','r','g','e',' ','t','r','o','p','h','y',' ','c','a','s',
        'e',',',' ','c','u','r','r','e','n','t','l','y',' ','e','m','p','t','y','.',0,
        // 64: "front door"
        'f','r','o','n','t',' ','d','o','o','r',0,
        // 65: "The door is boarded shut and you can't r..."
        'T','h','e',' ','d','o','o','r',' ','i','s',' ','b','o','a','r','d','e','d',' ','s','h','u','t',
        ' ','a','n','d',' ','y','o','u',' ','c','a','n','\'','t',' ','r','e','m','o','v','e',' ','t',
        'h','e',' ','b','o','a','r','d','s','.',0,
        // 66: "kitchen window"
        'k','i','t','c','h','e','n',' ','w','i','n','d','o','w',0,
        // 67: "The window is slightly ajar, but not eno..."
        'T','h','e',' ','w','i','n','d','o','w',' ','i','s',' ','s','l','i','g','h','t','l','y',' ','a',
        'j','a','r',',',' ','b','u','t',' ','n','o','t',' ','e','n','o','u','g','h',' ','t','o',' ','a',
        'l','l','o','w',' ','e','n','t','r','y','.',0,
        // 68: "large oriental rug"
        'l','a','r','g','e',' ','o','r','i','e','n','t','a','l',' ','r','u','g',0,
        // 69: "It's a large oriental rug, covering the ..."
        'I','t','\'','s',' ','a',' ','l','a','r','g','e',' ','o','r','i','e','n','t','a','l',' ','r',
        'u','g',',',' ','c','o','v','e','r','i','n','g',' ','t','h','e',' ','c','e','n','t','e','r',' ',
        'o','f',' ','t','h','e',' ','r','o','o','m','.',0,
        // 70: "trap door"
        't','r','a','p',' ','d','o','o','r',0,
        // 71: "The trap door is closed."
        'T','h','e',' ','t','r','a','p',' ','d','o','o','r',' ','i','s',' ','c','l','o','s','e','d','.',
        0,
        // 72: "grating"
        'g','r','a','t','i','n','g',0,
        // 73: "The grating is firmly embedded in the gr..."
        'T','h','e',' ','g','r','a','t','i','n','g',' ','i','s',' ','f','i','r','m','l','y',' ','e','m',
        'b','e','d','d','e','d',' ','i','n',' ','t','h','e',' ','g','r','o','u','n','d','.',0,
        // 74: "kitchen table"
        'k','i','t','c','h','e','n',' ','t','a','b','l','e',0,
        // 75: "It's a sturdy kitchen table."
        'I','t','\'','s',' ','a',' ','s','t','u','r','d','y',' ','k','i','t','c','h','e','n',' ','t',
        'a','b','l','e','.',0,
        // 76: "leaflet"
        'l','e','a','f','l','e','t',0,
        // 77: "WELCOME TO ZORK!\n\nZORK is a game of adve..."
        'W','E','L','C','O','M','E',' ','T','O',' ','Z','O','R','K','!','\n','\n','Z','O','R','K',' ',
        'i','s',' ','a',' ','g','a','m','e',' ','o','f',' ','a','d','v','e','n','t','u','r','e',',',' ',
        'd','a','n','g','e','r',',',' ','a','n','d',' ','l','o','w',' ','c','u','n','n','i','n','g','.',
        ' ','I','n',' ','i','t',' ','y','o','u',' ','w','i','l','l',' ','e','x','p','l','o','r','e',' ',
        's','o','m','e',' ','o','f',' ','t','h','e',' ','m','o','s','t',' ','a','m','a','z','i','n','g',
        ' ','t','e','r','r','i','t','o','r','y',' ','e','v','e','r',' ','s','e','e','n',' ','b','y',' ',
        'm','o','r','t','a','l','s','.',' ','N','o',' ','c','o','m','p','u','t','e','r',' ','s','h','o',
        'u','l','d',' ','b','e',' ','w','i','t','h','o','u','t',' ','o','n','e','!',0,
        // 78: "brass lantern"
        'b','r','a','s','s',' ','l','a','n','t','e','r','n',0,
        // 79: "It's a brass lantern."
        'I','t','\'','s',' ','a',' ','b','r','a','s','s',' ','l','a','n','t','e','r','n','.',0,
        // 80: "elvish sword"
        'e','l','v','i','s','h',' ','s','w','o','r','d',0,
        // 81: "It's an elvish sword of great antiquity."
        'I','t','\'','s',' ','a','n',' ','e','l','v','i','s','h',' ','s','w','o','r','d',' ','o','f',
        ' ','g','r','e','a','t',' ','a','n','t','i','q','u','i','t','y','.',0,
        // 82: "brown sack"
        'b','r','o','w','n',' ','s','a','c','k',0,
        // 83: "It's an elongated brown sack, smelling o..."
        'I','t','\'','s',' ','a','n',' ','e','l','o','n','g','a','t','e','d',' ','b','r','o','w','n',
        ' ','s','a','c','k',',',' ','s','m','e','l','l','i','n','g',' ','o','f',' ','h','o','t',' ','p',
        'e','p','p','e','r','s','.',0,
        // 84: "hot pepper sandwich"
        'h','o','t',' ','p','e','p','p','e','r',' ','s','a','n','d','w','i','c','h',0,
        // 85: "It's a hot pepper sandwich."
        'I','t','\'','s',' ','a',' ','h','o','t',' ','p','e','p','p','e','r',' ','s','a','n','d','w',
        'i','c','h','.',0,
        // 86: "clove of garlic"
        'c','l','o','v','e',' ','o','f',' ','g','a','r','l','i','c',0,
        // 87: "It's a clove of garlic."
        'I','t','\'','s',' ','a',' ','c','l','o','v','e',' ','o','f',' ','g','a','r','l','i','c','.',
        0,
        // 88: "glass bottle"
        'g','l','a','s','s',' ','b','o','t','t','l','e',0,
        // 89: "The glass bottle contains a quantity of ..."
        'T','h','e',' ','g','l','a','s','s',' ','b','o','t','t','l','e',' ','c','o','n','t','a','i','n',
        's',' ','a',' ','q','u','a','n','t','i','t','y',' ','o','f',' ','w','a','t','e','r','.',0,
        // 90: "nasty knife"
        'n','a','s','t','y',' ','k','n','i','f','e',0,
        // 91: "It's a nasty-looking knife."
        'I','t','\'','s',' ','a',' ','n','a','s','t','y','-','l','o','o','k','i','n','g',' ','k','n',
        'i','f','e','.',0,
        // 92: "coil of rope"
        'c','o','i','l',' ','o','f',' ','r','o','p','e',0,
        // 93: "It's a length of sturdy hemp rope."
        'I','t','\'','s',' ','a',' ','l','e','n','g','t','h',' ','o','f',' ','s','t','u','r','d','y',
        ' ','h','e','m','p',' ','r','o','p','e','.',0,
        // 94: "troll"
        't','r','o','l','l',0,
        // 95: "A nasty-looking troll, brandishing a blo..."
        'A',' ','n','a','s','t','y','-','l','o','o','k','i','n','g',' ','t','r','o','l','l',',',' ','b',
        'r','a','n','d','i','s','h','i','n','g',' ','a',' ','b','l','o','o','d','y',' ','a','x','e',',',
        ' ','b','l','o','c','k','s',' ','a','l','l',' ','p','a','s','s','a','g','e','s',' ','o','u','t',
        ' ','o','f',' ','t','h','e',' ','r','o','o','m','.',0,
        // 96: "bloody axe"
        'b','l','o','o','d','y',' ','a','x','e',0,
        // 97: "It's a large, nasty-looking axe covered ..."
        'I','t','\'','s',' ','a',' ','l','a','r','g','e',',',' ','n','a','s','t','y','-','l','o','o',
        'k','i','n','g',' ','a','x','e',' ','c','o','v','e','r','e','d',' ','w','i','t','h',' ','d','r',
        'i','e','d',' ','b','l','o','o','d','.',0,
        // 98: "skeleton key"
        's','k','e','l','e','t','o','n',' ','k','e','y',0,
        // 99: "It's a large skeleton key."
        'I','t','\'','s',' ','a',' ','l','a','r','g','e',' ','s','k','e','l','e','t','o','n',' ','k',
        'e','y','.',0,
        // 100: "bag of coins"
        'b','a','g',' ','o','f',' ','c','o','i','n','s',0,
        // 101: "It's a leather bag filled with gold coin..."
        'I','t','\'','s',' ','a',' ','l','e','a','t','h','e','r',' ','b','a','g',' ','f','i','l','l',
        'e','d',' ','w','i','t','h',' ','g','o','l','d',' ','c','o','i','n','s','.',' ','T','h','e',' ',
        'c','o','i','n','s',' ','a','r','e',' ','i','n','s','c','r','i','b','e','d',':',' ','\'','F',
        'R','O','B','O','Z','Z',' ','M','A','G','I','C',' ','C','O','I','N',' ','C','O','M','P','A','N',
        'Y','\'','.',0,
        // 102: "rusty knife"
        'r','u','s','t','y',' ','k','n','i','f','e',0,
        // 103: "It's a rusty, dull knife."
        'I','t','\'','s',' ','a',' ','r','u','s','t','y',',',' ','d','u','l','l',' ','k','n','i','f',
        'e','.',0,
        // 104: "pile of bones"
        'p','i','l','e',' ','o','f',' ','b','o','n','e','s',0,
        // 105: "These are the mortal remains of an unfor..."
        'T','h','e','s','e',' ','a','r','e',' ','t','h','e',' ','m','o','r','t','a','l',' ','r','e','m',
        'a','i','n','s',' ','o','f',' ','a','n',' ','u','n','f','o','r','t','u','n','a','t','e',' ','a',
        'd','v','e','n','t','u','r','e','r','.',0,
        // 106: "jewel-encrusted egg"
        'j','e','w','e','l','-','e','n','c','r','u','s','t','e','d',' ','e','g','g',0,
        // 107: "It's a beautiful jewel-encrusted egg, de..."
        'I','t','\'','s',' ','a',' ','b','e','a','u','t','i','f','u','l',' ','j','e','w','e','l','-',
        'e','n','c','r','u','s','t','e','d',' ','e','g','g',',',' ','d','e','l','i','c','a','t','e','l',
        'y',' ','c','r','a','f','t','e','d','.',0,
        // 108: "bird's nest"
        'b','i','r','d','\'','s',' ','n','e','s','t',0,
        // 109: "It's a large bird's nest, made from stra..."
        'I','t','\'','s',' ','a',' ','l','a','r','g','e',' ','b','i','r','d','\'','s',' ','n','e','s',
        't',',',' ','m','a','d','e',' ','f','r','o','m',' ','s','t','r','a','w',' ','a','n','d',' ','t',
        'w','i','g','s','.',0,
        // 110: "ivory torch"
        'i','v','o','r','y',' ','t','o','r','c','h',0,
        // 111: "It's a beautiful ivory torch, with an et..."
        'I','t','\'','s',' ','a',' ','b','e','a','u','t','i','f','u','l',' ','i','v','o','r','y',' ',
        't','o','r','c','h',',',' ','w','i','t','h',' ','a','n',' ','e','t','e','r','n','a','l',' ','f',
        'l','a','m','e','.',0,
        // 112: "painting"
        'p','a','i','n','t','i','n','g',0,
        // 113: "It's a masterful painting of unparallele..."
        'I','t','\'','s',' ','a',' ','m','a','s','t','e','r','f','u','l',' ','p','a','i','n','t','i',
        'n','g',' ','o','f',' ','u','n','p','a','r','a','l','l','e','l','e','d',' ','b','e','a','u','t',
        'y',',',' ','d','e','p','i','c','t','i','n','g',' ','a',' ','s','u','n','s','e','t',' ','o','v',
        'e','r',' ','a',' ','p','a','s','t','o','r','a','l',' ','s','c','e','n','e','.',0,
        // 114: "silver chalice"
        's','i','l','v','e','r',' ','c','h','a','l','i','c','e',0,
        // 115: "It's a beautiful silver chalice, encrust..."
        'I','t','\'','s',' ','a',' ','b','e','a','u','t','i','f','u','l',' ','s','i','l','v','e','r',
        ' ','c','h','a','l','i','c','e',',',' ','e','n','c','r','u','s','t','e','d',' ','w','i','t','h',
        ' ','s','a','p','p','h','i','r','e','s','.',0,
        // 116: "platinum bar"
        'p','l','a','t','i','n','u','m',' ','b','a','r',0,
        // 117: "It's a bar of solid platinum, about one ..."
        'I','t','\'','s',' ','a',' ','b','a','r',' ','o','f',' ','s','o','l','i','d',' ','p','l','a',
        't','i','n','u','m',',',' ','a','b','o','u','t',' ','o','n','e',' ','f','o','o','t',' ','l','o',
        'n','g','.',0,
        // 118: "sapphire bracelet"
        's','a','p','p','h','i','r','e',' ','b','r','a','c','e','l','e','t',0,
        // 119: "It's a beautiful sapphire-encrusted brac..."
        'I','t','\'','s',' ','a',' ','b','e','a','u','t','i','f','u','l',' ','s','a','p','p','h','i',
        'r','e','-','e','n','c','r','u','s','t','e','d',' ','b','r','a','c','e','l','e','t','.',0,
        // 120: "Taken."
        'T','a','k','e','n','.',0,
        // 121: "Dropped."
        'D','r','o','p','p','e','d','.',0,
        // 122: "You can't go that way."
        'Y','o','u',' ','c','a','n','\'','t',' ','g','o',' ','t','h','a','t',' ','w','a','y','.',0,
        // 123: "It is pitch black. You are likely to be ..."
        'I','t',' ','i','s',' ','p','i','t','c','h',' ','b','l','a','c','k','.',' ','Y','o','u',' ','a',
        'r','e',' ','l','i','k','e','l','y',' ','t','o',' ','b','e',' ','e','a','t','e','n',' ','b','y',
        ' ','a',' ','g','r','u','e','.',0,
        // 124: "You may be eaten by a grue."
        'Y','o','u',' ','m','a','y',' ','b','e',' ','e','a','t','e','n',' ','b','y',' ','a',' ','g','r',
        'u','e','.',0,
        // 125: "You can't see any such thing."
        'Y','o','u',' ','c','a','n','\'','t',' ','s','e','e',' ','a','n','y',' ','s','u','c','h',' ',
        't','h','i','n','g','.',0,
        // 126: "You don't have that."
        'Y','o','u',' ','d','o','n','\'','t',' ','h','a','v','e',' ','t','h','a','t','.',0,
        // 127: "You already have that."
        'Y','o','u',' ','a','l','r','e','a','d','y',' ','h','a','v','e',' ','t','h','a','t','.',0,
        // 128: "You can't take that."
        'Y','o','u',' ','c','a','n','\'','t',' ','t','a','k','e',' ','t','h','a','t','.',0,
        // 129: "Your hands are full."
        'Y','o','u','r',' ','h','a','n','d','s',' ','a','r','e',' ','f','u','l','l','.',0,
        // 130: "It isn't open."
        'I','t',' ','i','s','n','\'','t',' ','o','p','e','n','.',0,
        // 131: "It is already open."
        'I','t',' ','i','s',' ','a','l','r','e','a','d','y',' ','o','p','e','n','.',0,
        // 132: "You can't close that."
        'Y','o','u',' ','c','a','n','\'','t',' ','c','l','o','s','e',' ','t','h','a','t','.',0,
        // 133: "It is already closed."
        'I','t',' ','i','s',' ','a','l','r','e','a','d','y',' ','c','l','o','s','e','d','.',0,
        // 134: "Opened."
        'O','p','e','n','e','d','.',0,
        // 135: "Closed."
        'C','l','o','s','e','d','.',0,
        // 136: "You are empty-handed."
        'Y','o','u',' ','a','r','e',' ','e','m','p','t','y','-','h','a','n','d','e','d','.',0,
        // 137: "You are carrying:"
        'Y','o','u',' ','a','r','e',' ','c','a','r','r','y','i','n','g',':',0,
        // 138: "I don't know the word ""
        'I',' ','d','o','n','\'','t',' ','k','n','o','w',' ','t','h','e',' ','w','o','r','d',' ','"',
        0,
        // 139: "I don't understand that sentence."
        'I',' ','d','o','n','\'','t',' ','u','n','d','e','r','s','t','a','n','d',' ','t','h','a','t',
        ' ','s','e','n','t','e','n','c','e','.',0,
        // 140: "Oh no! A lurking grue slithered into the..."
        'O','h',' ','n','o','!',' ','A',' ','l','u','r','k','i','n','g',' ','g','r','u','e',' ','s','l',
        'i','t','h','e','r','e','d',' ','i','n','t','o',' ','t','h','e',' ','r','o','o','m',' ','a','n',
        'd',' ','d','e','v','o','u','r','e','d',' ','y','o','u','!',0,
        // 141: "The troll fends you off with a menacing ..."
        'T','h','e',' ','t','r','o','l','l',' ','f','e','n','d','s',' ','y','o','u',' ','o','f','f',' ',
        'w','i','t','h',' ','a',' ','m','e','n','a','c','i','n','g',' ','g','e','s','t','u','r','e','.',
        0,
        // 142: "The troll, disarmed, is overthrown and v..."
        'T','h','e',' ','t','r','o','l','l',',',' ','d','i','s','a','r','m','e','d',',',' ','i','s',' ',
        'o','v','e','r','t','h','r','o','w','n',' ','a','n','d',' ','v','a','n','i','s','h','e','s',' ',
        'i','n',' ','a',' ','c','l','o','u','d',' ','o','f',' ','g','r','e','a','s','y',' ','b','l','a',
        'c','k',' ','s','m','o','k','e','.',0,
        // 143: "Mini-ZORK I: The Great Underground Empir..."
        'M','i','n','i','-','Z','O','R','K',' ','I',':',' ','T','h','e',' ','G','r','e','a','t',' ','U',
        'n','d','e','r','g','r','o','u','n','d',' ','E','m','p','i','r','e','\n','A',' ','J','a','v',
        'a','C','a','r','d',' ','i','n','t','e','r','a','c','t','i','v','e',' ','f','i','c','t','i','o',
        'n',' ','b','y',' ','I','n','f','o','c','o','m',' ','(','r','e','i','m','p','l','e','m','e','n',
        't','e','d',')','\n','R','e','l','e','a','s','e',' ','1',' ','/',' ','S','e','r','i','a','l',
        ' ','n','u','m','b','e','r',' ','2','6','0','4','2','4','\n',0,
        // 144: "Resuming."
        'R','e','s','u','m','i','n','g','.',0,
        // 145: "Your score is "
        'Y','o','u','r',' ','s','c','o','r','e',' ','i','s',' ',0,
        // 146: "Time passes..."
        'T','i','m','e',' ','p','a','s','s','e','s','.','.','.',0,
        // 147: "Trying to attack that is pointless."
        'T','r','y','i','n','g',' ','t','o',' ','a','t','t','a','c','k',' ','t','h','a','t',' ','i','s',
        ' ','p','o','i','n','t','l','e','s','s','.',0,
        // 148: "The brass lantern is now on."
        'T','h','e',' ','b','r','a','s','s',' ','l','a','n','t','e','r','n',' ','i','s',' ','n','o','w',
        ' ','o','n','.',0,
        // 149: "The brass lantern is now off."
        'T','h','e',' ','b','r','a','s','s',' ','l','a','n','t','e','r','n',' ','i','s',' ','n','o','w',
        ' ','o','f','f','.',0,
        // 150: "Your lamp is getting dim."
        'Y','o','u','r',' ','l','a','m','p',' ','i','s',' ','g','e','t','t','i','n','g',' ','d','i','m',
        '.',0,
        // 151: "Your lamp has run out of power."
        'Y','o','u','r',' ','l','a','m','p',' ','h','a','s',' ','r','u','n',' ','o','u','t',' ','o','f',
        ' ','p','o','w','e','r','.',0,
        // 152: "There is nothing special about that."
        'T','h','e','r','e',' ','i','s',' ','n','o','t','h','i','n','g',' ','s','p','e','c','i','a','l',
        ' ','a','b','o','u','t',' ','t','h','a','t','.',0,
        // 153: "There's nothing to read on that."
        'T','h','e','r','e','\'','s',' ','n','o','t','h','i','n','g',' ','t','o',' ','r','e','a','d',
        ' ','o','n',' ','t','h','a','t','.',0,
        // 154: "The door is boarded and you can't remove..."
        'T','h','e',' ','d','o','o','r',' ','i','s',' ','b','o','a','r','d','e','d',' ','a','n','d',' ',
        'y','o','u',' ','c','a','n','\'','t',' ','r','e','m','o','v','e',' ','t','h','e',' ','b','o',
        'a','r','d','s','.',0,
        // 155: "The trap door crashes shut, and you hear..."
        'T','h','e',' ','t','r','a','p',' ','d','o','o','r',' ','c','r','a','s','h','e','s',' ','s','h',
        'u','t',',',' ','a','n','d',' ','y','o','u',' ','h','e','a','r',' ','s','o','m','e','o','n','e',
        ' ','b','a','r','r','i','n','g',' ','i','t','.',0,
        // 156: "\n    ****  You have died  ****\n"
        '\n',' ',' ',' ',' ','*','*','*','*',' ',' ','Y','o','u',' ','h','a','v','e',' ','d','i','e',
        'd',' ',' ','*','*','*','*','\n',0,
        // 157: "Game restarted."
        'G','a','m','e',' ','r','e','s','t','a','r','t','e','d','.',0,
        // 158: "Your game is always saved to the card au..."
        'Y','o','u','r',' ','g','a','m','e',' ','i','s',' ','a','l','w','a','y','s',' ','s','a','v','e',
        'd',' ','t','o',' ','t','h','e',' ','c','a','r','d',' ','a','u','t','o','m','a','t','i','c','a',
        'l','l','y','.',0,
        // 159: "Brief descriptions."
        'B','r','i','e','f',' ','d','e','s','c','r','i','p','t','i','o','n','s','.',0,
        // 160: "Verbose descriptions."
        'V','e','r','b','o','s','e',' ','d','e','s','c','r','i','p','t','i','o','n','s','.',0,
        // 161: "You can't put that there."
        'Y','o','u',' ','c','a','n','\'','t',' ','p','u','t',' ','t','h','a','t',' ','t','h','e','r',
        'e','.',0,
        // 162: "Done."
        'D','o','n','e','.',0,
        // 163: "With great effort, you open the window f..."
        'W','i','t','h',' ','g','r','e','a','t',' ','e','f','f','o','r','t',',',' ','y','o','u',' ','o',
        'p','e','n',' ','t','h','e',' ','w','i','n','d','o','w',' ','f','a','r',' ','e','n','o','u','g',
        'h',' ','t','o',' ','a','l','l','o','w',' ','e','n','t','r','y','.',0,
        // 164: "The window is now closed."
        'T','h','e',' ','w','i','n','d','o','w',' ','i','s',' ','n','o','w',' ','c','l','o','s','e','d',
        '.',0,
        // 165: "The grating is locked."
        'T','h','e',' ','g','r','a','t','i','n','g',' ','i','s',' ','l','o','c','k','e','d','.',0,
        // 166: "With a great effort, the rug is moved to..."
        'W','i','t','h',' ','a',' ','g','r','e','a','t',' ','e','f','f','o','r','t',',',' ','t','h','e',
        ' ','r','u','g',' ','i','s',' ','m','o','v','e','d',' ','t','o',' ','o','n','e',' ','s','i','d',
        'e',' ','o','f',' ','t','h','e',' ','r','o','o','m',',',' ','r','e','v','e','a','l','i','n','g',
        ' ','t','h','e',' ','d','u','s','t','y',' ','c','o','v','e','r',' ','o','f',' ','a',' ','c','l',
        'o','s','e','d',' ','t','r','a','p',' ','d','o','o','r','.',0,
        // 167: "ECHO echo echo ..."
        'E','C','H','O',' ','e','c','h','o',' ','e','c','h','o',' ','.','.','.',0,
        // 168: "Which do you mean, the "
        'W','h','i','c','h',' ','d','o',' ','y','o','u',' ','m','e','a','n',',',' ','t','h','e',' ',0,
        // 169: "That can't contain things."
        'T','h','a','t',' ','c','a','n','\'','t',' ','c','o','n','t','a','i','n',' ','t','h','i','n',
        'g','s','.',0,
        // 170: "Congratulations! You have mastered Mini-..."
        'C','o','n','g','r','a','t','u','l','a','t','i','o','n','s','!',' ','Y','o','u',' ','h','a','v',
        'e',' ','m','a','s','t','e','r','e','d',' ','M','i','n','i','-','Z','O','R','K',' ','I',':',' ',
        'T','h','e',' ','G','r','e','a','t',' ','U','n','d','e','r','g','r','o','u','n','d',' ','E','m',
        'p','i','r','e','!',' ','Y','o','u','r',' ','c','o','n','q','u','e','s','t',' ','o','f',' ','t',
        'h','e',' ','d','u','n','g','e','o','n',' ','h','a','s',' ','b','e','e','n',' ','a',' ','t','e',
        's','t','a','m','e','n','t',' ','t','o',' ','y','o','u','r',' ','w','i','s','d','o','m',' ','a',
        'n','d',' ','b','r','a','v','e','r','y','.',0,
        // 171: "Your lamp is getting dim. You'd better f..."
        'Y','o','u','r',' ','l','a','m','p',' ','i','s',' ','g','e','t','t','i','n','g',' ','d','i','m',
        '.',' ','Y','o','u','\'','d',' ','b','e','t','t','e','r',' ','f','i','n','d',' ','a','n','o',
        't','h','e','r',' ','s','o','u','r','c','e',' ','o','f',' ','l','i','g','h','t','.',0,
        // 172: "The trap door is closed."
        'T','h','e',' ','t','r','a','p',' ','d','o','o','r',' ','i','s',' ','c','l','o','s','e','d','.',0,
        // 173: "The troll laughs at your feeble attempt."
        'T','h','e',' ','t','r','o','l','l',' ','l','a','u','g','h','s',' ','a','t',' ','y','o','u','r',
        ' ','f','e','e','b','l','e',' ','a','t','t','e','m','p','t','.',0,
        // 174: "It is already on."
        'I','t',' ','i','s',' ','a','l','r','e','a','d','y',' ','o','n','.',0,
        // 175: "It is already off."
        'I','t',' ','i','s',' ','a','l','r','e','a','d','y',' ','o','f','f','.',0,
        // 176: "You can't move that."
        'Y','o','u',' ','c','a','n','\'','t',' ','m','o','v','e',' ','t','h','a','t','.',0,
    };

    public static final short[] PROSE_IDX = {
        0, // 0
        1, // 1
        2, // 2
        86, // 3
        100, // 4
        256, // 5
        271, // 6
        375, // 7
        390, // 8
        539, // 9
        552, // 10
        719, // 11
        727, // 12
        881, // 13
        893, // 14
        954, // 15
        960, // 16
        1127, // 17
        1139, // 18
        1196, // 19
        1203, // 20
        1324, // 21
        1331, // 22
        1422, // 23
        1429, // 24
        1516, // 25
        1525, // 26
        1678, // 27
        1688, // 28
        1746, // 29
        1763, // 30
        1936, // 31
        1943, // 32
        2121, // 33
        2132, // 34
        2282, // 35
        2296, // 36
        2424, // 37
        2432, // 38
        2471, // 39
        2483, // 40
        2607, // 41
        2618, // 42
        2688, // 43
        2703, // 44
        2848, // 45
        2862, // 46
        3052, // 47
        3062, // 48
        3118, // 49
        3123, // 50
        3179, // 51
        3184, // 52
        3240, // 53
        3245, // 54
        3255, // 55
        3264, // 56
        3360, // 57
        3373, // 58
        3374, // 59
        3375, // 60
        3389, // 61
        3411, // 62
        3423, // 63
        3466, // 64
        3477, // 65
        3535, // 66
        3550, // 67
        3610, // 68
        3629, // 69
        3689, // 70
        3699, // 71
        3724, // 72
        3732, // 73
        3778, // 74
        3792, // 75
        3821, // 76
        3829, // 77
        4016, // 78
        4030, // 79
        4052, // 80
        4065, // 81
        4106, // 82
        4117, // 83
        4172, // 84
        4192, // 85
        4220, // 86
        4236, // 87
        4260, // 88
        4273, // 89
        4320, // 90
        4332, // 91
        4360, // 92
        4373, // 93
        4408, // 94
        4414, // 95
        4500, // 96
        4511, // 97
        4569, // 98
        4582, // 99
        4609, // 100
        4622, // 101
        4720, // 102
        4732, // 103
        4758, // 104
        4772, // 105
        4831, // 106
        4851, // 107
        4909, // 108
        4921, // 109
        4974, // 110
        4986, // 111
        5039, // 112
        5048, // 113
        5140, // 114
        5155, // 115
        5214, // 116
        5227, // 117
        5278, // 118
        5296, // 119
        5342, // 120
        5349, // 121
        5358, // 122
        5381, // 123
        5438, // 124
        5466, // 125
        5496, // 126
        5517, // 127
        5540, // 128
        5561, // 129
        5582, // 130
        5597, // 131
        5617, // 132
        5639, // 133
        5661, // 134
        5669, // 135
        5677, // 136
        5699, // 137
        5717, // 138
        5741, // 139
        5775, // 140
        5839, // 141
        5888, // 142
        5970, // 143
        6104, // 144
        6114, // 145
        6129, // 146
        6144, // 147
        6180, // 148
        6209, // 149
        6239, // 150
        6265, // 151
        6297, // 152
        6334, // 153
        6367, // 154
        6420, // 155
        6481, // 156
        6513, // 157
        6529, // 158
        6582, // 159
        6602, // 160
        6624, // 161
        6650, // 162
        6656, // 163
        6722, // 164
        6748, // 165
        6771, // 166
        6883, // 167
        6902, // 168
        6926, // 169
        6953, // 170
        7109, // 171
        7178, // 172 - S_TRAP_DOOR_CLOSED
        7203, // 173 - S_TROLL_LAUGHS
        7244, // 174 - S_ALREADY_ON
        7262, // 175 - S_ALREADY_OFF
        7281, // 176 - S_CANT_MOVE
    };

    public static short copyProse(short stringId, byte[] dest, short destOff) {
        if (stringId < 0 || stringId >= NUM_STRINGS) return 0;
        short off = PROSE_IDX[stringId];
        short len = 0;
        while (PROSE[(short)(off + len)] != 0) {
            dest[(short)(destOff + len)] = PROSE[(short)(off + len)];
            len++;
        }
        return len;
    }

    public static short proseLength(short stringId) {
        if (stringId < 0 || stringId >= NUM_STRINGS) return 0;
        short off = PROSE_IDX[stringId];
        short len = 0;
        while (PROSE[(short)(off + len)] != 0) len++;
        return len;
    }

    // ===============================================================
    // ROOM TABLE
    // ===============================================================
    public static final byte ROOM_RECORD_SIZE = 10;

    public static final byte[] ROOM_EXITS = {
         2, 3, 0, 9, 2, 0, 0, 0, 0, 0, // Room 1
         0, 1, 4, 9, 0, 0, 4, 1, 0, 0, // Room 2
         1, 0, 4,11, 4, 1, 0, 0, 0, 0, // Room 3
         2, 3,12, 0, 0, 2, 0, 3, 0, 0, // Room 4
         0, 0, 6, 0, 0, 0, 0, 0, 7, 0, // Room 5
         0, 0, 0, 5, 0, 0, 0, 0, 0, 0, // Room 6
         0, 0, 0, 0, 0, 0, 0, 0, 0, 5, // Room 7
        12, 2,10, 9, 0, 0, 0, 0,13, 0, // Room 8
         8,11, 1, 9, 0, 0, 0, 0, 0, 0, // Room 9
         8,12,10, 8, 0, 0, 0, 0, 0, 0, // Room 10
         3,14,12, 9, 0, 0, 0, 0, 0, 0, // Room 11
         8, 8,10, 9, 0, 0, 0, 0, 0, 0, // Room 12
         0, 0, 0, 0, 0, 0, 0, 0, 0, 8, // Room 13
        11,11, 0, 9, 0, 0, 0, 0, 0, 0, // Room 14
        16,24, 0, 0, 0, 0, 0, 0, 0, 0, // Room 15
         0,15, 0, 0, 0, 0, 0, 0, 0, 0, // Room 16
        18, 0, 0,16,20, 0, 0, 0, 0, 0, // Room 17
        19,17, 0, 0, 0, 0, 0, 0, 0, 0, // Room 18
        20,18, 0, 0, 0, 0, 0, 0, 0, 0, // Room 19
        21,19,23,26, 0, 0, 0, 0, 0, 0, // Room 20
        22,20, 0, 0, 0, 0, 0, 0, 0, 0, // Room 21
         0,21, 0, 0, 0, 0, 0, 0, 0, 0, // Room 22
         0, 0, 0,20, 0, 0, 0, 0, 0, 0, // Room 23
        15,26,25,24, 0, 0, 0, 0,28, 0, // Room 24
        26,27,25,24, 0, 0, 0, 0, 0, 0, // Room 25
        24,25,20,26, 0, 0, 0, 0, 0, 0, // Room 26
        25, 0, 0, 0, 0, 0, 0, 0, 0, 0, // Room 27
         0,24, 0, 0, 0, 0, 0, 0, 0, 0, // Room 28
    };

    public static final byte[] ROOM_FLAGS = {
        0x03,0x03,0x03,0x03,0x01,0x01,0x01,0x03,0x03,0x03,0x03,0x03,0x03,0x03,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
    };

    public static byte getRoomExit(byte roomId, byte direction) {
        if (roomId < 1 || roomId > NUM_ROOMS) return R_NONE;
        if (direction < 0 || direction >= ROOM_RECORD_SIZE) return R_NONE;
        return ROOM_EXITS[(short)((roomId - 1) * ROOM_RECORD_SIZE + direction)];
    }

    public static byte getRoomFlags(byte roomId) {
        if (roomId < 1 || roomId > NUM_ROOMS) return 0;
        return ROOM_FLAGS[(short)(roomId - 1)];
    }

    public static boolean isRoomLit(byte roomId) {
        return (getRoomFlags(roomId) & RF_LIT) != 0;
    }

    // ===============================================================
    // OBJECT TABLE
    // ===============================================================
    public static final byte OBJ_RECORD_SIZE = 7;

    public static final byte[] OBJECT_DATA = {
           0,   6,   1,   1,   1,   5,   0, // Obj 1
           0,   2,   6,   0,   2,  50,   5, // Obj 2
           2,   0,   1,  13,   3,   0,   0, // Obj 3
           2,   4,   4,  14,   4,   0,   3, // Obj 4
           0,   0,   6,   0,   5,   0,   7, // Obj 5
           6,   4,   6,  15,   6,   0,   2, // Obj 6
           2,   4,  14,   0,   7,   0,   4, // Obj 7
           1,   0,   5,  14,   8,  20,   0, // Obj 8
           0,  17,   1,   0,   9,   0,   0, // Obj 9
           0,   9,   6,   2,  10,   0,   6, // Obj 10
           0,  33,   6,   3,  11,   3,   0, // Obj 11
           0,   7,   5,   4,  12,   9,   0, // Obj 12
           0,   1,  12,   0,  13,   0,   0, // Obj 13
           0,   1,  12,   0,  14,   0,   0, // Obj 14
           0,   3,   5,   0,  15,   4,   0, // Obj 15
           0,  33,   7,   5,  16,   2,   0, // Obj 16
           0,   1,   7,   0,  17,   0,   0, // Obj 17
           0,-128,  16,   0,  18,   0,   1, // Obj 18
           0,  33,  18,   0,  19,   4,   0, // Obj 19
           0,   1,  25,   7,  20,   0,   0, // Obj 20
           0,  65,  26,   0,  21,  10,   0, // Obj 21
           0,  33,  27,   6,  16,   1,   0, // Obj 22
           0,   0,  24,   0,  22,   0,   0, // Obj 23
           0,  67,  13,   8,  23,   5,   0, // Obj 24
           0,   3,  13,   0,  24,  20,   0, // Obj 25
           0,  73,  22,   9,  25,   6,   0, // Obj 26
           0,  65,  18,   0,  26,   6,   0, // Obj 27
           0,  65,  22,  12,  27,   5,   0, // Obj 28
           0,  65,  23,  10,  28,   5,   0, // Obj 29
           0,  65,  20,  11,  29,   5,   0, // Obj 30
    };

    public static short getObjFlags(byte objId) {
        if (objId < 1 || objId > NUM_OBJECTS) return 0;
        short base = (short)((objId - 1) * OBJ_RECORD_SIZE);
        return (short)(((OBJECT_DATA[base] & 0xFF) << 8) | (OBJECT_DATA[(short)(base + 1)] & 0xFF));
    }

    public static byte getObjInitialLocation(byte objId) {
        if (objId < 1 || objId > NUM_OBJECTS) return 0;
        return OBJECT_DATA[(short)((objId - 1) * OBJ_RECORD_SIZE + 2)];
    }

    public static byte getObjAdjective(byte objId) {
        if (objId < 1 || objId > NUM_OBJECTS) return 0;
        return OBJECT_DATA[(short)((objId - 1) * OBJ_RECORD_SIZE + 3)];
    }

    public static byte getObjNoun(byte objId) {
        if (objId < 1 || objId > NUM_OBJECTS) return 0;
        return OBJECT_DATA[(short)((objId - 1) * OBJ_RECORD_SIZE + 4)];
    }

    public static byte getObjCapacity(byte objId) {
        if (objId < 1 || objId > NUM_OBJECTS) return 0;
        return OBJECT_DATA[(short)((objId - 1) * OBJ_RECORD_SIZE + 5)];
    }

    public static byte getObjHandler(byte objId) {
        if (objId < 1 || objId > NUM_OBJECTS) return 0;
        return OBJECT_DATA[(short)((objId - 1) * OBJ_RECORD_SIZE + 6)];
    }

    public static boolean objHasFlag(byte objId, short flag) {
        return (getObjFlags(objId) & flag) != 0;
    }

    // ===============================================================
    // VOCABULARY TABLE
    // ===============================================================
    public static final byte VOCAB_ENTRY_SIZE = 6;

    public static final byte[] VOCAB = {
        'n','o','r','t', 0,0, // nort DIR 0
        'n',' ',' ',' ', 0,0, // n DIR 0
        's','o','u','t', 0,1, // sout DIR 1
        's',' ',' ',' ', 0,1, // s DIR 1
        'e','a','s','t', 0,2, // east DIR 2
        'e',' ',' ',' ', 0,2, // e DIR 2
        'w','e','s','t', 0,3, // west DIR 3
        'w',' ',' ',' ', 0,3, // w DIR 3
        'n','o','r','t', 0,4, // nort DIR 4
        'n','e',' ',' ', 0,4, // ne DIR 4
        'n','w',' ',' ', 0,5, // nw DIR 5
        's','e',' ',' ', 0,6, // se DIR 6
        's','w',' ',' ', 0,7, // sw DIR 7
        'u','p',' ',' ', 0,8, // up DIR 8
        'u',' ',' ',' ', 0,8, // u DIR 8
        'd','o','w','n', 0,9, // down DIR 9
        'd',' ',' ',' ', 0,9, // d DIR 9
        'i','n',' ',' ', 0,10, // in DIR 10
        'e','n','t','e', 0,10, // ente DIR 10
        'o','u','t',' ', 0,11, // out DIR 11
        'e','x','i','t', 0,11, // exit DIR 11
        'l','e','a','v', 0,11, // leav DIR 11
        'l','o','o','k', 1,12, // look VERB 12
        'l',' ',' ',' ', 1,12, // l VERB 12
        'e','x','a','m', 1,13, // exam VERB 13
        'x',' ',' ',' ', 1,13, // x VERB 13
        't','a','k','e', 1,14, // take VERB 14
        'g','e','t',' ', 1,14, // get VERB 14
        'g','r','a','b', 1,14, // grab VERB 14
        'p','i','c','k', 1,14, // pick VERB 14
        'd','r','o','p', 1,15, // drop VERB 15
        'i','n','v','e', 1,16, // inve VERB 16
        'i',' ',' ',' ', 1,16, // i VERB 16
        'o','p','e','n', 1,17, // open VERB 17
        'c','l','o','s', 1,18, // clos VERB 18
        's','h','u','t', 1,18, // shut VERB 18
        'a','t','t','a', 1,19, // atta VERB 19
        'k','i','l','l', 1,19, // kill VERB 19
        'f','i','g','h', 1,19, // figh VERB 19
        'h','i','t',' ', 1,19, // hit VERB 19
        'l','i','g','h', 1,20, // ligh VERB 20
        't','u','r','n', 1,38, // turn VERB 38
        'e','x','t','i', 1,21, // exti VERB 21
        'b','l','o','w', 1,21, // blow VERB 21
        'd','o','u','s', 1,21, // dous VERB 21
        'r','e','a','d', 1,22, // read VERB 22
        'p','u','t',' ', 1,23, // put VERB 23
        'p','l','a','c', 1,23, // plac VERB 23
        'i','n','s','e', 1,23, // inse VERB 23
        's','c','o','r', 1,24, // scor VERB 24
        'w','a','i','t', 1,25, // wait VERB 25
        'z',' ',' ',' ', 1,25, // z VERB 25
        'a','g','a','i', 1,26, // agai VERB 26
        'g',' ',' ',' ', 1,26, // g VERB 26
        'r','e','s','t', 1,27, // rest VERB 27
        'q','u','i','t', 1,28, // quit VERB 28
        'q',' ',' ',' ', 1,28, // q VERB 28
        'b','r','i','e', 1,29, // brie VERB 29
        'v','e','r','b', 1,30, // verb VERB 30
        'm','o','v','e', 1,31, // move VERB 31
        'p','u','s','h', 1,31, // push VERB 31
        'p','u','l','l', 1,31, // pull VERB 31
        'c','l','i','m', 1,32, // clim VERB 32
        'g','o',' ',' ', 1,12, // go VERB 12
        'd','i','a','g', 1,35, // diag VERB 35
        's','a','v','e', 1,36, // save VERB 36
        't','i','e',' ', 1,39, // tie VERB 39
        'm','a','i','l', 2,1, // mail NOUN 1
        'b','o','x',' ', 2,1, // box NOUN 1
        'c','a','s','e', 2,2, // case NOUN 2
        't','r','o','p', 2,2, // trop NOUN 2
        'd','o','o','r', 2,3, // door NOUN 3
        'w','i','n','d', 2,4, // wind NOUN 4
        'r','u','g',' ', 2,5, // rug NOUN 5
        'c','a','r','p', 2,5, // carp NOUN 5
        't','r','a','p', 2,6, // trap NOUN 6
        'g','r','a','t', 2,7, // grat NOUN 7
        't','a','b','l', 2,8, // tabl NOUN 8
        'l','e','a','f', 2,9, // leaf NOUN 9
        'p','a','p','e', 2,9, // pape NOUN 9
        'l','a','m','p', 2,10, // lamp NOUN 10
        'l','a','n','t', 2,10, // lant NOUN 10
        's','w','o','r', 2,11, // swor NOUN 11
        's','a','c','k', 2,12, // sack NOUN 12
        'b','a','g',' ', 2,12, // bag NOUN 12
        'l','u','n','c', 2,13, // lunc NOUN 13
        'f','o','o','d', 2,13, // food NOUN 13
        's','a','n','d', 2,13, // sand NOUN 13
        'g','a','r','l', 2,14, // garl NOUN 14
        'b','o','t','t', 2,15, // bott NOUN 15
        'k','n','i','f', 2,16, // knif NOUN 16
        'r','o','p','e', 2,17, // rope NOUN 17
        't','r','o','l', 2,18, // trol NOUN 18
        'a','x','e',' ', 2,19, // axe NOUN 19
        'k','e','y',' ', 2,20, // key NOUN 20
        'k','e','y','s', 2,20, // keys NOUN 20
        'c','o','i','n', 2,21, // coin NOUN 21
        'm','o','n','e', 2,21, // mone NOUN 21
        'b','o','n','e', 2,22, // bone NOUN 22
        's','k','e','l', 2,22, // skel NOUN 22
        'e','g','g',' ', 2,23, // egg NOUN 23
        'j','e','w','e', 2,23, // jewe NOUN 23
        'n','e','s','t', 2,24, // nest NOUN 24
        't','o','r','c', 2,25, // torc NOUN 25
        'p','a','i','n', 2,26, // pain NOUN 26
        'c','h','a','l', 2,27, // chal NOUN 27
        'b','a','r',' ', 2,28, // bar NOUN 28
        'p','l','a','t', 2,28, // plat NOUN 28
        'b','r','a','c', 2,29, // brac NOUN 29
        'a','l','l',' ', 2,30, // all NOUN 30
        'e','v','e','r', 2,30, // ever NOUN 30
        's','e','l','f', 2,31, // self NOUN 31
        'm','e',' ',' ', 2,31, // me NOUN 31
        'm','y','s','e', 2,31, // myse NOUN 31
        'h','o','u','s', 2,32, // hous NOUN 32
        't','r','e','e', 2,33, // tree NOUN 33
        'w','a','t','e', 2,34, // wate NOUN 34
        's','m','a','l', 3,1, // smal ADJ 1
        'b','r','a','s', 3,2, // bras ADJ 2
        'e','l','v','i', 3,3, // elvi ADJ 3
        'b','r','o','w', 3,4, // brow ADJ 4
        'n','a','s','t', 3,5, // nast ADJ 5
        'r','u','s','t', 3,6, // rust ADJ 6
        'i','v','o','r', 3,9, // ivor ADJ 9
        's','i','l','v', 3,12, // silv ADJ 12
        's','a','p','p', 3,11, // sapp ADJ 11
        'f','r','o','n', 3,13, // fron ADJ 13
        'k','i','t','c', 3,14, // kitc ADJ 14
        'w','i','t','h', 4,3, // with PREP 3
        'u','s','i','n', 4,3, // usin PREP 3
        'o','n',' ',' ', 4,2, // on PREP 2
        'o','n','t','o', 4,2, // onto PREP 2
        'i','n',' ',' ', 4,1, // in PREP 1
        'i','n','t','o', 4,1, // into PREP 1
        'a','t',' ',' ', 4,4, // at PREP 4
        't','o',' ',' ', 4,5, // to PREP 5
        'f','r','o','m', 4,6, // from PREP 6
        'u','n','d','e', 4,7, // unde PREP 7
    };

    public static short getVocabSize() {
        return (short)(VOCAB.length / VOCAB_ENTRY_SIZE);
    }
}
