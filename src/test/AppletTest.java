package test;

import com.licel.jcardsim.utils.AIDUtil;
import javacard.framework.AID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppletTest extends BaseTest {
    @Test
    public void testSelectionSucceeds() {
        AID aid = AIDUtil.create(APPLET_AID);
        byte[] response = session.transmitCommand(AIDUtil.select(aid));
        // SELECT should succeed (9000 or 61XX for more data)
        int sw = getSW(response);
        assertTrue(sw == 0x9000 || (sw & 0xFF00) == 0x6100,
            "SELECT should return 9000 or 61XX, got: " + Integer.toHexString(sw));
    }

    @Test
    public void testUnsupportedInstruction() {
        selectApplet();
        byte[] apdu = new byte[]{(byte) 0x80, (byte) 0xFF, 0x00, 0x00};
        byte[] response = session.transmitCommand(apdu);
        int sw = getSW(response);
        assertEquals(0x6D00, sw, "Unsupported INS should return 6D00");
    }

    @Test
    public void testWrongCLA() {
        selectApplet();
        byte[] apdu = new byte[]{(byte) 0x90, (byte) 0x10, 0x00, 0x00, 0x01, 0x41};
        byte[] response = session.transmitCommand(apdu);
        int sw = getSW(response);
        assertEquals(0x6E00, sw, "Wrong CLA should return 6E00");
    }
}
