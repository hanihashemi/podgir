package io.github.hanihashemi.podgir.model;

import com.orm.SugarRecord;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by hani on 8/22/15.
 */
public abstract class BaseModel<T> extends SugarRecord<T> {

    protected final String getHostUrl(String apiUrl, Object... args) {
        String host = "https://api.parse.com/1/";
        try {
            apiUrl = URLEncoder.encode(apiUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return host + String.format(apiUrl, args);
    }

    protected final String getHostUrl(String apiUrl) {
        String host = "https://api.parse.com/1/";
        return host + apiUrl;
    }
}
