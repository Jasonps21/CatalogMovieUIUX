package com.example.jason.catalogmovieuiux.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jason.catalogmovieuiux.MovieDetail;
import com.example.jason.catalogmovieuiux.R;
import com.example.jason.catalogmovieuiux.model.MovieItems;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.jason.catalogmovieuiux.Adapter.MovieAdapter.BASE_URL;
import static com.example.jason.catalogmovieuiux.Adapter.MovieAdapter.IMAGE_SIZE;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private Cursor movieCursor;
    private Activity activity;

    public FavoriteAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setListMovie(Cursor movieCursor){
        this.movieCursor = movieCursor;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        final MovieItems movie = getItem(position);

        Glide.with(activity).load(BASE_URL + IMAGE_SIZE + movie.getPosterPath()).into(holder.imgPoster);
        holder.tvJudul.setText(movie.getTitle());
        holder.tvSynopsis.setText(movie.getSynopsis());
        holder.tvRelease.setText(movie.getYear());
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MovieDetail.class);
                intent.putExtra(MovieDetail.EXTRA_ID, movie.getId());
                intent.putExtra(MovieDetail.EXTRA_JUDUL, movie.getTitle());
                intent.putExtra(MovieDetail.EXTRA_RELEASE, movie.getYear());
                intent.putExtra(MovieDetail.EXTRA_SINOPSI, movie.getSynopsis());
                intent.putExtra(MovieDetail.EXTRA_BACKDROP, movie.getImageURL());
                intent.putExtra(MovieDetail.EXTRA_POSTER, movie.getPosterPath());
                intent.putExtra(MovieDetail.EXTRA_DELETE, true);
                activity.startActivityForResult(intent, MovieDetail.REQUEST_DELETE);
            }
        });
    }

    private MovieItems getItem(int position){
        if (!movieCursor.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid");
        }
        return new MovieItems(movieCursor);
    }

    @Override
    public int getItemCount() {
        if (movieCursor == null) return 0;
        return movieCursor.getCount();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

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

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
