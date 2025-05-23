package org.example.mygametrackerjavafx.processtracker;

import org.example.mygametrackerjavafx.folderfinder.FixedFoldersLister;

import java.nio.file.Path;

public enum ProcessFolderVerifier {
    STEAM {
        @Override
        public Path getPath() {
            return FixedFoldersLister.getValidFolder().get(0);
        }
    },
    GAMES{
      @Override
      public Path getPath(){
          return FixedFoldersLister.getValidFolder().get(1);
      }
    };


    public abstract Path getPath();


    public static boolean matches(Path pathToCheck) {

        pathToCheck = pathToCheck.toAbsolutePath().normalize();


        for (ProcessFolderVerifier folders : values()) {
            Path basePath = folders.getPath();

            if (basePath == null) continue;

            basePath = basePath.toAbsolutePath().normalize();
            if (pathToCheck.startsWith(basePath)) return true;
        }
        return false;
    }
}
