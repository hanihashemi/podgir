package io.github.hanihashemi.podgir.util;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import io.github.hanihashemi.podgir.App;
import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.model.Feed;
import io.github.hanihashemi.podgir.model.Podcast;

/**
 * Created by hani on 8/27/15.
 */
public class DownloadFile extends AsyncTask<String, Long, Boolean> {

    private static final int CONNECT_TIMEOUT = 8000;
    private static final int READ_TIMEOUT = 8000;
    private Notification notification;
    private Podcast podcast;
    private Feed feed;

    public DownloadFile(Podcast podcast, Feed feed) {
        this.feed = feed;
        this.podcast = podcast;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            URLConnection connection = url.openConnection();
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.connect();

            int totalBytesFile = connection.getContentLength();
            InputStream input = new BufferedInputStream(url.openStream(), 4096);
            String outputFile = Directory.getInstance().getNewFile(podcast.getObjectId(), feed.getObjectId()).getAbsolutePath();
            OutputStream output = new FileOutputStream(outputFile);

            byte data[] = new byte[4096];
            long totalBytesWrite = 0;
            int count;
            int updateNotification = 0;

            while ((count = input.read(data)) != -1) {
                totalBytesWrite += count;
                output.write(data, 0, count);

                if (updateNotification == 50 || updateNotification == 0) {
                    onProgressUpdate((long) totalBytesFile, totalBytesWrite);
                    updateNotification = 1;
                } else {
                    updateNotification++;
                }
            }

            onProgressUpdate(100L, 100L);

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
        notification = new Notification();
        notification.show(String.format(App.getInstance().getApplicationContext().getString(R.string.notification_download_title)
                , podcast.getName(), feed.getTitle()), App.getInstance().getApplicationContext().getString(R.string.notification_download_text));
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (result) {
            notification.complete();
            feed.save();
            podcast.save();
        } else {
            notification.failed();
        }
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        super.onProgressUpdate(values);

        long total = values[0];
        long downloaded = values[1];

        notification.updateProgress((int) (100 * downloaded / total));
    }
}
