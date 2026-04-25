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
    static final byte CLA_ZORK = (byte) 0x80;
    static final byte INS_COMMAND = (byte) 0x10;
    static final byte INS_LOOK = (byte) 0x12;
    static final byte INS_RESTART = (byte) 0x14;
    static final byte INS_GET_RESPONSE = (byte) 0xC0;

    // ---------------------------------------------------------------
    // Direction Indices (0-11) - also used as verb IDs for movement
    // ---------------------------------------------------------------
    static final byte DIR_NORTH = 0;
    static final byte DIR_SOUTH = 1;
    static final byte DIR_EAST = 2;
    static final byte DIR_WEST = 3;
    static final byte DIR_NORTHEAST = 4;
    static final byte DIR_NORTHWEST = 5;
    static final byte DIR_SOUTHEAST = 6;
    static final byte DIR_SOUTHWEST = 7;
    static final byte DIR_UP = 8;
    static final byte DIR_DOWN = 9;
    static final byte DIR_IN = 10;
    static final byte DIR_OUT = 11;
    static final byte NUM_BASIC_DIRS = 8;  // N,S,E,W,NE,NW,SE,SW stored in room record
    static final byte NUM_ALL_DIRS = 12;

    // ---------------------------------------------------------------
    // Verb IDs (directions are 0-11, verbs start at 12)
    // ---------------------------------------------------------------
    static final byte VERB_LOOK = 12;
    static final byte VERB_EXAMINE = 13;
    static final byte VERB_TAKE = 14;
    static final byte VERB_DROP = 15;
    static final byte VERB_INVENTORY = 16;
    static final byte VERB_OPEN = 17;
    static final byte VERB_CLOSE = 18;
    static final byte VERB_ATTACK = 19;
    static final byte VERB_LIGHT = 20;
    static final byte VERB_EXTINGUISH = 21;
    static final byte VERB_READ = 22;
    static final byte VERB_PUT = 23;
    static final byte VERB_SCORE = 24;
    static final byte VERB_WAIT = 25;
    static final byte VERB_AGAIN = 26;
    static final byte VERB_RESTART = 27;
    static final byte VERB_QUIT = 28;
    static final byte VERB_BRIEF = 29;
    static final byte VERB_VERBOSE = 30;
    static final byte VERB_MOVE = 31;
    static final byte VERB_CLIMB = 32;
    static final byte VERB_ENTER = 33;
    static final byte VERB_EXIT = 34;
    static final byte VERB_DIAGNOSE = 35;
    static final byte VERB_SAVE = 36;
    static final byte VERB_RESTORE = 37;
    static final byte VERB_TURN = 38;
    static final byte VERB_TIE = 39;

    // ---------------------------------------------------------------
    // Room IDs (1-based, 0 = no room)
    // ---------------------------------------------------------------
    static final byte R_NONE = 0;
    // Surface
    static final byte R_WEST_OF_HOUSE = 1;
    static final byte R_NORTH_OF_HOUSE = 2;
    static final byte R_SOUTH_OF_HOUSE = 3;
    static final byte R_BEHIND_HOUSE = 4;
    static final byte R_KITCHEN = 5;
    static final byte R_LIVING_ROOM = 6;
    static final byte R_ATTIC = 7;
    static final byte R_FOREST_PATH = 8;
    static final byte R_FOREST_WEST = 9;
    static final byte R_FOREST_EAST = 10;
    static final byte R_FOREST_SOUTH = 11;
    static final byte R_CLEARING = 12;
    static final byte R_UP_A_TREE = 13;
    static final byte R_GRATING_CLEARING = 14;
    // Underground
    static final byte R_CELLAR = 15;
    static final byte R_TROLL_ROOM = 16;
    static final byte R_EAST_OF_CHASM = 17;
    static final byte R_GALLERY = 18;
    static final byte R_NS_PASSAGE = 19;
    static final byte R_ROUND_ROOM = 20;
    static final byte R_NARROW_PASSAGE = 21;
    static final byte R_TREASURE_ROOM = 22;
    static final byte R_LOUD_ROOM = 23;
    static final byte R_MAZE_1 = 24;
    static final byte R_MAZE_2 = 25;
    static final byte R_MAZE_3 = 26;
    static final byte R_DEAD_END = 27;
    static final byte R_GRATING_ROOM = 28;
    static final byte NUM_ROOMS = 28;

    // ---------------------------------------------------------------
    // Room flags
    // ---------------------------------------------------------------
    static final byte RF_LIT = 0x01;         // Room is naturally lit
    static final byte RF_OUTSIDE = 0x02;     // Outdoor room

    // ---------------------------------------------------------------
    // Object IDs (1-based, 0 = no object)
    // ---------------------------------------------------------------
    static final byte O_NONE = 0;
    // Scenery / Fixed
    static final byte O_MAILBOX = 1;
    static final byte O_TROPHY_CASE = 2;
    static final byte O_FRONT_DOOR = 3;
    static final byte O_KITCHEN_WINDOW = 4;
    static final byte O_RUG = 5;
    static final byte O_TRAP_DOOR = 6;
    static final byte O_GRATING = 7;
    static final byte O_KITCHEN_TABLE = 8;
    // Takeable items
    static final byte O_LEAFLET = 9;
    static final byte O_LAMP = 10;
    static final byte O_SWORD = 11;
    static final byte O_BROWN_SACK = 12;
    static final byte O_LUNCH = 13;
    static final byte O_GARLIC = 14;
    static final byte O_BOTTLE = 15;
    static final byte O_NASTY_KNIFE = 16;
    static final byte O_ROPE = 17;
    // Creatures
    static final byte O_TROLL = 18;
    static final byte O_AXE = 19;
    // Maze items
    static final byte O_SKELETON_KEY = 20;
    static final byte O_BAG_OF_COINS = 21;
    static final byte O_RUSTY_KNIFE = 22;
    static final byte O_BONES = 23;
    // Treasures and misc
    static final byte O_EGG = 24;
    static final byte O_NEST = 25;
    static final byte O_TORCH = 26;
    static final byte O_PAINTING = 27;
    static final byte O_CHALICE = 28;
    static final byte O_BAR = 29;
    static final byte O_BRACELET = 30;
    static final byte NUM_OBJECTS = 30;

    // ---------------------------------------------------------------
    // Object flags (bitmask in short)
    // ---------------------------------------------------------------
    static final short OF_TAKEABLE = 0x0001;
    static final short OF_CONTAINER = 0x0002;
    static final short OF_OPENABLE = 0x0004;
    static final short OF_LIGHT = 0x0008;
    static final short OF_READABLE = 0x0010;
    static final short OF_WEAPON = 0x0020;
    static final short OF_TREASURE = 0x0040;
    static final short OF_ACTOR = 0x0080;
    static final short OF_SURFACE = 0x0100;  // objects can be placed ON it
    static final short OF_DOOR = 0x0200;
    static final short OF_INVISIBLE = 0x0400; // not listed in room
    static final short OF_FLAMMABLE = 0x0800;

    // ---------------------------------------------------------------
    // Mutable object state flags (per-object byte in dynamic state)
    // ---------------------------------------------------------------
    static final byte OS_OPEN = 0x01;
    static final byte OS_ON = 0x02;     // light source is on
    static final byte OS_MOVED = 0x04;  // object has been moved from initial position
    static final byte OS_DEAD = 0x08;   // creature is dead
    static final byte OS_HIDDEN = 0x10; // currently hidden/invisible

    // ---------------------------------------------------------------
    // Game state flags (2-byte bitfield in STATE)
    // ---------------------------------------------------------------
    static final short GF_TROLL_DEAD = 0x0001;
    static final short GF_TRAP_OPEN = 0x0002;
    static final short GF_WINDOW_OPEN = 0x0004;
    static final short GF_RUG_MOVED = 0x0008;
    static final short GF_GRATING_OPEN = 0x0010;
    static final short GF_GRATING_REVEALED = 0x0020;
    static final short GF_GAME_OVER = 0x0040;
    static final short GF_CELLAR_VISITED = 0x0080;

    // ---------------------------------------------------------------
    // Special location values for object placement
    // ---------------------------------------------------------------
    static final short LOC_NOWHERE = 0;
    static final short LOC_PLAYER = (short) 200;
    // LOC_INSIDE_OBJ_BASE + obj_id = inside that container
    static final short LOC_INSIDE_OBJ_BASE = (short) 201;

    // ---------------------------------------------------------------
    // Noun IDs (for object resolution)
    // ---------------------------------------------------------------
    static final byte N_NONE = 0;
    static final byte N_MAILBOX = 1;
    static final byte N_CASE = 2;
    static final byte N_DOOR = 3;
    static final byte N_WINDOW = 4;
    static final byte N_RUG = 5;
    static final byte N_TRAP = 6;
    static final byte N_GRATING = 7;
    static final byte N_TABLE = 8;
    static final byte N_LEAFLET = 9;
    static final byte N_LAMP = 10;
    static final byte N_SWORD = 11;
    static final byte N_SACK = 12;
    static final byte N_LUNCH = 13;
    static final byte N_GARLIC = 14;
    static final byte N_BOTTLE = 15;
    static final byte N_KNIFE = 16;
    static final byte N_ROPE = 17;
    static final byte N_TROLL = 18;
    static final byte N_AXE = 19;
    static final byte N_KEY = 20;
    static final byte N_COINS = 21;
    static final byte N_BONES = 22;
    static final byte N_EGG = 23;
    static final byte N_NEST = 24;
    static final byte N_TORCH = 25;
    static final byte N_PAINTING = 26;
    static final byte N_CHALICE = 27;
    static final byte N_BAR = 28;
    static final byte N_BRACELET = 29;
    static final byte N_ALL = 30;
    static final byte N_SELF = 31;
    static final byte N_HOUSE = 32;
    static final byte N_TREE = 33;

    // ---------------------------------------------------------------
    // Adjective IDs
    // ---------------------------------------------------------------
    static final byte A_NONE = 0;
    static final byte A_SMALL = 1;
    static final byte A_BRASS = 2;
    static final byte A_ELVISH = 3;
    static final byte A_BROWN = 4;
    static final byte A_NASTY = 5;
    static final byte A_RUSTY = 6;
    static final byte A_SKELETON = 7;
    static final byte A_JEWELED = 8;
    static final byte A_IVORY = 9;
    static final byte A_PLATINUM = 10;
    static final byte A_SAPPHIRE = 11;
    static final byte A_SILVER = 12;
    static final byte A_FRONT = 13;
    static final byte A_KITCHEN = 14;
    static final byte A_TRAP = 15;

    // ---------------------------------------------------------------
    // Preposition IDs
    // ---------------------------------------------------------------
    static final byte P_NONE = 0;
    static final byte P_IN = 1;
    static final byte P_ON = 2;
    static final byte P_WITH = 3;
    static final byte P_AT = 4;
    static final byte P_TO = 5;
    static final byte P_FROM = 6;
    static final byte P_UNDER = 7;

    // ---------------------------------------------------------------
    // Vocab entry types
    // ---------------------------------------------------------------
    static final byte VT_DIRECTION = 0;
    static final byte VT_VERB = 1;
    static final byte VT_NOUN = 2;
    static final byte VT_ADJECTIVE = 3;
    static final byte VT_PREPOSITION = 4;
    static final byte VT_SPECIAL = 5;

    // ---------------------------------------------------------------
    // Special handler IDs
    // ---------------------------------------------------------------
    static final byte SH_NONE = 0;
    static final byte SH_TROLL = 1;
    static final byte SH_TRAP_DOOR = 2;
    static final byte SH_WINDOW = 3;
    static final byte SH_GRATING = 4;
    static final byte SH_TROPHY_CASE = 5;
    static final byte SH_LAMP = 6;
    static final byte SH_RUG = 7;
    static final byte SH_LOUD_ROOM = 8;

    // ---------------------------------------------------------------
    // Description mode
    // ---------------------------------------------------------------
    static final byte MODE_BRIEF = 0;
    static final byte MODE_VERBOSE = 1;
    static final byte MODE_SUPERBRIEF = 2;

    // ---------------------------------------------------------------
    // Treasure values (points when placed in trophy case)
    // Index by object ID - 1 (only for treasure objects)
    // ---------------------------------------------------------------
    static byte getTreasureValue(byte objId) {
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

    static final byte MAX_SCORE = 50; // 42 from treasures + 8 bonus
    static final byte MAX_INVENTORY = 8; // max items player can carry
    static final short LAMP_FUEL_INITIAL = 200; // turns of light

    // ===============================================================
    // PROSE TABLE
    // ===============================================================
    // String IDs for all game text.
    // NOTE: Uses String[] for simulator. Replace with byte[] for card.

    // String ID constants
    // Room descriptions: room_id * 2 = long desc, room_id * 2 + 1 = name
    // Offset by 0 since room IDs start at 1
    static short roomLongDesc(byte roomId) { return (short)(roomId * 2); }
    static short roomName(byte roomId) { return (short)(roomId * 2 + 1); }

    // Object strings start after rooms
    static final short OBJ_STR_BASE = (short)((NUM_ROOMS + 1) * 2);
    static short objName(byte objId) { return (short)(OBJ_STR_BASE + objId * 2); }
    static short objDesc(byte objId) { return (short)(OBJ_STR_BASE + objId * 2 + 1); }

    // Stock response strings start after objects
    static final short STOCK_STR_BASE = (short)(OBJ_STR_BASE + (NUM_OBJECTS + 1) * 2);
    static final short S_TAKEN = (short)(STOCK_STR_BASE + 0);
    static final short S_DROPPED = (short)(STOCK_STR_BASE + 1);
    static final short S_CANT_GO = (short)(STOCK_STR_BASE + 2);
    static final short S_DARK = (short)(STOCK_STR_BASE + 3);
    static final short S_DARK_WARNING = (short)(STOCK_STR_BASE + 4);
    static final short S_CANT_SEE = (short)(STOCK_STR_BASE + 5);
    static final short S_DONT_HAVE = (short)(STOCK_STR_BASE + 6);
    static final short S_ALREADY_HAVE = (short)(STOCK_STR_BASE + 7);
    static final short S_CANT_TAKE = (short)(STOCK_STR_BASE + 8);
    static final short S_HANDS_FULL = (short)(STOCK_STR_BASE + 9);
    static final short S_NOT_OPEN = (short)(STOCK_STR_BASE + 10);
    static final short S_ALREADY_OPEN = (short)(STOCK_STR_BASE + 11);
    static final short S_NOT_CLOSEABLE = (short)(STOCK_STR_BASE + 12);
    static final short S_ALREADY_CLOSED = (short)(STOCK_STR_BASE + 13);
    static final short S_OPENED = (short)(STOCK_STR_BASE + 14);
    static final short S_CLOSED = (short)(STOCK_STR_BASE + 15);
    static final short S_EMPTY_HANDED = (short)(STOCK_STR_BASE + 16);
    static final short S_CARRYING = (short)(STOCK_STR_BASE + 17);
    static final short S_UNKNOWN_WORD = (short)(STOCK_STR_BASE + 18);
    static final short S_HUH = (short)(STOCK_STR_BASE + 19);
    static final short S_GRUE = (short)(STOCK_STR_BASE + 20);
    static final short S_TROLL_BLOCKS = (short)(STOCK_STR_BASE + 21);
    static final short S_TROLL_DIES = (short)(STOCK_STR_BASE + 22);
    static final short S_INTRO = (short)(STOCK_STR_BASE + 23);
    static final short S_RESUMING = (short)(STOCK_STR_BASE + 24);
    static final short S_SCORE_MSG = (short)(STOCK_STR_BASE + 25);
    static final short S_TIME_PASSES = (short)(STOCK_STR_BASE + 26);
    static final short S_CANT_ATTACK = (short)(STOCK_STR_BASE + 27);
    static final short S_LAMP_ON = (short)(STOCK_STR_BASE + 28);
    static final short S_LAMP_OFF = (short)(STOCK_STR_BASE + 29);
    static final short S_LAMP_DIM = (short)(STOCK_STR_BASE + 30);
    static final short S_LAMP_DEAD = (short)(STOCK_STR_BASE + 31);
    static final short S_NOTHING_SPECIAL = (short)(STOCK_STR_BASE + 32);
    static final short S_NOT_READABLE = (short)(STOCK_STR_BASE + 33);
    static final short S_BOARDED = (short)(STOCK_STR_BASE + 34);
    static final short S_TRAP_CLOSED_BEHIND = (short)(STOCK_STR_BASE + 35);
    static final short S_GAME_OVER = (short)(STOCK_STR_BASE + 36);
    static final short S_RESTART_MSG = (short)(STOCK_STR_BASE + 37);
    static final short S_SAVE_MSG = (short)(STOCK_STR_BASE + 38);
    static final short S_BRIEF_MODE = (short)(STOCK_STR_BASE + 39);
    static final short S_VERBOSE_MODE = (short)(STOCK_STR_BASE + 40);
    static final short S_CANT_PUT = (short)(STOCK_STR_BASE + 41);
    static final short S_PUT_IN = (short)(STOCK_STR_BASE + 42);
    static final short S_WINDOW_OPEN = (short)(STOCK_STR_BASE + 43);
    static final short S_WINDOW_CLOSED = (short)(STOCK_STR_BASE + 44);
    static final short S_GRATING_LOCKED = (short)(STOCK_STR_BASE + 45);
    static final short S_RUG_MOVED = (short)(STOCK_STR_BASE + 46);
    static final short S_LOUD_ECHO = (short)(STOCK_STR_BASE + 47);
    static final short S_WHICH_DO_YOU_MEAN = (short)(STOCK_STR_BASE + 48);
    static final short S_NOT_CONTAINER = (short)(STOCK_STR_BASE + 49);
    static final short S_VICTORY = (short)(STOCK_STR_BASE + 50);
    static final short S_LAMP_GETTING_DIM = (short)(STOCK_STR_BASE + 51);

    // Total number of strings
    static final short NUM_STRINGS = (short)(STOCK_STR_BASE + 52);

    /**
     * Master prose table. Index = string ID.
     * Room long descs at roomId*2, room names at roomId*2+1.
     * Object names at OBJ_STR_BASE + objId*2, descs at +1.
     * Stock responses at STOCK_STR_BASE + n.
     */
    static final String[] PROSE = buildProseTable();

    private static String[] buildProseTable() {
        String[] p = new String[NUM_STRINGS];

        // Default everything to empty
        for (int i = 0; i < p.length; i++) p[i] = "";

        // ---- Room descriptions ----
        // Room 1: West of House
        p[R_WEST_OF_HOUSE * 2] =
            "You are standing in an open field west of a white house, with a boarded front door.";
        p[R_WEST_OF_HOUSE * 2 + 1] = "West of House";

        // Room 2: North of House
        p[R_NORTH_OF_HOUSE * 2] =
            "You are facing the north side of a white house. There is no door here, and all the windows are boarded up. A narrow path leads round the house to the east.";
        p[R_NORTH_OF_HOUSE * 2 + 1] = "North of House";

        // Room 3: South of House
        p[R_SOUTH_OF_HOUSE * 2] =
            "You are facing the south side of a white house. There is no door here, and all the windows are boarded.";
        p[R_SOUTH_OF_HOUSE * 2 + 1] = "South of House";

        // Room 4: Behind House
        p[R_BEHIND_HOUSE * 2] =
            "You are behind the white house. A path leads into the forest to the east. In one corner of the house there is a small window which is slightly ajar.";
        p[R_BEHIND_HOUSE * 2 + 1] = "Behind House";

        // Room 5: Kitchen
        p[R_KITCHEN * 2] =
            "You are in the kitchen of the white house. A table seems to have been used recently for the preparation of food. A passage leads to the west and a staircase leads up.";
        p[R_KITCHEN * 2 + 1] = "Kitchen";

        // Room 6: Living Room
        p[R_LIVING_ROOM * 2] =
            "You are in the living room. There is a doorway to the east, and a wooden door with strange gothic lettering to the west, which appears to be nailed shut.";
        p[R_LIVING_ROOM * 2 + 1] = "Living Room";

        // Room 7: Attic
        p[R_ATTIC * 2] =
            "This is the attic. The only exit is a stairway leading down.";
        p[R_ATTIC * 2 + 1] = "Attic";

        // Room 8: Forest Path
        p[R_FOREST_PATH * 2] =
            "This is a path winding through a dimly lit forest. The path heads north-south here. One particularly large tree with some low branches stands at the edge of the path.";
        p[R_FOREST_PATH * 2 + 1] = "Forest Path";

        // Room 9: Forest West
        p[R_FOREST_WEST * 2] =
            "This is a dimly lit forest, with large trees all around.";
        p[R_FOREST_WEST * 2 + 1] = "Forest";

        // Room 10: Forest East
        p[R_FOREST_EAST * 2] =
            "This is a dimly lit forest, with large trees all around. One particularly large tree with some low branches stands here.";
        p[R_FOREST_EAST * 2 + 1] = "Forest";

        // Room 11: Forest South
        p[R_FOREST_SOUTH * 2] =
            "This is a forest, with trees in all directions. To the east, there appears to be sunlight.";
        p[R_FOREST_SOUTH * 2 + 1] = "Forest";

        // Room 12: Clearing
        p[R_CLEARING * 2] =
            "You are in a clearing, with a forest surrounding you on all sides. A path leads south.";
        p[R_CLEARING * 2 + 1] = "Clearing";

        // Room 13: Up a Tree
        p[R_UP_A_TREE * 2] =
            "You are about 10 feet above the ground nestled among some large branches. The nearest branch above you is beyond your reach. Below you is a forest path.";
        p[R_UP_A_TREE * 2 + 1] = "Up a Tree";

        // Room 14: Grating Clearing
        p[R_GRATING_CLEARING * 2] =
            "You are in a small clearing in a well-marked forest path.";
        p[R_GRATING_CLEARING * 2 + 1] = "Grating Clearing";

        // Room 15: Cellar
        p[R_CELLAR * 2] =
            "You are in a dark and damp cellar with a narrow passageway leading north, and a crawlway to the south. To the west is the bottom of a steep metal ramp which is unclimbable.";
        p[R_CELLAR * 2 + 1] = "Cellar";

        // Room 16: Troll Room
        p[R_TROLL_ROOM * 2] =
            "This is a small room with passages to the east and south and a forbidding hole leading west. Bloodstains and deep scratches (perhaps made by straying adventurers) mar the walls.";
        p[R_TROLL_ROOM * 2 + 1] = "Troll Room";

        // Room 17: East of Chasm
        p[R_EAST_OF_CHASM * 2] =
            "You are on the east edge of a chasm, the bottom of which cannot be seen. A narrow passage leads north, and the path you are on continues to the east.";
        p[R_EAST_OF_CHASM * 2 + 1] = "East of Chasm";

        // Room 18: Gallery
        p[R_GALLERY * 2] =
            "This is an art gallery. Most of the paintings have been stolen by vandals with exceptional taste. A passage exits to the south.";
        p[R_GALLERY * 2 + 1] = "Gallery";

        // Room 19: N-S Passage
        p[R_NS_PASSAGE * 2] =
            "This is a long north-south passageway.";
        p[R_NS_PASSAGE * 2 + 1] = "N-S Passage";

        // Room 20: Round Room
        p[R_ROUND_ROOM * 2] =
            "This is a circular stone room with passages in all directions. Several of them have unfortunately been blocked by cave-ins.";
        p[R_ROUND_ROOM * 2 + 1] = "Round Room";

        // Room 21: Narrow Passage
        p[R_NARROW_PASSAGE * 2] =
            "This is a narrow passage. The passage turns from north to south here.";
        p[R_NARROW_PASSAGE * 2 + 1] = "Narrow Passage";

        // Room 22: Treasure Room
        p[R_TREASURE_ROOM * 2] =
            "This is a large room with a distinctly Gothic feel to it. On the walls are mounted the heads of many animals. A wide passage exits to the south.";
        p[R_TREASURE_ROOM * 2 + 1] = "Treasure Room";

        // Room 23: Loud Room
        p[R_LOUD_ROOM * 2] =
            "This is a large room with a ceiling which cannot be detected from the ground. A narrow passage exits to the west. The room is deafeningly loud with an unidentified, low frequency vibration.";
        p[R_LOUD_ROOM * 2 + 1] = "Loud Room";

        // Room 24: Maze 1
        p[R_MAZE_1 * 2] =
            "You are in a maze of twisty little passages, all alike.";
        p[R_MAZE_1 * 2 + 1] = "Maze";

        // Room 25: Maze 2
        p[R_MAZE_2 * 2] =
            "You are in a maze of twisty little passages, all alike.";
        p[R_MAZE_2 * 2 + 1] = "Maze";

        // Room 26: Maze 3
        p[R_MAZE_3 * 2] =
            "You are in a maze of twisty little passages, all alike.";
        p[R_MAZE_3 * 2 + 1] = "Maze";

        // Room 27: Dead End
        p[R_DEAD_END * 2] =
            "Dead end.";
        p[R_DEAD_END * 2 + 1] = "Dead End";

        // Room 28: Grating Room
        p[R_GRATING_ROOM * 2] =
            "You are in a small room near the surface. A narrow passage leads south. Above you is a grating.";
        p[R_GRATING_ROOM * 2 + 1] = "Grating Room";

        // ---- Object names and examine descriptions ----
        int b = OBJ_STR_BASE;

        // O_MAILBOX (1)
        p[b + O_MAILBOX * 2] = "small mailbox";
        p[b + O_MAILBOX * 2 + 1] = "It's a small mailbox.";

        // O_TROPHY_CASE (2)
        p[b + O_TROPHY_CASE * 2] = "trophy case";
        p[b + O_TROPHY_CASE * 2 + 1] = "It's a large trophy case, currently empty.";

        // O_FRONT_DOOR (3)
        p[b + O_FRONT_DOOR * 2] = "front door";
        p[b + O_FRONT_DOOR * 2 + 1] = "The door is boarded shut and you can't remove the boards.";

        // O_KITCHEN_WINDOW (4)
        p[b + O_KITCHEN_WINDOW * 2] = "kitchen window";
        p[b + O_KITCHEN_WINDOW * 2 + 1] = "The window is slightly ajar, but not enough to allow entry.";

        // O_RUG (5)
        p[b + O_RUG * 2] = "large oriental rug";
        p[b + O_RUG * 2 + 1] = "It's a large oriental rug, covering the center of the room.";

        // O_TRAP_DOOR (6)
        p[b + O_TRAP_DOOR * 2] = "trap door";
        p[b + O_TRAP_DOOR * 2 + 1] = "The trap door is closed.";

        // O_GRATING (7)
        p[b + O_GRATING * 2] = "grating";
        p[b + O_GRATING * 2 + 1] = "The grating is firmly embedded in the ground.";

        // O_KITCHEN_TABLE (8)
        p[b + O_KITCHEN_TABLE * 2] = "kitchen table";
        p[b + O_KITCHEN_TABLE * 2 + 1] = "It's a sturdy kitchen table.";

        // O_LEAFLET (9)
        p[b + O_LEAFLET * 2] = "leaflet";
        p[b + O_LEAFLET * 2 + 1] =
            "WELCOME TO ZORK!\n\nZORK is a game of adventure, danger, and low cunning. In it you will explore some of the most amazing territory ever seen by mortals. No computer should be without one!";

        // O_LAMP (10)
        p[b + O_LAMP * 2] = "brass lantern";
        p[b + O_LAMP * 2 + 1] = "It's a brass lantern.";

        // O_SWORD (11)
        p[b + O_SWORD * 2] = "elvish sword";
        p[b + O_SWORD * 2 + 1] = "It's an elvish sword of great antiquity.";

        // O_BROWN_SACK (12)
        p[b + O_BROWN_SACK * 2] = "brown sack";
        p[b + O_BROWN_SACK * 2 + 1] = "It's an elongated brown sack, smelling of hot peppers.";

        // O_LUNCH (13)
        p[b + O_LUNCH * 2] = "hot pepper sandwich";
        p[b + O_LUNCH * 2 + 1] = "It's a hot pepper sandwich.";

        // O_GARLIC (14)
        p[b + O_GARLIC * 2] = "clove of garlic";
        p[b + O_GARLIC * 2 + 1] = "It's a clove of garlic.";

        // O_BOTTLE (15)
        p[b + O_BOTTLE * 2] = "glass bottle";
        p[b + O_BOTTLE * 2 + 1] = "The glass bottle contains a quantity of water.";

        // O_NASTY_KNIFE (16)
        p[b + O_NASTY_KNIFE * 2] = "nasty knife";
        p[b + O_NASTY_KNIFE * 2 + 1] = "It's a nasty-looking knife.";

        // O_ROPE (17)
        p[b + O_ROPE * 2] = "coil of rope";
        p[b + O_ROPE * 2 + 1] = "It's a length of sturdy hemp rope.";

        // O_TROLL (18)
        p[b + O_TROLL * 2] = "troll";
        p[b + O_TROLL * 2 + 1] = "A nasty-looking troll, brandishing a bloody axe, blocks all passages out of the room.";

        // O_AXE (19)
        p[b + O_AXE * 2] = "bloody axe";
        p[b + O_AXE * 2 + 1] = "It's a large, nasty-looking axe covered with dried blood.";

        // O_SKELETON_KEY (20)
        p[b + O_SKELETON_KEY * 2] = "skeleton key";
        p[b + O_SKELETON_KEY * 2 + 1] = "It's a large skeleton key.";

        // O_BAG_OF_COINS (21)
        p[b + O_BAG_OF_COINS * 2] = "bag of coins";
        p[b + O_BAG_OF_COINS * 2 + 1] = "It's a leather bag filled with gold coins. The coins are inscribed: 'FROBOZZ MAGIC COIN COMPANY'.";

        // O_RUSTY_KNIFE (22)
        p[b + O_RUSTY_KNIFE * 2] = "rusty knife";
        p[b + O_RUSTY_KNIFE * 2 + 1] = "It's a rusty, dull knife.";

        // O_BONES (23)
        p[b + O_BONES * 2] = "pile of bones";
        p[b + O_BONES * 2 + 1] = "These are the mortal remains of an unfortunate adventurer.";

        // O_EGG (24)
        p[b + O_EGG * 2] = "jewel-encrusted egg";
        p[b + O_EGG * 2 + 1] = "It's a beautiful jewel-encrusted egg, delicately crafted.";

        // O_NEST (25)
        p[b + O_NEST * 2] = "bird's nest";
        p[b + O_NEST * 2 + 1] = "It's a large bird's nest, made from straw and twigs.";

        // O_TORCH (26)
        p[b + O_TORCH * 2] = "ivory torch";
        p[b + O_TORCH * 2 + 1] = "It's a beautiful ivory torch, with an eternal flame.";

        // O_PAINTING (27)
        p[b + O_PAINTING * 2] = "painting";
        p[b + O_PAINTING * 2 + 1] = "It's a masterful painting of unparalleled beauty, depicting a sunset over a pastoral scene.";

        // O_CHALICE (28)
        p[b + O_CHALICE * 2] = "silver chalice";
        p[b + O_CHALICE * 2 + 1] = "It's a beautiful silver chalice, encrusted with sapphires.";

        // O_BAR (29)
        p[b + O_BAR * 2] = "platinum bar";
        p[b + O_BAR * 2 + 1] = "It's a bar of solid platinum, about one foot long.";

        // O_BRACELET (30)
        p[b + O_BRACELET * 2] = "sapphire bracelet";
        p[b + O_BRACELET * 2 + 1] = "It's a beautiful sapphire-encrusted bracelet.";

        // ---- Stock responses ----
        int s = STOCK_STR_BASE;
        p[s + 0] = "Taken.";
        p[s + 1] = "Dropped.";
        p[s + 2] = "You can't go that way.";
        p[s + 3] = "It is pitch black. You are likely to be eaten by a grue.";
        p[s + 4] = "You may be eaten by a grue.";
        p[s + 5] = "You can't see any such thing.";
        p[s + 6] = "You don't have that.";
        p[s + 7] = "You already have that.";
        p[s + 8] = "You can't take that.";
        p[s + 9] = "Your hands are full.";
        p[s + 10] = "It isn't open.";
        p[s + 11] = "It is already open.";
        p[s + 12] = "You can't close that.";
        p[s + 13] = "It is already closed.";
        p[s + 14] = "Opened.";
        p[s + 15] = "Closed.";
        p[s + 16] = "You are empty-handed.";
        p[s + 17] = "You are carrying:";
        p[s + 18] = "I don't know the word \"";
        p[s + 19] = "I don't understand that sentence.";
        p[s + 20] = "Oh no! A lurking grue slithered into the room and devoured you!";
        p[s + 21] = "The troll fends you off with a menacing gesture.";
        p[s + 22] = "The troll, disarmed, is overthrown and vanishes in a cloud of greasy black smoke.";
        p[s + 23] =
            "Mini-ZORK I: The Great Underground Empire\n" +
            "A JavaCard interactive fiction by Infocom (reimplemented)\n" +
            "Release 1 / Serial number 260424\n";
        p[s + 24] = "Resuming.";
        p[s + 25] = "Your score is ";
        p[s + 26] = "Time passes...";
        p[s + 27] = "Trying to attack that is pointless.";
        p[s + 28] = "The brass lantern is now on.";
        p[s + 29] = "The brass lantern is now off.";
        p[s + 30] = "Your lamp is getting dim.";
        p[s + 31] = "Your lamp has run out of power.";
        p[s + 32] = "There is nothing special about that.";
        p[s + 33] = "There's nothing to read on that.";
        p[s + 34] = "The door is boarded and you can't remove the boards.";
        p[s + 35] = "The trap door crashes shut, and you hear someone barring it.";
        p[s + 36] = "\n    ****  You have died  ****\n";
        p[s + 37] = "Game restarted.";
        p[s + 38] = "Your game is always saved to the card automatically.";
        p[s + 39] = "Brief descriptions.";
        p[s + 40] = "Verbose descriptions.";
        p[s + 41] = "You can't put that there.";
        p[s + 42] = "Done.";
        p[s + 43] = "With great effort, you open the window far enough to allow entry.";
        p[s + 44] = "The window is now closed.";
        p[s + 45] = "The grating is locked.";
        p[s + 46] = "With a great effort, the rug is moved to one side of the room, revealing the dusty cover of a closed trap door.";
        p[s + 47] = "ECHO echo echo ...";
        p[s + 48] = "Which do you mean, the ";
        p[s + 49] = "That can't contain things.";
        p[s + 50] =
            "Congratulations! You have mastered Mini-ZORK I: The Great Underground Empire! " +
            "Your conquest of the dungeon has been a testament to your wisdom and bravery.";
        p[s + 51] = "Your lamp is getting dim. You'd better find another source of light.";

        return p;
    }

    /**
     * Copy a prose string to a byte buffer.
     * Returns the number of bytes written.
     */
    static short copyProse(short stringId, byte[] dest, short destOff) {
        if (stringId < 0 || stringId >= PROSE.length) return 0;
        String s = PROSE[stringId];
        if (s == null) return 0;
        short len = (short) s.length();
        for (short i = 0; i < len; i++) {
            dest[(short)(destOff + i)] = (byte) s.charAt(i);
        }
        return len;
    }

    static short proseLength(short stringId) {
        if (stringId < 0 || stringId >= PROSE.length) return 0;
        String s = PROSE[stringId];
        return (s == null) ? 0 : (short) s.length();
    }

    // ===============================================================
    // ROOM TABLE
    // ===============================================================
    // Each room: 16 bytes
    // [0-1] long_desc_id (computed from room ID)
    // [2-3] name_id (computed from room ID)
    // [4-11] exits: N,S,E,W,NE,NW,SE,SW
    // [12-13] extra exits: U, D (for rooms that need them)
    // [14] flags
    // [15] special_handler_id

    // Exit table: for each room, 10 bytes (N,S,E,W,NE,NW,SE,SW,U,D)
    // Index: (roomId - 1) * 10
    static final byte ROOM_RECORD_SIZE = 10;
    static final byte[] ROOM_EXITS = buildRoomExits();

    // Room flags: 1 byte per room
    static final byte[] ROOM_FLAGS = buildRoomFlags();

    private static byte[] buildRoomExits() {
        // 10 exits per room: N, S, E, W, NE, NW, SE, SW, U, D
        byte[] e = new byte[NUM_ROOMS * ROOM_RECORD_SIZE];

        // Helper: set exits for a room
        // R_WEST_OF_HOUSE (1): N→North, S→South, W→ForestWest, NE→North
        setExits(e, R_WEST_OF_HOUSE,
            R_NORTH_OF_HOUSE, R_SOUTH_OF_HOUSE, R_NONE, R_FOREST_WEST,
            R_NORTH_OF_HOUSE, R_NONE, R_NONE, R_NONE,
            R_NONE, R_NONE);

        // R_NORTH_OF_HOUSE (2): S→West, E→Behind, W→ForestWest, SE→Behind, SW→West
        setExits(e, R_NORTH_OF_HOUSE,
            R_NONE, R_WEST_OF_HOUSE, R_BEHIND_HOUSE, R_FOREST_WEST,
            R_NONE, R_NONE, R_BEHIND_HOUSE, R_WEST_OF_HOUSE,
            R_NONE, R_NONE);

        // R_SOUTH_OF_HOUSE (3): N→West, E→Behind, W→ForestSouth, NE→Behind, NW→West
        setExits(e, R_SOUTH_OF_HOUSE,
            R_WEST_OF_HOUSE, R_NONE, R_BEHIND_HOUSE, R_FOREST_SOUTH,
            R_BEHIND_HOUSE, R_WEST_OF_HOUSE, R_NONE, R_NONE,
            R_NONE, R_NONE);

        // R_BEHIND_HOUSE (4): N→North, S→South, E→Clearing, NW→North, SW→South
        // W→Kitchen is conditional (window open) - handled in special logic
        setExits(e, R_BEHIND_HOUSE,
            R_NORTH_OF_HOUSE, R_SOUTH_OF_HOUSE, R_CLEARING, R_NONE,
            R_NONE, R_NORTH_OF_HOUSE, R_NONE, R_SOUTH_OF_HOUSE,
            R_NONE, R_NONE);

        // R_KITCHEN (5): E→LivingRoom, U→Attic
        // W→BehindHouse is conditional (window open) - handled in special logic
        setExits(e, R_KITCHEN,
            R_NONE, R_NONE, R_LIVING_ROOM, R_NONE,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_ATTIC, R_NONE);

        // R_LIVING_ROOM (6): E→Kitchen
        // D→Cellar is conditional (trap door open) - handled in special logic
        setExits(e, R_LIVING_ROOM,
            R_NONE, R_NONE, R_KITCHEN, R_NONE,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_NONE, R_NONE);

        // R_ATTIC (7): D→Kitchen
        setExits(e, R_ATTIC,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_NONE, R_KITCHEN);

        // R_FOREST_PATH (8): N→Clearing, S→NorthOfHouse, U→UpATree, E→ForestEast, W→ForestWest
        setExits(e, R_FOREST_PATH,
            R_CLEARING, R_NORTH_OF_HOUSE, R_FOREST_EAST, R_FOREST_WEST,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_UP_A_TREE, R_NONE);

        // R_FOREST_WEST (9): E→WestOfHouse, N→ForestPath, S→ForestSouth
        setExits(e, R_FOREST_WEST,
            R_FOREST_PATH, R_FOREST_SOUTH, R_WEST_OF_HOUSE, R_FOREST_WEST,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_NONE, R_NONE);

        // R_FOREST_EAST (10): W→ForestPath, S→Clearing, N→ForestPath
        setExits(e, R_FOREST_EAST,
            R_FOREST_PATH, R_CLEARING, R_FOREST_EAST, R_FOREST_PATH,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_NONE, R_NONE);

        // R_FOREST_SOUTH (11): N→SouthOfHouse, E→Clearing, W→ForestWest
        setExits(e, R_FOREST_SOUTH,
            R_SOUTH_OF_HOUSE, R_GRATING_CLEARING, R_CLEARING, R_FOREST_WEST,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_NONE, R_NONE);

        // R_CLEARING (12): S→ForestPath, W→ForestWest, E→ForestEast, N→ForestPath
        setExits(e, R_CLEARING,
            R_FOREST_PATH, R_FOREST_PATH, R_FOREST_EAST, R_FOREST_WEST,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_NONE, R_NONE);

        // R_UP_A_TREE (13): D→ForestPath
        setExits(e, R_UP_A_TREE,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_NONE, R_FOREST_PATH);

        // R_GRATING_CLEARING (14): N→ForestSouth, S→ForestSouth, W→ForestWest
        // D→GratingRoom is conditional (grating open)
        setExits(e, R_GRATING_CLEARING,
            R_FOREST_SOUTH, R_FOREST_SOUTH, R_NONE, R_FOREST_WEST,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_NONE, R_NONE);

        // R_CELLAR (15): N→TrollRoom, S→Maze1
        // U→LivingRoom is conditional (trap door open, handled in engine)
        setExits(e, R_CELLAR,
            R_TROLL_ROOM, R_MAZE_1, R_NONE, R_NONE,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_NONE, R_NONE);

        // R_TROLL_ROOM (16): S→Cellar
        // E→EastOfChasm is conditional (troll dead)
        setExits(e, R_TROLL_ROOM,
            R_NONE, R_CELLAR, R_NONE, R_NONE,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_NONE, R_NONE);

        // R_EAST_OF_CHASM (17): W→TrollRoom, N→Gallery, NE→RoundRoom
        setExits(e, R_EAST_OF_CHASM,
            R_GALLERY, R_NONE, R_NONE, R_TROLL_ROOM,
            R_ROUND_ROOM, R_NONE, R_NONE, R_NONE,
            R_NONE, R_NONE);

        // R_GALLERY (18): S→EastOfChasm, N→NSPassage
        setExits(e, R_GALLERY,
            R_NS_PASSAGE, R_EAST_OF_CHASM, R_NONE, R_NONE,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_NONE, R_NONE);

        // R_NS_PASSAGE (19): S→Gallery, N→RoundRoom
        setExits(e, R_NS_PASSAGE,
            R_ROUND_ROOM, R_GALLERY, R_NONE, R_NONE,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_NONE, R_NONE);

        // R_ROUND_ROOM (20): S→NSPassage, N→NarrowPassage, E→LoudRoom, W→Maze3
        setExits(e, R_ROUND_ROOM,
            R_NARROW_PASSAGE, R_NS_PASSAGE, R_LOUD_ROOM, R_MAZE_3,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_NONE, R_NONE);

        // R_NARROW_PASSAGE (21): S→RoundRoom, N→TreasureRoom
        setExits(e, R_NARROW_PASSAGE,
            R_TREASURE_ROOM, R_ROUND_ROOM, R_NONE, R_NONE,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_NONE, R_NONE);

        // R_TREASURE_ROOM (22): S→NarrowPassage
        setExits(e, R_TREASURE_ROOM,
            R_NONE, R_NARROW_PASSAGE, R_NONE, R_NONE,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_NONE, R_NONE);

        // R_LOUD_ROOM (23): W→RoundRoom
        setExits(e, R_LOUD_ROOM,
            R_NONE, R_NONE, R_NONE, R_ROUND_ROOM,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_NONE, R_NONE);

        // R_MAZE_1 (24): N→Cellar, E→Maze2, S→Maze3, W→Maze1
        setExits(e, R_MAZE_1,
            R_CELLAR, R_MAZE_3, R_MAZE_2, R_MAZE_1,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_GRATING_ROOM, R_NONE);

        // R_MAZE_2 (25): W→Maze1, N→Maze3, S→DeadEnd, E→Maze2
        setExits(e, R_MAZE_2,
            R_MAZE_3, R_DEAD_END, R_MAZE_2, R_MAZE_1,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_NONE, R_NONE);

        // R_MAZE_3 (26): N→Maze1, E→RoundRoom, S→Maze2, W→Maze3
        setExits(e, R_MAZE_3,
            R_MAZE_1, R_MAZE_2, R_ROUND_ROOM, R_MAZE_3,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_NONE, R_NONE);

        // R_DEAD_END (27): N→Maze2
        setExits(e, R_DEAD_END,
            R_MAZE_2, R_NONE, R_NONE, R_NONE,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_NONE, R_NONE);

        // R_GRATING_ROOM (28): S→Maze1
        // U→GratingClearing is conditional (grating open)
        setExits(e, R_GRATING_ROOM,
            R_NONE, R_MAZE_1, R_NONE, R_NONE,
            R_NONE, R_NONE, R_NONE, R_NONE,
            R_NONE, R_NONE);

        return e;
    }

    private static void setExits(byte[] e, byte room,
            byte n, byte s, byte east, byte w,
            byte ne, byte nw, byte se, byte sw,
            byte u, byte d) {
        int base = (room - 1) * ROOM_RECORD_SIZE;
        e[base + DIR_NORTH] = n;
        e[base + DIR_SOUTH] = s;
        e[base + DIR_EAST] = east;
        e[base + DIR_WEST] = w;
        e[base + DIR_NORTHEAST] = ne;
        e[base + DIR_NORTHWEST] = nw;
        e[base + DIR_SOUTHEAST] = se;
        e[base + DIR_SOUTHWEST] = sw;
        e[base + 8] = u;  // UP index
        e[base + 9] = d;  // DOWN index
    }

    private static byte[] buildRoomFlags() {
        byte[] f = new byte[NUM_ROOMS];
        // Surface rooms are lit
        f[R_WEST_OF_HOUSE - 1] = RF_LIT | RF_OUTSIDE;
        f[R_NORTH_OF_HOUSE - 1] = RF_LIT | RF_OUTSIDE;
        f[R_SOUTH_OF_HOUSE - 1] = RF_LIT | RF_OUTSIDE;
        f[R_BEHIND_HOUSE - 1] = RF_LIT | RF_OUTSIDE;
        f[R_KITCHEN - 1] = RF_LIT;
        f[R_LIVING_ROOM - 1] = RF_LIT;
        f[R_ATTIC - 1] = RF_LIT;
        f[R_FOREST_PATH - 1] = RF_LIT | RF_OUTSIDE;
        f[R_FOREST_WEST - 1] = RF_LIT | RF_OUTSIDE;
        f[R_FOREST_EAST - 1] = RF_LIT | RF_OUTSIDE;
        f[R_FOREST_SOUTH - 1] = RF_LIT | RF_OUTSIDE;
        f[R_CLEARING - 1] = RF_LIT | RF_OUTSIDE;
        f[R_UP_A_TREE - 1] = RF_LIT | RF_OUTSIDE;
        f[R_GRATING_CLEARING - 1] = RF_LIT | RF_OUTSIDE;
        // Underground rooms: dark by default (no RF_LIT)
        // Grating room gets some light from above
        f[R_GRATING_ROOM - 1] = 0; // dark unless grating open
        return f;
    }

    /**
     * Get exit room for a given room and direction.
     * Returns R_NONE if no exit.
     */
    static byte getRoomExit(byte roomId, byte direction) {
        if (roomId < 1 || roomId > NUM_ROOMS) return R_NONE;
        if (direction < 0 || direction >= ROOM_RECORD_SIZE) return R_NONE;
        return ROOM_EXITS[(roomId - 1) * ROOM_RECORD_SIZE + direction];
    }

    static byte getRoomFlags(byte roomId) {
        if (roomId < 1 || roomId > NUM_ROOMS) return 0;
        return ROOM_FLAGS[roomId - 1];
    }

    static boolean isRoomLit(byte roomId) {
        return (getRoomFlags(roomId) & RF_LIT) != 0;
    }

    // ===============================================================
    // OBJECT TABLE
    // ===============================================================
    // Per-object static data (immutable after install)
    // [0-1] flags (short)
    // [2] initial_location (byte)
    // [3] adjective_id (byte)
    // [4] noun_id (byte)
    // [5] capacity / treasure_value (byte)
    // [6] special_handler_id (byte)
    static final byte OBJ_RECORD_SIZE = 7;
    static final byte[] OBJECT_DATA = buildObjectData();

    private static byte[] buildObjectData() {
        byte[] d = new byte[NUM_OBJECTS * OBJ_RECORD_SIZE];

        // O_MAILBOX (1): container, openable, in West of House
        setObj(d, O_MAILBOX, (short)(OF_CONTAINER | OF_OPENABLE), R_WEST_OF_HOUSE, A_SMALL, N_MAILBOX, (byte)5, SH_NONE);
        // O_TROPHY_CASE (2): container, in Living Room
        setObj(d, O_TROPHY_CASE, (short)(OF_CONTAINER), R_LIVING_ROOM, A_NONE, N_CASE, (byte)50, SH_TROPHY_CASE);
        // O_FRONT_DOOR (3): door, in West of House
        setObj(d, O_FRONT_DOOR, (short)(OF_DOOR), R_WEST_OF_HOUSE, A_FRONT, N_DOOR, (byte)0, SH_NONE);
        // O_KITCHEN_WINDOW (4): door, openable, in Behind House
        setObj(d, O_KITCHEN_WINDOW, (short)(OF_DOOR | OF_OPENABLE), R_BEHIND_HOUSE, A_KITCHEN, N_WINDOW, (byte)0, SH_WINDOW);
        // O_RUG (5): in Living Room
        setObj(d, O_RUG, (short)0, R_LIVING_ROOM, A_NONE, N_RUG, (byte)0, SH_RUG);
        // O_TRAP_DOOR (6): door, openable, invisible initially, in Living Room
        setObj(d, O_TRAP_DOOR, (short)(OF_DOOR | OF_OPENABLE | OF_INVISIBLE), R_LIVING_ROOM, A_TRAP, N_TRAP, (byte)0, SH_TRAP_DOOR);
        // O_GRATING (7): door, openable, in Grating Clearing
        setObj(d, O_GRATING, (short)(OF_DOOR | OF_OPENABLE), R_GRATING_CLEARING, A_NONE, N_GRATING, (byte)0, SH_GRATING);
        // O_KITCHEN_TABLE (8): surface, in Kitchen
        setObj(d, O_KITCHEN_TABLE, (short)(OF_SURFACE), R_KITCHEN, A_KITCHEN, N_TABLE, (byte)20, SH_NONE);
        // O_LEAFLET (9): takeable, readable, inside mailbox
        setObj(d, O_LEAFLET, (short)(OF_TAKEABLE | OF_READABLE), (byte)(LOC_INSIDE_OBJ_BASE + O_MAILBOX - 201), A_NONE, N_LEAFLET, (byte)0, SH_NONE);
        // O_LAMP (10): takeable, light source, in Living Room
        setObj(d, O_LAMP, (short)(OF_TAKEABLE | OF_LIGHT), R_LIVING_ROOM, A_BRASS, N_LAMP, (byte)0, SH_LAMP);
        // O_SWORD (11): takeable, weapon, in Living Room
        setObj(d, O_SWORD, (short)(OF_TAKEABLE | OF_WEAPON), R_LIVING_ROOM, A_ELVISH, N_SWORD, (byte)3, SH_NONE);
        // O_BROWN_SACK (12): takeable, container, openable, on Kitchen Table
        setObj(d, O_BROWN_SACK, (short)(OF_TAKEABLE | OF_CONTAINER | OF_OPENABLE), R_KITCHEN, A_BROWN, N_SACK, (byte)9, SH_NONE);
        // O_LUNCH (13): takeable, inside brown sack
        setObj(d, O_LUNCH, (short)(OF_TAKEABLE), (byte)(LOC_INSIDE_OBJ_BASE + O_BROWN_SACK - 201), A_NONE, N_LUNCH, (byte)0, SH_NONE);
        // O_GARLIC (14): takeable, inside brown sack
        setObj(d, O_GARLIC, (short)(OF_TAKEABLE), (byte)(LOC_INSIDE_OBJ_BASE + O_BROWN_SACK - 201), A_NONE, N_GARLIC, (byte)0, SH_NONE);
        // O_BOTTLE (15): takeable, container, on Kitchen Table
        setObj(d, O_BOTTLE, (short)(OF_TAKEABLE | OF_CONTAINER), R_KITCHEN, A_NONE, N_BOTTLE, (byte)4, SH_NONE);
        // O_NASTY_KNIFE (16): takeable, weapon, in Attic
        setObj(d, O_NASTY_KNIFE, (short)(OF_TAKEABLE | OF_WEAPON), R_ATTIC, A_NASTY, N_KNIFE, (byte)2, SH_NONE);
        // O_ROPE (17): takeable, in Attic
        setObj(d, O_ROPE, (short)(OF_TAKEABLE), R_ATTIC, A_NONE, N_ROPE, (byte)0, SH_NONE);
        // O_TROLL (18): actor, in Troll Room
        setObj(d, O_TROLL, (short)(OF_ACTOR), R_TROLL_ROOM, A_NONE, N_TROLL, (byte)0, SH_TROLL);
        // O_AXE (19): takeable, weapon, carried by troll (inside troll)
        setObj(d, O_AXE, (short)(OF_TAKEABLE | OF_WEAPON), (byte)(LOC_INSIDE_OBJ_BASE + O_TROLL - 201), A_NONE, N_AXE, (byte)4, SH_NONE);
        // O_SKELETON_KEY (20): takeable, in Maze 2
        setObj(d, O_SKELETON_KEY, (short)(OF_TAKEABLE), R_MAZE_2, A_SKELETON, N_KEY, (byte)0, SH_NONE);
        // O_BAG_OF_COINS (21): takeable, treasure, in Maze 3
        setObj(d, O_BAG_OF_COINS, (short)(OF_TAKEABLE | OF_TREASURE), R_MAZE_3, A_NONE, N_COINS, (byte)10, SH_NONE);
        // O_RUSTY_KNIFE (22): takeable, weapon, in Dead End
        setObj(d, O_RUSTY_KNIFE, (short)(OF_TAKEABLE | OF_WEAPON), R_DEAD_END, A_RUSTY, N_KNIFE, (byte)1, SH_NONE);
        // O_BONES (23): in Maze 1
        setObj(d, O_BONES, (short)0, R_MAZE_1, A_NONE, N_BONES, (byte)0, SH_NONE);
        // O_EGG (24): takeable, treasure, in Up a Tree
        setObj(d, O_EGG, (short)(OF_TAKEABLE | OF_TREASURE | OF_CONTAINER), R_UP_A_TREE, A_JEWELED, N_EGG, (byte)5, SH_NONE);
        // O_NEST (25): takeable, container, in Up a Tree
        setObj(d, O_NEST, (short)(OF_TAKEABLE | OF_CONTAINER), R_UP_A_TREE, A_NONE, N_NEST, (byte)20, SH_NONE);
        // O_TORCH (26): takeable, treasure, light, in Treasure Room
        setObj(d, O_TORCH, (short)(OF_TAKEABLE | OF_TREASURE | OF_LIGHT), R_TREASURE_ROOM, A_IVORY, N_TORCH, (byte)6, SH_NONE);
        // O_PAINTING (27): takeable, treasure, in Gallery
        setObj(d, O_PAINTING, (short)(OF_TAKEABLE | OF_TREASURE), R_GALLERY, A_NONE, N_PAINTING, (byte)6, SH_NONE);
        // O_CHALICE (28): takeable, treasure, in Treasure Room
        setObj(d, O_CHALICE, (short)(OF_TAKEABLE | OF_TREASURE), R_TREASURE_ROOM, A_SILVER, N_CHALICE, (byte)5, SH_NONE);
        // O_BAR (29): takeable, treasure, in Loud Room
        setObj(d, O_BAR, (short)(OF_TAKEABLE | OF_TREASURE), R_LOUD_ROOM, A_PLATINUM, N_BAR, (byte)5, SH_NONE);
        // O_BRACELET (30): takeable, treasure, in Round Room
        setObj(d, O_BRACELET, (short)(OF_TAKEABLE | OF_TREASURE), R_ROUND_ROOM, A_SAPPHIRE, N_BRACELET, (byte)5, SH_NONE);

        return d;
    }

    private static void setObj(byte[] d, byte objId, short flags, byte location,
            byte adjId, byte nounId, byte capacity, byte handler) {
        int base = (objId - 1) * OBJ_RECORD_SIZE;
        d[base] = (byte)(flags >> 8);
        d[base + 1] = (byte)(flags & 0xFF);
        d[base + 2] = location;
        d[base + 3] = adjId;
        d[base + 4] = nounId;
        d[base + 5] = capacity;
        d[base + 6] = handler;
    }

    // Object data accessors
    static short getObjFlags(byte objId) {
        if (objId < 1 || objId > NUM_OBJECTS) return 0;
        int base = (objId - 1) * OBJ_RECORD_SIZE;
        return (short)(((OBJECT_DATA[base] & 0xFF) << 8) | (OBJECT_DATA[base + 1] & 0xFF));
    }

    static byte getObjInitialLocation(byte objId) {
        if (objId < 1 || objId > NUM_OBJECTS) return 0;
        return OBJECT_DATA[(objId - 1) * OBJ_RECORD_SIZE + 2];
    }

    static byte getObjAdjective(byte objId) {
        if (objId < 1 || objId > NUM_OBJECTS) return 0;
        return OBJECT_DATA[(objId - 1) * OBJ_RECORD_SIZE + 3];
    }

    static byte getObjNoun(byte objId) {
        if (objId < 1 || objId > NUM_OBJECTS) return 0;
        return OBJECT_DATA[(objId - 1) * OBJ_RECORD_SIZE + 4];
    }

    static byte getObjCapacity(byte objId) {
        if (objId < 1 || objId > NUM_OBJECTS) return 0;
        return OBJECT_DATA[(objId - 1) * OBJ_RECORD_SIZE + 5];
    }

    static byte getObjHandler(byte objId) {
        if (objId < 1 || objId > NUM_OBJECTS) return 0;
        return OBJECT_DATA[(objId - 1) * OBJ_RECORD_SIZE + 6];
    }

    static boolean objHasFlag(byte objId, short flag) {
        return (getObjFlags(objId) & flag) != 0;
    }

    // ===============================================================
    // VOCABULARY TABLE
    // ===============================================================
    // Each entry: 4 bytes (word, truncated/padded) + 1 byte (type) + 1 byte (value)
    static final byte VOCAB_ENTRY_SIZE = 6;

    // Vocabulary entries: {char, char, char, char, type, value}
    static final byte[] VOCAB = buildVocab();

    private static byte[] buildVocab() {
        java.util.List<byte[]> entries = new java.util.ArrayList<>();

        // Directions
        addVocab(entries, "nort", VT_DIRECTION, DIR_NORTH);
        addVocab(entries, "n   ", VT_DIRECTION, DIR_NORTH);
        addVocab(entries, "sout", VT_DIRECTION, DIR_SOUTH);
        addVocab(entries, "s   ", VT_DIRECTION, DIR_SOUTH);
        addVocab(entries, "east", VT_DIRECTION, DIR_EAST);
        addVocab(entries, "e   ", VT_DIRECTION, DIR_EAST);
        addVocab(entries, "west", VT_DIRECTION, DIR_WEST);
        addVocab(entries, "w   ", VT_DIRECTION, DIR_WEST);
        addVocab(entries, "nort", VT_DIRECTION, DIR_NORTHEAST); // "northeast" truncates to "nort" - conflict!
        // Use "ne  " instead
        addVocab(entries, "ne  ", VT_DIRECTION, DIR_NORTHEAST);
        addVocab(entries, "nw  ", VT_DIRECTION, DIR_NORTHWEST);
        addVocab(entries, "se  ", VT_DIRECTION, DIR_SOUTHEAST);
        addVocab(entries, "sw  ", VT_DIRECTION, DIR_SOUTHWEST);
        addVocab(entries, "up  ", VT_DIRECTION, DIR_UP);
        addVocab(entries, "u   ", VT_DIRECTION, DIR_UP);
        addVocab(entries, "down", VT_DIRECTION, DIR_DOWN);
        addVocab(entries, "d   ", VT_DIRECTION, DIR_DOWN);
        addVocab(entries, "in  ", VT_DIRECTION, DIR_IN);
        addVocab(entries, "ente", VT_DIRECTION, DIR_IN);
        addVocab(entries, "out ", VT_DIRECTION, DIR_OUT);
        addVocab(entries, "exit", VT_DIRECTION, DIR_OUT);
        addVocab(entries, "leav", VT_DIRECTION, DIR_OUT);

        // Verbs
        addVocab(entries, "look", VT_VERB, VERB_LOOK);
        addVocab(entries, "l   ", VT_VERB, VERB_LOOK);
        addVocab(entries, "exam", VT_VERB, VERB_EXAMINE);
        addVocab(entries, "x   ", VT_VERB, VERB_EXAMINE);
        addVocab(entries, "take", VT_VERB, VERB_TAKE);
        addVocab(entries, "get ", VT_VERB, VERB_TAKE);
        addVocab(entries, "grab", VT_VERB, VERB_TAKE);
        addVocab(entries, "pick", VT_VERB, VERB_TAKE);
        addVocab(entries, "drop", VT_VERB, VERB_DROP);
        addVocab(entries, "inve", VT_VERB, VERB_INVENTORY);
        addVocab(entries, "i   ", VT_VERB, VERB_INVENTORY);
        addVocab(entries, "open", VT_VERB, VERB_OPEN);
        addVocab(entries, "clos", VT_VERB, VERB_CLOSE);
        addVocab(entries, "shut", VT_VERB, VERB_CLOSE);
        addVocab(entries, "atta", VT_VERB, VERB_ATTACK);
        addVocab(entries, "kill", VT_VERB, VERB_ATTACK);
        addVocab(entries, "figh", VT_VERB, VERB_ATTACK);
        addVocab(entries, "hit ", VT_VERB, VERB_ATTACK);
        addVocab(entries, "ligh", VT_VERB, VERB_LIGHT);
        addVocab(entries, "turn", VT_VERB, VERB_TURN);
        addVocab(entries, "exti", VT_VERB, VERB_EXTINGUISH);
        addVocab(entries, "blow", VT_VERB, VERB_EXTINGUISH);
        addVocab(entries, "dous", VT_VERB, VERB_EXTINGUISH);
        addVocab(entries, "read", VT_VERB, VERB_READ);
        addVocab(entries, "put ", VT_VERB, VERB_PUT);
        addVocab(entries, "plac", VT_VERB, VERB_PUT);
        addVocab(entries, "inse", VT_VERB, VERB_PUT);
        addVocab(entries, "scor", VT_VERB, VERB_SCORE);
        addVocab(entries, "wait", VT_VERB, VERB_WAIT);
        addVocab(entries, "z   ", VT_VERB, VERB_WAIT);
        addVocab(entries, "agai", VT_VERB, VERB_AGAIN);
        addVocab(entries, "g   ", VT_VERB, VERB_AGAIN);
        addVocab(entries, "rest", VT_VERB, VERB_RESTART);
        addVocab(entries, "quit", VT_VERB, VERB_QUIT);
        addVocab(entries, "q   ", VT_VERB, VERB_QUIT);
        addVocab(entries, "brie", VT_VERB, VERB_BRIEF);
        addVocab(entries, "verb", VT_VERB, VERB_VERBOSE);
        addVocab(entries, "move", VT_VERB, VERB_MOVE);
        addVocab(entries, "push", VT_VERB, VERB_MOVE);
        addVocab(entries, "pull", VT_VERB, VERB_MOVE);
        addVocab(entries, "clim", VT_VERB, VERB_CLIMB);
        addVocab(entries, "go  ", VT_VERB, VERB_LOOK); // "go" alone → look, "go north" handled by parser
        addVocab(entries, "diag", VT_VERB, VERB_DIAGNOSE);
        addVocab(entries, "save", VT_VERB, VERB_SAVE);
        addVocab(entries, "tie ", VT_VERB, VERB_TIE);

        // Nouns
        addVocab(entries, "mail", VT_NOUN, N_MAILBOX);
        addVocab(entries, "box ", VT_NOUN, N_MAILBOX);
        addVocab(entries, "case", VT_NOUN, N_CASE);
        addVocab(entries, "trop", VT_NOUN, N_CASE);
        addVocab(entries, "door", VT_NOUN, N_DOOR);
        addVocab(entries, "wind", VT_NOUN, N_WINDOW);
        addVocab(entries, "rug ", VT_NOUN, N_RUG);
        addVocab(entries, "carp", VT_NOUN, N_RUG);
        addVocab(entries, "trap", VT_NOUN, N_TRAP);
        addVocab(entries, "grat", VT_NOUN, N_GRATING);
        addVocab(entries, "tabl", VT_NOUN, N_TABLE);
        addVocab(entries, "leaf", VT_NOUN, N_LEAFLET);
        addVocab(entries, "pape", VT_NOUN, N_LEAFLET);
        addVocab(entries, "lamp", VT_NOUN, N_LAMP);
        addVocab(entries, "lant", VT_NOUN, N_LAMP);
        addVocab(entries, "swor", VT_NOUN, N_SWORD);
        addVocab(entries, "sack", VT_NOUN, N_SACK);
        addVocab(entries, "bag ", VT_NOUN, N_SACK);
        addVocab(entries, "lunc", VT_NOUN, N_LUNCH);
        addVocab(entries, "food", VT_NOUN, N_LUNCH);
        addVocab(entries, "sand", VT_NOUN, N_LUNCH);
        addVocab(entries, "garl", VT_NOUN, N_GARLIC);
        addVocab(entries, "bott", VT_NOUN, N_BOTTLE);
        addVocab(entries, "knif", VT_NOUN, N_KNIFE);
        addVocab(entries, "rope", VT_NOUN, N_ROPE);
        addVocab(entries, "trol", VT_NOUN, N_TROLL);
        addVocab(entries, "axe ", VT_NOUN, N_AXE);
        addVocab(entries, "key ", VT_NOUN, N_KEY);
        addVocab(entries, "keys", VT_NOUN, N_KEY);
        addVocab(entries, "coin", VT_NOUN, N_COINS);
        addVocab(entries, "mone", VT_NOUN, N_COINS);
        addVocab(entries, "bone", VT_NOUN, N_BONES);
        addVocab(entries, "skel", VT_NOUN, N_BONES);
        addVocab(entries, "egg ", VT_NOUN, N_EGG);
        addVocab(entries, "jewe", VT_NOUN, N_EGG);
        addVocab(entries, "nest", VT_NOUN, N_NEST);
        addVocab(entries, "torc", VT_NOUN, N_TORCH);
        addVocab(entries, "pain", VT_NOUN, N_PAINTING);
        addVocab(entries, "chal", VT_NOUN, N_CHALICE);
        addVocab(entries, "bar ", VT_NOUN, N_BAR);
        addVocab(entries, "plat", VT_NOUN, N_BAR);
        addVocab(entries, "brac", VT_NOUN, N_BRACELET);
        addVocab(entries, "all ", VT_NOUN, N_ALL);
        addVocab(entries, "ever", VT_NOUN, N_ALL);
        addVocab(entries, "self", VT_NOUN, N_SELF);
        addVocab(entries, "me  ", VT_NOUN, N_SELF);
        addVocab(entries, "myse", VT_NOUN, N_SELF);
        addVocab(entries, "hous", VT_NOUN, N_HOUSE);
        addVocab(entries, "tree", VT_NOUN, N_TREE);
        addVocab(entries, "wate", VT_NOUN, (byte)34); // water - special

        // Adjectives
        addVocab(entries, "smal", VT_ADJECTIVE, A_SMALL);
        addVocab(entries, "bras", VT_ADJECTIVE, A_BRASS);
        addVocab(entries, "elvi", VT_ADJECTIVE, A_ELVISH);
        addVocab(entries, "brow", VT_ADJECTIVE, A_BROWN);
        addVocab(entries, "nast", VT_ADJECTIVE, A_NASTY);
        addVocab(entries, "rust", VT_ADJECTIVE, A_RUSTY);
        addVocab(entries, "ivor", VT_ADJECTIVE, A_IVORY);
        addVocab(entries, "silv", VT_ADJECTIVE, A_SILVER);
        addVocab(entries, "sapp", VT_ADJECTIVE, A_SAPPHIRE);
        addVocab(entries, "fron", VT_ADJECTIVE, A_FRONT);
        addVocab(entries, "kitc", VT_ADJECTIVE, A_KITCHEN);

        // Prepositions
        addVocab(entries, "with", VT_PREPOSITION, P_WITH);
        addVocab(entries, "usin", VT_PREPOSITION, P_WITH);
        addVocab(entries, "on  ", VT_PREPOSITION, P_ON);
        addVocab(entries, "onto", VT_PREPOSITION, P_ON);
        addVocab(entries, "in  ", VT_PREPOSITION, P_IN);
        addVocab(entries, "into", VT_PREPOSITION, P_IN);
        addVocab(entries, "at  ", VT_PREPOSITION, P_AT);
        addVocab(entries, "to  ", VT_PREPOSITION, P_TO);
        addVocab(entries, "from", VT_PREPOSITION, P_FROM);
        addVocab(entries, "unde", VT_PREPOSITION, P_UNDER);

        // Convert to flat array
        byte[] vocab = new byte[entries.size() * VOCAB_ENTRY_SIZE];
        for (int i = 0; i < entries.size(); i++) {
            System.arraycopy(entries.get(i), 0, vocab, i * VOCAB_ENTRY_SIZE, VOCAB_ENTRY_SIZE);
        }
        return vocab;
    }

    private static void addVocab(java.util.List<byte[]> entries, String word, byte type, byte value) {
        byte[] entry = new byte[VOCAB_ENTRY_SIZE];
        for (int i = 0; i < 4; i++) {
            entry[i] = (i < word.length()) ? (byte) word.charAt(i) : (byte) ' ';
        }
        entry[4] = type;
        entry[5] = value;
        entries.add(entry);
    }

    static short getVocabSize() {
        return (short)(VOCAB.length / VOCAB_ENTRY_SIZE);
    }
}
