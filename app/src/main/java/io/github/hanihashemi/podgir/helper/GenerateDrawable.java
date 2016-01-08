package io.github.hanihashemi.podgir.helper;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

/**
 * Created by hani on 1/9/16.
 * email:jhanihashemi@gmail.com
 */
public class GenerateDrawable {
    private Context context;

    public GenerateDrawable(Context context) {
        this.context = context;
    }

    public IconicsDrawable actionBar(GoogleMaterial.Icon icon, @ColorInt int color) {
        return new IconicsDrawable(context)
                .icon(icon)
                .color(color)
                .sizeDp(32)
                .paddingDp(7);
    }

    public IconicsDrawable actionBar(GoogleMaterial.Icon icon) {
        return actionBar(icon, Color.WHITE);
    }

    public IconicsDrawable tabLayout(GoogleMaterial.Icon icon) {
        return new IconicsDrawable(context)
                .icon(icon)
                .color(Color.WHITE)
                .sizeDp(16);
    }
}
