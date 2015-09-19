package io.github.hanihashemi.podgir.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

/**
 * Created by hani on 9/19/15.
 */
public class AppPlayButton extends ImageButton implements View.OnClickListener {

    private boolean play = false;
    private PlayListener playListener;

    public AppPlayButton(Context context) {
        this(context, null);
    }

    public AppPlayButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppPlayButton(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
        update(context, isPlay());
    }

    public void update(Context context, boolean isPlay) {
        setPlay(isPlay);
        IconicsDrawable iconicsDrawable;

        if (isPlay) {
            iconicsDrawable = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_pause)
                    .color(Color.WHITE)
                    .sizeDp(50);
        } else {
            iconicsDrawable = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_play_arrow)
                    .color(Color.WHITE)
                    .sizeDp(50);
        }

        this.setImageDrawable(iconicsDrawable);
    }

    public void setPlayListener(PlayListener playListener) {
        this.playListener = playListener;
    }

    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }

    @Override
    public void onClick(View view) {
        update(getContext(), !isPlay());

        if (playListener != null)
            playListener.onClick(isPlay());
    }

    public interface PlayListener {
        void onClick(boolean isPlay);
    }
}
