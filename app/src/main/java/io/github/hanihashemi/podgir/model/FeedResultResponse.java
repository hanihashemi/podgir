package io.github.hanihashemi.podgir.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hani on 8/26/15.
 */
public class FeedResultResponse {
    @SerializedName("results")
    private List<Episode> episodes;

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> podcasts) {
        this.episodes = episodes;
    }
}
