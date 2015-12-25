package io.github.hanihashemi.podgir.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.Bind;
import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.base.BaseActivity;
import io.github.hanihashemi.podgir.fragment.DownloadedFragment;
import io.github.hanihashemi.podgir.fragment.PodcastFragment;
import io.github.hanihashemi.podgir.helper.IntentHelper;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;
    @Bind(R.id.nav_view)
    protected NavigationView navigationView;

    private PodcastFragment podcastFragment;
    private DownloadedFragment downloadFragment;

    public static Intent getIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void customizeUI() {
        super.customizeUI();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        podcastFragment = new PodcastFragment();
        downloadFragment = new DownloadedFragment();
        navigationView.setNavigationItemSelectedListener(this);
        addFragmentToContainer(podcastFragment);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                podcastFragment.onRefresh();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_podcast:
                addFragmentToContainer(podcastFragment);
                setTitle(R.string.app_name_fa);
                break;
            case R.id.nav_download:
                addFragmentToContainer(downloadFragment);
                setTitle(R.string.downloaded);
                break;
            case R.id.nav_feedback:
                new IntentHelper().sendMail(this, R.string.feedback_email, R.string.feedback_subject, R.string.feedback_text);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addFragmentToContainer(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}
