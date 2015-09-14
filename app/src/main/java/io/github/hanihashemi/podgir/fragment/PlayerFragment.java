package io.github.hanihashemi.podgir.fragment;

import android.os.Bundle;

import butterknife.Bind;
import butterknife.OnClick;
import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.base.BaseFragment;
import io.github.hanihashemi.podgir.model.Feed;
import io.github.hanihashemi.podgir.service.MediaPlayerService;
import io.github.hanihashemi.podgir.widget.AppTextView;

/**
 * Created by hani on 8/30/15.
 */
public class PlayerFragment extends BaseFragment {

    public static final String ARG_FEED = "feed";
    @Bind(R.id.name)
    AppTextView name;
    @Bind(R.id.title)
    AppTextView title;
    private Feed feed;

    public static PlayerFragment getInstance(Feed feed) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_FEED, feed);
        PlayerFragment playerFragment = new PlayerFragment();
        playerFragment.setArguments(bundle);
        return playerFragment;
    }

    @Override
    protected void gatherArguments(Bundle bundle) {
        super.gatherArguments(bundle);
        feed = bundle.getParcelable(ARG_FEED);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_player;
    }

    @Override
    protected void customizeUI() {
        super.customizeUI();
        name.setText(feed.getPodcastName());
        title.setText(feed.getTitle());
    }

    @OnClick(R.id.play)
    protected void onClickedPlay() {
        getActivity().startService(MediaPlayerService.getIntent(getActivity(), feed));
    }
}
