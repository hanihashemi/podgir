package io.github.hanihashemi.podgir.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import io.github.hanihashemi.podgir.base.BaseActivityWithSingleFragment;
import io.github.hanihashemi.podgir.fragment.PlayerFragment;
import io.github.hanihashemi.podgir.model.Feed;

/**
 * Created by hani on 8/30/15.
 */
public class PlayerActivity extends BaseActivityWithSingleFragment {

    public static final String ARG_FEED = "feed";
    private Feed feed;

    public static Intent getIntent(Context context, Feed feed) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(ARG_FEED, feed);
        return intent;
    }

    @Override
    public Fragment getFragment() {
        return PlayerFragment.getInstance(feed);
    }

    @Override
    protected void gatherArguments(Bundle bundle) {
        super.gatherArguments(bundle);
        feed = getIntent().getParcelableExtra(ARG_FEED);
    }
}
