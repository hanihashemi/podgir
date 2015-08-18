package io.github.hanihashemi.bulbul.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.github.hanihashemi.bulbul.R;
import io.github.hanihashemi.bulbul.adapter.PodcastsRecyclerView;
import io.github.hanihashemi.bulbul.base.BaseFragment;
import io.github.hanihashemi.bulbul.model.Podcast;

/**
 * Created by hani on 8/18/15.
 */
public class PodcastsFragment extends BaseFragment {
    @Bind(R.id.recycler_view)
    protected RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

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

        List<Podcast> podcasts = new ArrayList<>();
        podcasts.add(new Podcast(10, "Hani", "https://d1f6f41kywpi5p.cloudfront.net/static/artists/300/andy-a9e86386.jpg"));
        podcasts.add(new Podcast(10, "Hani", "https://d1f6f41kywpi5p.cloudfront.net/static/artists/300/andy-a9e86386.jpg"));
        podcasts.add(new Podcast(10, "Hani", "https://d1f6f41kywpi5p.cloudfront.net/static/artists/300/andy-a9e86386.jpg"));
        podcasts.add(new Podcast(10, "Hani", "https://d1f6f41kywpi5p.cloudfront.net/static/artists/300/andy-a9e86386.jpg"));
        podcasts.add(new Podcast(10, "Hani", "https://d1f6f41kywpi5p.cloudfront.net/static/artists/300/andy-a9e86386.jpg"));
        podcasts.add(new Podcast(10, "Hani", "https://d1f6f41kywpi5p.cloudfront.net/static/artists/300/andy-a9e86386.jpg"));
        podcasts.add(new Podcast(10, "Hani", "https://d1f6f41kywpi5p.cloudfront.net/static/artists/300/andy-a9e86386.jpg"));

        adapter = new PodcastsRecyclerView(podcasts);
        recyclerView.setAdapter(adapter);
    }
}
