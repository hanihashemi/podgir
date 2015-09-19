package io.github.hanihashemi.podgir;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.orm.SugarApp;
import com.squareup.otto.Bus;

import io.github.hanihashemi.podgir.network.request.BaseRequest;
import timber.log.Timber;

/**
 * Created by hani on 7/19/15.
 */
public class App extends SugarApp {

    private static App app;
    private static Bus bus;
    RequestQueue requestQueue;

    public static App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

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

    public RequestQueue getRequestQueue() {
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

    private static class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }
        }
    }

}
