package io.github.hanihashemi.bulbul.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import io.github.hanihashemi.bulbul.R;
import io.github.hanihashemi.bulbul.adapter.viewholder.PodcastViewHolder;
import io.github.hanihashemi.bulbul.model.Podcast;

/**
 * Created by hani on 8/18/15.
 */
public class PodcastsRecyclerView extends RecyclerView.Adapter<PodcastViewHolder> {

    List<Podcast> podcasts;

    public PodcastsRecyclerView(List<Podcast> podcasts) {
        this.podcasts = podcasts;
    }

    @Override
    public PodcastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PodcastViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_podcast, parent, false));
    }

    @Override
    public void onBindViewHolder(PodcastViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return podcasts.size();
    }
}
