package com.fat7y.movies.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.fat7y.movies.DetailActivity;
import com.fat7y.movies.Movie;
import com.fat7y.movies.Review;
import com.fat7y.movies.Trailer;

public class DBInsertTask extends AsyncTask<Movie, Void, Void> {
    private final String LOG_TAG = DBInsertTask.class.getSimpleName();
    private DetailActivity.DetailFragment fragment;

    public  DBInsertTask(DetailActivity.DetailFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected Void doInBackground(Movie... params) {

        DatabaseHelper dbhelper = new DatabaseHelper(fragment.getContext());
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        try {

            ContentValues movieValues = new ContentValues();
            Movie movie = params[0];
            movieValues.put(MovieContract.MovieEntry.MOVIE_KEY, movie.id);
            movieValues.put(MovieContract.MovieEntry.COLUMN_NAME, movie.name);
            movieValues.put(MovieContract.MovieEntry.COLUMN_RATING, movie.rating);
            movieValues.put(MovieContract.MovieEntry.COLUMN_DATE, movie.date);
            movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.overview);
            movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER, movie.poster);
            db.insert(MovieContract.MovieEntry.TABLE_NAME, null, movieValues);

            Review[] reviews = fragment.reviewsMap.get(movie.id);
            for(Review review: reviews) {
                movieValues = new ContentValues();
                movieValues.put("CONTENT", review.content);
                movieValues.put("AUTHOR", review.author);
                movieValues.put("ID", movie.id);
                db.insert("REVIEWS", null, movieValues);
            }

            Trailer[] trailers = fragment.trailersMap.get(movie.id);
            for(Trailer trailer: trailers) {
                movieValues = new ContentValues();
                movieValues.put("LINK", trailer.link);
                movieValues.put("NAME", trailer.name);
                movieValues.put("ID", movie.id);
                db.insert("TRAILERS", null, movieValues);
            }
            db.close();
        } catch (Exception e) {
            db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS REVIEWS");
            db.execSQL("DROP TABLE IF EXISTS TRAILERS");
            Log.e(LOG_TAG, "Error ", e);
        }
        return null;
    }
}
