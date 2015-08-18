package io.github.hanihashemi.bulbul.networklayer;

import com.google.gson.Gson;

/**
 * Created by hani on 8/19/15.
 */
public class GsonProvider {
    private static Gson gson;

    public static Gson getGson() {
        if (gson == null)
            gson = new Gson();
        return gson;
    }
}
