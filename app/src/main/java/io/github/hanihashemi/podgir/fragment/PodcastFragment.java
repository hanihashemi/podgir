package io.github.hanihashemi.podgir.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import io.github.hanihashemi.podgir.App;
import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.activity.PodcastDetailActivity;
import io.github.hanihashemi.podgir.adapter.PodcastsRecyclerView;
import io.github.hanihashemi.podgir.adapter.viewholder.PodcastViewHolder;
import io.github.hanihashemi.podgir.base.BaseFragment;
import io.github.hanihashemi.podgir.model.Podcast;
import io.github.hanihashemi.podgir.model.PodcastResultResponse;

/**
 * Created by hani on 8/18/15.
 */
public class PodcastFragment extends BaseFragment implements Response.Listener<PodcastResultResponse>, PodcastViewHolder.OnClick {
    @Bind(R.id.recycler_view)
    protected RecyclerView recyclerView;
    @Bind(R.id.error)
    FrameLayout error;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

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
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);

        podcasts = new ArrayList<>();
        adapter = new PodcastsRecyclerView(getActivity(), podcasts, this);
        recyclerView.setAdapter(adapter);

        fetchData();
    }

    @OnClick(R.id.retry)
    protected void fetchData() {
        this.error.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        App.getInstance().addRequestToQueue(new Podcast().reqFindAll(this, this), this);
    }

    @Override
    public void onResponse(PodcastResultResponse response) {
        Podcast.saveResults(Podcast.class, response);

        podcasts.clear();
        podcasts.addAll(Podcast.findAllAsList(Podcast.class));
        progressBar.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onImageView(int position) {
        startActivity(PodcastDetailActivity.getIntent(getActivity(), podcasts.get(position)));
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        progressBar.setVisibility(View.GONE);
        this.error.setVisibility(View.VISIBLE);
    }
}
