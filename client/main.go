package main

import (
	"bufio"
	"encoding/hex"
	"flag"
	"fmt"
	"os"
	"strings"

	"github.com/ebfe/scard"
)

// APDU constants
var (
	appletAID = []byte{0xA0, 0x00, 0x00, 0x06, 0x47, 0x2E, 0x5A, 0x4F, 0x52, 0x4B, 0x01}

	claZork     byte = 0x80
	insCommand  byte = 0x10
	insLook     byte = 0x12
	insRestart  byte = 0x14
	insGetResp  byte = 0xC0
)

func main() {
	testMode := flag.Bool("test", false, "Run automated test walkthrough")
	reader := flag.String("reader", "ACS ACR1555 1S CL Reader(1)", "Smartcard reader name (substring match)")
	flag.BoolVar(&debug, "debug", false, "Show APDU hex dumps")
	flag.Parse()

	card := connectCard(*reader)
	defer card.Disconnect(scard.LeaveCard)

	if *testMode {
		os.Exit(runTests(card))
	}

	runInteractive(card)
}

func connectCard(readerName string) *scard.Card {
	ctx, err := scard.EstablishContext()
	if err != nil {
		fmt.Fprintf(os.Stderr, "Cannot establish context: %v\n", err)
		os.Exit(1)
	}
	// Note: context leaked intentionally - lives for process lifetime

	readers, err := ctx.ListReaders()
	if err != nil {
		fmt.Fprintf(os.Stderr, "Cannot list readers: %v\n", err)
		os.Exit(1)
	}

	found := ""
	for _, r := range readers {
		if strings.Contains(r, readerName) {
			found = r
			break
		}
	}
	if found == "" {
		fmt.Fprintf(os.Stderr, "Reader %q not found. Available:\n", readerName)
		for _, r := range readers {
			fmt.Fprintf(os.Stderr, "  %s\n", r)
		}
		os.Exit(1)
	}

	card, err := ctx.Connect(found, scard.ShareShared, scard.ProtocolAny)
	if err != nil {
		fmt.Fprintf(os.Stderr, "Cannot connect to card: %v\n", err)
		os.Exit(1)
	}

	fmt.Fprintf(os.Stderr, "Connected to %s\n", found)
	return card
}

func runInteractive(card *scard.Card) {
	text, err := selectApplet(card)
	if err != nil {
		fmt.Fprintf(os.Stderr, "SELECT failed: %v\n", err)
		os.Exit(1)
	}
	fmt.Print(text)

	scanner := bufio.NewScanner(os.Stdin)
	for {
		fmt.Print("\n> ")
		if !scanner.Scan() {
			break
		}
		line := strings.TrimSpace(scanner.Text())
		if line == "" {
			continue
		}

		lower := strings.ToLower(line)
		if lower == "/quit" || lower == "/exit" {
			break
		}
		if lower == "/look" {
			text, err = sendLook(card)
		} else if lower == "/restart" {
			text, err = sendRestart(card)
		} else if lower == "/help" {
			fmt.Println("Commands: type game commands directly (e.g. 'north', 'take lamp')")
			fmt.Println("  /look    - re-describe room (INS 0x12)")
			fmt.Println("  /restart - restart game (INS 0x14)")
			fmt.Println("  /debug   - toggle APDU debug output")
			fmt.Println("  /quit    - exit client")
			continue
		} else if lower == "/debug" {
			debug = !debug
			fmt.Fprintf(os.Stderr, "Debug: %v\n", debug)
			continue
		} else {
			text, err = sendCommand(card, line)
		}

		if err != nil {
			fmt.Fprintf(os.Stderr, "Error: %v\n", err)
			continue
		}
		fmt.Print(text)
	}
	fmt.Println()
}

var debug bool

func selectApplet(card *scard.Card) (string, error) {
	// SELECT: 00 A4 04 00 Lc AID
	apdu := make([]byte, 5+len(appletAID))
	apdu[0] = 0x00 // CLA
	apdu[1] = 0xA4 // INS SELECT
	apdu[2] = 0x04 // P1: by name
	apdu[3] = 0x00 // P2
	apdu[4] = byte(len(appletAID))
	copy(apdu[5:], appletAID)

	return transmitAndCollect(card, apdu)
}

