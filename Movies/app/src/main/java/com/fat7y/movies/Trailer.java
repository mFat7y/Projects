package com.fat7y.movies;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

public class Trailer {
    public String link, name, movieID;

    public Trailer(JSONObject trailer, String id) throws JSONException {
        movieID = new String(id);
        name = new String(trailer.getString("name"));
        link = "https://www.youtube.com/watch?v=" + trailer.getString("key");
    }
    public Trailer(Cursor cursorT) {
        name = cursorT.getString(cursorT.getColumnIndex("NAME"));
        link = cursorT.getString(cursorT.getColumnIndex("LINK"));
        movieID = cursorT.getString(cursorT.getColumnIndex("ID"));
    }
}