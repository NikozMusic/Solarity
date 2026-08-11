package net.solsticeteam.solarity.terminal;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntryReader {

    private static final String ENTRY_RESOURCE_PATH = "/net/solsticeteam/solarity/entries/";
    private static final Pattern ID_PATTERN = Pattern.compile("\"([^\"]+)\"");

    public static Set<String> listUnlockedIds(File savePath) {
        Set<String> ids = new LinkedHashSet<>();
        File file = new File(savePath, "solarity/unlocked_entries.json");
        if (!file.exists()) return ids;

        try {
            String content = Files.readString(file.toPath());
            Matcher m = ID_PATTERN.matcher(content);
            while (m.find()) ids.add(m.group(1));
        } catch (IOException ignored) {
            //Do nothing
        }
        return ids;
    }

    //First line of the entry's txt resource, or null if it doesn't exist.
    public static String getTitle(String entryId) {
        String[] lines = readResourceLines(entryId);
        return lines == null || lines.length == 0 ? null : lines[0];
    }

    //Rest of the file
    public static String getBody(String entryId) {
        String[] lines = readResourceLines(entryId);
        if (lines == null || lines.length <= 1) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < lines.length; i++) {
            sb.append(lines[i]);
            if (i < lines.length - 1) sb.append("\n");
        }
        return sb.toString();
    }

    public static boolean entryExists(String entryId) {
        try (InputStream in = EntryReader.class.getResourceAsStream(ENTRY_RESOURCE_PATH + entryId + ".txt")) {
            return in != null;
        } catch (IOException e) {
            return false;
        }
    }

    private static String[] readResourceLines(String entryId) {
        try (InputStream in = EntryReader.class.getResourceAsStream(ENTRY_RESOURCE_PATH + entryId + ".txt")) {
            if (in == null) return null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                return reader.lines().toArray(String[]::new);
            }
        } catch (IOException e) {
            return null;
        }
    }
}