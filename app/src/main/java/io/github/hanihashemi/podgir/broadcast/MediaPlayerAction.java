package io.github.hanihashemi.podgir.broadcast;

/**
 * Created by hani on 9/19/15.
 */
public class MediaPlayerAction {
    public static final int ACTION_PLAY = 0;
    public static final int ACTION_PAUSE = 1;
    public static final int ACTION_BACK_FIFTEEN_SECONDS = 2;

    private int action;

    public MediaPlayerAction(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }
}
