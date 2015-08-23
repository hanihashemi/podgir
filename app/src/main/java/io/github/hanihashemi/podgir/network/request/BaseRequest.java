package io.github.hanihashemi.podgir.network.request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by hani on 8/21/15.
 */
public abstract class BaseRequest<T> extends JsonRequest<T> {

    public BaseRequest(int method, String url, String requestBody, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data, HttpHeaderParser.parseCharset(response.headers));
            
            Timber.d(json);
        } catch (UnsupportedEncodingException e) {
            Timber.e(e, "baseRequest");
        }
        return null;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if (headers == null
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<>();
        }

        headers.put("X-Parse-Application-Id", "Mt93Jmgz6Jo9X9dXtJrINauz15dAN9NaOQ1xPJKt");
        headers.put("X-Parse-REST-API-Key", "wVyR3hb5gi68PBuknM6syqPWYPpcHcgYCsWw1Lx7");

        return headers;
    }
}
