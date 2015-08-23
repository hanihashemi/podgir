package io.github.hanihashemi.podgir.model;

/**
 * Created by hani on 8/22/15.
 */
public class BaseModel {

    protected String getUrl(String apiUrl) {
        String host = "https://api.parse.com/1/";
        return host + "/" + apiUrl;
    }
}
