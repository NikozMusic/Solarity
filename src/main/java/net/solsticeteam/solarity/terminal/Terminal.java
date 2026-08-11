
package net.solsticeteam.solarity.terminal;

import net.solsticeteam.solarity.Solarity;
import net.neoforged.fml.ModList;

import java.io.File;
import java.util.Scanner;
import java.util.Set;

public class Terminal {

    // ANSI COLORS
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String DIM = "\u001B[2m";

    private static final String BLACK = "\u001B[30m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";

    // TERMINAL CONTROL
    private static final String CLEAR_SCREEN = "\u001B[2J\u001B[H";
    private static final String CLEAR_LINE = "\u001B[2K\r";

    private HostSession session;

    public void run() {

        clear();

        File instanceRoot = InstanceLocator.findInstanceRoot();

        if (instanceRoot == null) {
            error("No Session Detected.");
            SleepFunc();
            return;
        }

        printStatus("Session Connected.");

        session = HostSession.connect(instanceRoot);

        if (session == null) {
            error("No Host Detected...");
            SleepFunc();
            return;
        }

        printStatus("Host Connection Established...");
        printStatus("Session Registered: " + session.world.toUpperCase());

        System.out.println();

        printBanner();

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.print(
                    BOLD + CYAN + "=> " + RESET
            );

            String input;

            try {
                input = scanner.nextLine().trim();
            } catch (Exception e) {
                break;
            }

            if (input.isEmpty()) {
                continue;
            }

            String command = input.toUpperCase();

            if (command.equals("EXIT")) {
                break;

            } else if (command.equals("HELP")) {
                handleHelp();

            } else if (command.equals("CLEAR")) {
                clear();

            } else if (command.equals("LIST")) {
                handleList();

            } else if (command.startsWith("VIEW ")) {
                handleRead(command.substring(5).trim());

            } else {
                error("Command Error: " + command);
            }
        }
    }

    private void printBanner() {


        System.out.println(
                CYAN +
                         "Sol Project Archive Manager [SPAM] v2.1" +
                        RESET
        );

        System.out.println(
                CYAN +
                        "==========================================" +
                        RESET
        );

        System.out.println();

        System.out.println(
                DIM + "Type " + WHITE + BOLD + "HELP" +
                        DIM + " for a list of commands." + RESET
        );

        System.out.println();
    }

    private void handleHelp() {

        System.out.println();

        System.out.println(
                CYAN + BOLD + "Commands:" + RESET
        );

        System.out.println(
                DIM + "--------------------------------------------" + RESET
        );

        printCommand("LIST", "List archives");
        printCommand("VIEW <ID>", "View an archive");
        printCommand("CLEAR", "Clear terminal");
        printCommand("HELP", "Display command list");
        printCommand("EXIT", "Terminate session");

        System.out.println();
    }

    private void printCommand(String command, String description) {

        System.out.printf(
                "  %s%-12s%s %s%s%s%n",
                GREEN,
                command,
                RESET,
                DIM,
                description,
                RESET
        );
    }

    private void handleList() {

        System.out.println();

        System.out.println(
                CYAN + BOLD + "Archives:" + RESET
        );

        System.out.println(
                DIM + "--------------------------------------------" + RESET
        );

        Set<String> entries =
                EntryReader.listUnlockedIds(session.savePath);

        if (entries.isEmpty()) {

            System.out.println(
                    DIM + "No Entries Found." + RESET
            );

            System.out.println();
            return;
        }

        for (String id : entries) {

            String title = EntryReader.getTitle(id);

            System.out.print(
                    GREEN + "  [" + id.toUpperCase() + "]" + RESET
            );

            if (title != null) {
                System.out.print(
                        " " + WHITE + title.toUpperCase() + RESET
                );
            }

            System.out.println();
        }

        System.out.println();

        System.out.println(
                DIM + entries.size() + " Records." + RESET
        );

        System.out.println();
    }

    private void handleRead(String entryId) {

        entryId = entryId.toUpperCase();

        Set<String> entries =
                EntryReader.listUnlockedIds(session.savePath);

        if (!entries.contains(entryId) || !EntryReader.entryExists(entryId)) {

            error(
                    "Entry '" + entryId + "' Not Found."
            );

            return;
        }

        String title = EntryReader.getTitle(entryId);
        String body = EntryReader.getBody(entryId);

        System.out.println();

        System.out.println(
                WHITE + BOLD +
                        (title != null
                                ? title.toUpperCase()
                                : "UNKNOWN ENTRY") + ".txt" +
                        RESET
        );

        System.out.println(
                DIM +
                        "Record: " + entryId +
                        RESET
        );

        System.out.println(
                WHITE +
                        "============================================" +
                        RESET
        );

        System.out.println();

        if (body != null) {
            System.out.println(body.toUpperCase());
        }

        System.out.println();

        System.out.println(
                DIM + "< End Of File >" + RESET
        );

        System.out.println();
    }

    private void printStatus(String message) {

        System.out.println(
                GREEN + "[ OK ] " + RESET +
                        message.toUpperCase()
        );
    }

    private void warning(String message) {

        System.out.println(
                YELLOW + "[WARN] " + RESET +
                        message.toUpperCase()
        );
    }

    private void error(String message) {

        System.out.println(
                RED + "[ERROR] " + RESET +
                        RED + message.toUpperCase() +
                        RESET
        );

        System.out.println();
    }

    private void clear() {

        System.out.print(CLEAR_SCREEN);
        System.out.flush();
    }

    private void SleepFunc() {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
