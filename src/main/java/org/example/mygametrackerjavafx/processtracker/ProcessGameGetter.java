package org.example.mygametrackerjavafx.processtracker;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ProcessGameGetter{
    public static String getGameName(String gamePath) {
        try {
            Path f = Paths.get(gamePath);

            if (f.toAbsolutePath().toString().contains(ProcessFolderVerifier.STEAM.getPath().toString())) {
                for (int i = 0; i < f.normalize().getNameCount(); i++) {
                    if (f.getName(i).toString().equalsIgnoreCase("common")) {
                        if (i + 1 < f.getNameCount()) {
                            return f.getName(i + 1).toString();
                        }

                    }
                }
            }
            if (f.toAbsolutePath().toString().contains(ProcessFolderVerifier.GAMES.getPath().toString()))
                for (int i = 0; i < f.normalize().getNameCount(); i++) {
                    if (f.getName(i).toString().equals("Games")) {
                        if (i + 1 < f.getNameCount()) {
                            return f.getName(i + 1).toString();
                        }
                    }
                }


        } catch (Exception ignored) {
            return "";
        }
        return "";
    }
}

