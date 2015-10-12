package io.github.hanihashemi.podgir.model;

import java.util.List;

/**
 * Created by hani on 10/12/15.
 */
public class ResultResponse<T> {
    private List<T> results;

    public List<T> getResults() {
        return results;
    }
}
