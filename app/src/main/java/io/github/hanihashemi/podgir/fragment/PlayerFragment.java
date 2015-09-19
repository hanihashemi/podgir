package io.github.hanihashemi.podgir.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import io.github.hanihashemi.podgir.App;
import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.base.BaseFragment;
import io.github.hanihashemi.podgir.model.Episode;
import io.github.hanihashemi.podgir.widget.AppPlayButton;
import io.github.hanihashemi.podgir.widget.AppTextView;
import timber.log.Timber;

/**
 * Created by hani on 8/30/15.
 */
public class PlayerFragment extends BaseFragment implements AppPlayButton.PlayListener {

    public static final String ARG_FEED = "episode";
    @Bind(R.id.name)
    AppTextView name;
    @Bind(R.id.title)
    AppTextView title;
    @Bind(R.id.play)
    AppPlayButton play;
    @Bind(R.id.back_ten_seconds)
    ImageButton backTenSeconds;

    private Episode episode;


    public static PlayerFragment getInstance(Episode episode) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_FEED, episode);
        PlayerFragment playerFragment = new PlayerFragment();
        playerFragment.setArguments(bundle);
        return playerFragment;
    }

    @Override
    protected void gatherArguments(Bundle bundle) {
        super.gatherArguments(bundle);
        episode = bundle.getParcelable(ARG_FEED);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_player;
    }

    @Override
    protected void customizeUI() {
        super.customizeUI();
        setRetainInstance(true);
        name.setText(episode.getPodcastName());
        title.setText(episode.getTitle());
        play.setPlayListener(this);
        backTenSeconds.setImageDrawable(new IconicsDrawable(getContext()).icon(GoogleMaterial.Icon.gmd_replay_10)
                .color(Color.WHITE)
                .sizeDp(50));
    }

//        getActivity().startService(MediaPlayerService.getIntent(getActivity(), episode));

    @Override
    public void onResume() {
        super.onResume();
        App.getInstance().getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        App.getInstance().getBus().unregister(this);
    }

    protected void onPlayClicked() {

    }

    protected void onPauseClicked() {

    }

    protected void onFifteenSeconsClicked() {

    }

    @Subscribe
    public void answerAvailable(String event) {
        Timber.d("Salam " + event);
    }

    @Override
    public void onClick(boolean isPlay) {

    }
}
