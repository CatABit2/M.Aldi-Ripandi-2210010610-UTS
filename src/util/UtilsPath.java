package util;

import java.io.File;

public final class UtilsPath {
    private UtilsPath(){}

    public static File dataDir() {
        return new File(System.getProperty("user.home"), ".resep-app");
    }

    public static File resepTxt() {
        return new File(dataDir(), "resep.txt");
    }
}
