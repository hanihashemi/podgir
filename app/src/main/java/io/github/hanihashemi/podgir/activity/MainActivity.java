package io.github.hanihashemi.podgir.activity;

import android.app.Fragment;
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

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;
    @Bind(R.id.nav_view)
    protected NavigationView navigationView;

    @Override
    protected void customizeUI() {
        super.customizeUI();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        addFragmentToContainer(new PodcastFragment());
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                break;
            case R.id.action_refresh:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_podcast:
                addFragmentToContainer(new PodcastFragment());
                break;
            case R.id.nav_download:
                addFragmentToContainer(new DownloadedFragment());
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
