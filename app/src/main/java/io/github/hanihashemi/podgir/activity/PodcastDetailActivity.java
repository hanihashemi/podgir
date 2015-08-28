package io.github.hanihashemi.podgir.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import io.github.hanihashemi.podgir.base.BaseActivityWithSingleFragment;
import io.github.hanihashemi.podgir.fragment.PodcastDetailFragment;
import io.github.hanihashemi.podgir.model.Podcast;

/**
 * Created by hani on 8/18/15.
 */
@SuppressWarnings("deprecation")
public class PodcastDetailActivity extends BaseActivityWithSingleFragment {

    public static final String ARG_PODCAST = "podcast";
    private Podcast podcast;

    public static Intent getIntent(Context context, Podcast podcast) {
        Intent intent = new Intent(context, PodcastDetailActivity.class);
        intent.putExtra(ARG_PODCAST, podcast);
        return intent;
    }

    @Override
    public void customizeUI() {
        super.customizeUI();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void gatherArguments(Bundle bundle) {
        super.gatherArguments(bundle);
        this.podcast = getIntent().getExtras().getParcelable(ARG_PODCAST);
    }

    @Override
    public Fragment getFragment() {
        return PodcastDetailFragment.getInstance(podcast);
    }
}
