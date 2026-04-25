package dev.cunningbets.zork;

import javacard.framework.*;

/**
 * Mini-Zork I JavaCard Applet.
 * APDU protocol handler for the Zork game engine.
 *
 * AID: A0:00:00:06:47:2E:5A:4F:52:4B:01
 */
public class Zork extends Applet {

    private Engine engine;

    protected Zork(byte[] bArray, short bOffset, byte bLength) {
        engine = new Engine();
        register();
    }

    public static void install(byte[] bArray, short bOffset, byte bLength) {
        new Zork(bArray, bOffset, bLength);
    }

    public boolean select() {
        if (!engine.isInitialized()) {
            // First selection after install: initialize in a transaction
            JCSystem.beginTransaction();
            engine.onSelect();
            JCSystem.commitTransaction();
        } else {
            engine.onSelect();
        }
        return true;
    }

    public void process(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        byte cla = buffer[ISO7816.OFFSET_CLA];
        byte ins = buffer[ISO7816.OFFSET_INS];

        // Handle SELECT (already processed in select())
        if (selectingApplet()) {
            // Return intro/resume text
            sendBufferedOutput(apdu);
            return;
        }

        // GET RESPONSE - standard ISO 7816
        if (cla == 0x00 && ins == Data.INS_GET_RESPONSE) {
            sendBufferedOutput(apdu);
            return;
        }

        // All other commands use CLA 0x80
        if (cla != Data.CLA_ZORK) {
            ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
        }

        // Check P1/P2 are zero
        if (buffer[ISO7816.OFFSET_P1] != 0 || buffer[ISO7816.OFFSET_P2] != 0) {
            ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);
        }

        switch (ins) {
            case Data.INS_COMMAND:
                processGameCommand(apdu);
                break;
            case Data.INS_LOOK:
                engine.processLook();
                sendBufferedOutput(apdu);
                break;
            case Data.INS_RESTART:
                JCSystem.beginTransaction();
                engine.processRestart();
                JCSystem.commitTransaction();
                sendBufferedOutput(apdu);
                break;
            default:
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }

    /**
     * Process a game command APDU (INS 0x10).
     * Data field contains ASCII text of the player's command.
     */
    private void processGameCommand(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        short bytesRead = apdu.setIncomingAndReceive();
        short lc = apdu.getIncomingLength();

        if (lc < 1 || lc > 80) {
            ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
        }

        short dataOffset = apdu.getOffsetCdata();

        // Wrap in transaction for atomicity
        JCSystem.beginTransaction();
        try {
            engine.processCommand(buffer, dataOffset, lc);
            JCSystem.commitTransaction();
        } catch (Exception e) {
            JCSystem.abortTransaction();
            ISOException.throwIt(ISO7816.SW_UNKNOWN);
        }

        sendBufferedOutput(apdu);
    }

    /**
     * Send buffered output from the engine.
     * If output exceeds 256 bytes, returns 0x61XX to signal GET RESPONSE needed.
     */
    private void sendBufferedOutput(APDU apdu) {
        short avail = engine.outputAvailable();
        if (avail <= 0) {
            // No data, just return 9000
            return;
        }

        byte[] buffer = apdu.getBuffer();
        // Send up to 256 bytes per response
        short maxSend = 256;
        short toSend = (avail < maxSend) ? avail : maxSend;

        short copied = engine.getOutput(buffer, (short) 0, toSend);
        apdu.setOutgoingAndSend((short) 0, copied);

        // Check if more data remains
        short remaining = engine.outputAvailable();
        if (remaining > 0) {
            // Signal more data with 0x61XX
            short sw;
            if (remaining > 255) {
                sw = (short) 0x6100; // 0x00 means >=255 bytes remaining
            } else {
                sw = (short)(0x6100 | (remaining & 0xFF));
            }
            ISOException.throwIt(sw);
        }
    }
}
