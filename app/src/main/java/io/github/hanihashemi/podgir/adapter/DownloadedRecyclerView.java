package io.github.hanihashemi.podgir.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.adapter.viewholder.FeedInDownloadedViewHolder;
import io.github.hanihashemi.podgir.model.Feed;

/**
 * Created by hani on 8/28/15.
 */
public class DownloadedRecyclerView extends RecyclerView.Adapter<FeedInDownloadedViewHolder> {

    private FeedInDownloadedViewHolder.OnClick feedOnClick;
    private List<Feed> feeds;

    public DownloadedRecyclerView(List<Feed> feeds, FeedInDownloadedViewHolder.OnClick feedOnClick) {
        this.feedOnClick = feedOnClick;
        this.feeds = feeds;
    }

    @Override
    public FeedInDownloadedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeedInDownloadedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_feed_in_downloaded, parent, false), feedOnClick);
    }

    @Override
    public void onBindViewHolder(FeedInDownloadedViewHolder holder, int position) {
        Feed feed = feeds.get(position);
        holder.name.setText(feed.getTitle() != null ? feed.getTitle() : "");
        holder.feed.setText(feed.getSummary() != null ? feed.getSummary() : "");
    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }
}
