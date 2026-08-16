package net.solsticeteam.solarity.terminal;

import java.io.File;
import java.util.Scanner;
import java.util.Set;

public class Terminal {

    // ANSI COLORS
    private static final String RESET   = "\u001B[0m";
    private static final String BOLD    = "\u001B[1m";
    private static final String DIM     = "\u001B[2m";

    private static final String RED     = "\u001B[31m";
    private static final String GREEN   = "\u001B[32m";
    private static final String YELLOW  = "\u001B[33m";
    private static final String CYAN    = "\u001B[36m";
    private static final String WHITE   = "\u001B[37m";

    // TERMINAL CONTROL
    private static final String CLEAR_SCREEN = "\u001B[2J\u001B[H";

    private static final String VERSION = "2.1";

    private HostSession session;

    public void run() {

        clear();

        File instanceRoot = InstanceLocator.findInstanceRoot();

        if (instanceRoot == null) {
            error("No session detected.");
            sleep();
            return;
        }

        status("Session connected.");

        session = HostSession.connect(instanceRoot);

        if (session == null) {
            error("No host detected.");
            sleep();
            return;
        }

        status("Host connection established.");
        status("Session registered: " + session.world);

        System.out.println();

        printBanner();

        Scanner scanner = new Scanner(System.in);

        while (true) {

            prompt();

            String input;

            try {
                input = scanner.nextLine().trim();
            } catch (Exception e) {
                break;
            }

            if (input.isEmpty()) {
                continue;
            }

            String command = input.toLowerCase();

            if (command.equals("exit")) {
                break;

            } else if (command.equals("help")) {
                handleHelp();

            } else if (command.equals("clear")) {
                clear();
                printBanner();

            } else if (command.equals("list")) {
                handleList();

            } else if (command.startsWith("view ")) {
                handleRead(input.substring(5).trim());

            } else {
                error("Unknown command: " + input);
            }
        }
    }

    private void printBanner() {

        System.out.println(
                CYAN + BOLD +
                        "SOL PROJECT ARCHIVE MANAGER" +
                        RESET
        );

        System.out.println(
                DIM +
                        "SPAM Terminal Interface v" + VERSION +
                        RESET
        );

        System.out.println(
                DIM +
                        "----------------------------------------" +
                        RESET
        );

        System.out.println(
                DIM +
                        "Type HELP for a list of commands." +
                        RESET
        );

        System.out.println();
    }

    private void prompt() {

        System.out.print(
                CYAN + "user" +
                        DIM + "@" +
                        WHITE + "archive" +
                        DIM + ":~ " +
                        RESET +
                        "> "
        );
    }

    private void handleHelp() {

        System.out.println();

        System.out.println(
                CYAN + BOLD +
                        "Commands" +
                        RESET
        );

        System.out.println(
                DIM +
                        "----------------------------------------" +
                        RESET
        );

        printCommand("LIST", "List archives");
        printCommand("VIEW <ID>", "View an archive");
        printCommand("CLEAR", "Clear terminal");
        printCommand("HELP", "Display command list");
        printCommand("EXIT", "Terminate session");

        System.out.println();
    }

    private void printCommand(
            String command,
            String description
    ) {

        System.out.printf(
                "  " +
                        GREEN + "%-14s" +
                        RESET +
                        DIM + "%s" +
                        RESET +
                        "%n",
                command,
                description
        );
    }

    private void handleList() {

        System.out.println();

        System.out.println(
                CYAN + BOLD +
                        "Archives" +
                        RESET
        );

        System.out.println(
                DIM +
                        "----------------------------------------" +
                        RESET
        );

        Set<String> entries =
                EntryReader.listUnlockedIds(session.savePath);

        if (entries.isEmpty()) {

            System.out.println(
                    DIM + "No entries found." + RESET
            );

            System.out.println();
            return;
        }

        for (String id : entries) {

            String title = EntryReader.getTitle(id);

            System.out.print(
                    GREEN + id + RESET
            );

            if (title != null && !title.isBlank()) {

                System.out.print(
                        DIM + " - " + RESET +
                                WHITE + title + RESET
                );
            }

            System.out.println();
        }

        System.out.println();

        System.out.println(
                DIM +
                        entries.size() +
                        (entries.size() == 1
                                ? " record."
                                : " records.") +
                        RESET
        );

        System.out.println();
    }

    private void handleRead(String entryId) {

        if (entryId.toLowerCase().endsWith(".txt")) {
            entryId =
                    entryId.substring(
                            0,
                            entryId.length() - 4
                    );
        }

        Set<String> entries =
                EntryReader.listUnlockedIds(session.savePath);

        if (!entries.contains(entryId)) {

            error(
                    "Entry '" +
                            entryId +
                            ".txt' not found."
            );

            return;
        }

        if (!EntryReader.entryExists(entryId)) {

            error(
                    "Archive resource '" +
                            entryId +
                            ".txt' not found."
            );

            return;
        }

        String title = EntryReader.getTitle(entryId);
        String body = EntryReader.getBody(entryId);

        System.out.println();

        System.out.println(
                WHITE + BOLD +
                        (title != null
                                ? title
                                : "Unknown Entry") +
                        RESET
        );

        System.out.println(
                DIM +
                        "Record: " +
                        entryId +
                        RESET
        );

        System.out.println(
                DIM +
                        "----------------------------------------" +
                        RESET
        );

        System.out.println();

        if (body != null && !body.isBlank()) {
            System.out.println(body);
        } else {
            System.out.println(
                    DIM +
                            "[ No data available. ]" +
                            RESET
            );
        }

        System.out.println();

        System.out.println(
                GREEN +
                        "< End Of Record >" +
                        RESET
        );

        System.out.println();
    }

    private void status(String message) {

        System.out.println(
                GREEN + "[ OK ] " +
                        RESET +
                        message
        );
    }

    private void warning(String message) {

        System.out.println(
                YELLOW + "[WARN] " +
                        RESET +
                        message
        );
    }

    private void error(String message) {

        System.out.println(
                RED + "[ERROR] " +
                        RESET +
                        RED + message +
                        RESET
        );

        System.out.println();
    }

    private void clear() {

        System.out.print(CLEAR_SCREEN);
        System.out.flush();
    }

    private void sleep() {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
