package com.fat7y.movies;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

public class Review {
    public String author, content, movieID;

    public Review(JSONObject review, String id) throws JSONException {
        movieID = new String(id);
        author = review.getString("author");
        content = review.getString("content");
    }

    public Review(Cursor cursorR) {
        author = cursorR.getString(cursorR.getColumnIndex("AUTHOR"));
        content = cursorR.getString(cursorR.getColumnIndex("CONTENT"));
        movieID = cursorR.getString(cursorR.getColumnIndex("ID"));
    }
    public Review() {}
}
