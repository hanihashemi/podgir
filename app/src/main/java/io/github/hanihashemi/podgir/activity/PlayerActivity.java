package io.github.hanihashemi.podgir.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import io.github.hanihashemi.podgir.base.BaseActivityWithSingleFragment;
import io.github.hanihashemi.podgir.fragment.PlayerFragment;
import io.github.hanihashemi.podgir.helper.GenerateDrawable;
import io.github.hanihashemi.podgir.model.Episode;

/**
 * Created by hani on 8/30/15.
 */
public class PlayerActivity extends BaseActivityWithSingleFragment {

    public static final String ARG_FEED = "episode";
    private Episode episode;

    public static Intent getIntent(Context context, Episode episode) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(ARG_FEED, episode);
        return intent;
    }

    @Override
    public Fragment getFragment() {
        return PlayerFragment.getInstance(episode);
    }

    @Override
    protected void gatherArguments(Bundle bundle) {
        super.gatherArguments(bundle);
        episode = getIntent().getParcelableExtra(ARG_FEED);
    }

    @Override
    public void customizeUI() {
        super.customizeUI();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(new GenerateDrawable(this).actionBar(GoogleMaterial.Icon.gmd_clear));
        }
    }
}
