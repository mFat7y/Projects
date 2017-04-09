package com.fat7y.movies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchTrailersTask extends AsyncTask<String, Void, Trailer[]> {
    private DetailActivity.DetailFragment fragment;
    public FetchTrailersTask(DetailActivity.DetailFragment fragment) {
        this.fragment = fragment;
    }

    private final String LOG_TAG = FetchReviewTask.class.getSimpleName();
    private Trailer[] getMoviesDataFromJson(String reviewsJsonStr, String id)
            throws JSONException {
        JSONObject reviewsJson = new JSONObject(reviewsJsonStr);
        JSONArray reviewsArray = reviewsJson.getJSONArray("results");
        Trailer[] resultreviews = new Trailer[reviewsArray.length()];
        for(int i = 0; i < reviewsArray.length(); i++) {
            JSONObject trailerObject = reviewsArray.getJSONObject(i);
            Trailer trailer = new Trailer(trailerObject, id);
            resultreviews[i] = trailer;
        }
        return resultreviews;
    }


    @Override
    protected Trailer[] doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String trailerJsonStr = null;

        try {
            final String REVIEWS_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String APPID_PARAM = "api_key";

            Uri builtUri = Uri.parse(REVIEWS_BASE_URL).buildUpon()
                    .appendPath(params[0])
                    .appendEncodedPath("videos")
                    .appendQueryParameter(APPID_PARAM, MainActivity.OPEN_MOVIE_API_KEY)
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
            trailerJsonStr = buffer.toString();
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
            fragment.trailersMap.put(params[0], getMoviesDataFromJson(trailerJsonStr, params[0]));
            return fragment.trailersMap.get(params[0]);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Trailer[] trailers) {
        fragment.trailerAdapter.clear();
        fragment.trailerAdapter.addAll(trailers);
        for (int i = 0; i < fragment.trailerAdapter.size(); i++) {
            View view = fragment.trailerAdapter.getView(i, null, fragment.listViewTrailers);
            if (view != null)
                fragment.listViewTrailers.addView(view);
        }
    }
}
