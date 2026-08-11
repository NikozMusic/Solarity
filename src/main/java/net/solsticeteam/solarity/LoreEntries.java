package net.solsticeteam.solarity;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoreEntries {

    private static final Pattern ID_PATTERN = Pattern.compile("\"([^\"]+)\"");

    public static void unlock(MinecraftServer server, String entryId) {
        try {
            File file = getUnlockedFile(server);
            Set<String> unlocked = readUnlocked(file);

            if (!unlocked.add(entryId)) return;

            writeUnlocked(file, unlocked);
            Solarity.LOGGER.info("Unlocked entry: {}", entryId);
        } catch (IOException e) {
            Solarity.LOGGER.warn("Failed to unlock entry {}", entryId, e);
        }
    }

    private static File getUnlockedFile(MinecraftServer server) {
        Path saveRoot = server.getWorldPath(LevelResource.ROOT);
        File dir = new File(saveRoot.toFile(), "solarity");
        dir.mkdirs();
        return new File(dir, "unlocked_entries.json");
    }

    private static Set<String> readUnlocked(File file) throws IOException {
        Set<String> ids = new LinkedHashSet<>();
        if (!file.exists()) return ids;

        String content = Files.readString(file.toPath());
        Matcher m = ID_PATTERN.matcher(content);
        while (m.find()) ids.add(m.group(1));
        return ids;
    }

    private static void writeUnlocked(File file, Set<String> ids) throws IOException {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (String id : ids) {
            if (!first) sb.append(",");
            sb.append("\"").append(id).append("\"");
            first = false;
        }
        sb.append("]");
        Files.writeString(file.toPath(), sb.toString());
    }
}