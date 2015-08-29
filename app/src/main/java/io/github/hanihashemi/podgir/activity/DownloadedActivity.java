package io.github.hanihashemi.podgir.activity;

import android.support.v4.app.Fragment;

import io.github.hanihashemi.podgir.base.BaseActivityWithSingleFragment;
import io.github.hanihashemi.podgir.fragment.DownloadedFragment;

/**
 * Created by hani on 8/28/15.
 */
public class DownloadedActivity extends BaseActivityWithSingleFragment {
    @Override
    public Fragment getFragment() {
        return new DownloadedFragment();
    }
}
