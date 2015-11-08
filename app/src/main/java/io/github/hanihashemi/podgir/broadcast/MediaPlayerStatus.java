package io.github.hanihashemi.podgir.broadcast;

/**
 * Created by hani on 10/9/15.
 */
public class MediaPlayerStatus {
    private boolean play;
    private int totalTime;
    private int correctTime;

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getCorrectTime() {
        return correctTime;
    }

    public void setCorrectTime(int correctTime) {
        this.correctTime = correctTime;
    }

    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }
}
