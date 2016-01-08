package io.github.hanihashemi.podgir.network.helper;

import android.os.Handler;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.util.ArrayList;

import io.github.hanihashemi.podgir.App;
import io.github.hanihashemi.podgir.R;
import timber.log.Timber;

/**
 * Created by hani on 8/21/15.
 */
public class VolleyErrorHandler {
    public static final int DELAY_MILLIS = 5000;
    private static VolleyErrorHandler volleyErrorHandler;
    ArrayList<ErrorModel> showingErrors = new ArrayList<>();

    private VolleyErrorHandler() {

    }

    public static VolleyErrorHandler getInstance() {
        if (volleyErrorHandler == null)
            volleyErrorHandler = new VolleyErrorHandler();
        return volleyErrorHandler;
    }

    private ErrorModel getErrorInfo(VolleyError error) {
        try {
            if (error instanceof TimeoutError) {
                return new ErrorModel(App.getInstance().getApplicationContext().getString(R.string.server_timeout_error), true);
            } else if (error instanceof com.android.volley.ParseError) {
                return new ErrorModel(App.getInstance().getApplicationContext().getString(R.string.could_not_parse_server_response), true);
            } else if (isServerUnavailable(error)) {
                return new ErrorModel(App.getInstance().getApplicationContext().getString(R.string.could_not_reach_server), true);
            } else if (error instanceof ServerError) {
                Timber.d("===>  ServerError");
                getServerError(error);
                return new ErrorModel(App.getInstance().getApplicationContext().getString(R.string.server_problem), true);
            } else {
                Timber.d("===>  Server went wrong");
                return new ErrorModel(App.getInstance().getApplicationContext().getString(R.string.server_unknown_problem), true);
            }
        } catch (Exception ex) {
            Timber.w(ex, "errorCode");
            return null;
        }
    }

    private boolean isServerUnavailable(Object error) {
        return (error instanceof NoConnectionError && ((NoConnectionError) error).getMessage().contains("Connection refused"));
    }

    public ErrorModel showProperMessage(VolleyError volleyError, Listener listener) {
        try {
            ErrorModel error = getErrorInfo(volleyError);

            if (error != null) {
                Timber.d(error.message);
                if (error.showing)
                    showDialog(error, listener);
            } else
                Timber.d("ErrorModel is null!!");

            return error;
        } catch (Exception ex) {
            Timber.e(ex, "showProperMessage");
            return null;
        }
    }

    private void showDialog(ErrorModel error, Listener listener) {
        if (showingErrors.contains(error))
            return;

        if (listener != null)
            listener.onCatching(error);

        showingErrors.add(error);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showingErrors.clear();
            }
        }, DELAY_MILLIS);
    }

    private void getServerError(Object err) {
        VolleyError error = (VolleyError) err;
        NetworkResponse response = error.networkResponse;

        if (response == null)
            return;

        try {
            Timber.d(new String(response.data, "UTF-8"));
        } catch (Exception ex) {
            Timber.d(ex, "showServerError");
        }
    }

    public interface Listener {
        void onCatching(ErrorModel error);
    }

    public class ErrorModel {
        public String message;
        public boolean showing = false;

        public ErrorModel(String message, boolean showing) {
            this.message = message;
            this.showing = showing;
        }

        @Override
        public String toString() {
            return message;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (!(object instanceof ErrorModel)) return false;

            ErrorModel that = (ErrorModel) object;
            return message.equals(that.message);
        }
    }

}