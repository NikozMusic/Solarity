package net.solsticeteam.solarity.terminal;

import java.io.File;
import java.net.URISyntaxException;

public class Main {

    private static final String RELAUNCH_FLAG = "SOLARITY_RELAUNCHED";

    public static void main(String[] args) throws Exception {
        if (System.getenv(RELAUNCH_FLAG) == null && System.console() == null) {
            relaunchInTerminal();
            return;
        }
        new Terminal().run();
    }

    private static void relaunchInTerminal() throws Exception {
        File jarFile = getJarFile();
        String os = System.getProperty("os.name").toLowerCase();

        ProcessBuilder pb;
        //Windows only has one terminal
        if (os.contains("win")) {
            pb = new ProcessBuilder("cmd", "/c", "start", "\"SOL Archive\"",
                    "cmd", "/k", "java", "-jar", jarFile.getAbsolutePath());
        } else if (os.contains("mac")) {
            String script = "tell application \"Terminal\" to do script \"java -jar '"
                    + jarFile.getAbsolutePath() + "'\"";
            pb = new ProcessBuilder("osascript", "-e", script);
        } else {
            // Check for common Linux terminals
            String[] candidates = {"x-terminal-emulator", "gnome-terminal", "konsole", "xterm"};
            pb = null;
            for (String term : candidates) {
                if (isOnPath(term)) {
                    if (term.equals("gnome-terminal")) {
                        pb = new ProcessBuilder(term, "--", "java", "-jar", jarFile.getAbsolutePath());
                    } else {
                        pb = new ProcessBuilder(term, "-e",
                                "java -jar \"" + jarFile.getAbsolutePath() + "\"");
                    }
                    break;
                }
            }
            if (pb == null) {
                // No terminal found just run in-place (in case of MACOS or BSD or some other weird system)
                new Terminal().run();
                return;
            }
        }

        pb.environment().put(RELAUNCH_FLAG, "1");
        pb.start();
    }

    private static boolean isOnPath(String command) {
        for (String dir : System.getenv("PATH").split(File.pathSeparator)) {
            if (new File(dir, command).canExecute()) return true;
        }
        return false;
    }

    private static File getJarFile() throws URISyntaxException {
        return new File(Main.class.getProtectionDomain()
                .getCodeSource().getLocation().toURI());
    }
}