package com.fat7y.movies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
public class DetailActivity extends ActionBarActivity{ //Detail Activity
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sort_top) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class DetailFragment extends Fragment {
        private JSONObject movie;
        private ReviewsAdapter reviewsAdapter;
        private TrailersAdapter trailerAdapter;
        private Integer movieKey;
        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            setHasOptionsMenu(true);

            final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            Intent intent = getActivity().getIntent();
            Bundle extras = intent.getExtras();

            FetchReviewTask moviesTask = new FetchReviewTask();
            FetchTrailersTask trailerTask = new FetchTrailersTask();

            movieKey = intent.getIntExtra("key", 0);
            try {
                moviesTask.execute(MainActivity.mp.get(movieKey).getString("id"));
                trailerTask.execute(MainActivity.mp.get(movieKey).getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            reviewsAdapter =
                    new ReviewsAdapter(
                            getActivity(),
                            new ArrayList());

            trailerAdapter =
                    new TrailersAdapter(
                            getActivity(),
                            new ArrayList());


            if (intent != null && movieKey != null) {
                try {
                    ScrollView scrollView = (ScrollView)rootView.findViewById(R.id.scrollView);
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                    scrollView.smoothScrollTo(0,0);
                    movie = MainActivity.mp.get(movieKey);
                    ((TextView) rootView.findViewById(R.id.movieName))
                            .setText(movie.getString(MoviesFragment.OWM_TITLE));
                    ((TextView) rootView.findViewById(R.id.overviewText))
                            .setText(movie.getString(MoviesFragment.OWM_DESCRIPTION));
                    ((TextView) rootView.findViewById(R.id.ratingText))
                            .setText(movie.getString(MoviesFragment.OWM_VOTE));
                    ((TextView) rootView.findViewById(R.id.dateText))
                            .setText(movie.getString(MoviesFragment.OWM_DATE));
                    Picasso.with(getContext()).load(CustomAdapter.link + "w185" +
                            movie.getString(MoviesFragment.OWM_POSTER)).into(
                            (ImageView) rootView.findViewById(R.id.poster));

                    ListView listView = (ListView) rootView.findViewById(R.id.listView_detail);
                    listView.setAdapter(reviewsAdapter);
                    GridView gridView = (GridView) rootView.findViewById(R.id.grid_trailers);
                    gridView.setAdapter(trailerAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return rootView;
        }
        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.detailfragment, menu);

            MenuItem menuItem = menu.findItem(R.id.action_share);

            ShareActionProvider mShareActionProvider =
                    (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

            if (mShareActionProvider != null ) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            } else {
                Log.d(LOG_TAG, "Share Action Provider is null?");
            }
        }

        private Intent createShareForecastIntent() {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
         /*   shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    movieStr + FORECAST_SHARE_HASHTAG);*/
            return shareIntent;
        }



        public class FetchReviewTask extends AsyncTask<String, Void, JSONObject[]> {
            private final String LOG_TAG = FetchReviewTask.class.getSimpleName();

            private JSONObject[] getMoviesDataFromJson(String reviewsJsonStr)
                    throws JSONException {
                JSONObject reviewsJson = new JSONObject(reviewsJsonStr);
                JSONArray reviewsArray = reviewsJson.getJSONArray("results");
                JSONObject[] resultreviews = new JSONObject[reviewsArray.length()];
                for(int i = 0; i < reviewsArray.length(); i++) {
                    JSONObject reviewObject = reviewsArray.getJSONObject(i);
                    resultreviews[i] = reviewObject;
                }
                return resultreviews;
            }

            @Override
            protected JSONObject[] doInBackground(String... params) {
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                // Will contain the raw JSON response as a string.
                String reviewsJsonStr = null;
                String OPEN_MOVIE_API_KEY = "8e8d3f9effda8ce61536a1ecfba91f88";
                try {
                    final String REVIEWS_BASE_URL = "http://api.themoviedb.org/3/movie/";
                    final String APPID_PARAM = "api_key";

                    Uri builtUri = Uri.parse(REVIEWS_BASE_URL).buildUpon()
                            .appendPath(params[0])
                            .appendEncodedPath("reviews")
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
                    reviewsJsonStr = buffer.toString();
                    Log.v(LOG_TAG, "Reviews JSON String" + reviewsJsonStr);

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
                    return getMoviesDataFromJson(reviewsJsonStr);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(JSONObject[] movies) {
                reviewsAdapter.clear();
                reviewsAdapter.addAll(movies);
            }
        }

        public class FetchTrailersTask extends AsyncTask<String, Void, JSONObject[]> {
            private final String LOG_TAG = FetchReviewTask.class.getSimpleName();

            private JSONObject[] getMoviesDataFromJson(String reviewsJsonStr)
                    throws JSONException {
                JSONObject reviewsJson = new JSONObject(reviewsJsonStr);
                JSONArray reviewsArray = reviewsJson.getJSONArray("results");
                JSONObject[] resultreviews = new JSONObject[reviewsArray.length()];
                for(int i = 0; i < reviewsArray.length(); i++) {
                    JSONObject reviewObject = reviewsArray.getJSONObject(i);
                    resultreviews[i] = reviewObject;
                }
                return resultreviews;
            }

            @Override
            protected JSONObject[] doInBackground(String... params) {
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                // Will contain the raw JSON response as a string.
                String reviewsJsonStr = null;
                String OPEN_MOVIE_API_KEY = "8e8d3f9effda8ce61536a1ecfba91f88";
                try {
                    final String REVIEWS_BASE_URL = "http://api.themoviedb.org/3/movie/";
                    final String APPID_PARAM = "api_key";

                    Uri builtUri = Uri.parse(REVIEWS_BASE_URL).buildUpon()
                            .appendPath(params[0])
                            .appendEncodedPath("videos")
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
                    reviewsJsonStr = buffer.toString();
                    Log.v(LOG_TAG, "Reviews JSON String" + reviewsJsonStr);

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
                    return getMoviesDataFromJson(reviewsJsonStr);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(JSONObject[] movies) {
                trailerAdapter.clear();
                trailerAdapter.addAll(movies);
            }
        }

    }

}
