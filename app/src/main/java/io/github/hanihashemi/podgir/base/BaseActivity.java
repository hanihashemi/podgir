package io.github.hanihashemi.podgir.base;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by hani on 8/19/15.
 */
@SuppressWarnings("deprecation")
public abstract class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        customizeUI();
    }

    public void customizeUI() {

    }

    public abstract int getLayoutResource();
}