func sendCommand(card *scard.Card, cmd string) (string, error) {
	data := []byte(cmd)
	if len(data) > 80 {
		data = data[:80]
	}
	apdu := make([]byte, 5+len(data))
	apdu[0] = claZork
	apdu[1] = insCommand
	apdu[2] = 0x00
	apdu[3] = 0x00
	apdu[4] = byte(len(data))
	copy(apdu[5:], data)

	return transmitAndCollect(card, apdu)
}

func sendLook(card *scard.Card) (string, error) {
	apdu := []byte{claZork, insLook, 0x00, 0x00}
	return transmitAndCollect(card, apdu)
}

func sendRestart(card *scard.Card) (string, error) {
	apdu := []byte{claZork, insRestart, 0x00, 0x00}
	return transmitAndCollect(card, apdu)
}

// transmitAndCollect sends an APDU and collects all response data,
// handling GET RESPONSE chaining (SW 61XX).
func transmitAndCollect(card *scard.Card, apdu []byte) (string, error) {
	if debug {
		fmt.Fprintf(os.Stderr, ">> %s\n", hex.EncodeToString(apdu))
	}

	resp, err := card.Transmit(apdu)
	if err != nil {
		return "", fmt.Errorf("transmit: %w", err)
	}

	if debug {
		fmt.Fprintf(os.Stderr, "<< %s\n", hex.EncodeToString(resp))
	}

	if len(resp) < 2 {
		return "", fmt.Errorf("response too short: %d bytes", len(resp))
	}

	var result []byte
	for {
		sw1 := resp[len(resp)-2]
		sw2 := resp[len(resp)-1]
		data := resp[:len(resp)-2]
		result = append(result, data...)

		if sw1 == 0x90 && sw2 == 0x00 {
			// Success, no more data
			break
		} else if sw1 == 0x61 {
			// More data available - send GET RESPONSE
			le := sw2
			getResp := []byte{0x00, insGetResp, 0x00, 0x00, le}
			if debug {
				fmt.Fprintf(os.Stderr, ">> %s\n", hex.EncodeToString(getResp))
			}
			resp, err = card.Transmit(getResp)
			if err != nil {
				return string(result), fmt.Errorf("GET RESPONSE: %w", err)
			}
			if debug {
				fmt.Fprintf(os.Stderr, "<< %s\n", hex.EncodeToString(resp))
			}
			if len(resp) < 2 {
				return string(result), fmt.Errorf("GET RESPONSE too short")
			}
		} else {
			return string(result), fmt.Errorf("SW=%02X%02X", sw1, sw2)
		}
	}

	return string(result), nil
}

// ---------------------------------------------------------------
// Automated test walkthrough
// ---------------------------------------------------------------

type testStep struct {
	cmd    string   // command to send ("" = use special action)
	expect []string // substrings that must appear in response
	name   string   // test description
}

