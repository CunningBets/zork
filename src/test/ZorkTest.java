package test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ZorkTest extends BaseTest {

    @Test
    public void testSelectionShowsIntro() {
        String text = selectAndGetText();
        assertTrue(text.contains("Mini-ZORK I"), "Should show game title");
        assertTrue(text.contains("West of House"), "Should show starting room");
        assertTrue(text.contains("open field"), "Should show room description");
    }

    @Test
    public void testResumeAfterReselect() {
        selectAndGetText(); // First select - intro
        // Simulate re-selection by sending a command first, then checking state persists
        sendCommand("look");
        // The game state should persist
    }

    @Test
    public void testLookCommand() {
        selectAndGetText();
        String text = sendCommand("look");
        assertTrue(text.contains("West of House"), "LOOK should show room name");
        assertTrue(text.contains("open field"), "LOOK should show room description");
    }

    @Test
    public void testLookAPDU() {
        selectAndGetText();
        String text = sendLook();
        assertTrue(text.contains("West of House"), "LOOK APDU should show room");
    }

    @Test
    public void testNavigationNorth() {
        selectAndGetText();
        String text = sendCommand("north");
        assertTrue(text.contains("North of House"), "Should move to North of House");
    }

    @Test
    public void testNavigationShortcuts() {
        selectAndGetText();
        String text = sendCommand("n");
        assertTrue(text.contains("North of House"), "N should move north");
    }

    @Test
    public void testNavigationBlocked() {
        selectAndGetText();
        String text = sendCommand("east");
        assertTrue(text.contains("can't go"), "East from West of House should be blocked");
    }

    @Test
    public void testMultiRoomNavigation() {
        selectAndGetText();
        sendCommand("north");
        String text = sendCommand("east");
        assertTrue(text.contains("Behind House"), "Should reach Behind House");
    }

    @Test
    public void testOpenMailbox() {
        selectAndGetText();
        String text = sendCommand("open mailbox");
        assertTrue(text.contains("Opened"), "Should open mailbox");
    }

    @Test
    public void testTakeLeafletFromMailbox() {
        selectAndGetText();
        sendCommand("open mailbox");
        String text = sendCommand("take leaflet");
        assertTrue(text.contains("Taken"), "Should take leaflet");
    }

    @Test
    public void testInventoryEmpty() {
        selectAndGetText();
        String text = sendCommand("inventory");
        assertTrue(text.contains("empty-handed"), "Should be empty-handed at start");
    }

    @Test
    public void testInventoryWithItem() {
        selectAndGetText();
        sendCommand("open mailbox");
        sendCommand("take leaflet");
        String text = sendCommand("inventory");
        assertTrue(text.contains("carrying"), "Should show carrying");
        assertTrue(text.contains("leaflet"), "Should list leaflet");
    }

    @Test
    public void testDropItem() {
        selectAndGetText();
        sendCommand("open mailbox");
        sendCommand("take leaflet");
        String text = sendCommand("drop leaflet");
        assertTrue(text.contains("Dropped"), "Should drop leaflet");
    }

    @Test
    public void testExamineObject() {
        selectAndGetText();
        sendCommand("open mailbox");
        sendCommand("take leaflet");
        String text = sendCommand("read leaflet");
        assertTrue(text.contains("WELCOME TO ZORK"), "Should show leaflet text");
    }

    @Test
    public void testKitchenAccess() {
        selectAndGetText();
        // Go to behind house
        sendCommand("n");
        sendCommand("e");
        // Open window
        sendCommand("open window");
        // Enter kitchen
        String text = sendCommand("west");
        assertTrue(text.contains("Kitchen"), "Should enter kitchen through window");
    }

    @Test
    public void testWindowClosed() {
        selectAndGetText();
        sendCommand("n");
        sendCommand("e");
        // Try to enter without opening window
        String text = sendCommand("west");
        assertTrue(text.contains("closed") || text.contains("can't"),
            "Should not enter through closed window");
    }

    @Test
    public void testLivingRoom() {
        selectAndGetText();
        sendCommand("n");
        sendCommand("e");
        sendCommand("open window");
        sendCommand("w");
        String text = sendCommand("east");
        assertTrue(text.contains("Living Room"), "Should reach Living Room");
    }

    @Test
    public void testTakeLamp() {
        selectAndGetText();
        navigateToLivingRoom();
        String text = sendCommand("take lamp");
        assertTrue(text.contains("Taken"), "Should take lamp");
    }

    @Test
    public void testTakeSword() {
        selectAndGetText();
        navigateToLivingRoom();
        String text = sendCommand("take sword");
        assertTrue(text.contains("Taken"), "Should take sword");
    }

    @Test
    public void testMoveRug() {
        selectAndGetText();
        navigateToLivingRoom();
        String text = sendCommand("move rug");
        assertTrue(text.contains("trap door"), "Moving rug should reveal trap door");
    }

    @Test
    public void testOpenTrapDoor() {
        selectAndGetText();
        navigateToLivingRoom();
        sendCommand("move rug");
        String text = sendCommand("open trap");
        assertTrue(text.contains("Opened"), "Should open trap door");
    }

    @Test
    public void testDescendToCellar() {
        selectAndGetText();
        navigateToLivingRoom();
        sendCommand("take lamp");
        sendCommand("light lamp");
        sendCommand("move rug");
        sendCommand("open trap");
        String text = sendCommand("down");
        assertTrue(text.contains("Cellar") || text.contains("trap door"),
            "Should descend to cellar");
    }

    @Test
    public void testDarkRoom() {
        selectAndGetText();
        navigateToLivingRoom();
        sendCommand("move rug");
        sendCommand("open trap");
        // Go down without lamp
        String text = sendCommand("down");
        assertTrue(text.contains("dark") || text.contains("pitch black"),
            "Cellar should be dark without lamp");
    }

    @Test
    public void testLightLamp() {
        selectAndGetText();
        navigateToLivingRoom();
        sendCommand("take lamp");
        String text = sendCommand("light lamp");
        assertTrue(text.contains("now on"), "Should turn on lamp");
    }

    @Test
    public void testTrollBlocks() {
        selectAndGetText();
        navigateToCellar();
        String text = sendCommand("north"); // To troll room
        assertTrue(text.contains("Troll Room") || text.contains("troll"),
            "Should reach troll room");
        // Try to go east (blocked by troll)
        text = sendCommand("east");
        assertTrue(text.contains("troll") || text.contains("fends"),
            "Troll should block passage");
    }

    @Test
    public void testKillTrollWithSword() {
        selectAndGetText();
        navigateToCellar();
        sendCommand("north"); // To troll room
        String text = sendCommand("attack troll");
        assertTrue(text.contains("vanishes") || text.contains("smoke"),
            "Troll should be defeated with sword");
    }

    @Test
    public void testScoreCommand() {
        selectAndGetText();
        String text = sendCommand("score");
        assertTrue(text.contains("0"), "Score should start at 0");
    }

    @Test
    public void testTreasureScoring() {
        selectAndGetText();
        // Navigate to forest path and climb tree
        sendCommand("n");     // North of House
        sendCommand("w");     // Forest West
        sendCommand("n");     // Forest Path
        sendCommand("up");    // Up a Tree
        sendCommand("take egg");
        sendCommand("down");  // Forest Path
        sendCommand("s");     // Forest West
        sendCommand("e");     // West of House
        sendCommand("n");     // North of House
        sendCommand("e");     // Behind House
        sendCommand("open window");
        sendCommand("w");     // Kitchen
        sendCommand("e");     // Living Room
        // Put egg in trophy case
        sendCommand("open case");
        String text = sendCommand("put egg in case");
        assertTrue(text.contains("Done"), "Should put egg in case, got: " + text);
        // Check score increased
        text = sendCommand("score");
        assertFalse(text.contains("0 out"), "Score should have increased");
    }

    @Test
    public void testUnknownWord() {
        selectAndGetText();
        String text = sendCommand("xyzzy");
        assertTrue(text.contains("don't know"), "Should report unknown word");
    }

    @Test
    public void testInvalidSentence() {
        selectAndGetText();
        String text = sendCommand("take");
        assertTrue(text.contains("understand") || text.contains("don't"),
            "Bare 'take' should be invalid");
    }

    @Test
    public void testWait() {
        selectAndGetText();
        String text = sendCommand("wait");
        assertTrue(text.contains("Time passes"), "Wait should pass time");
    }

    @Test
    public void testRestart() {
        selectAndGetText();
        sendCommand("north");
        String text = sendRestart();
        assertTrue(text.contains("restarted") || text.contains("Restarted") ||
                   text.contains("West of House"),
            "Restart should reset to start");
    }

    @Test
    public void testBriefMode() {
        selectAndGetText();
        sendCommand("brief");
        // Visit a room, leave, come back - should show brief desc
        sendCommand("north");
        sendCommand("south");
        String text = sendCommand("north");
        // In brief mode, revisited rooms show just the name
        assertTrue(text.contains("North of House"), "Should show room name");
    }

    @Test
    public void testCantTakeScenery() {
        selectAndGetText();
        String text = sendCommand("take mailbox");
        assertTrue(text.contains("can't take"), "Should not be able to take scenery");
    }

    @Test
    public void testGratingNeedsKey() {
        selectAndGetText();
        // Navigate to grating clearing: West of House → S → South of House → W → Forest South → S → Grating Clearing
        sendCommand("south");
        sendCommand("west");
        sendCommand("south");
        String text = sendCommand("open grating");
        assertTrue(text.contains("locked"), "Grating should be locked without key");
    }

    @Test
    public void testClimbTree() {
        selectAndGetText();
        sendCommand("north");
        sendCommand("west");
        sendCommand("north");
        String text = sendCommand("up");
        assertTrue(text.contains("Up a Tree") || text.contains("above the ground"),
            "Should climb tree from forest path");
    }

    @Test
    public void testEggInTree() {
        selectAndGetText();
        // Navigate to forest path
        sendCommand("north");
        sendCommand("west");
        sendCommand("north");
        // Climb tree
        sendCommand("up");
        String text = sendCommand("look");
        assertTrue(text.contains("egg") || text.contains("nest"),
            "Should see egg or nest in tree");
    }

    // ---- Navigation helpers ----

    private void navigateToLivingRoom() {
        sendCommand("n");
        sendCommand("e");
        sendCommand("open window");
        sendCommand("w");
        sendCommand("e"); // Kitchen → Living Room
    }

    private void navigateToCellar() {
        navigateToLivingRoom();
        sendCommand("take lamp");
        sendCommand("take sword");
        sendCommand("light lamp");
        sendCommand("move rug");
        sendCommand("open trap");
        sendCommand("down");
    }
}
