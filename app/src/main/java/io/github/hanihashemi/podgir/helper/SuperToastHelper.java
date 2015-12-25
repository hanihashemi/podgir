package io.github.hanihashemi.podgir.helper;

import android.app.Activity;
import android.support.annotation.StringRes;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

import io.github.hanihashemi.podgir.App;
import io.github.hanihashemi.podgir.R;

/**
 * Created by hani on 12/25/15.
 * email:jhanihashemi@gmail.com
 */
public class SuperToastHelper {
    public void show(String message) {
        SuperToast.create(App.getInstance().getApplicationContext(), message, SuperToast.Duration.SHORT, Style.getStyle(Style.getBackground(R.color.accent), SuperToast.Animations.POPUP)).show();
    }

    public void show(@StringRes int res) {
        show(App.getInstance().getApplicationContext().getString(res));
    }

    public void showActivity(Activity activity, String message) {
        SuperActivityToast.create(activity, message, SuperToast.Duration.SHORT, Style.getStyle(Style.getBackground(R.color.accent), SuperToast.Animations.POPUP)).show();
    }

    public void showActivity(Activity activity, @StringRes int res) {
        showActivity(activity, activity.getString(res));
    }
}
