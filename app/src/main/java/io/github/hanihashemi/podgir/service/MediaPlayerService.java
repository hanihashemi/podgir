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
import io.github.hanihashemi.podgir.helper.NotificationUtils;
import io.github.hanihashemi.podgir.helper.PlayerUtils;
import io.github.hanihashemi.podgir.model.Episode;
import timber.log.Timber;

/**
 * Created by hani on 9/13/15.
 */
public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, Runnable, MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener {
    private static final String ARG_FEED = "feed_model";
    private MediaPlayer mediaPlayer;
    private boolean isRegistered;
    private Handler handler;
    private Episode episode;
    private boolean isPlay;

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
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        int result;
        do {
            Timber.d("Try to grant audio focus ...");
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignore) {
            }
            result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        } while (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setWakeMode(App.getInstance().getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        if (episode.getDuration() == null || episode.getDuration() <= 0) {
            play();
        } else {
            player.seekTo(episode.getDuration());
            play();
        }

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
        stopForeground(true);
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
                pause();
                break;
            case MediaPlayerAction.ACTION_BACK_TEN_SECONDS:
                int position = mediaPlayer.getCurrentPosition() - 10000;
                position = position < 0 ? 0 : position;
                mediaPlayer.seekTo(position);
                break;
            case MediaPlayerAction.ACTION_PLAY:
                play();
                break;
            case MediaPlayerAction.ACTION_CHANGE_POSITION_BY_PERCENT:
                mediaPlayer.seekTo(new PlayerUtils().getProgressToTimer(action.getPositionPercent(), mediaPlayer.getDuration()));
                break;
            case MediaPlayerAction.ACTION_CHANGE_POSITION:
                mediaPlayer.seekTo(action.getPosition());
                break;
        }
    }

    private void pause() {
        stopForeground(true);
        mediaPlayer.pause();
        isPlay = false;
    }

    private void play() {
        showNotification();
        mediaPlayer.start();
        isPlay = true;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        stopForeground(true);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (mediaPlayer != null && isPlay) {
                    try {
                        play();
                        mediaPlayer.setVolume(1.0f, 1.0f);
                    } catch (Exception ex) {
                        Timber.w(ex, "AUDIOFOCUS_GAIN");
                    }
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mediaPlayer.isPlaying()) pause();
                releaseMediaPlayer();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mediaPlayer.isPlaying()) mediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }
}
