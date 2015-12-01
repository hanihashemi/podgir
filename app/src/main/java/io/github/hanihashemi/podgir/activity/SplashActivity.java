package io.github.hanihashemi.podgir.activity;

import android.os.Handler;

import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.base.BaseActivity;

public class SplashActivity extends BaseActivity implements Runnable {

    private Handler handler;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected void customizeUI() {
        super.customizeUI();
        handler = new Handler();
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.postDelayed(this, 5000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(this);
    }

    @Override
    public void run() {
        startActivity(MainActivity.getIntent(this));
    }
}
