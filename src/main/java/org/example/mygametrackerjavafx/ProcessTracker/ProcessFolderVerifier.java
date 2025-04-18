package org.example.mygametrackerjavafx.ProcessTracker;


public class ProcessFolderVerifier {

    private static final String[] trustedPaths = new String[]{
            "steam\\steamapps\\common",
            "c:\\games"};

    public static boolean folderExists(String path) {
        String pathLowerCase = path.toLowerCase();
        for (String trustedPath : trustedPaths) {
            if (pathLowerCase.contains(trustedPath)) {
                return true;
            }
        }
        return false;
    }
}
