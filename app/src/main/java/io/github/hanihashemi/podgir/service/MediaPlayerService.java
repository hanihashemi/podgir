package io.github.hanihashemi.podgir.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.io.IOException;

import io.github.hanihashemi.podgir.App;
import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.broadcast.MediaPlayerAction;
import io.github.hanihashemi.podgir.broadcast.MediaPlayerStatus;
import io.github.hanihashemi.podgir.model.Episode;
import io.github.hanihashemi.podgir.util.NotificationUtils;
import io.github.hanihashemi.podgir.util.PlayerUtils;
import timber.log.Timber;

/**
 * Created by hani on 9/13/15.
 */
public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, Runnable, MediaPlayer.OnCompletionListener {
    private static final String ARG_FEED = "feed_model";
    private MediaPlayer mediaPlayer;
    private boolean isRegistered;
    private Handler handler;
    private Episode episode;

    public static Intent getIntent(Context context, Episode episode) {
        Intent intent = new Intent(context, MediaPlayerService.class);
        intent.putExtra(ARG_FEED, episode);
        return intent;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getExtras() == null || !intent.getExtras().containsKey(ARG_FEED))
            return START_NOT_STICKY;

        Episode newEpisode = intent.getExtras().getParcelable(ARG_FEED);

        if (newEpisode == null)
            return START_NOT_STICKY;

        if (episode != null && newEpisode.getObjectId().equals(episode.getObjectId())) {
            doAction(new MediaPlayerAction(MediaPlayerAction.ACTION_PLAY));
            return START_NOT_STICKY;
        }
        episode = newEpisode;

        releaseMediaPlayer();
        initMediaPlayer();

        try {
            mediaPlayer.setDataSource(this, Uri.parse(episode.getFile().getAbsolutePath()));
            mediaPlayer.prepareAsync();
            showNotification();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            registerBus();
        }

        return START_NOT_STICKY;
    }

    private void showNotification() {
        startForeground(2, new NotificationUtils(210).initService(episode));
    }

    private void registerBus() {
        if (!isRegistered)
            App.getInstance().getBus().register(this);
        isRegistered = true;
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setWakeMode(App.getInstance().getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        player.start();
        if (handler == null) {
            handler = new Handler();
            handler.postDelayed(this, 100);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Timber.d("=========> MediaPlayer Service onError");
        Toast.makeText(this, R.string.player_serivce_error, Toast.LENGTH_LONG).show();
        stopForeground(true);
        mediaPlayer.reset();
        return true;
    }

    @Override
    public void onDestroy() {
        MediaPlayerStatus mediaPlayerStatus = new MediaPlayerStatus();
        mediaPlayerStatus.setPlay(mediaPlayer.isPlaying());
        App.getInstance().getBus().post(mediaPlayerStatus);

        App.getInstance().getBus().unregister(this);
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void run() {
        try {
            MediaPlayerStatus mediaPlayerStatus = new MediaPlayerStatus();
            mediaPlayerStatus.setPlay(mediaPlayer.isPlaying());
            mediaPlayerStatus.setCorrectTime(mediaPlayer.getCurrentPosition());
            mediaPlayerStatus.setTotalTime(mediaPlayer.getDuration());
            mediaPlayerStatus.setFileId(episode.getObjectId());
            App.getInstance().getBus().post(mediaPlayerStatus);
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }

        handler.postDelayed(this, 100);
    }

    @Subscribe
    public void doAction(MediaPlayerAction action) {
        if (mediaPlayer == null)
            return;

        switch (action.getCode()) {
            case MediaPlayerAction.ACTION_PAUSE:
                stopForeground(true);
                mediaPlayer.pause();
                break;
            case MediaPlayerAction.ACTION_BACK_TEN_SECONDS:
                int position = mediaPlayer.getCurrentPosition() - 10000;
                position = position < 0 ? 0 : position;
                mediaPlayer.seekTo(position);
                break;
            case MediaPlayerAction.ACTION_PLAY:
                showNotification();
                mediaPlayer.start();
                break;
            case MediaPlayerAction.ACTION_CHANGE_POSITION:
                mediaPlayer.seekTo(new PlayerUtils().getProgressToTimer(action.getPositionPercent(), mediaPlayer.getDuration()));
                break;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        stopForeground(true);
    }
}
