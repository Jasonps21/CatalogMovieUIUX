package com.example.jason.catalogmovieuiux;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.jason.catalogmovieuiux.Utils.MyAsyncTaskLoader;
import com.example.jason.catalogmovieuiux.Utils.Setting;
import com.example.jason.catalogmovieuiux.model.MovieItems;
import com.example.jason.catalogmovieuiux.scheduler.SchedulerTask;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    ActionBarDrawerToggle toggle;
    private ArrayList<MovieItems> list;

    private String currentSort;

    private Fragment_Now_Playing fragment_now_playing;

    private SchedulerTask schedulerTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentSort = MyAsyncTaskLoader.EXTRA_NOW_PLAYING;

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        Bundle bundle = new Bundle();

        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        navigationView.setNavigationItemSelectedListener(this);
        if(savedInstanceState==null){
            fragment_now_playing = new Fragment_Now_Playing();
//            bundle.putString(SORT_MODE, currentSort);
//            fragment_now_playing.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment_now_playing).commit();
        }else if(savedInstanceState!=null){
            fragment_now_playing = (Fragment_Now_Playing) getSupportFragmentManager().findFragmentByTag("fragment_now_playing");
//            currentSort = savedInstanceState.getString(SORT_MODE);
//            Fragment currentFragment = new Fragment_Now_Playing();
//            bundle.putString(SORT_MODE, currentSort);
//            currentFragment.setArguments(bundle);
//            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, currentFragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        drawer.removeDrawerListener(toggle);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String title = "";
        Fragment fragment = null;
        if (id == R.id.nav_sedang_tayang) {
            title = getResources().getString(R.string.sedang_tayang);
            fragment = new Fragment_Now_Playing();
            currentSort = MyAsyncTaskLoader.EXTRA_NOW_PLAYING;
            Bundle bundle = new Bundle();
            bundle.putString("sort", currentSort);
        } else if (id == R.id.nav_akan_datang) {
            title = getResources().getString(R.string.akan_tayang);
            fragment = new Fragment_Upcoming();
            currentSort = MyAsyncTaskLoader.EXTRA_UP_COMING;
            Bundle bundle = new Bundle();
            bundle.putString("sort", currentSort);
        } else if (id == R.id.nav_favorite) {
            title = "Favorite";
            fragment = new FavoriteMovie();
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, Setting.class);
            startActivity(intent);
            return true;
        }
        if (fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
        }

        getSupportActionBar().setTitle(title);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);

    }
}
