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
    private BroadcastReceiver broadcastReceiver;
    private Listener listener;

    public DownloadManagerHelper(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
        referenceIds = new ArrayList<>();
    }

    public long add(String title, String description, String fileName, String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(title);
        request.setDescription(description);
        request.setDestinationInExternalPublicDir(Directory.BASE_DIRECTORY, fileName);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);

        Timber.d("Added to download manager: %s", url);
        long requestId = getDownloadManager().enqueue(request);
        referenceIds.add(requestId);
        return requestId;
    }

    public BroadcastReceiver getBroadcastReceiver() {
        if (broadcastReceiver == null)
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    for (Long referenceId : referenceIds) {
                        long receivedReferenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                        if (referenceId != receivedReferenceId)
                            continue;

                        checkDownloadStatus(referenceId, listener);
                    }
                }
            };
        return broadcastReceiver;
    }

    public void checkDownloadStatus(Long referenceId, Listener listener) {
        try {
            Cursor cursor = getDownloadManager().query(new DownloadManager.Query().setFilterById(referenceId));
            cursor.moveToFirst();
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            String fileName = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));

            switch (status) {
                case DownloadManager.STATUS_FAILED:
                case DownloadManager.STATUS_PENDING:
                    Timber.d("File pending/failed: %s", fileName);
                    listener.onDownloadFailed(referenceId);
                    break;
                case DownloadManager.STATUS_PAUSED:
                    Timber.d("File is paused: %s", fileName);
                    break;
                case DownloadManager.STATUS_RUNNING:
                    Timber.d("File downloading: %s", fileName);
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    Timber.d("File successfully downloaded: %s", fileName);
                    listener.onDownloadSuccess(referenceId);
                    break;
                default:
                    Timber.d("Unknown status");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            listener.onDownloadFailed(referenceId);
        }
    }

    private DownloadManager getDownloadManager() {
        return (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public interface Listener {
        void onDownloadFailed(long downloadId);

        void onDownloadSuccess(long downloadId);
    }
}