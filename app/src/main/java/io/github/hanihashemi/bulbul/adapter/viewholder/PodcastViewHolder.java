package io.github.hanihashemi.bulbul.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;

import butterknife.Bind;
import io.github.hanihashemi.bulbul.base.BaseViewHolder;
import io.github.hanihashemi.bulbul.R;

/**
 * Created by hani on 8/18/15.
 */
public class PodcastViewHolder extends BaseViewHolder {

    @Bind(R.id.imageView)
    ImageView imageView;

    public PodcastViewHolder(View itemView) {
        super(itemView);
    }
}
