package io.github.hanihashemi.bulbul.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import io.github.hanihashemi.bulbul.network.request.GsonListRequest;

/**
 * Created by hani on 8/18/15.
 */
public class Podcast extends BaseModel implements Parcelable {
    private String objectId;
    private String name;
    private String imageUrl;

    public Podcast() {
    }

    public GsonListRequest<List<Podcast>> findAll(Response.Listener<List<Podcast>> onSuccess, Response.ErrorListener onFailed) {
        Type type = new TypeToken<List<Podcast>>() {
        }.getType();

        return new GsonListRequest<>(
                Request.Method.GET,
                getUrl("classes/podcast"),
                null,
                type,
                onSuccess,
                onFailed
        );
    }

    public String getId() {
        return objectId;
    }

    public void setId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.objectId);
        dest.writeString(this.name);
        dest.writeString(this.imageUrl);
    }

    protected Podcast(Parcel in) {
        this.objectId = in.readString();
        this.name = in.readString();
        this.imageUrl = in.readString();
    }

    public static final Parcelable.Creator<Podcast> CREATOR = new Parcelable.Creator<Podcast>() {
        public Podcast createFromParcel(Parcel source) {
            return new Podcast(source);
        }

        public Podcast[] newArray(int size) {
            return new Podcast[size];
        }
    };
}
