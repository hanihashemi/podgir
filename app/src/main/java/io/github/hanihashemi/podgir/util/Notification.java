package io.github.hanihashemi.podgir.util;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import io.github.hanihashemi.podgir.App;

/**
 * Created by hani on 8/27/15.
 */
public class Notification {
    public static final int NOTIFICATION_ID = 120;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    public Notification() {
        notificationManager = (NotificationManager) App.getInstance().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void show(String title, String text) {
        builder = new NotificationCompat.Builder(App.getInstance().getApplicationContext());
        builder.setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(android.R.drawable.stat_sys_download);
        builder.setProgress(0, 0, true);
        fire();
    }

    public void updateProgress(int percent) {
        builder.setProgress(100, percent, false);
        fire();
    }

    public void complete() {
        builder.setProgress(0, 0, false);
        builder.setContentTitle("Download completed")
                .setContentText("")
                .setSmallIcon(android.R.drawable.stat_sys_download_done);
        fire();
    }

    public void failed() {
        builder.setProgress(0, 0, false);
        builder.setContentTitle("Failed to download")
                .setContentText("")
                .setSmallIcon(android.R.drawable.stat_notify_error);
        fire();
    }

    private void fire() {
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

}
