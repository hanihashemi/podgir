package io.github.hanihashemi.podgir.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.adapter.viewholder.PodcastViewHolder;
import io.github.hanihashemi.podgir.helper.PicassoHelper;
import io.github.hanihashemi.podgir.model.Podcast;

/**
 * Created by hani on 8/18/15.
 */
public class PodcastsAdapter extends RecyclerView.Adapter<PodcastViewHolder> {

    private List<Podcast> podcasts;
    private PodcastViewHolder.OnClick podcastOnClick;
    private Context context;

    public PodcastsAdapter(Context context, List<Podcast> podcasts, PodcastViewHolder.OnClick podcastOnClick) {
        this.podcasts = podcasts;
        this.podcastOnClick = podcastOnClick;
        this.context = context;
    }

    @Override
    public PodcastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PodcastViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_podcast, parent, false), podcastOnClick);
    }

    @Override
    public void onBindViewHolder(PodcastViewHolder holder, int position) {
        PicassoHelper.load(context, podcasts.get(position).getImageUrl(), holder.imageView);
    }

    @Override
    public int getItemCount() {
        return podcasts.size();
    }
}
