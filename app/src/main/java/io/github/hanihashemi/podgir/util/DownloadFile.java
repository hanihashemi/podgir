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
public class DownloadFile extends AsyncTask<String, Integer, Boolean> {

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
            URLConnection conection = url.openConnection();
            conection.connect();

            int lenghtOfFile = conection.getContentLength();
            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            OutputStream output = new FileOutputStream(Directory.getInstance().getNewFile(podcast.getObjectId(), feed.getObjectId()).getAbsolutePath());

            byte data[] = new byte[1024];
            long total = 0;
            int count;

            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress((int) (total * 100) / lenghtOfFile);
                output.write(data, 0, count);
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
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        notification.updateProgress(values[0]);
    }
}
