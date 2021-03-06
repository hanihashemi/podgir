package io.github.hanihashemi.podgir.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.ButterKnife;
import io.github.hanihashemi.podgir.App;

/**
 * Created by hani on 8/19/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        ButterKnife.bind(this);
        gatherArguments(getIntent().getExtras());
        customizeUI();
    }

    protected void customizeUI() {

    }

    protected void gatherArguments(Bundle bundle) {

    }

    public abstract int getLayoutResource();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendAnalyticsHit();
    }

    private void sendAnalyticsHit() {
        Tracker tracker = App.getInstance().getDefaultTracker();
        tracker.setScreenName(this.getClass().getSimpleName());
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}