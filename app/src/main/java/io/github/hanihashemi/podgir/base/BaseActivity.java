package io.github.hanihashemi.podgir.base;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by hani on 8/19/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        gatherArguments(getIntent().getExtras());
        customizeUI();
    }

    protected void customizeUI() {

    }

    protected void gatherArguments(Bundle bundle) {

    }

    public abstract int getLayoutResource();
}
