package io.github.hanihashemi.podgir.helper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.StringRes;

import io.github.hanihashemi.podgir.R;

/**
 * Created by hani on 12/24/15.
 * email:jhanihashemi@gmail.com
 */
public class IntentHelper {

    public void sendMail(Activity activity, @StringRes int email, @StringRes int subject, @StringRes int text) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", activity.getString(email), null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(subject));
        emailIntent.putExtra(Intent.EXTRA_TEXT, activity.getString(text));
        startActivityInBadScenario(activity, Intent.createChooser(emailIntent, "Send email..."));
    }

    private void startActivityInBadScenario(Activity activity, Intent intent) {
        try {
            activity.startActivity(intent);
        } catch (Exception ex) {
            new SuperToastHelper().show(R.string.no_app_found_to_run_intent);
        }
    }
}
