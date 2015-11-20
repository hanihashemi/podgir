package io.github.hanihashemi.podgir.util;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by hani on 11/20/15.
 */
public class DownloadManagerHelper {
    private Context context;
    private List<Long> referenceIds;

    public DownloadManagerHelper(Context context) {
        this.context = context;
        referenceIds = new ArrayList<>();
    }

    public void add(String title, String description, String fileName, String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(title);
        request.setDescription(description);
        request.setDestinationInExternalPublicDir(Directory.BASE_DIRECTORY, fileName);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);

        Timber.d("Added to download manager: %s", url);
        referenceIds.add(getDownloadManager().enqueue(request));
    }

    public BroadcastReceiver getBroadcastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                for (Long referenceId : referenceIds) {
                    long receivedReferenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                    if (referenceId != receivedReferenceId)
                        continue;

                    Cursor cursor = getDownloadManager().query(new DownloadManager.Query().setFilterById(referenceId));
                    cursor.moveToFirst();
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    String fileName = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));

                    switch (status) {
                        case DownloadManager.STATUS_FAILED:
                            Timber.d("File failed to download: %s", fileName);
                            break;
                        case DownloadManager.STATUS_PAUSED:
                            break;
                        case DownloadManager.STATUS_PENDING:
                            Timber.d("File pending: %s", fileName);
                            break;
                        case DownloadManager.STATUS_RUNNING:
                            break;
                        case DownloadManager.STATUS_SUCCESSFUL:
                            Timber.d("File successfully downloaded: %s", fileName);
                            break;
                    }
                }
            }
        };
    }

    private DownloadManager getDownloadManager() {
        return (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }
}
