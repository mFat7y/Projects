package com.fat7y.movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fat7y.movies.data.MovieContract.MovieEntry;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "Movies.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Main DB table contains Movies details but for reviews and trailers
        String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry.MOVIE_KEY + " TEXT PRIMARY KEY," +
                MovieEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RATING + " TEXT, " +
                MovieEntry.COLUMN_DATE + " TEXT, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieEntry.COLUMN_POSTER + " TEXT" +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
        //reviews table
        SQL_CREATE_MOVIES_TABLE = "CREATE TABLE REVIEWS (" +
                "CONTENT TEXT PRIMARY KEY," +
                "AUTHOR TEXT NOT NULL, " +
                "ID TEXT NOT NULL " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
        //trailers table
        SQL_CREATE_MOVIES_TABLE = "CREATE TABLE TRAILERS (" +
                "LINK TEXT PRIMARY KEY," +
                "NAME TEXT NOT NULL, " +
                "ID TEXT NOT NULL " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS REVIEWS");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS TRAILERS");
        onCreate(sqLiteDatabase);
    }
}
