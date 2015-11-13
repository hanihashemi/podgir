package io.github.hanihashemi.podgir.model;

import com.orm.SugarRecord;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    public static <T extends BaseModel<?>> void saveResults(Class<T> type, ResultResponse<T> response) {
        for (T item : response.getResults())
            if (find(type, item.getObjectId()) == null)
                item.save();
    }

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

    public abstract String getObjectId();
}
