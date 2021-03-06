package io.github.hanihashemi.podgir.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.orm.dsl.Ignore;

import java.io.File;
import java.util.List;

import io.github.hanihashemi.podgir.annotate.Exclude;
import io.github.hanihashemi.podgir.helper.Directory;
import io.github.hanihashemi.podgir.network.request.GsonRequest;

/**
 * Created by hani on 8/21/15.
 */
public class Episode extends BaseModel<Episode> implements Parcelable {
    public static final Creator<Episode> CREATOR = new Creator<Episode>() {
        public Episode createFromParcel(Parcel source) {
            return new Episode(source);
        }

        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    };
    private static Episode episode;
    private String objectId;
    private String title;
    private String url;
    private String summary;
    private String parent;
    private Long downloadId;
    private Integer duration;
    private Integer number;
    private String day;
    private String month;
    @Ignore
    @Exclude
    private Podcast podcast;

    public Episode() {
    }

    protected Episode(Parcel in) {
        this.objectId = in.readString();
        this.title = in.readString();
        this.url = in.readString();
        this.summary = in.readString();
        this.parent = in.readString();
        this.downloadId = (Long) in.readValue(Long.class.getClassLoader());
        this.duration = (Integer) in.readValue(Integer.class.getClassLoader());
        this.number = (Integer) in.readValue(Integer.class.getClassLoader());
        this.podcast = in.readParcelable(Podcast.class.getClassLoader());
    }

    public static Episode getModel() {
        if (episode == null)
            episode = new Episode();
        return episode;
    }

    public static List<Episode> findAll(String parent) {
        return Episode.find(Episode.class, "PARENT = ?", new String[]{parent}, "", "ID DESC", "");
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Podcast getParent() {
        if (podcast != null)
            return podcast;

        List<Podcast> podcasts = Podcast.find(Podcast.class, "OBJECT_ID=?", parent);
        if (podcasts != null && podcasts.size() == 1)
            podcast = podcasts.get(0);
        return podcast;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Episode toDatabaseObject() {
        List<Episode> episodes = Episode.find(Episode.class, "OBJECT_ID=?", objectId);
        return episodes != null && episodes.size() == 1 ? episodes.get(0) : null;
    }

    public File getFile() {
        return Directory.getInstance().getFile(getObjectId());
    }

    public boolean isDownloaded() {
        return getFile().exists() && getDownloadId() == null;
    }

    public GsonRequest<EpisodeResultResponse> remoteFindAll(String parent, Response.Listener<EpisodeResultResponse> onSuccess, Response.ErrorListener onFailed) {
        String argument = "{\"parent\":\"" + parent + "\"}";

        return new GsonRequest<>(
                Request.Method.GET,
                getHostUrl("classes/feed?order=number&where=%s", argument),
                null,
                EpisodeResultResponse.class,
                onSuccess,
                onFailed
        );
    }

    public String getObjectId() {
        return objectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public String getSummary() {
        return summary;
    }

    public Long getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(Long downloadId) {
        this.downloadId = downloadId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.objectId);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.summary);
        dest.writeString(this.parent);
        dest.writeValue(this.downloadId);
        dest.writeValue(this.duration);
        dest.writeValue(this.number);
        dest.writeParcelable(this.podcast, 0);
    }

    public String getNumber() {
        String number = String.valueOf(this.number);
        if (!TextUtils.isEmpty(number))
            return number + ":";
        return "";
    }

    public String getMonth() {
        if (TextUtils.isEmpty(month))
            return "-";
        return month;
    }

    public String getDay() {
        return day;
    }
}