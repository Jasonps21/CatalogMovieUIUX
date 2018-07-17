package com.example.jason.favoritemovie;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;

public class MovieDetail extends AppCompatActivity {

    @BindView(R.id.tv_judul_film_detail)
    TextView tvMovieName;
    @BindView(R.id.img_backdrop)
    ImageView imgBackdrop;
    @BindView(R.id.tv_tanggal_rilis_detail)
    TextView tvYear;
    @BindView(R.id.tv_sinopsi_detail)
    TextView tvSynopsis;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    private int movieid;
    String posterPath, nama, release, sinopsis, backdropPath;

    public static String EXTRA_NOTE_ITEM = "extra_note_item";
    private movieItems movieItem = null;
    private Boolean isFavorite = false;

    public static int RESULT_DELETE = 101;
    public static int REQUEST_DELETE = 100;
    public static String EXTRA_ID = "extra_id";
    public static String EXTRA_JUDUL = "extra_judul";
    public static String EXTRA_RELEASE = "extra_release";
    public static String EXTRA_BACKDROP = "extra_bacdrop";
    public static String EXTRA_POSTER = "extra_poster";
    public static String EXTRA_SINOPSI = "extra_sinopsi";
    public static String EXTRA_DELETE = "extra_delete";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Uri uri = getIntent().getData();

        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            if (cursor != null){

                if(cursor.moveToFirst()) movieItem = new movieItems(cursor);
                cursor.close();
            }
        }

        tvMovieName.setText(movieItem.getTitle());
        tvYear.setText(movieItem.getYear());
        tvSynopsis.setText(movieItem.getSynopsis());
        Glide.with(this).load(FavoriteAdapters.BASE_URL + FavoriteAdapters.IMAGE_SIZE + movieItem.getImageURL()).into(imgBackdrop);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_view:
                                Intent intent = new Intent(MovieDetail.this, PosterView.class);
                                intent.putExtra(EXTRA_POSTER, posterPath);
                                intent.putExtra(EXTRA_JUDUL, nama);
                                startActivity(intent);
                                break;
                            case R.id.action_favorite:
                                Uri uri = getIntent().getData();
                                getContentResolver().delete(uri, null, null);
                                Toast.makeText(MovieDetail.this, "Favorite dihapus", Toast.LENGTH_SHORT).show();
                                finish();
                                break;
                            case R.id.action_share:
                                break;
                        }
                        return true;
                    }
                });
    }
}