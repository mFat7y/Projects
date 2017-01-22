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

public class FetchReviewTask extends AsyncTask<String, Void, Review[]> {
    private final String LOG_TAG = FetchReviewTask.class.getSimpleName();
    private DetailActivity.DetailFragment fragment;

    public FetchReviewTask(DetailActivity.DetailFragment fragment) {
        this.fragment = fragment;
    }


    private Review[] getMoviesDataFromJson(String reviewsJsonStr, String id)
            throws JSONException {
        JSONObject reviewsJson = new JSONObject(reviewsJsonStr);
        JSONArray reviewsArray = reviewsJson.getJSONArray("results");
        Review[] resultreviews = new Review[reviewsArray.length()];
        for(int i = 0; i < reviewsArray.length(); i++) {
            JSONObject reviewObject = reviewsArray.getJSONObject(i);
            Review review = new Review(reviewObject, id);
            resultreviews[i] = review;
        }
        return resultreviews;
    }

    @Override
    protected Review[] doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String reviewsJsonStr = null;
        try {
            final String REVIEWS_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String APPID_PARAM = "api_key";

            Uri builtUri = Uri.parse(REVIEWS_BASE_URL).buildUpon()
                    .appendPath(params[0])
                    .appendEncodedPath("reviews")
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
            reviewsJsonStr = buffer.toString();
            Log.v(LOG_TAG, "Reviews JSON String" + reviewsJsonStr);

        } catch (IOException e) {
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
        try {
            fragment.reviewsMap.put(params[0], getMoviesDataFromJson(reviewsJsonStr, params[0]));
            return fragment.reviewsMap.get(params[0]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Review[] reviews) {
        fragment.reviewsAdapter.clear();
        fragment.reviewsAdapter.addAll(reviews);
        for (int i = 0; i < fragment.reviewsAdapter.size(); i++) {
            View view = fragment.reviewsAdapter.getView(i, null, fragment.listViewReviews);
            if(view != null)
                fragment.listViewReviews.addView(view);
        }
    }
}
