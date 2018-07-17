package com.example.jason.catalogmovieuiux.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jason.catalogmovieuiux.Utils.DateTime;
import com.example.jason.catalogmovieuiux.MovieDetail;
import com.example.jason.catalogmovieuiux.R;
import com.example.jason.catalogmovieuiux.model.MovieItems;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    public static final String BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String IMAGE_SIZE = "w500";

    private Context context;
    private ArrayList<MovieItems> moviesList;

    public MovieAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<MovieItems> getMoviesList() {
        return moviesList;
    }

    public void setMoviesList(ArrayList<MovieItems> moviesList) {
        this.moviesList = moviesList;
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        final MovieItems movie = getMoviesList().get(position);

        Glide.with(context).load(BASE_URL + IMAGE_SIZE + movie.getPosterPath()).into(holder.imgPoster);
        holder.tvJudul.setText(movie.getTitle());
        holder.tvSynopsis.setText(movie.getSynopsis());
        holder.tvRelease.setText(DateTime.getLongDate(movie.getYear()));
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MovieDetail.class);
                intent.putExtra(MovieDetail.EXTRA_ID, movie.getId());
                intent.putExtra(MovieDetail.EXTRA_JUDUL, movie.getTitle());
                intent.putExtra(MovieDetail.EXTRA_RELEASE, DateTime.getLongDate(movie.getYear()));
                intent.putExtra(MovieDetail.EXTRA_SINOPSI, movie.getSynopsis());
                intent.putExtra(MovieDetail.EXTRA_BACKDROP, movie.getImageURL());
                intent.putExtra(MovieDetail.EXTRA_POSTER, movie.getPosterPath());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
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

        private MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
