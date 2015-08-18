package io.github.hanihashemi.bulbul.base;

import android.support.v4.app.Fragment;

import io.github.hanihashemi.bulbul.R;

/**
 * Created by hani on 8/19/15.
 */
public abstract class BaseActivityWithSingleFragment extends BaseActivity {

    @Override
    public void customizeUI() {
        super.customizeUI();
        getSupportFragmentManager()
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
