package com.example.jason.favoritemovie;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.jason.favoritemovie.DatabaseContract.MovieColumns.POSTER_PATH;
import static com.example.jason.favoritemovie.DatabaseContract.MovieColumns.SYNOPSIS;
import static com.example.jason.favoritemovie.DatabaseContract.MovieColumns.TITLE;
import static com.example.jason.favoritemovie.DatabaseContract.MovieColumns.YEAR;
import static com.example.jason.favoritemovie.DatabaseContract.getColumnString;

public class FavoriteAdapters extends CursorAdapter {

    public static final String BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String IMAGE_SIZE = "w500";

    @BindView(R.id.img_item_photo)
    ImageView imgPoster;

    @BindView(R.id.tvJudul)
    TextView tvJudul;

    @BindView(R.id.tv_Synopsis)
    TextView tvSynopsis;

    @BindView(R.id.tvRelease)
    TextView tvRelease;

    @BindView(R.id.btn_set_detail)
    Button btnDetail;

    public FavoriteAdapters(Context context, Cursor c, boolean autorecovery) {
        super(context, c, autorecovery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, viewGroup, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if(cursor!=null){
            Glide.with(context).load(BASE_URL + IMAGE_SIZE + getColumnString(cursor,POSTER_PATH)).into(imgPoster);
            tvJudul.setText(getColumnString(cursor,TITLE));
            tvSynopsis.setText(getColumnString(cursor,SYNOPSIS));
            tvRelease.setText(getColumnString(cursor,YEAR));
        }
    }
}
