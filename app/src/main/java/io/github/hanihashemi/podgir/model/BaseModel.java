package io.github.hanihashemi.podgir.model;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hani on 8/22/15.
 */
public abstract class BaseModel<T> extends SugarRecord<T> {

    public static <T extends BaseModel<?>> List<T> findAllAsList(Class<T> type) {
        Iterator<T> iterator = findAll(type);

        ArrayList<T> items = new ArrayList<>();
        while (iterator.hasNext())
            items.add(iterator.next());

        return items;
    }

    public static <T extends BaseModel<?>> T find(Class<T> type, String objectId) {
        List<T> items = T.find(type, "OBJECT_ID=?", objectId);
        return items.size() >= 1 ? items.get(0) : null;
    }

    public static <T extends BaseModel<?>> void saveResults(Class<T> type, ResultResponse<T> response) {
        for (T item : response.getResults())
            if (find(type, item.getObjectId()) == null)
                item.save();
    }

    protected final String getHostUrl(String api, Object... args) {
        String host = "https://api.parse.com/1/";
        api = String.format(api, args);
        return host + api;
    }

    protected final String getHostUrl(String apiUrl) {
        String host = "https://api.parse.com/1/";
        return host + apiUrl;
    }

    public abstract String getObjectId();
}
