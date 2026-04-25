package test;

import dev.cunningbets.zork.Data;

/**
 * Utility to generate JavaCard-compatible byte array source code
 * from the current String-based Data.java tables.
 * Run main() to produce the output, then paste into Data.java.
 */
public class GenerateCardData {

    public static void main(String[] args) {
        generateVocab();
        System.out.println();
        generateProse();
        System.out.println();
        generateRoomExits();
        System.out.println();
        generateRoomFlags();
        System.out.println();
        generateObjectData();
    }

    static void generateVocab() {
        byte[] v = Data.VOCAB;
        System.out.println("    static final byte[] VOCAB = {");
        for (int i = 0; i < v.length; i += Data.VOCAB_ENTRY_SIZE) {
            StringBuilder sb = new StringBuilder("        ");
            // Word chars
            for (int j = 0; j < 4; j++) {
                byte b = v[i + j];
                if (b >= 32 && b < 127) {
                    sb.append("'").append((char) b).append("',");
                } else {
                    sb.append(b).append(",");
                }
            }
            // Type and value
            sb.append(" ").append(v[i + 4]).append(",").append(v[i + 5]).append(",");
            // Comment
            sb.append(" // ");
            for (int j = 0; j < 4; j++) {
                byte b = v[i + j];
                if (b >= 33 && b < 127) sb.append((char) b);
            }
            String typeName;
            switch (v[i + 4]) {
                case 0: typeName = "DIR"; break;
                case 1: typeName = "VERB"; break;
                case 2: typeName = "NOUN"; break;
                case 3: typeName = "ADJ"; break;
                case 4: typeName = "PREP"; break;
                default: typeName = "?"; break;
            }
            sb.append(" ").append(typeName).append(" ").append(v[i + 5]);
            System.out.println(sb);
        }
        System.out.println("    };");
    }

    static void generateProse() {
        System.out.println("    static final byte[] PROSE = {");
        StringBuilder idx = new StringBuilder("    static final short[] PROSE_IDX = {\n");
        int offset = 0;
        for (int id = 0; id < Data.NUM_STRINGS; id++) {
            short len = Data.proseLength((short) id);
            idx.append("        ").append(offset).append(", // ").append(id).append("\n");

            if (len == 0) {
                System.out.println("        0, // " + id + ": (empty)");
                offset += 1;
                continue;
            }

            // Get the string content
            byte[] buf = new byte[len];
            Data.copyProse((short) id, buf, (short) 0);

            // Format first 40 chars as comment
            String preview = new String(buf, 0, Math.min(len, 40));
            preview = preview.replace("\n", "\\n");
            System.out.println("        // " + id + ": \"" + preview + (len > 40 ? "..." : "") + "\"");

            StringBuilder line = new StringBuilder("        ");
            for (int j = 0; j < len; j++) {
                byte b = buf[j];
                if (b == '\n') {
                    line.append("'\\n',");
                } else if (b >= 32 && b < 127 && b != '\'') {
                    line.append("'").append((char) b).append("',");
                } else if (b == '\'') {
                    line.append("'\\'',");
                } else {
                    line.append(b).append(",");
                }
                // Line wrap every ~16 chars
                if (line.length() > 100) {
                    System.out.println(line);
                    line = new StringBuilder("        ");
                }
            }
            line.append("0,");
            System.out.println(line);
            offset += len + 1; // +1 for the null terminator
        }
        System.out.println("    };");
        System.out.println();
        idx.append("    };");
        System.out.println(idx);
    }

    static void generateRoomExits() {
        byte[] e = Data.ROOM_EXITS;
        System.out.println("    static final byte[] ROOM_EXITS = {");
        for (int r = 0; r < Data.NUM_ROOMS; r++) {
            StringBuilder sb = new StringBuilder("        ");
            for (int d = 0; d < Data.ROOM_RECORD_SIZE; d++) {
                sb.append(String.format("%2d,", e[r * Data.ROOM_RECORD_SIZE + d]));
            }
            sb.append(" // Room ").append(r + 1);
            System.out.println(sb);
        }
        System.out.println("    };");
    }

    static void generateRoomFlags() {
        byte[] f = Data.ROOM_FLAGS;
        System.out.println("    static final byte[] ROOM_FLAGS = {");
        StringBuilder sb = new StringBuilder("        ");
        for (int i = 0; i < f.length; i++) {
            sb.append(String.format("0x%02X,", f[i] & 0xFF));
        }
        System.out.println(sb);
        System.out.println("    };");
    }

    static void generateObjectData() {
        byte[] d = Data.OBJECT_DATA;
        System.out.println("    static final byte[] OBJECT_DATA = {");
        for (int o = 0; o < Data.NUM_OBJECTS; o++) {
            int base = o * Data.OBJ_RECORD_SIZE;
            StringBuilder sb = new StringBuilder("        ");
            for (int j = 0; j < Data.OBJ_RECORD_SIZE; j++) {
                sb.append(String.format("%4d,", d[base + j]));
            }
            sb.append(" // Obj ").append(o + 1);
            System.out.println(sb);
        }
        System.out.println("    };");
    }
}
