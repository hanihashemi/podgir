package io.github.hanihashemi.podgir.helper;

import android.os.Environment;

import java.io.File;

/**
 * Created by hani on 8/27/15.
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class Directory {
    public static final String BASE_DIRECTORY = "Podgir";
    private static Directory directory;
    private File rootDir;

    public static Directory getInstance() {
        if (directory == null) {
            directory = new Directory();
            directory.initRootDir();
        }
        return directory;
    }

    public File getFile(String fileName) {
        return new File(rootDir.getAbsolutePath(), fileName);
    }

    private void initRootDir() {
        if (rootDir == null) {
            File sdDir = Environment.getExternalStorageDirectory();
            rootDir = new File(sdDir.getAbsolutePath(), BASE_DIRECTORY);
            if (!rootDir.exists())
                rootDir.mkdir();
        }
    }
}
