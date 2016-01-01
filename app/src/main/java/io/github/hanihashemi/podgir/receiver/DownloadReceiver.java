package io.github.hanihashemi.podgir.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import java.io.File;

import io.github.hanihashemi.podgir.App;
import timber.log.Timber;

/**
 * Created by hani on 1/1/16.
 * email:jhanihashemi@gmail.com
 */
public class DownloadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

        try {
            Cursor cursor = getDownloadManager().query(new DownloadManager.Query().setFilterById(referenceId));
            cursor.moveToFirst();
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            String fileName = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));

            switch (status) {
                case DownloadManager.STATUS_FAILED:
                case DownloadManager.STATUS_PENDING:
                    new File(fileName).deleteOnExit();
                    Timber.d("DownloadReceiver ========> File is failed or pending: %s", fileName);
                    break;
                case DownloadManager.STATUS_PAUSED:
                    Timber.d("DownloadReceiver ========> File is paused: %s", fileName);
                    break;
                case DownloadManager.STATUS_RUNNING:
                    Timber.d("DownloadReceiver ========> File downloading: %s", fileName);
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    Timber.d("DownloadReceiver ========> File successfully downloaded: %s", fileName);
                    break;
                default:
                    Timber.d("DownloadReceiver ========> Unknown status");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private DownloadManager getDownloadManager() {
        return (DownloadManager) App.getInstance().getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
    }
}
