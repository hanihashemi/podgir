package io.github.hanihashemi.podgir.network.tool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.util.ArrayList;

import io.github.hanihashemi.podgir.R;
import timber.log.Timber;

/**
 * Created by hani on 8/21/15.
 */
public class VolleyErrorHandler {
    private static VolleyErrorHandler volleyErrorHandler;
    ArrayList<ErrorModel> showingErrors = new ArrayList<>();

    public static VolleyErrorHandler getInstance() {
        if (volleyErrorHandler == null)
            volleyErrorHandler = new VolleyErrorHandler();
        return volleyErrorHandler;
    }

    private ErrorModel getErrorCode(VolleyError error, Context context) {
        try {
            if (error instanceof TimeoutError) {
                return new ErrorModel(
                        context.getString(R.string.server_timeout_error), true);
            } else if (error instanceof com.android.volley.ParseError) {
                return new ErrorModel(
                        "Volley Parser Error", false
                );
            } else if (isServerUnavailable(error)) {
                return new ErrorModel(
                        context.getString(R.string.could_not_reach_server), true);
            } else if (!isNetworkAvailable(context)) {
                return new ErrorModel(
                        context.getString(R.string.no_internet_error), true);
            } else if (error instanceof ServerError) {
                showServerError(error);
                return new ErrorModel(
                        context.getString(R.string.server_response_problem), false);
            } else {
                return new ErrorModel(
                        "Server went wrong", false);
            }
        } catch (Exception ex) {
            Timber.w(ex, "errorCode");
            return null;
        }
    }

    private boolean isServerUnavailable(Object error) {
        return (error instanceof NoConnectionError && ((NoConnectionError) error).getMessage().contains("Connection refused"));
    }

    public ErrorModel handle(Context context, VolleyError error) {
        try {
            ErrorModel errorModel = getErrorCode(error, context);
//            if (errorModel != null && errorModel.showing)
//                showDialog(context, errorModel);
//            else if (errorModel != null)
            Timber.d(error, errorModel.toString());

            return errorModel;
        } catch (Exception ex) {
            Timber.e("showErrorDialog", ex);
            return null;
        }
    }

//    private void showDialog(Context context, final ErrorModel errorModel) {
//        if (showingErrors.contains(errorModel))
//            return;
//
//        showingErrors.add(errorModel);
//        try {
//            DialogUtils.getInstance().showMessage(
//                    context,
//                    context.getString(R.string.error_dialog_title),
//                    errorModel.toString(),
//                    context.getString(R.string.button_ok),
//                    new MaterialDialog.ButtonCallback() {
//                        @Override
//                        public void onPositive(MaterialDialog dialog) {
//                            showingErrors.clear();
//                        }
//                    }
//            );
//        } catch (Exception ex) {
//            Timber.d("showDialog", ex);
//        }
//    }

    private void showServerError(Object err) {
        VolleyError error = (VolleyError) err;
        NetworkResponse response = error.networkResponse;

        if (response == null)
            return;

        try {
            Timber.d(new String(response.data, "UTF-8"));
        } catch (Exception ex) {
            Timber.d("showServerError", ex);
        }
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    class ErrorModel {

        public String message;
        public boolean showing = false;
        public int code;

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