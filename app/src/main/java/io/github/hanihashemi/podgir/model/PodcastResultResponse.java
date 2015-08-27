package io.github.hanihashemi.podgir.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hani on 8/23/15.
 */
public class PodcastResultResponse {
    @SerializedName("results")
    private List<Podcast> podcasts;

    public List<Podcast> getPodcasts() {
        return podcasts;
    }

    public void setPodcasts(List<Podcast> podcasts) {
        this.podcasts = podcasts;
    }
}
