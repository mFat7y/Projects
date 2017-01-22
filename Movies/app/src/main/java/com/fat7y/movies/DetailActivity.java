package com.fat7y.movies;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fat7y.movies.data.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends ActionBarActivity{
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_movies, new DetailFragment())
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class DetailFragment extends Fragment {
        private Movie movie;
        public ReviewsAdapter reviewsAdapter;
        public TrailersAdapter trailerAdapter;
        private Integer movieKey;
        public DetailFragment() {
            setHasOptionsMenu(true);
        }
        public Map<String, Review[]> reviewsMap;
        public Map<String, Trailer[]> trailersMap;
        public LinearLayout listViewTrailers;
        public LinearLayout listViewReviews;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            Intent intent = getActivity().getIntent();

            //add trailers and reviews to linear layouts
            listViewTrailers = (LinearLayout) rootView.findViewById(R.id.list_trailers);
            listViewReviews = (LinearLayout) rootView.findViewById(R.id.list_reviews);
            //link reviews and trailers to their movies using movie ID
            reviewsMap = new HashMap<>();
            trailersMap = new HashMap<>();

            if(intent != null && intent.hasExtra("key")) {  //phone mode
                movieKey = intent.getIntExtra("key", 0);
            }
            else{
                movieKey = getArguments().getInt("key");    //tablet mode
                if(movieKey == null) movieKey = 0;
            }
            movie = MainActivity.mp.get(movieKey);

            if(movie == null) {
                return rootView;
            }
            try {
                reviewsAdapter = new ReviewsAdapter(getActivity(), new ArrayList());
                trailerAdapter = new TrailersAdapter(getActivity(), new ArrayList());
                DatabaseHelper dbhelper = new DatabaseHelper(getContext());
                SQLiteDatabase db = dbhelper.getReadableDatabase();
                String queryR = "Select * from REVIEWS where ID = " + movie.id;
                String queryT = "Select * from TRAILERS where ID = " + movie.id;
                Cursor cursorR = db.rawQuery(queryR, null);
                Cursor cursorT = db.rawQuery(queryT, null);

                if (cursorR == null || cursorR.getCount() <= 0) {//movie must be fetched
                    FetchReviewTask moviesTask = new FetchReviewTask(this);
                    FetchTrailersTask trailerTask = new FetchTrailersTask(this);
                    moviesTask.execute(MainActivity.mp.get(movieKey).id);
                    trailerTask.execute(MainActivity.mp.get(movieKey).id);
                } else {                                        //movie is found in database
                    Trailer[] trailers = new Trailer[cursorT.getCount()];
                    Review[] reviews = new Review[cursorT.getCount()];
                    int i = 0;
                    while (cursorT.moveToNext() && cursorR.moveToNext()) {
                        Review review = new Review(cursorR);
                        Trailer trailer = new Trailer(cursorT);
                        trailers[i] = trailer;
                        reviews[i] = review;
                        i++;
                    }
                    trailerAdapter.clear();
                    trailerAdapter.addAll(trailers);
                    for (int j = 0; j < trailerAdapter.size(); j++) { //add trailers to LinearLayout
                        View view = trailerAdapter.getView(j, null, listViewTrailers);
                        if (view != null)
                            listViewTrailers.addView(view);
                    }
                    reviewsAdapter.clear();
                    reviewsAdapter.addAll(reviews);
                    for (int j = 0; j < reviewsAdapter.size(); j++) {  //add reviews to LinearLayout
                        View view = reviewsAdapter.getView(j, null, listViewReviews);
                        if (view != null)
                            listViewReviews.addView(view);
                    }
                }
                cursorR.close();
                cursorT.close();

                if (intent != null && movieKey != null) {
                    ScrollView scrollView = (ScrollView)rootView.findViewById(R.id.scrollView);
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                    scrollView.smoothScrollTo(0,0);

                    String query = "Select * from movies where movie_id = " + movie.id;
                    Cursor cursor = db.rawQuery(query, null);
                    movie.selected = !(cursor == null || cursor.getCount() <= 0);
                    db.close();
                    ((TextView) rootView.findViewById(R.id.movieName))
                            .setText(movie.name);
                    ((TextView) rootView.findViewById(R.id.overviewText))
                            .setText(movie.overview);
                    ((TextView) rootView.findViewById(R.id.ratingText))
                            .setText(movie.rating);
                    ((TextView) rootView.findViewById(R.id.dateText))
                            .setText(movie.date);
                    ((Button) rootView.findViewById(R.id.likeButton)).setSelected(movie.selected);
                    ((Button) rootView.findViewById(R.id.likeButton)).setOnClickListener(
                            new CustomClickListener(this, movie));
                    Picasso.with(getContext()).load(movie.poster_small).
                            error(getContext().getResources()   //if no poster found
                            .getDrawable(R.drawable.not_found)).
                            into((ImageView) rootView.findViewById(R.id.poster));
                }
            }catch(Exception e) {
                e.printStackTrace();
                return rootView;
            }
            finally {
                return rootView;
            }
        }
    }
}
