package io.github.hanihashemi.podgir.util;

import android.os.Environment;

import java.io.File;

import timber.log.Timber;

/**
 * Created by hani on 8/27/15.
 */
public class Directory {
    public static final String BASE_DIRECTORY = "Podgir";
    private static Directory directory;
    private File podgirDir;

    public static Directory getInstance() {
        if (directory == null)
            directory = new Directory();
        return directory;
    }

    public File getNewFile(String directory, String fileName) {
        getPodgirDir();

        File podcastDir = new File(podgirDir.getAbsolutePath(), directory);
        if (!podcastDir.exists())
            podcastDir.mkdir();

        File file = new File(podcastDir.getAbsolutePath(), fileName);
        Timber.d("File created: %s", file.getAbsolutePath());
        return file;
    }

    public boolean isFileThere(String directory, String fileName) {
        getPodgirDir();

        File podcastDir = new File(podgirDir.getAbsolutePath(), directory);
        return new File(podcastDir.getAbsolutePath(), fileName).exists();
    }

    private void getPodgirDir() {
        if (podgirDir == null) {
            File sdDir = Environment.getExternalStorageDirectory();
            podgirDir = new File(sdDir.getAbsolutePath(), BASE_DIRECTORY);
            if (!podgirDir.exists())
                podgirDir.mkdir();
        }
    }
}
