package com.fat7y.movies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Hashtable;
import java.util.Map;

public class MainActivity extends ActionBarActivity implements MoviesFragment.Callback ,
        MoviesFragment.CallbackUpdate{
    public static Map<Integer, Movie> mp;
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private MoviesFragment fragment;
    private boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    public static String OPEN_MOVIE_API_KEY = "8e8d3f9effda8ce61536a1ecfba91f88";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mp = new Hashtable<>(); //map that save ID with movie for intent linkage
        fragment = new MoviesFragment();
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment).commit();

        mTwoPane =  (findViewById(R.id.container_movies) != null);  //tablet mode flag
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(!haveNetworkConnection(this)) {
            Toast.makeText(fragment.getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            fragment.updateMovies("favourite");
        }
        else if (id == R.id.sort_pop) {
            fragment.updateMovies("popular");
            return true;
        }
        else if(id == R.id.sort_top) {
            fragment.updateMovies("top_rated");
            return true;
        }
        else if(id == R.id.fav) {
            fragment.updateMovies("favourite");
        }
        return super.onOptionsItemSelected(item);
    }

    public static boolean haveNetworkConnection(Activity activity) {    //Network checker
        ConnectivityManager cm = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI") && ni.isConnected()) {
                return true;
            }
            else if (ni.getTypeName().equalsIgnoreCase("MOBILE") && ni.isConnected()) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void onItemSelected(int position) {
        if(mTwoPane) {
            Bundle args = new Bundle();
            args.putInt("key", position);
            DetailActivity.DetailFragment fragment = new DetailActivity.DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_movies, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        }
        else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("key", position);
            startActivity(intent);
        }
    }
    @Override
    public void requestUpdate() {   //update movies grid if favourite menu changed
        fragment.requestUpdate();
    }
}
