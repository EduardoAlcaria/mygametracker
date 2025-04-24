package org.example.mygametrackerjavafx.FolderFinder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public enum FixedFoldersEnum {
    STEAM("steam\\steamapps"),
    CUSTOM("");

    private final String identifier;

    FixedFoldersEnum(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public boolean isMatch(Path path) throws IOException {

        Path normalized = path.toAbsolutePath().normalize();
        if (CUSTOM.getIdentifier().equals(" ")) {
            return normalized.toString().toLowerCase().contains(Objects.requireNonNull(FolderUserInput.UserCustomPathReader()));
        }
        return normalized.toString().toLowerCase().contains(STEAM.getIdentifier());

    }
    public static FixedFoldersEnum fromPath(Path path) throws IOException {
        for (FixedFoldersEnum folder : values()) {
            if (folder.isMatch(path)) {
                return folder;
            }
        }
        return null;
    }
}