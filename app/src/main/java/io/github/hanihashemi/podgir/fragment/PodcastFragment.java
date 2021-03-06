package io.github.hanihashemi.podgir.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.github.hanihashemi.podgir.App;
import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.activity.PodcastDetailActivity;
import io.github.hanihashemi.podgir.adapter.PodcastsAdapter;
import io.github.hanihashemi.podgir.adapter.viewholder.PodcastViewHolder;
import io.github.hanihashemi.podgir.base.BaseSwipeFragment;
import io.github.hanihashemi.podgir.model.Podcast;
import io.github.hanihashemi.podgir.model.PodcastResultResponse;
import io.github.hanihashemi.podgir.network.request.GsonListRequest;

/**
 * Created by hani on 8/18/15.
 */
public class PodcastFragment extends BaseSwipeFragment<PodcastResultResponse> implements PodcastViewHolder.OnClick {
    private RecyclerView.Adapter adapter;
    private List<Podcast> podcasts;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recycler;
    }

    @Override
    protected void customizeUI() {
        super.customizeUI();

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        podcasts = new ArrayList<>();
        adapter = new PodcastsAdapter(getActivity(), podcasts, this);
        recyclerView.setAdapter(adapter);
    }

    protected void fetchData() {
        GsonListRequest request = Podcast.getModel().reqFindAll(this, this);
        App.getInstance().addRequestToQueue(request, this);
    }

    @Override
    public void onResponse(PodcastResultResponse response) {
        super.onResponse(response);
        Podcast.saveResults(Podcast.class, response);

        podcasts.clear();
        podcasts.addAll(Podcast.findAllAsList(Podcast.class));

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onImageView(int position) {
        startActivity(PodcastDetailActivity.getIntent(getActivity(), podcasts.get(position)));
    }

    @Override
    protected void gatherArguments(Bundle bundle) {
    }
}
