package com.example.jason.catalogmovieuiux;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.jason.catalogmovieuiux.Adapter.FavoriteAdapter;
import com.example.jason.catalogmovieuiux.Adapter.MovieAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.jason.catalogmovieuiux.Database.DatabaseContract.CONTENT_URI;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteMovie extends Fragment {

    @BindView(R.id.rvMovie)
    RecyclerView rvMovie;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private Cursor list;
    public FavoriteAdapter adapter;

    public FavoriteMovie() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_movie, container, false);
        ButterKnife.bind(this, view);
        rvMovie.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new FavoriteAdapter(getActivity());
        adapter.notifyDataSetChanged();
        adapter.setListMovie(list);
        rvMovie.setAdapter(adapter);

        new LoadMovieAsync().execute();
        return view;
    }

    public class LoadMovieAsync extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return getActivity().getApplicationContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            progressBar.setVisibility(View.GONE);

            list = cursor;
            adapter.setListMovie(list);
            adapter.notifyDataSetChanged();

            if (list.getCount() == 0) {
                Snackbar.make(rvMovie, "Tidak ada favorite saat ini", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MovieDetail.REQUEST_DELETE){
            if (resultCode == MovieDetail.RESULT_DELETE) {
                new LoadMovieAsync().execute();
            }
        }

    }
}
