package io.github.hanihashemi.podgir.network.request;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import io.github.hanihashemi.podgir.annotate.Exclude;

/**
 * Created by hani on 10/17/15.
 */
public class GsonAnnotationExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(Exclude.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
