package test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ZorkTest extends BaseTest {

    // Helper to build APDU: CLA=80, INS, P1, P2, no data
    private byte[] apdu(byte ins, byte p1, byte p2) {
        return new byte[]{(byte) 0x80, ins, p1, p2};
    }

}
