package io.github.hanihashemi.podgir.network.request;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by hani on 8/19/15.
 */
public class GsonRequest<T> extends BaseRequest<T> {

    private Class<T> responseClass;

    public GsonRequest(int method,
                       String url,
                       JSONObject body,
                       Class<T> responseClass,
                       Response.Listener<T> success,
                       Response.ErrorListener error) {

        super(method, url, (body == null) ? null : body.toString(),
                success, error);
        this.responseClass = responseClass;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        super.parseNetworkResponse(response);
        try {
            String json = new String(
                    response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    GsonProvider.getGson().fromJson(json, responseClass), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}