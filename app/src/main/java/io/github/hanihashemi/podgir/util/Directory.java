package io.github.hanihashemi.podgir.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by hani on 8/27/15.
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class Directory {
    public static final String BASE_DIRECTORY = "Podgir";
    private static Directory directory;
    private File podgirDir;

    public static Directory getInstance() {
        if (directory == null) {
            directory = new Directory();
            directory.initRootDir();
        }
        return directory;
    }

    public File getFile(String fileName) {
        return new File(podgirDir.getAbsolutePath(), fileName);
    }

    private void initRootDir() {
        if (podgirDir == null) {
            File sdDir = Environment.getExternalStorageDirectory();
            podgirDir = new File(sdDir.getAbsolutePath(), BASE_DIRECTORY);
            if (!podgirDir.exists())
                podgirDir.mkdir();
        }
    }
}
