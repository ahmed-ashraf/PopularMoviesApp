package com.udacity.ahmed.popularmoviesapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.method.MovementMethod;

/**
 * Created by Stas on 19.07.15.
 */
public class Movie implements Parcelable {

    public static final Creator CREATOR
            = new Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public MovementMethod[] newArray(int size) {
            return new MovementMethod[size];
        }
    };

    private String overview;
    private String backdropPath;
    private String title;
    private String posterPath;
    private String voteAvarage;
    private String releaseDate="";
    private String id;
    private int runtime;
    private String cachedPosterPath;

    public Movie() {

    }

    // parcel construtor
    protected Movie(Parcel p) {
        id = p.readString();
        overview = p.readString();
        posterPath = p.readString();
        title = p.readString();
        voteAvarage = p.readString();
        releaseDate=p.readString();
        runtime=p.readInt();
        cachedPosterPath=p.readString();
        backdropPath=p.readString();
    }

    public String getCachedPosterPath() {
        return cachedPosterPath;
    }

    public void setCachedPosterPath(String cachedPosterPath) {
        this.cachedPosterPath = cachedPosterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVoteAvarage() {
        return voteAvarage;
    }

    public void setVoteAvarage(String voteAvarage) {
        this.voteAvarage = voteAvarage;
    }

    public String getReleaseYear(){
        return releaseDate.substring(0,4);
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public void setBackdropPath(String path){
        this.backdropPath = path;
    }
    public String getBackdropPath(){
        return backdropPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(title);
        dest.writeString(voteAvarage);
        dest.writeString(releaseDate);
        dest.writeInt(runtime);
        dest.writeString(cachedPosterPath);
        dest.writeString(backdropPath);
    }
}
