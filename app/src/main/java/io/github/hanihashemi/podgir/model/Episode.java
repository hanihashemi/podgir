package io.github.hanihashemi.podgir.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.orm.dsl.Ignore;

import java.io.File;
import java.util.List;

import io.github.hanihashemi.podgir.annotate.Exclude;
import io.github.hanihashemi.podgir.network.request.GsonRequest;
import io.github.hanihashemi.podgir.util.Directory;

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
    private String objectId;
    private String title;
    private String url;
    private String summary;
    @Ignore
    private boolean downloaded;
    private String parent;
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
        this.downloaded = in.readByte() != 0;
        this.parent = in.readString();
        this.podcast = in.readParcelable(Podcast.class.getClassLoader());
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

    private Episode getObjectDB() {
        List<Episode> episodes = Episode.find(Episode.class, "OBJECT_ID=?", objectId);
        return episodes != null && episodes.size() == 1 ? episodes.get(0) : null;
    }

    private File getFile() {
        return Directory.getInstance().getFile(getParent().getObjectId(), getObjectId());
    }

    private boolean isThereFile() {
        return getFile().exists();
    }

    public String getFilePath() {
        return getFile().getAbsolutePath();
    }

    public boolean isThere() {
        Episode episodeDB = getObjectDB();

        if (isThereFile()) {
            if (episodeDB == null)
                this.save();
            return true;
        } else if (episodeDB != null)
            episodeDB.delete();
        return false;
    }

    public GsonRequest<EpisodeResultResponse> remoteFindAll(String parent, Response.Listener<EpisodeResultResponse> onSuccess, Response.ErrorListener onFailed) {
        String argument = "{\"parent\":\"" + parent + "\"}";

        return new GsonRequest<>(
                Request.Method.GET,
                getHostUrl("classes/feed?where=" +
                        argument),
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

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
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
        dest.writeByte(downloaded ? (byte) 1 : (byte) 0);
        dest.writeString(this.parent);
        dest.writeParcelable(this.podcast, 0);
    }
}
