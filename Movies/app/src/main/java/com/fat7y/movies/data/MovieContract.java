package com.fat7y.movies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.fat7y.movies.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movies";
        public static final String MOVIE_KEY = "movie_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RATING = "vote_average";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_POSTER = "poster";
    }
}
