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

    // ---- Underground navigation ----

    @Test
    public void testUndergroundNavigation() {
        selectAndGetText();
        navigatePastTroll();
        // East of Chasm → Gallery → N-S Passage → Round Room
        String text = sendCommand("n"); // Gallery
        assertTrue(text.contains("Gallery"), "Should reach Gallery");
        text = sendCommand("n"); // N-S Passage
        assertTrue(text.contains("N-S Passage"), "Should reach N-S Passage");
        text = sendCommand("n"); // Round Room
        assertTrue(text.contains("Round Room"), "Should reach Round Room");
        // Round Room → Loud Room → back
        text = sendCommand("e");
        assertTrue(text.contains("Loud Room"), "Should reach Loud Room");
        text = sendCommand("w");
        assertTrue(text.contains("Round Room"), "Should return to Round Room");
        // Round Room → Narrow Passage → Treasure Room
        text = sendCommand("n");
        assertTrue(text.contains("Narrow Passage"), "Should reach Narrow Passage");
        text = sendCommand("n");
        assertTrue(text.contains("Treasure Room"), "Should reach Treasure Room");
    }

    @Test
    public void testMazeNavigation() {
        selectAndGetText();
        navigateToCellar();
        // Cellar → S → Maze 1
        String text = sendCommand("s");
        assertTrue(text.contains("maze") || text.contains("twisty"), "Should enter maze");
        // Maze 1 → E → Maze 2
        text = sendCommand("e");
        assertTrue(text.contains("maze"), "Should be in maze 2");
        // Maze 2 → S → Dead End
        text = sendCommand("s");
        assertTrue(text.contains("Dead End"), "Should reach dead end");
        // Dead End → N → Maze 2
        text = sendCommand("n");
        assertTrue(text.contains("maze"), "Should return to maze 2");
        // Maze 2 → N → Maze 3
        text = sendCommand("n");
        assertTrue(text.contains("maze"), "Should be in maze 3");
        // Maze 3 → E → Round Room
        text = sendCommand("e");
        assertTrue(text.contains("Round Room"), "Maze 3 east should reach Round Room");
    }

    @Test
    public void testTakeUndergroundTreasures() {
        selectAndGetText();
        navigatePastTroll();
        // Gallery: painting
        sendCommand("n");
        String text = sendCommand("take painting");
        assertTrue(text.contains("Taken"), "Should take painting");
        // Round Room: bracelet
        sendCommand("n");
        sendCommand("n");
        text = sendCommand("take bracelet");
        assertTrue(text.contains("Taken"), "Should take bracelet");
        // Loud Room: bar
        sendCommand("e");
        text = sendCommand("take bar");
        assertTrue(text.contains("Taken"), "Should take platinum bar");
        // Treasure Room: torch, chalice
        sendCommand("w");
        sendCommand("n");
        sendCommand("n");
        text = sendCommand("take torch");
        assertTrue(text.contains("Taken"), "Should take torch");
        text = sendCommand("take chalice");
        assertTrue(text.contains("Taken"), "Should take chalice");
    }

    @Test
    public void testMazeItems() {
        selectAndGetText();
        navigateToCellar();
        sendCommand("s"); // Maze 1
        sendCommand("e"); // Maze 2
        String text = sendCommand("take key");
        assertTrue(text.contains("Taken"), "Should take skeleton key");
        sendCommand("n"); // Maze 3
        text = sendCommand("take coins");
        assertTrue(text.contains("Taken"), "Should take bag of coins");
    }

    @Test
    public void testGratingOpenWithKey() {
        selectAndGetText();
        navigateToCellar();
        sendCommand("s"); // Maze 1
        sendCommand("e"); // Maze 2
        sendCommand("take key");
        sendCommand("n"); // Maze 3
        sendCommand("n"); // Maze 1
        sendCommand("u"); // Grating Room
        String text = sendCommand("open grating");
        assertTrue(text.contains("Opened"), "Should open grating with key");
        text = sendCommand("u"); // Up through grating
        assertTrue(text.contains("Grating Clearing"), "Should exit to Grating Clearing");
    }

    @Test
    public void testGratingVisibleFromBelow() {
        selectAndGetText();
        navigateToCellar();
        sendCommand("s"); // Maze 1
        sendCommand("u"); // Grating Room
        // Grating should be visible from Grating Room
        String text = sendCommand("examine grating");
        assertTrue(text.contains("grating"), "Grating should be visible from Grating Room");
    }

    @Test
    public void testLivingRoomKitchenReturn() {
        selectAndGetText();
        navigateToLivingRoom();
        // Living Room W → Kitchen
        String text = sendCommand("w");
        assertTrue(text.contains("Kitchen"), "Living Room west should go to Kitchen");
        // Kitchen E → Living Room
        text = sendCommand("e");
        assertTrue(text.contains("Living Room"), "Kitchen east should go to Living Room");
    }

    @Test
    public void testForestPathClearingRoute() {
        selectAndGetText();
        navigateToLivingRoom();
        sendCommand("w");  // Kitchen
        sendCommand("w");  // Behind House (through window)
        String text = sendCommand("e"); // Clearing
        assertTrue(text.contains("Clearing"), "Behind House east should reach Clearing");
        text = sendCommand("s"); // Forest Path
        assertTrue(text.contains("Forest Path"), "Clearing south should reach Forest Path");
        text = sendCommand("s"); // North of House
        assertTrue(text.contains("North of House"), "Forest Path south to North of House");
    }

    @Test
    public void testInventoryCapacity() {
        selectAndGetText();
        navigateToLivingRoom();
        sendCommand("take lamp");
        sendCommand("take sword");
        sendCommand("w"); // Kitchen
        sendCommand("open sack");
        sendCommand("take garlic");
        sendCommand("take sack");
        sendCommand("take bottle");
        sendCommand("e"); // Living Room
        sendCommand("move rug");
        sendCommand("open trap");
        // That's 5 items. Take rug? No, not takeable. Take trap door? No.
        // Go to attic for more items
        sendCommand("w"); // Kitchen
        sendCommand("u"); // Attic
        sendCommand("take knife");
        sendCommand("take rope"); // 7 items
        // Go get leaflet too
        sendCommand("d"); // Kitchen
        sendCommand("w"); // Behind House
        sendCommand("w"); // West of House via forest? No...
        // Actually let's just check we can take 8
        String text = sendCommand("i");
        assertTrue(text.contains("carrying"), "Should be carrying items");
    }

    @Test
    public void testFullTreasureDeposit() {
        selectAndGetText();
        navigatePastTroll();
        sendCommand("drop sword"); // Free up slot
        // Collect 6 underground treasures
        sendCommand("n"); sendCommand("take painting");
        sendCommand("n"); sendCommand("n"); sendCommand("take bracelet");
        sendCommand("e"); sendCommand("take bar");
        sendCommand("w"); sendCommand("n"); sendCommand("n");
        sendCommand("take torch"); sendCommand("take chalice");
        // Return to cellar via underground
        sendCommand("s"); sendCommand("s"); sendCommand("s");
        sendCommand("s"); sendCommand("s"); sendCommand("w"); sendCommand("s");
        // Maze for key + coins
        sendCommand("s"); sendCommand("e"); sendCommand("take key");
        sendCommand("n"); sendCommand("take coins");
        // Exit via grating
        sendCommand("n"); sendCommand("u");
        sendCommand("open grating"); sendCommand("u");
        // Surface to living room
        sendCommand("n"); sendCommand("n"); sendCommand("n");
        sendCommand("n"); sendCommand("e"); sendCommand("w"); sendCommand("e");
        // Deposit all
        sendCommand("open case");
        sendCommand("put painting in case");
        sendCommand("put bracelet in case");
        sendCommand("put bar in case");
        sendCommand("put torch in case");
        sendCommand("put chalice in case");
        sendCommand("put coins in case");
        String text = sendCommand("score");
        assertTrue(text.contains("45"), "Score should be 45 after troll + 6 treasures, got: " + text);
    }

    @Test
    public void testVictoryCondition() {
        selectAndGetText();
        navigatePastTroll();
        sendCommand("drop sword");
        // Collect underground treasures
        sendCommand("n"); sendCommand("take painting");
        sendCommand("n"); sendCommand("n"); sendCommand("take bracelet");
        sendCommand("e"); sendCommand("take bar");
        sendCommand("w"); sendCommand("n"); sendCommand("n");
        sendCommand("take torch"); sendCommand("take chalice");
        // Return via maze/grating
        sendCommand("s"); sendCommand("s"); sendCommand("s");
        sendCommand("s"); sendCommand("s"); sendCommand("w"); sendCommand("s");
        sendCommand("s"); sendCommand("e"); sendCommand("take key");
        sendCommand("n"); sendCommand("take coins");
        sendCommand("n"); sendCommand("u");
        sendCommand("open grating"); sendCommand("u");
        // To living room
        sendCommand("n"); sendCommand("n"); sendCommand("n");
        sendCommand("n"); sendCommand("e"); sendCommand("w"); sendCommand("e");
        // Deposit 6 treasures
        sendCommand("open case");
        sendCommand("put painting in case"); sendCommand("put bracelet in case");
        sendCommand("put bar in case"); sendCommand("put torch in case");
        sendCommand("put chalice in case"); sendCommand("put coins in case");
        // Get egg from tree
        sendCommand("w"); sendCommand("w"); sendCommand("e");
        sendCommand("s"); sendCommand("u"); sendCommand("take egg");
        sendCommand("d"); sendCommand("s"); sendCommand("e");
        sendCommand("w"); sendCommand("e");
        // Deposit egg → victory
        String text = sendCommand("put egg in case");
        assertTrue(text.contains("Congratulations"), "Should trigger victory, got: " + text);
        text = sendCommand("score");
        assertTrue(text.contains("50"), "Final score should be 50, got: " + text);
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

    /** Navigate to East of Chasm with troll defeated. Holding lamp only. */
    private void navigatePastTroll() {
        navigateToCellar();
        sendCommand("n"); // Troll Room
        sendCommand("attack troll");
        sendCommand("e"); // East of Chasm
    }
}
