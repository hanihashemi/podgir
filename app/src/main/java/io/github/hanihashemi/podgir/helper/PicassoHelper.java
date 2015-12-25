package io.github.hanihashemi.podgir.helper;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import io.github.hanihashemi.podgir.R;

/**
 * Created by hani on 11/20/15.
 */
public class PicassoHelper {
    public static void load(Context context, String url, ImageView imageView) {
        Picasso picasso = Picasso.with(context);
        picasso.setIndicatorsEnabled(true);
        picasso.load(url).placeholder(R.color.primary_light).into(imageView);
    }
}
