package net.solsticeteam.solarity.terminal;

import java.io.File;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HostSession {

    private static final long STALE_MS = 30000;

    public final String world;
    public final File savePath;

    private HostSession(String world, File savePath) {
        this.world = world;
        this.savePath = savePath;
    }

    public static HostSession connect(File instanceRoot) {
        try {
            File sessionFile = new File(instanceRoot, "solarity/session.json");
            if (!sessionFile.exists()) return null;

            String content = Files.readString(sessionFile.toPath());
            long timestamp = extractLong(content, "timestamp");
            if (System.currentTimeMillis() - timestamp > STALE_MS) return null;

            String world = extractString(content, "world");
            String savePath = extractString(content, "savePath");
            return new HostSession(world, new File(savePath));
        } catch (Exception e) {
            return null;
        }
    }

    private static String extractString(String json, String key) {
        Matcher m = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]*)\"").matcher(json);
        return m.find() ? m.group(1) : null;
    }

    private static long extractLong(String json, String key) {
        Matcher m = Pattern.compile("\"" + key + "\"\\s*:\\s*(\\d+)").matcher(json);
        return m.find() ? Long.parseLong(m.group(1)) : 0L;
    }
}