package io.github.hanihashemi.podgir.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;

/**
 * Created by hani on 10/20/15.
 */
public class AppImageView extends com.facebook.drawee.view.SimpleDraweeView {
    public AppImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public AppImageView(Context context) {
        super(context);
    }

    public AppImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
