package io.github.hanihashemi.podgir.util;

import android.os.Environment;

import java.io.File;

import timber.log.Timber;

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

    public File getNewFile(String fileName) {
        File file = new File(podgirDir.getAbsolutePath(), fileName);
        Timber.d("File created: %s", file.getAbsolutePath());
        return file;
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
