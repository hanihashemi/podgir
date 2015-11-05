package io.github.hanihashemi.podgir.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.base.BaseViewHolder;

/**
 * Created by hani on 8/25/15.
 */
public class PodcastDetailViewHolder extends BaseViewHolder {
    @Bind(R.id.imageView)
    public SimpleDraweeView imageView;
    @Bind(R.id.name)
    public TextView name;

    public PodcastDetailViewHolder(View itemView) {
        super(itemView);
    }
}