func runTests(card *scard.Card) int {
	passed, failed := 0, 0

	check := func(name, text string, expect []string, err error) bool {
		if err != nil {
			fmt.Printf("FAIL  %s: %v\n", name, err)
			failed++
			return false
		}
		for _, s := range expect {
			if !strings.Contains(text, s) {
				fmt.Printf("FAIL  %s: expected %q in response\n", name, s)
				fmt.Printf("      got: %s\n", truncate(text, 200))
				failed++
				return false
			}
		}
		fmt.Printf("  ok  %s\n", name)
		passed++
		return true
	}

	// -- SELECT applet, then restart to clean state --
	text, err := selectApplet(card)
	if !check("select applet", text, []string{}, err) {
		fmt.Println("Cannot select applet - aborting tests")
		return 1
	}

	text, err = sendRestart(card)
	if !check("restart", text, []string{"West of House"}, err) {
		fmt.Println("Cannot restart - aborting tests")
		return 1
	}

	steps := []testStep{
		// ======== Basic navigation ========
		{"look", []string{"West of House", "open field"}, "look at starting room"},
		{"n", []string{"North of House"}, "go north"},
		{"s", []string{"West of House"}, "go back south"},
		{"s", []string{"South of House"}, "go south"},
		{"n", []string{"West of House"}, "return north to start"},

		// ======== Mailbox puzzle ========
		{"open mailbox", []string{"Opened"}, "open mailbox"},
		{"take leaflet", []string{"Taken"}, "take leaflet"},
		{"read leaflet", []string{"WELCOME TO ZORK"}, "read leaflet"},
		{"i", []string{"carrying", "leaflet"}, "inventory shows leaflet"},
		{"drop leaflet", []string{"Dropped"}, "drop leaflet"},

		// ======== Enter house via window ========
		// West of House → N → North of House → E → Behind House
		{"n", []string{"North of House"}, "north to north of house"},
		{"e", []string{"Behind House"}, "east to behind house"},
		{"open window", []string{"effort", "open"}, "open kitchen window"},
		{"w", []string{"Kitchen"}, "enter kitchen through window"},

		// ======== Kitchen ========
		{"look", []string{"Kitchen", "table"}, "look in kitchen"},
		{"open sack", []string{"Opened"}, "open brown sack"},

		// ======== Living room: get lamp + sword ========
		{"e", []string{"Living Room"}, "east to living room"},
		{"take lamp", []string{"Taken"}, "take brass lantern"},   // inv: 1
		{"take sword", []string{"Taken"}, "take elvish sword"},   // inv: 2

		// ======== Rug → trap door → cellar ========
		{"move rug", []string{"trap door"}, "move rug reveals trap door"},
		{"open trap", []string{"Opened"}, "open trap door"},
		{"light lamp", []string{"now on"}, "light the lantern"},
		{"down", []string{"Cellar", "trap door"}, "descend to cellar (trap closes)"},
		{"look", []string{"Cellar"}, "confirm cellar"},
		{"u", []string{"closed"}, "trap door closed behind us"},

		// ======== Troll combat ========
		{"n", []string{"Troll Room"}, "north to troll room"},
		{"look", []string{"troll"}, "troll is present"},
		{"e", []string{"troll"}, "troll blocks east"},
		{"attack troll", []string{"vanishes", "smoke"}, "kill troll with sword"},
		{"drop sword", []string{"Dropped"}, "drop sword (free inv slot)"}, // inv: 1
		{"e", []string{"East of Chasm"}, "east now unblocked"},

		// ======== Collect underground treasures ========
		{"n", []string{"Gallery"}, "north to gallery"},
		{"take painting", []string{"Taken"}, "take painting"},     // inv: 2
		{"n", []string{"N-S Passage"}, "north to passage"},
		{"n", []string{"Round Room"}, "north to round room"},
		{"take bracelet", []string{"Taken"}, "take bracelet"},     // inv: 3
		{"e", []string{"Loud Room"}, "east to loud room"},
		{"take bar", []string{"Taken"}, "take platinum bar"},      // inv: 4
		{"w", []string{"Round Room"}, "back to round room"},
		{"n", []string{"Narrow Passage"}, "north to narrow passage"},
		{"n", []string{"Treasure Room"}, "north to treasure room"},
		{"take torch", []string{"Taken"}, "take ivory torch"},     // inv: 5
		{"take chalice", []string{"Taken"}, "take silver chalice"},// inv: 6
		{"i", []string{"carrying", "painting", "bracelet", "chalice"}, "inventory check"},

		// ======== Return underground → maze for key + coins ========
		{"s", []string{"Narrow Passage"}, "back south"},
		{"s", []string{"Round Room"}, "back to round room"},
		{"s", []string{"N-S Passage"}, "back to passage"},
		{"s", []string{"Gallery"}, "back to gallery"},
		{"s", []string{"East of Chasm"}, "back to chasm"},
		{"w", []string{"Troll Room"}, "west to troll room"},
		{"s", []string{"Cellar"}, "south to cellar"},
		{"s", []string{"maze", "twisty"}, "south into maze 1"},
		{"e", []string{"maze", "twisty"}, "east to maze 2"},
		{"take key", []string{"Taken"}, "take skeleton key"},      // inv: 7
		{"n", []string{"maze"}, "north to maze 3"},
		{"take coins", []string{"Taken"}, "take bag of coins"},    // inv: 8 (full)

		// ======== Exit via grating ========
		{"n", []string{"maze"}, "north to maze 1"},
		{"u", []string{"Grating Room"}, "up to grating room"},
		{"open grating", []string{"Opened"}, "open grating with key"},
		{"u", []string{"Grating Clearing"}, "up through grating"},

		// ======== Surface → living room to deposit treasures ========
		// Grating Clearing → N → Forest South → N → South of House
		// → N → West of House → N → North of House → E → Behind House
		// → W → Kitchen (window still open) → E → Living Room
		{"n", []string{"Forest"}, "north to forest south"},
		{"n", []string{"South of House"}, "north to south of house"},
		{"n", []string{"West of House"}, "north to west of house"},
		{"n", []string{"North of House"}, "north to north of house"},
		{"e", []string{"Behind House"}, "east to behind house"},
		{"w", []string{"Kitchen"}, "west through window to kitchen"},
		{"e", []string{"Living Room"}, "east to living room"},

		// ======== Deposit 6 treasures ========
		{"open case", []string{"Opened"}, "open trophy case"},
		{"put painting in case", []string{"Done"}, "deposit painting"},
		{"put bracelet in case", []string{"Done"}, "deposit bracelet"},
		{"put bar in case", []string{"Done"}, "deposit platinum bar"},
		{"put torch in case", []string{"Done"}, "deposit torch"},
		{"put chalice in case", []string{"Done"}, "deposit chalice"},
		{"put coins in case", []string{"Done"}, "deposit bag of coins"},
		// Score: 8 (troll) + 6+5+5+6+5+10 = 45
		{"score", []string{"45"}, "score after troll + 6 treasures"},

		// ======== Get egg from tree ========
		// Living Room → E(=Kitchen) → W → Behind House → E → Clearing
		// → S → Forest Path → U → Up a Tree
		{"w", []string{"Kitchen"}, "back to kitchen"},
		{"w", []string{"Behind House"}, "out through window"},
		{"e", []string{"Clearing"}, "east to clearing"},
		{"s", []string{"Forest Path"}, "south to forest path"},
		{"u", []string{"Up a Tree"}, "climb tree"},
		{"take egg", []string{"Taken"}, "take jeweled egg"},
		// Down → Forest Path → S → North of House → E → Behind House
		// → W → Kitchen → E → Living Room
		{"d", []string{"Forest Path"}, "climb down"},
		{"s", []string{"North of House"}, "south from forest path"},
		{"e", []string{"Behind House"}, "east to behind house"},
		{"w", []string{"Kitchen"}, "through window"},
		{"e", []string{"Living Room"}, "to living room"},

		// ======== Final treasure → victory ========
		{"put egg in case", []string{"Done", "Congratulations"}, "deposit egg - victory!"},
		{"score", []string{"50"}, "final score is 50"},

		// ======== Error handling & misc ========
		{"xyzzy", []string{"don't know"}, "unknown word"},
		{"take", []string{"don't understand"}, "incomplete sentence"},
		{"brief", []string{"Brief"}, "brief mode"},
		{"verbose", []string{"Verbose"}, "verbose mode"},
		{"wait", []string{"Time passes"}, "wait command"},
		{"diagnose", []string{"Turns", "Score"}, "diagnose command"},
	}

	for _, step := range steps {
		text, err := sendCommand(card, step.cmd)
		if !check(step.name, text, step.expect, err) {
			// Print context for debugging
			fmt.Printf("      cmd: %q\n", step.cmd)
		}
	}

	// Test the LOOK APDU (INS 0x12)
	text, err = sendLook(card)
	check("LOOK APDU", text, []string{"Living Room"}, err)

	// Test restart
	text, err = sendRestart(card)
	check("RESTART APDU", text, []string{"West of House", "Mini-ZORK"}, err)

	fmt.Printf("\n%d passed, %d failed\n", passed, failed)
	if failed > 0 {
		return 1
	}
	return 0
}

func truncate(s string, n int) string {
	s = strings.ReplaceAll(s, "\n", "\\n")
	if len(s) > n {
		return s[:n] + "..."
	}
	return s
}
