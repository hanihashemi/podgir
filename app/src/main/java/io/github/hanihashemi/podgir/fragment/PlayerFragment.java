package io.github.hanihashemi.podgir.fragment;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;
import io.github.hanihashemi.podgir.App;
import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.base.BaseFragment;
import io.github.hanihashemi.podgir.broadcast.MediaPlayerStatus;
import io.github.hanihashemi.podgir.model.Episode;
import io.github.hanihashemi.podgir.service.MediaPlayerService;
import io.github.hanihashemi.podgir.widget.AppImageView;
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
    @Bind(R.id.back_fifteen_seconds)
    ImageButton backFifteenSeconds;
    @Bind(R.id.seekBar)
    SeekBar seekBar;
    @Bind(R.id.image)
    AppImageView imageView;

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
        name.setText(episode.getParent().getName());
        title.setText(episode.getTitle());
        seekBar.setEnabled(false);
        backFifteenSeconds.setEnabled(false);
        loadImageArt();
        backFifteenSeconds.setImageDrawable(new IconicsDrawable(getContext()).icon(GoogleMaterial.Icon.gmd_replay_10).color(Color.WHITE).sizeDp(50));
    }

    private void loadImageArt() {
        imageView.setImageURI(Uri.parse(episode.getParent().getImageUrl()));
    }

    @Override
    public void onResume() {
        super.onResume();
        App.getInstance().getBus().register(this);
    }

    @Override
    public void onPause() {
        App.getInstance().getBus().unregister(this);
        super.onPause();
    }

    @OnClick(R.id.play)
    protected void onPlayClicked() {
        getActivity().startService(MediaPlayerService.getIntent(getActivity(), episode));
    }

    protected void onPauseClicked() {

    }

    @OnClick(R.id.back_fifteen_seconds)
    protected void onFifteenSecondsClicked() {

    }

    @Subscribe
    public void mediaPlayerStatus(MediaPlayerStatus status) {
        Timber.d("Player: " + status.isPlay());
    }

    @Override
    public void onClick(boolean isPlay) {

    }
}
