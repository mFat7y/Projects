package com.fat7y.movies;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by user on 10/21/2016.
 */
public class MainActivity extends ActionBarActivity{
    public static Map<Integer, JSONObject> mp;
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private MoviesFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mp = new Hashtable<>();
        fragment = new MoviesFragment();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment).commit();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sort_pop) {
            fragment.updateMovies("popular");
            return true;
        }
        else if(id == R.id.sort_top) {
            fragment.updateMovies("top_rated");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
