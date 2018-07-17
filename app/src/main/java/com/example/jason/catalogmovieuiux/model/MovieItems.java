package com.example.jason.catalogmovieuiux.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import static android.provider.BaseColumns._ID;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.MovieColumns.BACKDROP_PATH;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.MovieColumns.POSTER_PATH;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.MovieColumns.SYNOPSIS;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.MovieColumns.TITLE;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.MovieColumns.YEAR;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.getColumnInt;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.getColumnString;

public class MovieItems implements Parcelable {
    private int page;
    private int id;
    private String imageURL;
    private String posterPath;
    private String title;
    private String year;
    private double rating;
    private String synopsis;

    public MovieItems() {

    }

    public MovieItems(Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.imageURL = getColumnString(cursor, BACKDROP_PATH);
        this.posterPath = getColumnString(cursor, POSTER_PATH);
        this.title = getColumnString(cursor, TITLE);
        this.year = getColumnString(cursor, YEAR);
        this.synopsis = getColumnString(cursor, SYNOPSIS);
    }

    protected MovieItems(Parcel in) {
        page = in.readInt();
        id = in.readInt();
        imageURL = in.readString();
        posterPath = in.readString();
        title = in.readString();
        year = in.readString();
        rating = in.readDouble();
        synopsis = in.readString();
    }

    public static final Creator<MovieItems> CREATOR = new Creator<MovieItems>() {
        @Override
        public MovieItems createFromParcel(Parcel in) {
            return new MovieItems(in);
        }

        @Override
        public MovieItems[] newArray(int size) {
            return new MovieItems[size];
        }
    };

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public MovieItems(JSONObject object, int page) {
        try {
            int id = object.getInt("id");
            String posterUrl = object.getString("poster_path");
            String title = object.getString("original_title");
            String year = object.getString("release_date");
            double rating = object.getDouble("vote_average");
            String synopsis = object.getString("overview");
            String imgUrl = object.getString("backdrop_path");
            this.id = id;
            this.page = page;
            this.imageURL = imgUrl;
            this.title = title;
            this.synopsis = synopsis;
            this.posterPath = posterUrl;
            this.year = year;
            this.rating = rating;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return (id);
    }

    public String getImageURL() {
        return (imageURL);
    }

    public String getTitle() {
        return (title);
    }

    public String getYear() {
        return (year);
    }

    public String getSynopsis() {
        return (synopsis);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(page);
        parcel.writeInt(id);
        parcel.writeString(imageURL);
        parcel.writeString(posterPath);
        parcel.writeString(title);
        parcel.writeString(year);
        parcel.writeDouble(rating);
        parcel.writeString(synopsis);
    }
}
