package io.github.hanihashemi.podgir.fragment;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.github.hanihashemi.podgir.App;
import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.activity.PodcastDetailActivity;
import io.github.hanihashemi.podgir.adapter.PodcastsRecyclerView;
import io.github.hanihashemi.podgir.adapter.viewholder.PodcastViewHolder;
import io.github.hanihashemi.podgir.base.BaseFragment;
import io.github.hanihashemi.podgir.model.Podcast;
import io.github.hanihashemi.podgir.model.PodcastResultResponse;
import io.github.hanihashemi.podgir.widget.AppTextView;

/**
 * Created by hani on 8/18/15.
 */
public class PodcastFragment extends BaseFragment implements Response.Listener<PodcastResultResponse>, PodcastViewHolder.OnClick, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.recycler_view)
    protected RecyclerView recyclerView;
    @Bind(R.id.error_message)
    AppTextView errorMessage;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

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
        swipeRefresh.setColorSchemeResources(R.color.accent);
        swipeRefresh.setOnRefreshListener(this);
        fetchData();
    }

    protected void fetchData() {
        errorMessage.setVisibility(View.GONE);
        setSwipeRefresh(true);
        App.getInstance().addRequestToQueue(new Podcast().reqFindAll(this, this), this);
    }

    @Override
    public void onResponse(PodcastResultResponse response) {
        Podcast.saveResults(Podcast.class, response);

        podcasts.clear();
        podcasts.addAll(Podcast.findAllAsList(Podcast.class));

        setSwipeRefresh(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onImageView(int position) {
        startActivity(PodcastDetailActivity.getIntent(getActivity(), podcasts.get(position)));
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        errorMessage.setVisibility(View.VISIBLE);
        setSwipeRefresh(false);
    }

    @Override
    public void onRefresh() {
        fetchData();
    }

    private void setSwipeRefresh(final boolean value) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(value);
            }
        }, 100);
    }
}
