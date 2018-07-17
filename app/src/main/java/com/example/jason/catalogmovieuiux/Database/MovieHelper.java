package com.example.jason.catalogmovieuiux.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.jason.catalogmovieuiux.model.MovieItems;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.MovieColumns.BACKDROP_PATH;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.MovieColumns.POSTER_PATH;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.MovieColumns.SYNOPSIS;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.MovieColumns.TITLE;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.MovieColumns.YEAR;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.TABLE_NAME;

public class MovieHelper {

    private static String DATABASE_TABLE = TABLE_NAME;
    private Context context;
    private DatabaseHelper databaseHelper;

    private SQLiteDatabase database;

    public MovieHelper(Context context) {
        this.context = context;
    }

    public MovieHelper open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        database.close();
    }

    public ArrayList<MovieItems> query() {
        ArrayList<MovieItems> arrayList = new ArrayList<MovieItems>();
        Cursor cursor = database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null, _ID + " DESC"
                , null);
        cursor.moveToFirst();
        MovieItems movieList;
        if (cursor.getCount() > 0) {
            do {
                movieList = new MovieItems();
                movieList.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                movieList.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movieList.setYear(cursor.getString(cursor.getColumnIndexOrThrow(YEAR)));
                movieList.setSynopsis(cursor.getString(cursor.getColumnIndexOrThrow(SYNOPSIS)));
                movieList.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));
                movieList.setImageURL(cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP_PATH)));
                arrayList.add(movieList);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(MovieItems movieData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(_ID, movieData.getId());
        contentValues.put(TITLE, movieData.getTitle());
        contentValues.put(YEAR, movieData.getYear());
        contentValues.put(SYNOPSIS, movieData.getSynopsis());
        contentValues.put(POSTER_PATH, movieData.getPosterPath());
        contentValues.put(BACKDROP_PATH, movieData.getImageURL());
        return database.insert(DATABASE_TABLE, null, contentValues);
    }

    public int update(MovieItems movieData) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(_ID, movieData.getId());
        initialValues.put(TITLE, movieData.getTitle());
        initialValues.put(YEAR, movieData.getYear());
        initialValues.put(SYNOPSIS, movieData.getSynopsis());
        initialValues.put(POSTER_PATH, movieData.getPosterPath());
        initialValues.put(BACKDROP_PATH, movieData.getImageURL());
        return database.update(DATABASE_TABLE, initialValues, _ID + " = '" + movieData.getId() + "'", null);
    }

    public int delete(int id) {
        return database.delete(TABLE_NAME, _ID + " ='" + id + "'", null);
    }

    public boolean getMoviebyName(String title) {
        boolean result = false;
        Cursor cursor = database.query(DATABASE_TABLE, null, TITLE + " = '"+title+"'",null, null, null, _ID + " ASC", null);
        cursor.moveToFirst();
        ArrayList<MovieItems> arrayList = new ArrayList<>();
        MovieItems kamusModel;
        if (cursor.getCount() > 0) {
            result = true;
        }
        cursor.close();
        return result;
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE, null
                , _ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , _ID + " DESC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, _ID + " = ?", new String[]{id});
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }
}
