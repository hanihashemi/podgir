package io.github.hanihashemi.podgir.network.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by hani on 8/19/15.
 */
public class GsonProvider {
    private static Gson gson;

    public static Gson getGson() {
        if (gson == null)
            gson = new GsonBuilder().setExclusionStrategies(new GsonAnnotationExclusionStrategy()).create();
        return gson;
    }
}
