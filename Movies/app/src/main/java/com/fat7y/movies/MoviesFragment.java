package com.fat7y.movies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by user on 10/21/2016.
 */
public class MoviesFragment extends Fragment {
    public final static String OWM_TITLE = "title";
    public final static String OWM_RESULT = "results";
    public final static String OWM_POSTER = "poster_path";
    public final static String OWM_DESCRIPTION = "overview";
    public final static String OWM_RATING = "popularity";
    public final static String OWM_DATE = "release_date";
    public final static String OWM_VOTE = "vote_average";
    private CustomAdapter moviesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onStart () {
        super.onStart();
        updateMovies("popular");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviesfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh) {
            updateMovies("popular");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateMovies(String sortBy) {
        FetchMoviesTask moviesTask = new FetchMoviesTask();
       // SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //String location = prefs.getString(getString(R.string.pref_location_key),
         //       getString(R.string.pref_location_default));
        moviesTask.execute(sortBy);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        moviesAdapter =
                new CustomAdapter(
                        getActivity(),
                        new ArrayList());

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.

        GridView gridView = (GridView) rootView.findViewById(R.id.gridView_movies);
        gridView.setAdapter(moviesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("key", position);
                startActivity(intent);
            }
        });
        return rootView;
    }


    public class FetchMoviesTask extends AsyncTask<String, Void, JSONObject[]> {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        private JSONObject[] getMoviesDataFromJson(String moviesJsonStr, int numMovies)
                throws JSONException {
            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(OWM_RESULT);
            JSONObject[] resultMovies = new JSONObject[numMovies];

            for(int i = 0; i < Math.min(moviesArray.length(), numMovies); i++) {
                JSONObject movieObject = moviesArray.getJSONObject(i);
                resultMovies[i] = movieObject;
            }

//            for (String s : resultStrs) {
//                Log.v(LOG_TAG, "Movie entry: " + s);
//            }
            return resultMovies;

        }

        @Override
        protected JSONObject[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;
            String OPEN_MOVIE_API_KEY = "8e8d3f9effda8ce61536a1ecfba91f88";
            try {
                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendEncodedPath(params[0])
                        .appendQueryParameter(APPID_PARAM, OPEN_MOVIE_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI : " + builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                Log.v(LOG_TAG, "Success");

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Movies JSON String" + moviesJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            try {
                return getMoviesDataFromJson(moviesJsonStr, 20);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject[] movies) {
            moviesAdapter.clear();
            moviesAdapter.addAll(movies);
        }
    }

}
