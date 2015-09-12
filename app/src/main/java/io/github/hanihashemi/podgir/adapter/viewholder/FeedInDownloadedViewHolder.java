package io.github.hanihashemi.podgir.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.base.BaseViewHolder;

/**
 * Created by hani on 8/28/15.
 */
public class FeedInDownloadedViewHolder extends BaseViewHolder {

    @Bind(R.id.name)
    public TextView name;
    @Bind(R.id.feed)
    public TextView feed;
    private OnClick onClick;

    public FeedInDownloadedViewHolder(View itemView, OnClick onClick) {
        super(itemView);
        this.onClick = onClick;
    }

    @butterknife.OnClick(R.id.button)
    protected void onPlay() {
        onClick.onPlay(getPosition());
    }

    public interface OnClick {
        void onPlay(int position);
    }
}
