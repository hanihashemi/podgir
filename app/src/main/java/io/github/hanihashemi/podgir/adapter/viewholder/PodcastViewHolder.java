package io.github.hanihashemi.podgir.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;

import butterknife.Bind;
import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.base.BaseViewHolder;

/**
 * Created by hani on 8/18/15.
 */
public class PodcastViewHolder extends BaseViewHolder {

    @Bind(R.id.imageView)
    public ImageView imageView;

    private OnClick onClick;

    public interface OnClick {
        void onImageView(int position);
    }

    public PodcastViewHolder(View itemView, OnClick onClick) {
        super(itemView);
        this.onClick = onClick;
    }

    @butterknife.OnClick(R.id.imageView)
    protected void onImageViewClick() {
        onClick.onImageView(getPosition());
    }
}
