package io.github.hanihashemi.podgir.network.request;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

/**
 * Created by hani on 8/19/15.
 */
public class GsonListRequest<T> extends BaseRequest<T> {

    private final Type mType;

    public GsonListRequest(int method, String url, JSONObject requestObject, Type type, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, (requestObject == null) ? null : requestObject.toString(), listener, errorListener);
        mType = type;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        super.parseNetworkResponse(response);
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            T parseObject = GsonProvider.getGson().fromJson(jsonString, mType);
            return Response.success(parseObject, HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException je) {
            return Response.error(new ParseError(je));
        }
    }


}