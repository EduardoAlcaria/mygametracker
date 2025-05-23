package org.example.mygametrackerjavafx.folderfinder;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;


import java.util.List;




public class FixedFoldersLister {
    private static List<Path> cachedFolders = null;

    public static List<Path> getValidFolder() {
        if (cachedFolders != null) return cachedFolders;

        Path[] drives = {Path.of("C:\\"), Path.of("D:\\")};
        List<Path> validFolders = new ArrayList<>();

        System.out.println("initializing the folders validation");

        for (Path drive : drives) {
            List<Path> res = FolderScanner.scanForFixedFolder(drive);
            validFolders.addAll(res);
        }

        List<String> folderPriority = List.of("Steam", "Games");

        validFolders.sort(Comparator.comparingInt(path -> {
            String folderPath = path.toAbsolutePath().toString().toLowerCase().replace(" ", "");

            for (int i = 0; i < folderPriority.size(); i++) {
                if (folderPath.contains(folderPriority.get(i).toLowerCase())) {
                    return i;
                }
            }
            return Integer.MAX_VALUE;
        }));
        System.out.println("finished the folders validation");
        System.out.println("Ready to initiate a game");

        cachedFolders = validFolders;

        return validFolders;
    }

}

