package io.github.hanihashemi.podgir.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;

import com.squareup.otto.Produce;

import java.io.IOException;

import io.github.hanihashemi.podgir.App;
import io.github.hanihashemi.podgir.activity.PlayerActivity;
import io.github.hanihashemi.podgir.broadcast.MediaPlayerStatus;
import io.github.hanihashemi.podgir.model.Episode;
import io.github.hanihashemi.podgir.util.NotificationUtils;

/**
 * Created by hani on 9/13/15.
 */
public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private static final String ARG_FEED = "feed_model";
    private MediaPlayer mediaPlayer;

    public static Intent getIntent(Context context, Episode episode) {
        Intent intent = new Intent(context, MediaPlayerService.class);
        intent.putExtra(ARG_FEED, episode);
        return intent;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Episode episode = intent.getExtras().getParcelable(ARG_FEED);

        Uri myUri = Uri.parse(episode.getFile().getAbsolutePath());
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(App.getInstance().getApplicationContext(), myUri);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setWakeMode(App.getInstance().getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mediaPlayer.prepareAsync();

            startForeground(2, new NotificationUtils(210).initService("title", "text", PlayerActivity.class));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            App.getInstance().getBus().register(this);
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        player.start();

        MediaPlayerStatus mediaPlayerStatus = new MediaPlayerStatus();
        mediaPlayerStatus.setPlay(player.isPlaying());

        App.getInstance().getBus().post(mediaPlayerStatus);
    }

    @Produce
    public MediaPlayerStatus produceAnswer() {
        MediaPlayerStatus mediaPlayerStatus = new MediaPlayerStatus();
        mediaPlayerStatus.setPlay(false);
        return mediaPlayerStatus;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mediaPlayer.reset();
        return true;
    }

    @Override
    public void onDestroy() {
        App.getInstance().getBus().unregister(this);
        if (mediaPlayer != null) mediaPlayer.release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
