package com.example.jason.catalogmovieuiux.Utils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;

import com.example.jason.catalogmovieuiux.BuildConfig;
import com.example.jason.catalogmovieuiux.model.MovieItems;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MyAsyncTaskLoader extends AsyncTaskLoader<ArrayList<MovieItems>> {

    public static final String EXTRA_NOW_PLAYING = "Now Playing";
    public static final String EXTRA_UP_COMING = "Up Coming";
    public static final String EXTRA_SEARCH = "Search";
    private ArrayList<MovieItems> mData;
    private boolean mHasResult = false;
    private String mJudulFilm;
    private String kategori;

    public MyAsyncTaskLoader(final Context context, String mJudulFilm, String kategori) {
        super(context);
        onContentChanged();
        this.mJudulFilm = mJudulFilm;
        this.kategori = kategori;
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged()) forceLoad();
        else if (mHasResult) deliverResult(mData);
    }

    @Override
    public void deliverResult(ArrayList<MovieItems> data) {
        mData = data;
        mHasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mHasResult) {
            onReleaseResource(mData);
            mData = null;
            mHasResult = false;
        }
    }

    @Override
    public ArrayList<MovieItems> loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();
        final ArrayList<MovieItems> moviedata = new ArrayList<>();
        String url = "";
        if (kategori == EXTRA_NOW_PLAYING) {
            url = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + BuildConfig.movieDB_Api_Key + "&language=en-US";
        } else if (kategori == EXTRA_UP_COMING) {
            url = "https://api.themoviedb.org/3/movie/upcoming?api_key=" + BuildConfig.movieDB_Api_Key + "&language=en-US";
        } else if (kategori == EXTRA_SEARCH) {
            if (!TextUtils.isEmpty(mJudulFilm)) {
                url = "https://api.themoviedb.org/3/search/movie?api_key=" + BuildConfig.movieDB_Api_Key + "&language=en-US&query=" + mJudulFilm;
            }
        }
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responObject = new JSONObject(result);
                    int page = responObject.getInt("page");
                    JSONArray movieList = responObject.getJSONArray("results");

                    for (int i = 0; i < movieList.length(); i++) {
                        JSONObject data = movieList.getJSONObject(i);
                        MovieItems movieItems1 = new MovieItems(data, page);
                        moviedata.add(movieItems1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
        return moviedata;
    }

    protected void onReleaseResource(ArrayList<MovieItems> mData) {

    }
}