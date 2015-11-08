package io.github.hanihashemi.podgir.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.activity.PlayerActivity;
import io.github.hanihashemi.podgir.adapter.DownloadedRecyclerView;
import io.github.hanihashemi.podgir.adapter.viewholder.FeedInDownloadedViewHolder;
import io.github.hanihashemi.podgir.base.BaseFragment;
import io.github.hanihashemi.podgir.model.Episode;

/**
 * Created by hani on 8/28/15.
 */
public class DownloadedFragment extends BaseFragment {

    @Bind(R.id.recycler_view)
    protected RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Episode> episodes;
    FeedInDownloadedViewHolder.OnClick feedOnClick = new FeedInDownloadedViewHolder.OnClick() {
        @Override
        public void onPlay(int position) {
            startActivity(PlayerActivity.getIntent(DownloadedFragment.this.getActivity(), episodes.get(position)));
        }
    };

    @Override
    protected void customizeUI() {
        super.customizeUI();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        episodes = new ArrayList<>();
        adapter = new DownloadedRecyclerView(episodes, feedOnClick);
        recyclerView.setAdapter(adapter);

        fetchData();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recycler;
    }

    @Override
    protected void gatherArguments(Bundle bundle) {

    }

    private void fetchData() {
        List<Episode> allEpisodes = Episode.listAll(Episode.class);
        List<Episode> downloadedEpisodes = new ArrayList<>();

        for (Episode episode : allEpisodes)
            if (episode.isDownloaded())
                downloadedEpisodes.add(episode);

        episodes.addAll(downloadedEpisodes);
        adapter.notifyDataSetChanged();
    }
}
