package io.github.hanihashemi.podgir.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import io.github.hanihashemi.podgir.base.BaseActivityWithSingleFragment;
import io.github.hanihashemi.podgir.fragment.PlayerFragment;

/**
 * Created by hani on 8/30/15.
 */
public class PlayerActivity extends BaseActivityWithSingleFragment {

    public static Intent getIntent(Context context) {
        return new Intent(context, PlayerActivity.class);
    }

    @Override
    public Fragment getFragment() {
        return new PlayerFragment();
    }
}
