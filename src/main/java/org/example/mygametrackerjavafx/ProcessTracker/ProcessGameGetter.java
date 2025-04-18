package org.example.mygametrackerjavafx.ProcessTracker;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ProcessGameGetter {

    public static String getGameName(String gamePath) {
        try {
            Path f = Paths.get(gamePath);
            Path ParentPath = f.getParent();
            if (ParentPath != null) {
                return ParentPath.getFileName().toString();
            }

        } catch (Exception ignored) {
            return "";
        }
        return "";
    }

}
