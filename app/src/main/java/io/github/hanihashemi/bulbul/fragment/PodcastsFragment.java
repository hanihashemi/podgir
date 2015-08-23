package io.github.hanihashemi.bulbul.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.github.hanihashemi.bulbul.App;
import io.github.hanihashemi.bulbul.R;
import io.github.hanihashemi.bulbul.adapter.PodcastsRecyclerView;
import io.github.hanihashemi.bulbul.base.BaseFragment;
import io.github.hanihashemi.bulbul.model.Podcast;

/**
 * Created by hani on 8/18/15.
 */
public class PodcastsFragment extends BaseFragment implements Response.Listener<List<Podcast>> {
    @Bind(R.id.recycler_view)
    protected RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Podcast> podcasts;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_podcasts;
    }

    @Override
    protected void customizeUI() {
        super.customizeUI();

        recyclerView.setHasFixedSize(true);

        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);

        podcasts = new ArrayList<>();
        adapter = new PodcastsRecyclerView(podcasts);
        recyclerView.setAdapter(adapter);

        fetchData();
    }

    private void fetchData() {
        App.getInstance().addRequestToQueue(new Podcast().findAll(this, this), this);
    }

    @Override
    public void onResponse(List<Podcast> response) {
        podcasts.clear();
        podcasts.addAll(response);
        adapter.notifyDataSetChanged();
    }
}
