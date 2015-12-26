package io.github.hanihashemi.podgir.helper;

import android.util.Log;

import timber.log.Timber;

/**
 * Created by hani on 12/27/15.
 * email:jhanihashemi@gmail.com
 */
public class CrashReportingTree extends Timber.Tree {
    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if (priority == Log.INFO) {
            Log.i(tag, "=========> " + message);
            return;
        }
        if (priority == Log.VERBOSE || priority == Log.DEBUG)
            return;

        if (t != null)
            new BugReporter().send(tag, message, t);
    }
}
