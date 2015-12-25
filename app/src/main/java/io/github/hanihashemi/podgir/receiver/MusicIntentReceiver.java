package io.github.hanihashemi.podgir.receiver;

import android.content.Context;
import android.content.Intent;

import io.github.hanihashemi.podgir.App;
import io.github.hanihashemi.podgir.broadcast.MediaPlayerAction;

/**
 * Created by hani on 12/26/15.
 * email:jhanihashemi@gmail.com
 */
public class MusicIntentReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context ctx, Intent intent) {
        if (intent.getAction().equals(android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
            App.getInstance().getBus().post(new MediaPlayerAction(MediaPlayerAction.ACTION_PAUSE));
        }
    }
}