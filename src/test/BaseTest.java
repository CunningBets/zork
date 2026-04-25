package test;

import com.licel.jcardsim.utils.AIDUtil;
import dev.cunningbets.zork.Zork;
import javacard.framework.AID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import pro.javacard.engine.EngineSession;
import pro.javacard.engine.JavaCardEngine;

public class BaseTest {
    protected static String APPLET_AID = "F0434254545401";

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
}
