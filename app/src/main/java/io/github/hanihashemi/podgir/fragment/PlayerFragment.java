package io.github.hanihashemi.podgir.fragment;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;
import io.github.hanihashemi.podgir.App;
import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.base.BaseFragment;
import io.github.hanihashemi.podgir.broadcast.MediaPlayerAction;
import io.github.hanihashemi.podgir.broadcast.MediaPlayerStatus;
import io.github.hanihashemi.podgir.model.Episode;
import io.github.hanihashemi.podgir.service.MediaPlayerService;
import io.github.hanihashemi.podgir.util.PlayerUtils;
import io.github.hanihashemi.podgir.widget.AppPlayButton;
import io.github.hanihashemi.podgir.widget.AppTextView;

/**
 * Created by hani on 8/30/15.
 */
public class PlayerFragment extends BaseFragment implements AppPlayButton.PlayListener, SeekBar.OnSeekBarChangeListener {

    public static final String ARG_FEED = "episode";
    @Bind(R.id.name)
    AppTextView name;
    @Bind(R.id.title)
    AppTextView title;
    @Bind(R.id.play)
    AppPlayButton play;
    @Bind(R.id.back_ten_seconds)
    ImageButton backTenSeconds;
    @Bind(R.id.seekBar)
    SeekBar seekBar;
    @Bind(R.id.image)
    ImageView imageView;
    @Bind(R.id.time)
    AppTextView time;

    private PlayerUtils playerUtils;
    private Episode episode;
    private boolean seekBarTouched;

    public PlayerFragment() {
        playerUtils = new PlayerUtils();
    }

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
        seekBar.setOnSeekBarChangeListener(this);
        imageView.setImageURI(Uri.parse(episode.getParent().getImageUrl()));
        backTenSeconds.setImageDrawable(new IconicsDrawable(getActivity()).icon(GoogleMaterial.Icon.gmd_replay_10).color(Color.WHITE).sizeDp(50));
        play.setPlayListener(this);
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

    @OnClick(R.id.back_ten_seconds)
    protected void onTenSecondsClicked() {
        App.getInstance().getBus().post(new MediaPlayerAction(MediaPlayerAction.ACTION_BACK_TEN_SECONDS));
    }

    @Subscribe
    public void onStatusChanged(MediaPlayerStatus status) {
        if (!episode.getObjectId().equals(status.getFileId())) {
            getActivity().startService(MediaPlayerService.getIntent(getActivity(), episode));
            return;
        }

        if (status.isPlay())
            seekBar.setEnabled(true);

        play.setPlay(status.isPlay());
        time.setText(playerUtils.getMilliSecondsToTimer(status.getCorrectTime()));
        if (!seekBarTouched)
            seekBar.setProgress(playerUtils.getProgressPercentage(status.getCorrectTime(), status.getTotalTime()));
    }

    @Override
    public void onClick(boolean isPlay) {
        if (!isPlay) {
            seekBar.setEnabled(true);
            getActivity().startService(MediaPlayerService.getIntent(getActivity(), episode));
        } else {
            App.getInstance().getBus().post(new MediaPlayerAction(MediaPlayerAction.ACTION_PAUSE));
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        seekBarTouched = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        App.getInstance().getBus().post(new MediaPlayerAction(MediaPlayerAction.ACTION_CHANGE_POSITION, seekBar.getProgress()));
        seekBarTouched = false;
    }
}
