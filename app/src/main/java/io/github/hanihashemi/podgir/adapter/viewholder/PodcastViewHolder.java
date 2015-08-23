package io.github.hanihashemi.podgir.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;

import butterknife.Bind;
import io.github.hanihashemi.podgir.base.BaseViewHolder;
import io.github.hanihashemi.podgir.R;

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
