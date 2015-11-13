package io.github.hanihashemi.podgir.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.Request;
import com.android.volley.Response;

import io.github.hanihashemi.podgir.network.request.GsonListRequest;

/**
 * Created by hani on 8/18/15.
 */
public class Podcast extends BaseModel<Podcast> implements Parcelable {
    public static final Creator<Podcast> CREATOR = new Creator<Podcast>() {
        public Podcast createFromParcel(Parcel source) {
            return new Podcast(source);
        }

        public Podcast[] newArray(int size) {
            return new Podcast[size];
        }
    };
    public static Podcast podcast;
    private String objectId;
    private String name;
    private String imageUrl;
    private String summary;
    public Podcast() {
    }

    protected Podcast(Parcel in) {
        this.objectId = in.readString();
        this.name = in.readString();
        this.imageUrl = in.readString();
        this.summary = in.readString();
    }

    public static Podcast getModel() {
        if (podcast == null)
            podcast = new Podcast();
        return podcast;
    }

    public GsonListRequest<PodcastResultResponse> reqFindAll(Response.Listener<PodcastResultResponse> onSuccess, Response.ErrorListener onFailed) {
        return new GsonListRequest<>(
                Request.Method.GET,
                getHostUrl("classes/podcast"),
                null,
                PodcastResultResponse.class,
                onSuccess,
                onFailed
        );
    }

    public String getObjectId() {
        return objectId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.objectId);
        dest.writeString(this.name);
        dest.writeString(this.imageUrl);
        dest.writeString(this.summary);
    }
}
