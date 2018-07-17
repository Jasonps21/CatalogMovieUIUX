package com.example.jason.catalogmovieuiux.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.MovieColumns.BACKDROP_PATH;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.MovieColumns.POSTER_PATH;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.MovieColumns.SYNOPSIS;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.MovieColumns.TITLE;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.MovieColumns.YEAR;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.TABLE_NAME;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "dbmovie";

    public static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_MOVIE = "CREATE TABLE " + TABLE_NAME +
            " (" + _ID + " integer primary key, " +
            TITLE + " text not null, " +
            YEAR + " text not null, " +
            SYNOPSIS + " text not null, " +
            POSTER_PATH + " text not null, " +
            BACKDROP_PATH + " text not null);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
