package io.github.hanihashemi.podgir.helper;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.analytics.ExceptionReporter;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import io.github.hanihashemi.podgir.App;

/**
 * Created by hani on 12/27/15.
 * email:jhanihashemi@gmail.com
 */
public class BugReporter {
    public void send(String tag, String message, Throwable ex, boolean isFatal) {
        Tracker tracker =
                App.getInstance().getDefaultTracker();

        tracker.send(new HitBuilders.ExceptionBuilder()
                .setDescription(getDescription(tag, message, ex))
                .setFatal(isFatal)
                .build());
    }

    private String getDescription(String tag, String message, Throwable ex) {
        String description = "";
        description += "t:" + tag;
        description += ",m:" + message;

        if (ex != null) {
            description += Log.getStackTraceString(ex);
        }
        return description;
    }

    public void send(String tag, Throwable ex, boolean isFatal) {
        send(tag, "", ex, isFatal);
    }

    public void send(String tag, String message, Throwable ex) {
        send(tag, message, ex, false);
    }

    public void send(String tag, Throwable ex) {
        send(tag, ex, false);
    }

    public AnalyticsExceptionReporter getExceptionReporter() {
        return new AnalyticsExceptionReporter(App.getInstance().getDefaultTracker(), Thread.getDefaultUncaughtExceptionHandler(), App.getInstance().getApplicationContext());
    }

    private class AnalyticsExceptionReporter extends ExceptionReporter {

        public AnalyticsExceptionReporter(Tracker tracker, Thread.UncaughtExceptionHandler originalHandler, Context context) {
            super(tracker, originalHandler, context);
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            new BugReporter().send("<UE>", e);
            super.uncaughtException(t, e);
        }
    }
}
