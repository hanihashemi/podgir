package io.github.hanihashemi.podgir.fragment;

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
import io.github.hanihashemi.podgir.base.BaseFragment;
import io.github.hanihashemi.podgir.model.Episode;
import io.github.hanihashemi.podgir.model.EpisodeResultResponse;
import io.github.hanihashemi.podgir.model.Podcast;
import io.github.hanihashemi.podgir.network.request.GsonRequest;
import io.github.hanihashemi.podgir.util.DownloadFile;

/**
 * Created by hani on 8/24/15.
 */
public class PodcastDetailFragment extends BaseFragment implements Response.Listener<EpisodeResultResponse> {
    public static final String ARG_PODCAST = "arg_podcast";
    @Bind(R.id.recycler_view)
    protected RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Podcast podcast;
    private List<Episode> episodes;

    private FeedInPodcastDetailViewHolder.OnClick feedOnClick = new FeedInPodcastDetailViewHolder.OnClick() {
        @Override
        public void onDownload(int position) {
            Episode episode = episodes.get(position - 1);

            if (!episode.isDownloaded()) {
                new DownloadFile(podcast, episode).execute(episode.getUrl());
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
    protected void customizeUI() {
        super.customizeUI();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        episodes = new ArrayList<>();
        adapter = new PodcastDetailRecyclerView(podcast, episodes, feedOnClick);
        recyclerView.setAdapter(adapter);

        fetchData();
    }

    private void fetchData() {
        GsonRequest<EpisodeResultResponse> request = new Episode().remoteFindAll(podcast.getObjectId(), this, this);
        App.getInstance().addRequestToQueue(request, this);
    }

    @Override
    public void onResponse(EpisodeResultResponse response) {
        Episode.saveResults(Episode.class, response);

        episodes.clear();
        episodes.addAll(Episode.findAllAsList(Episode.class));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        podcast = getArguments().getParcelable(ARG_PODCAST);
    }
}
