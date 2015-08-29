package io.github.hanihashemi.podgir.adapter.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.base.BaseViewHolder;

/**
 * Created by hani on 8/25/15.
 */
public class FeedInPodcastDetailViewHolder extends BaseViewHolder {
    @Bind(R.id.name)
    public TextView name;
    @Bind(R.id.button)
    public Button download;

    private OnClick onClick;

    public FeedInPodcastDetailViewHolder(View itemView, OnClick onClick) {
        super(itemView);
        this.onClick = onClick;
    }

    @butterknife.OnClick(R.id.button)
    protected void onClickDownload() {
        onClick.onDownload(getPosition());
    }

    public interface OnClick {
        void onDownload(int position);
    }
}
