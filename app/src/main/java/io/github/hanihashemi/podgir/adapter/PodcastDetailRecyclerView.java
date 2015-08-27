package io.github.hanihashemi.podgir.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import io.github.hanihashemi.podgir.App;
import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.adapter.viewholder.FeedViewHolder;
import io.github.hanihashemi.podgir.adapter.viewholder.PodcastDetailViewHolder;
import io.github.hanihashemi.podgir.model.Feed;
import io.github.hanihashemi.podgir.model.Podcast;

/**
 * Created by hani on 8/25/15.
 */
public class PodcastDetailRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_HOLDER_PODCAST_DETAIL = 0;
    private static final int VIEW_HOLDER_FEEDS = 1;
    private Podcast podcast;
    private List<Feed> feeds;
    private FeedViewHolder.OnClick feedOnClick;

    public PodcastDetailRecyclerView(Podcast podcast, List<Feed> feeds, FeedViewHolder.OnClick feedOnClick) {
        this.podcast = podcast;
        this.feeds = feeds;
        this.feedOnClick = feedOnClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_HOLDER_FEEDS:
                return new FeedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_feed, parent, false), feedOnClick);
            case VIEW_HOLDER_PODCAST_DETAIL:
                return new PodcastDetailViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_podcast_detail, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_HOLDER_PODCAST_DETAIL:
                PodcastDetailViewHolder podcastHolder = (PodcastDetailViewHolder) holder;
                podcastHolder.name.setText(podcast.getName());
                break;
            case VIEW_HOLDER_FEEDS:
                FeedViewHolder feedHolder = (FeedViewHolder) holder;
                Feed feed = feeds.get(position - 1);
                feedHolder.name.setText(feed.getTitle());
                feedHolder.download.setText(feed.isDownloaded() ? App.getInstance().getString(R.string.podcast_detail_feed_downloaded) : App.getInstance().getString(R.string.podcast_detail_feed_not_download));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return VIEW_HOLDER_PODCAST_DETAIL;
        return VIEW_HOLDER_FEEDS;
    }

    @Override
    public int getItemCount() {
        return feeds.size() + 1;
    }
}

