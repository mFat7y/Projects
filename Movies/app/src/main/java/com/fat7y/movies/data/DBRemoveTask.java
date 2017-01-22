package com.fat7y.movies.data;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.fat7y.movies.DetailActivity;
import com.fat7y.movies.Movie;

public class DBRemoveTask extends AsyncTask<Movie, Void, Void> {
    private final String LOG_TAG = DBInsertTask.class.getSimpleName();
    private DetailActivity.DetailFragment fragment;

    public  DBRemoveTask(DetailActivity.DetailFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected Void doInBackground(Movie... params) {
        try {
            DatabaseHelper dbhelper = new DatabaseHelper(fragment.getContext());
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            Movie movie = params[0];
            db.delete(MovieContract.MovieEntry.TABLE_NAME,
                    MovieContract.MovieEntry.MOVIE_KEY + "=?" , new String[] {movie.id});
            db.delete("REVIEWS", "ID=?" , new String[] {movie.id});
            db.delete("TRAILERS", "ID=?" , new String[] {movie.id});

            db.close();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error ", e);
        }
        return null;
    }
}