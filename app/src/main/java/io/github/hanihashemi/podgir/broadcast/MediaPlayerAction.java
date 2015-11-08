package io.github.hanihashemi.podgir.broadcast;

/**
 * Created by hani on 9/19/15.
 */
public class MediaPlayerAction {
    public static final int ACTION_PLAY = 0;
    public static final int ACTION_PAUSE = 1;
    public static final int ACTION_BACK_TEN_SECONDS = 2;
    public static final int ACTION_CHANGE_POSITION = 3;

    private int action;
    private int positionPercent;

    public MediaPlayerAction(int action) {
        this.action = action;
    }

    public MediaPlayerAction(int action, int positionPercent) {
        this.positionPercent = positionPercent;
        this.action = action;
    }

    public int getCode() {
        return action;
    }

    public int getPositionPercent() {
        return positionPercent;
    }
}
