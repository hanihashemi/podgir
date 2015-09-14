package io.github.hanihashemi.podgir.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import io.github.hanihashemi.podgir.App;

/**
 * Created by hani on 8/27/15.
 */
public class NotificationUtils {
    private int id;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    public NotificationUtils(int id) {
        this.id = id;
        notificationManager = (NotificationManager) App.getInstance().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public Notification initService(String title, String text, Class<?> cls) {
        builder = new NotificationCompat.Builder(App.getInstance().getApplicationContext());
        return builder.setContentIntent(createPendingIntent(cls))
                .setSmallIcon(android.R.drawable.stat_sys_download).setTicker(text).setWhen(System.currentTimeMillis())
                .setAutoCancel(false).setContentTitle(title)
                .setContentText(text).build();
    }

    private PendingIntent createPendingIntent(Class<?> cls) {
        Intent contentIntent = new Intent(App.getInstance().getApplicationContext(), cls);
        return PendingIntent.getActivity(App.getInstance().getApplicationContext(), 0, contentIntent, 0);
    }

    public void initProgress(String title, String text) {
        builder = new NotificationCompat.Builder(App.getInstance().getApplicationContext());
        builder.setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(android.R.drawable.stat_sys_download).build();
        startProgress();
    }

    private void startProgress() {
        builder.setProgress(100, 0, false);//builder.setProgress(0, 0, true);
        notificationManager.notify(id, builder.build());
    }

    public void updateProgress(int percent) {
        builder.setProgress(100, percent, false);
        notificationManager.notify(id, builder.build());
    }

    public void completeProgress() {
        builder.setProgress(0, 0, false);
        builder.setContentTitle("Download completed")
                .setContentText("")
                .setSmallIcon(android.R.drawable.stat_sys_download_done);
        notificationManager.notify(id, builder.build());
    }

    public void failedProgress() {
        builder.setProgress(0, 0, false);
        builder.setContentTitle("Failed to download")
                .setContentText("")
                .setSmallIcon(android.R.drawable.stat_notify_error);
        notificationManager.notify(id, builder.build());
    }
}
