package com.fat7y.movies;

import android.database.Cursor;

import com.fat7y.movies.data.MovieContract;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie {
    public String id, name, date, overview, rating, poster, poster_small;
    public Boolean selected;
    public static final String link = "http://image.tmdb.org/t/p/";
    private final String size = "w780";
    public Movie (JSONObject movie) throws JSONException {
        id  = movie.getString("id");
        name  = movie.getString(MoviesFragment.OWM_TITLE);
        date  = movie.getString(MoviesFragment.OWM_DATE);
        rating  = movie.getString(MoviesFragment.OWM_VOTE);
        overview = movie.getString(MoviesFragment.OWM_DESCRIPTION);
        poster = link + size + movie.getString(MoviesFragment.OWM_POSTER);
        poster_small = link + "w185" + movie.getString(MoviesFragment.OWM_POSTER);
        selected = false;
    }
    public Movie (Cursor cursor) {
        id = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_KEY));
        name = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME));
        date = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DATE));
        rating = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING));
        overview = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW));
        poster = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER));
        poster_small = poster.replace("w780", "w185");
        selected = false;
    }
}
