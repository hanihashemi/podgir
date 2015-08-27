package io.github.hanihashemi.podgir.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hani on 8/26/15.
 */
public class FeedResultResponse {
    @SerializedName("results")
    private List<Feed> feeds;

    public List<Feed> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<Feed> podcasts) {
        this.feeds = feeds;
    }
}
