package org.example.mygametrackerjavafx.ProcessTracker;

import org.example.mygametrackerjavafx.FolderFinder.FixedFoldersLister;

import java.nio.file.Path;

public enum ProcessFolderVerifier {
    STEAM {
        @Override
        public Path getPath() {
            return FixedFoldersLister.getValidFolder().stream()
                    .filter(p -> p.toString().toLowerCase().contains("steamapps\\common"))
                    .findFirst()
                    .orElse(null);
        }
    },
    GAMES{
      @Override
      public Path getPath(){
          return FixedFoldersLister.getValidFolder().stream()
                  .filter(p -> p.toString().toLowerCase().contains("games"))
                  .findFirst()
                  .orElse(null);
      }
    };


    public abstract Path getPath();


    public static boolean matches(Path pathToCheck) {

        pathToCheck = pathToCheck.toAbsolutePath().normalize();



        for (ProcessFolderVerifier folders : values()) {
            Path basePath = folders.getPath().toAbsolutePath().normalize();
            if (pathToCheck.equals(basePath)) return true;
            if (pathToCheck.startsWith(basePath)) return true;
        }
        return false;
    }
}
