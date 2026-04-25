package main

import (
	"bufio"
	"encoding/hex"
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
	readerName := "ACS ACR1555 1S CL Reader(1)"
	if len(os.Args) > 1 {
		readerName = os.Args[1]
	}

	ctx, err := scard.EstablishContext()
	if err != nil {
		fmt.Fprintf(os.Stderr, "Cannot establish context: %v\n", err)
		os.Exit(1)
	}
	defer ctx.Release()

	readers, err := ctx.ListReaders()
	if err != nil {
		fmt.Fprintf(os.Stderr, "Cannot list readers: %v\n", err)
		os.Exit(1)
	}

	// Find matching reader
	found := ""
	for _, r := range readers {
		if strings.Contains(r, readerName) || strings.Contains(r, "1555") {
			found = r
			break
		}
	}
	if found == "" {
		fmt.Fprintf(os.Stderr, "Reader not found. Available readers:\n")
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
	defer card.Disconnect(scard.LeaveCard)

	fmt.Fprintf(os.Stderr, "Connected to %s\n\n", found)

	// SELECT applet
	text, err := selectApplet(card)
	if err != nil {
		fmt.Fprintf(os.Stderr, "SELECT failed: %v\n", err)
		os.Exit(1)
	}
	fmt.Print(text)

	// Interactive loop
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

		// Local commands
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
