package com.fat7y.movies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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

public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {
    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    private CustomAdapter moviesAdapter;

    public FetchMoviesTask(CustomAdapter moviesAdapter) {
        this.moviesAdapter = moviesAdapter;
    }

    private ArrayList<Movie> getMoviesDataFromJson(int begin, String moviesJsonStr)
            throws JSONException {
        ArrayList<Movie> resultMovies = new ArrayList<Movie>();
        try {
            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(MoviesFragment.OWM_RESULT);
            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject movieObject = moviesArray.getJSONObject(i);
                Movie movie = new Movie(movieObject);
                resultMovies.add(movie);
                MainActivity.mp.put(i + begin, movie);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return resultMovies;
    }

    @Override
    protected synchronized ArrayList<Movie> doInBackground(String... params) {
        MainActivity.mp.clear();
        ArrayList<Movie> ret = new ArrayList<>();
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJsonStr;
        String OPEN_MOVIE_API_KEY = "8e8d3f9effda8ce61536a1ecfba91f88";
        try {
            final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String APPID_PARAM = "api_key";
            for(int i = 1; i < 3; i++) {
                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendEncodedPath(params[0])
                        .appendQueryParameter(APPID_PARAM, OPEN_MOVIE_API_KEY)
                        .appendQueryParameter("page", String.valueOf(i))
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
                    return null;
                }
                moviesJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Movies JSON String" + moviesJsonStr);
                ret.addAll(getMoviesDataFromJson(ret.size(), moviesJsonStr));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }
    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        try{
            moviesAdapter.clear();
            moviesAdapter.addAll(movies);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
