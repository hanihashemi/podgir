package io.github.hanihashemi.podgir.fragment;

import android.app.DownloadManager;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.github.hanihashemi.podgir.App;
import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.activity.PlayerActivity;
import io.github.hanihashemi.podgir.adapter.PodcastDetailAdapter;
import io.github.hanihashemi.podgir.adapter.viewholder.EpisodeInPodcastDetailViewHolder;
import io.github.hanihashemi.podgir.base.BaseSwipeFragment;
import io.github.hanihashemi.podgir.helper.DownloadManagerHelper;
import io.github.hanihashemi.podgir.helper.DownloadManagerHelper.Listener;
import io.github.hanihashemi.podgir.model.Episode;
import io.github.hanihashemi.podgir.model.EpisodeResultResponse;
import io.github.hanihashemi.podgir.model.Podcast;
import io.github.hanihashemi.podgir.network.request.GsonRequest;
import timber.log.Timber;

/**
 * Created by hani on 8/24/15.
 */
public class PodcastDetailFragment extends BaseSwipeFragment<EpisodeResultResponse> implements Response.Listener<EpisodeResultResponse>, Listener {
    public static final String ARG_PODCAST = "arg_podcast";
    @Bind(R.id.recycler_view)
    protected RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Podcast podcast;
    private List<Episode> episodes;
    private DownloadManagerHelper downloadManagerHelper;

    private EpisodeInPodcastDetailViewHolder.OnClick feedOnClick = new EpisodeInPodcastDetailViewHolder.OnClick() {
        @Override
        public void onDownload(int position) {
            Episode episode = episodes.get(position - 1);

            if (!episode.isDownloaded()) {
                long downloadId = downloadManagerHelper.add(getString(R.string.notification_download_title, podcast.getName(), episode.getTitle()), getString(R.string.app_name_fa), episode.getObjectId(), episode.getUrl());
//                long downloadId = downloadManagerHelper.add(getString(R.string.notification_download_title, podcast.getName(), episode.getTitle()), getString(R.string.app_name_fa), episode.getObjectId(), "http://cdn.p30download.com/?b=p30dl-ebook&f=Click.549.1394.10.06_p30download.com.zip");
                episode.setDownloadId(downloadId);
                episode.save();
                adapter.notifyDataSetChanged();
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
        adapter = new PodcastDetailAdapter(getActivity(), podcast, episodes, feedOnClick);
        recyclerView.setAdapter(adapter);
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
        CheckDownloadStatusByDownloadManager();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        downloadManagerHelper = new DownloadManagerHelper(getActivity(), this);
        getActivity().registerReceiver(downloadManagerHelper.getBroadcastReceiver(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        CheckDownloadStatusByDownloadManager();
    }

    private void CheckDownloadStatusByDownloadManager() {
        if (episodes == null)
            return;

        Timber.d("======================== Start Checking ======================");
        for (Episode episode : episodes)
            if (episode.getDownloadId() != null) {
                Timber.d("DownloadManager ===> checking episode %s: id is %s", episode.getTitle(), episode.getDownloadId());
                downloadManagerHelper.checkDownloadStatus(episode.getDownloadId(), this);
            }
        Timber.d("======================== End Checking ======================");
    }

    @Override
    public void onPause() {
        try {
            getActivity().unregisterReceiver(downloadManagerHelper.getBroadcastReceiver());
        } catch (Exception ignore) {
        }
        super.onPause();
    }

    @Override
    public void onDownloadFailed(long downloadId) {
        Timber.d("DownloadManager ===> File to download");

        Episode episode = getEpisode(downloadId);
        if (episode == null) {
            Timber.w("Couldn't find episode with this downloadId");
            return;
        }

        episode.setDownloadId(null);
        episode.getFile().deleteOnExit();
        episode.save();

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDownloadSuccess(long downloadId) {
        Timber.d("DownloadManager ===> File is downloaded");

        Episode episode = getEpisode(downloadId);
        if (episode == null) {
            Timber.w("Couldn't find episode with this downloadId");
            return;
        }

        episode.setDownloadId(null);
        episode.save();

        adapter.notifyDataSetChanged();
    }

    @Nullable
    private Episode getEpisode(long downloadId) {
        for (Episode episode : episodes)
            if (episode.getDownloadId() != null && episode.getDownloadId() == downloadId)
                return episode;
        return null;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        episodes.clear();
        adapter.notifyDataSetChanged();
    }
}