package com.example.jason.catalogmovieuiux;

import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jason.catalogmovieuiux.Adapter.MovieAdapter;
import com.example.jason.catalogmovieuiux.Database.MovieHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.provider.BaseColumns._ID;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.CONTENT_URI;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.MovieColumns.BACKDROP_PATH;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.MovieColumns.POSTER_PATH;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.MovieColumns.SYNOPSIS;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.MovieColumns.TITLE;
import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.MovieColumns.YEAR;

public class MovieDetail extends AppCompatActivity{

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

    MovieHelper movieHelper;

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

        ButterKnife.bind(this);
        movieid = getIntent().getIntExtra(EXTRA_ID, 0);
        nama = getIntent().getStringExtra(EXTRA_JUDUL);
        posterPath = getIntent().getStringExtra(EXTRA_POSTER);
        sinopsis = getIntent().getStringExtra(EXTRA_SINOPSI);
        release = getIntent().getStringExtra(EXTRA_RELEASE);
        backdropPath = getIntent().getStringExtra(EXTRA_BACKDROP);
        movieHelper = new MovieHelper(this);

        movieHelper.open();
        isFavorite = movieHelper.getMoviebyName(nama);
        movieHelper.close();

        tvMovieName.setText(nama);
        tvYear.setText(release);
        tvSynopsis.setText(sinopsis);
        Glide.with(this).load(MovieAdapter.BASE_URL + MovieAdapter.IMAGE_SIZE + backdropPath).into(imgBackdrop);
        setToolbar();
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
                                if(isFavorite){
                                    movieHelper.open();
                                    movieHelper.deleteProvider(String.valueOf(movieid));
                                    movieHelper.close();
                                    Toast.makeText(MovieDetail.this, "Favorite Dihapus", Toast.LENGTH_SHORT).show();
                                }else {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(_ID, movieid);
                                    contentValues.put(TITLE, nama);
                                    contentValues.put(YEAR, release);
                                    contentValues.put(SYNOPSIS, sinopsis);
                                    contentValues.put(POSTER_PATH, posterPath);
                                    contentValues.put(BACKDROP_PATH, backdropPath);
                                    getContentResolver().insert(CONTENT_URI, contentValues);
                                    Toast.makeText(MovieDetail.this, "Favorite Ditambahkan", Toast.LENGTH_SHORT).show();
                                    isFavorite = true;
                                }
                                break;
                            case R.id.action_share:
                                Intent intent2 = new Intent(Intent.ACTION_SEND);
                                intent2.setType("text/plain");
                                intent2.putExtra(Intent.EXTRA_TEXT, nama);
                                intent2.putExtra(Intent.EXTRA_SUBJECT, sinopsis);
                                intent2.putExtra(Intent.EXTRA_TEXT, nama + "\n\n"+ release + "\n\n"+sinopsis);
                                startActivity(Intent.createChooser(intent2, nama));
                                break;
                        }
                        return true;
                    }
                });
    }

    private void setToolbar() {
        collapsingToolbar.setContentScrimColor(ContextCompat.getColor(MovieDetail.this, R.color.colorPrimary));
        collapsingToolbar.setTitle(nama);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedToolbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedToolbar);
        collapsingToolbar.setTitleEnabled(true);

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
