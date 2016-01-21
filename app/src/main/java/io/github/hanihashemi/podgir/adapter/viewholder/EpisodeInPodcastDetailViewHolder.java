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
public class EpisodeInPodcastDetailViewHolder extends BaseViewHolder {
    @Bind(R.id.name)
    public TextView name;
    @Bind(R.id.download)
    public Button download;
    @Bind(R.id.number)
    public TextView number;
    @Bind(R.id.day)
    public TextView day;
    @Bind(R.id.month)
    public TextView month;

    private OnClick onClick;

    public EpisodeInPodcastDetailViewHolder(View itemView, OnClick onClick) {
        super(itemView);
        this.onClick = onClick;
    }

    @butterknife.OnClick(R.id.download)
    protected void onClickDownload() {
        onClick.onDownload(getAdapterPosition());
    }

    public interface OnClick {
        void onDownload(int position);
    }
}
