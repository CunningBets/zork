package test;

import com.licel.jcardsim.utils.AIDUtil;
import javacard.framework.AID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppletTest extends BaseTest {
    @Test
    public void VerifySelectionResponse() {
        AID aid = AIDUtil.create(APPLET_AID);
        byte[] response = session.transmitCommand(AIDUtil.select(aid));
        // SELECT should return just SW 9000
        int sw = ((response[response.length - 2] & 0xFF) << 8) | (response[response.length - 1] & 0xFF);
        assertEquals(0x9000, sw);
    }
}
