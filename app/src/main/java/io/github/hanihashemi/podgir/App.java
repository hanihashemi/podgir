package io.github.hanihashemi.podgir;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.orm.SugarApp;
import com.squareup.otto.Bus;

import io.github.hanihashemi.podgir.helper.BugReporter;
import io.github.hanihashemi.podgir.helper.CrashReportingTree;
import io.github.hanihashemi.podgir.network.request.BaseRequest;
import timber.log.Timber;

/**
 * Created by hani on 7/19/15.
 */
public class App extends SugarApp {

    private static App app;
    private static Bus bus;
    private RequestQueue requestQueue;
    private Tracker mTracker;

    public synchronized static App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        setUncaughtExceptionHandler();
        timberConfig();
    }

    private void setUncaughtExceptionHandler() {
        Thread.UncaughtExceptionHandler myHandler = new BugReporter().getExceptionReporter();
        Thread.setDefaultUncaughtExceptionHandler(myHandler);
    }

    private void timberConfig() {
        if (BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());
        else
            Timber.plant(new CrashReportingTree());
    }

    public void addRequestToQueue(BaseRequest request, Object owner) {
        request.setTag(owner);
        getRequestQueue().add(request);
        Timber.d("Adding request to queue: %s", request.getUrl());
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        return requestQueue;
    }

    public void cancelRequest(Object owner) {
        if (requestQueue == null || owner == null)
            return;

        final String simpleName = owner.getClass().getSimpleName();
        Timber.d("Cancel request from queue :%S", simpleName);

        getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return request.getTag().equals(simpleName);
            }
        });
    }

    public Bus getBus() {
        if (bus == null)
            bus = new Bus();
        return bus;
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}
