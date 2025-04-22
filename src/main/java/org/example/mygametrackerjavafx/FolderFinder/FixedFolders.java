package org.example.mygametrackerjavafx.FolderFinder;

import java.nio.file.Path;

public enum FixedFolders {
    STEAM("steamapps\\common"),
    GAMES("C:\\Games");

    private final String identifier;

    FixedFolders(String identifier) {
        this.identifier = identifier.toLowerCase();
    }

    public boolean isMatch(Path path) {
        Path normalized = path.toAbsolutePath().normalize();
        return normalized.toString().toLowerCase().contains(identifier);
    }

    public static FixedFolders fromPath(Path path) {
        for (FixedFolders folder : values()) {
            if (folder.isMatch(path)) {
                return folder;
            }
        }
        return null;
    }

}

