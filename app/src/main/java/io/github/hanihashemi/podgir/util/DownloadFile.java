package io.github.hanihashemi.podgir.util;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import io.github.hanihashemi.podgir.App;
import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.model.Episode;
import io.github.hanihashemi.podgir.model.Podcast;
import timber.log.Timber;

/**
 * Created by hani on 8/27/15.
 */
public class DownloadFile extends AsyncTask<String, Integer, Boolean> {

    private NotificationUtils notificationUtils;
    private Podcast podcast;
    private Episode episode;

    public DownloadFile(Podcast podcast, Episode episode) {
        this.episode = episode;
        this.podcast = podcast;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            Timber.d("Start to downloading %s", url);

            URLConnection connection = url.openConnection();
            connection.connect();

            int lengthOfFile = connection.getContentLength();
            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            OutputStream output = new FileOutputStream(Directory.getInstance().getFile(episode.getObjectId()).getAbsolutePath());

            byte data[] = new byte[1024];
            long total = 0;
            int readFile;
            int tempDownloadedPercent = 0;

            while ((readFile = input.read(data)) != -1) {
                total += readFile;
                int downloadedPercent = (int) (total * 100) / lengthOfFile;
                if (downloadedPercent != tempDownloadedPercent) {
                    publishProgress(downloadedPercent);
                    tempDownloadedPercent = downloadedPercent;
                }
                output.write(data, 0, readFile);
            }
            output.flush();
            output.close();
            input.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        notificationUtils = new NotificationUtils(120);
        notificationUtils.initProgress(String.format(App.getInstance().getApplicationContext().getString(R.string.notification_download_title), podcast.getName(), episode.getTitle()), App.getInstance().getApplicationContext().getString(R.string.notification_download_text));
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (result) {
            notificationUtils.completeProgress();
        } else {
            File file = Directory.getInstance().getFile(episode.getObjectId());
            if (file.exists())
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            notificationUtils.failedProgress();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Timber.d(values[0] + "");
        notificationUtils.updateProgress(values[0]);
    }
}
