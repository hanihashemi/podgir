package io.github.hanihashemi.podgir.fragment;

import android.app.DownloadManager;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.github.hanihashemi.podgir.App;
import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.activity.PlayerActivity;
import io.github.hanihashemi.podgir.adapter.PodcastDetailRecyclerView;
import io.github.hanihashemi.podgir.adapter.viewholder.FeedInPodcastDetailViewHolder;
import io.github.hanihashemi.podgir.base.BaseSwipeFragment;
import io.github.hanihashemi.podgir.model.Episode;
import io.github.hanihashemi.podgir.model.EpisodeResultResponse;
import io.github.hanihashemi.podgir.model.Podcast;
import io.github.hanihashemi.podgir.network.request.GsonRequest;
import io.github.hanihashemi.podgir.util.DownloadManagerHelper;

/**
 * Created by hani on 8/24/15.
 */
public class PodcastDetailFragment extends BaseSwipeFragment<EpisodeResultResponse> implements Response.Listener<EpisodeResultResponse> {
    public static final String ARG_PODCAST = "arg_podcast";
    @Bind(R.id.recycler_view)
    protected RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Podcast podcast;
    private List<Episode> episodes;
    private DownloadManagerHelper downloadManagerHelper;

    private FeedInPodcastDetailViewHolder.OnClick feedOnClick = new FeedInPodcastDetailViewHolder.OnClick() {
        @Override
        public void onDownload(int position) {
            Episode episode = episodes.get(position - 1);

            if (!episode.isDownloaded()) {
                downloadManagerHelper.add(getString(R.string.app_name), getString(R.string.notification_download_title, podcast.getName(), episode.getTitle()), episode.getObjectId(), episode.getUrl());
            } else {
                startActivity(PlayerActivity.getIntent(PodcastDetailFragment.this.getActivity(), episode));
            }
        }
    };

    public static PodcastDetailFragment getInstance(Podcast podcast) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PODCAST, podcast);
        PodcastDetailFragment fragment = new PodcastDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recycler;
    }

    @Override
    protected void gatherArguments(Bundle bundle) {
        podcast = getArguments().getParcelable(ARG_PODCAST);
    }

    @Override
    protected void customizeUI() {
        super.customizeUI();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        episodes = new ArrayList<>();
        adapter = new PodcastDetailRecyclerView(podcast, episodes, feedOnClick);
        recyclerView.setAdapter(adapter);
        downloadManagerHelper = new DownloadManagerHelper(getActivity());
    }

    protected void fetchData() {
        GsonRequest<EpisodeResultResponse> request = Episode.getModel().remoteFindAll(podcast.getObjectId(), this, this);
        App.getInstance().addRequestToQueue(request, this);
    }

    @Override
    public void onResponse(final EpisodeResultResponse response) {
        super.onResponse(response);
        Episode.saveResults(Episode.class, response);

        episodes.clear();

        episodes.addAll(Episode.findAll(podcast.getObjectId()));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(downloadManagerHelper.getBroadcastReceiver(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            getActivity().unregisterReceiver(downloadManagerHelper.getBroadcastReceiver());
        } catch (Exception ignore) {
        }
    }
}
