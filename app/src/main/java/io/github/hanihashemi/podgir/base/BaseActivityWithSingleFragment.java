package io.github.hanihashemi.podgir.base;

import android.app.Fragment;

import io.github.hanihashemi.podgir.R;

/**
 * Created by hani on 8/19/15.
 */
public abstract class BaseActivityWithSingleFragment extends BaseActivity {

    @Override
    public void customizeUI() {
        super.customizeUI();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.container, getFragment())
                .commit();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_with_single_activity;
    }

    public abstract Fragment getFragment();
}
