package com.example.jason.catalogmovieuiux;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.jason.catalogmovieuiux.Adapter.MovieAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PosterView extends AppCompatActivity {

    @BindView(R.id.img_poster_view)
    ImageView imgPosterView;

    String judul, posterURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_view);

        ButterKnife.bind(this);
        judul = getIntent().getStringExtra(MovieDetail.EXTRA_JUDUL);
        posterURL = getIntent().getStringExtra(MovieDetail.EXTRA_POSTER);
        getSupportActionBar().setTitle(judul);
        Glide.with(this).load(MovieAdapter.BASE_URL+MovieAdapter.IMAGE_SIZE+posterURL).into(imgPosterView);
    }
}
