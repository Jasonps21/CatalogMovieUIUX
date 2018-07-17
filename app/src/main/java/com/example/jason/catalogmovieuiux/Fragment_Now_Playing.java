package com.example.jason.catalogmovieuiux;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.jason.catalogmovieuiux.Adapter.MovieAdapter;
import com.example.jason.catalogmovieuiux.Utils.MyAsyncTaskLoader;
import com.example.jason.catalogmovieuiux.model.MovieItems;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Fragment_Now_Playing extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<MovieItems>> {

    @BindView(R.id.rvMovie)
    RecyclerView rvMovie;

    private ArrayList<MovieItems> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now__playing, container, false);
        ButterKnife.bind(this, view);

        rvMovie.setHasFixedSize(true);

        Bundle bundle = new Bundle();
        bundle.putString("kategori", MyAsyncTaskLoader.EXTRA_NOW_PLAYING);
        getActivity().getLoaderManager().restartLoader(0, bundle, this);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().supportInvalidateOptionsMenu();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                loadMovieSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int i, Bundle bundle) {
        String kategori = "";
        String judul = "";
        if (bundle != null) {
            kategori = bundle.getString("kategori");
            judul = bundle.getString("judul");
        }
        return new MyAsyncTaskLoader(getContext(), judul, kategori);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieItems>> loader, ArrayList<MovieItems> MovieItems) {
        list = MovieItems;
        showRecycle();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieItems>> loader) {

    }

    public void loadMovieSearch(String judul){
        Bundle bundle = new Bundle();
        bundle.putString("kategori", MyAsyncTaskLoader.EXTRA_SEARCH);
        bundle.putString("judul", judul);
        getActivity().getLoaderManager().restartLoader(0, bundle, this);
    }

    private void showRecycle() {
        rvMovie.setLayoutManager(new LinearLayoutManager(getContext()));
        MovieAdapter listMovieAdapter = new MovieAdapter(getContext());
        listMovieAdapter.setMoviesList(list);
        rvMovie.setAdapter(listMovieAdapter);
    }
}
