package org.example.mygametrackerjavafx.FolderFinder;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FolderScanner {
    public static List<Path> scanForFixedFolder(Path startDir){
        List<Path> foundFolders = new ArrayList<>();

        try {
            Files.walkFileTree(startDir, new SimpleFileVisitor<>(){
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    try {
                        dir.toRealPath();
                    }catch (AccessDeniedException ade){
                        return FileVisitResult.SKIP_SUBTREE;
                    }catch (IOException e){
                        return FileVisitResult.SKIP_SUBTREE;
                    }

                    FixedFoldersEnum match = FixedFoldersEnum.fromPath(dir);
                    if (match != null){
                        foundFolders.add(dir);
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc){
                    return FileVisitResult.SKIP_SUBTREE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return foundFolders;
    }


}

