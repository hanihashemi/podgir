package io.github.hanihashemi.podgir.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.adapter.viewholder.EpisodeInPodcastDetailViewHolder;
import io.github.hanihashemi.podgir.adapter.viewholder.PodcastDetailViewHolder;
import io.github.hanihashemi.podgir.helper.PicassoHelper;
import io.github.hanihashemi.podgir.model.Episode;
import io.github.hanihashemi.podgir.model.Podcast;

/**
 * Created by hani on 8/25/15.
 */
public class PodcastDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_HOLDER_PODCAST_DETAIL = 0;
    private static final int VIEW_HOLDER_FEEDS = 1;
    private Podcast podcast;
    private List<Episode> episodes;
    private EpisodeInPodcastDetailViewHolder.OnClick feedOnClick;
    private Context context;

    public PodcastDetailAdapter(Context context, Podcast podcast, List<Episode> episodes, EpisodeInPodcastDetailViewHolder.OnClick feedOnClick) {
        this.podcast = podcast;
        this.episodes = episodes;
        this.feedOnClick = feedOnClick;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_HOLDER_FEEDS:
                return new EpisodeInPodcastDetailViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_episode_in_podcast_detail, parent, false), feedOnClick);
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
                podcastHolder.summary.setText(podcast.getSummary());
                PicassoHelper.load(context, podcast.getImageUrl(), podcastHolder.imageView);
                break;
            case VIEW_HOLDER_FEEDS:
                EpisodeInPodcastDetailViewHolder episodeHolder = (EpisodeInPodcastDetailViewHolder) holder;
                Episode episode = episodes.get(position - 1);
                episodeHolder.name.setText(episode.getTitle());
                episodeHolder.number.setText(episode.getNumber());
                episodeHolder.day.setText(episode.getDay());
                episodeHolder.month.setText(episode.getMonth());
                setDownloadButtonStatus(episode, episodeHolder.download);
                break;
        }
    }

    private void setDownloadButtonStatus(Episode episode, Button download) {
        if (episode.getDownloadId() != null) {
            download.setText(R.string.podcast_detail_feed_downloading);
            download.setEnabled(false);
        } else if (episode.isDownloaded()) {
            download.setText(R.string.podcast_detail_feed_downloaded);
            download.setEnabled(true);
        } else {
            download.setText(R.string.podcast_detail_feed_not_download);
            download.setEnabled(true);
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
        return episodes.size() + 1;
    }
}

