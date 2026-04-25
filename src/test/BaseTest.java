package test;

import com.licel.jcardsim.utils.AIDUtil;
import dev.cunningbets.zork.Zork;
import javacard.framework.AID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import pro.javacard.engine.EngineSession;
import pro.javacard.engine.JavaCardEngine;

public class BaseTest {
    protected static String APPLET_AID = "A0000006472E5A4F524B01";

    protected JavaCardEngine engine;
    protected EngineSession session;

    protected void selectApplet() {
        AID aid = AIDUtil.create(APPLET_AID);
        session.transmitCommand(AIDUtil.select(aid));
    }

    @BeforeEach
    public void Setup() {
        engine = JavaCardEngine.create();
        AID aid = AIDUtil.create(APPLET_AID);
        engine.installApplet(aid, Zork.class);
        session = engine.connect();
    }

    @AfterEach
    public void tearDown() {
        if (session != null) {
            session.close();
        }
    }

    // ---- Helper methods for game testing ----

    /**
     * Select the applet and return the full response text (handles GET RESPONSE chaining).
     */
    protected String selectAndGetText() {
        AID aid = AIDUtil.create(APPLET_AID);
        byte[] response = session.transmitCommand(AIDUtil.select(aid));
        return collectResponse(response);
    }

    /**
     * Send a game command and return the response text.
     */
    protected String sendCommand(String command) {
        byte[] cmdBytes = command.getBytes();
        byte[] apdu = new byte[5 + cmdBytes.length];
        apdu[0] = (byte) 0x80; // CLA
        apdu[1] = (byte) 0x10; // INS: command
        apdu[2] = 0x00;        // P1
        apdu[3] = 0x00;        // P2
        apdu[4] = (byte) cmdBytes.length; // Lc
        System.arraycopy(cmdBytes, 0, apdu, 5, cmdBytes.length);
        byte[] response = session.transmitCommand(apdu);
        return collectResponse(response);
    }

    /**
     * Send LOOK command and return response text.
     */
    protected String sendLook() {
        byte[] apdu = new byte[]{(byte) 0x80, (byte) 0x12, 0x00, 0x00};
        byte[] response = session.transmitCommand(apdu);
        return collectResponse(response);
    }

    /**
     * Send RESTART command and return response text.
     */
    protected String sendRestart() {
        byte[] apdu = new byte[]{(byte) 0x80, (byte) 0x14, 0x00, 0x00};
        byte[] response = session.transmitCommand(apdu);
        return collectResponse(response);
    }

    /**
     * Extract SW from response.
     */
    protected int getSW(byte[] response) {
        return ((response[response.length - 2] & 0xFF) << 8) |
               (response[response.length - 1] & 0xFF);
    }

    /**
     * Extract text from response and handle GET RESPONSE chaining.
     */
    private String collectResponse(byte[] response) {
        StringBuilder sb = new StringBuilder();

        // Extract data (everything except last 2 bytes which are SW)
        if (response.length > 2) {
            for (int i = 0; i < response.length - 2; i++) {
                sb.append((char) response[i]);
            }
        }

        int sw = getSW(response);

        // Handle GET RESPONSE chaining
        while ((sw & 0xFF00) == 0x6100) {
            byte le = (byte)(sw & 0xFF);
            byte[] getResponse = new byte[]{0x00, (byte) 0xC0, 0x00, 0x00, le};
            response = session.transmitCommand(getResponse);

            if (response.length > 2) {
                for (int i = 0; i < response.length - 2; i++) {
                    sb.append((char) response[i]);
                }
            }
            sw = getSW(response);
        }

        return sb.toString();
    }
}
