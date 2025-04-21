package org.example.mygametrackerjavafx.FolderFinder;


import java.nio.file.Path;
import java.nio.file.Paths;

public enum FixedFolders {
    STEAM{
        @Override
        public Path getPath(){
            return Paths.get ("C:\\Program Files (x86)\\Steam\\steamapps\\common");
        }
    };


    public abstract Path getPath();
    public static boolean matches(Path pathToCheck){
        for (FixedFolders folders : values()){
            if (folders.getPath().equals(pathToCheck)){
                return true;
            }
            if (pathToCheck.getParent().equals(folders.getPath())){
                return true;
            }
            if (pathToCheck.getParent().toString().contains("steamapps")){
                return true;
            }
        }
        return false;
    }





}
