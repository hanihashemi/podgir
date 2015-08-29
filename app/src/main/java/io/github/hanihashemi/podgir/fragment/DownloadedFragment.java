package io.github.hanihashemi.podgir.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.github.hanihashemi.podgir.App;
import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.adapter.DownloadedRecyclerView;
import io.github.hanihashemi.podgir.adapter.PodcastDetailRecyclerView;
import io.github.hanihashemi.podgir.adapter.viewholder.FeedInDownloadedViewHolder;
import io.github.hanihashemi.podgir.base.BaseFragment;
import io.github.hanihashemi.podgir.model.Feed;
import io.github.hanihashemi.podgir.model.FeedResultResponse;
import io.github.hanihashemi.podgir.network.request.GsonRequest;

/**
 * Created by hani on 8/28/15.
 */
public class DownloadedFragment extends BaseFragment {

    @Bind(R.id.recycler_view)
    protected RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Feed> feeds;
    FeedInDownloadedViewHolder.OnClick feedOnClick = new FeedInDownloadedViewHolder.OnClick() {
        @Override
        public void onPlay() {

        }
    };

    @Override
    protected void customizeUI() {
        super.customizeUI();

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        feeds = new ArrayList<>();
        adapter = new DownloadedRecyclerView(feeds, feedOnClick);
        recyclerView.setAdapter(adapter);

        fetchData();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recycler;
    }

    private void fetchData() {
        feeds.addAll(Feed.listAll(Feed.class));
        adapter.notifyDataSetChanged();
    }
}
