package net.solsticeteam.solarity.terminal;

import java.io.File;
import java.net.URISyntaxException;

public class InstanceLocator {

    public static File findInstanceRoot() {
        File jarFile;
        try {
            jarFile = new File(Main.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            return null;
        }


        //First checks if the mod is even installed properly
        File modsDir = jarFile.getParentFile();
        if (modsDir == null || !modsDir.getName().equalsIgnoreCase("mods")) {
            return null;
        }

        File instanceRoot = modsDir.getParentFile();
        if (instanceRoot == null) return null;

        //Require the entries (written at runtime)
        File marker = new File(instanceRoot, "solarity");
        if (!marker.isDirectory()) return null;

        return instanceRoot;
    }
}