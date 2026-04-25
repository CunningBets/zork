package dev.cunningbets.zork;

import javacard.framework.*;

public class Zork extends Applet {

    protected Zork(byte[] bArray, short bOffset, byte bLength) {
        register();
    }

    public static void install(byte[] bArray, short bOffset, byte bLength) {
        new Zork(bArray, bOffset, bLength);
    }

    public void process(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        byte cla = buffer[ISO7816.OFFSET_CLA];
        byte ins = buffer[ISO7816.OFFSET_INS];

        if (selectingApplet()) {
            return;
        }

        if (cla != Constants.CLA_TTT) {
            ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
        }
    }
}
