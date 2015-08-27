package io.github.hanihashemi.podgir.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.adapter.viewholder.PodcastViewHolder;
import io.github.hanihashemi.podgir.model.Podcast;

/**
 * Created by hani on 8/18/15.
 */
public class PodcastsRecyclerView extends RecyclerView.Adapter<PodcastViewHolder> {

    private List<Podcast> podcasts;
    private PodcastViewHolder.OnClick podcastOnClick;

    public PodcastsRecyclerView(List<Podcast> podcasts, PodcastViewHolder.OnClick podcastOnClick) {
        this.podcasts = podcasts;
        this.podcastOnClick = podcastOnClick;
    }

    @Override
    public PodcastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PodcastViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_podcast, parent, false), podcastOnClick);
    }

    @Override
    public void onBindViewHolder(PodcastViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return podcasts.size();
    }
}
