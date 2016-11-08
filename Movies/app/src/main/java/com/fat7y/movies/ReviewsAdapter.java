package com.fat7y.movies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by user on 10/29/2016.
 */
public class ReviewsAdapter extends ArrayAdapter<JSONObject> {
    public ReviewsAdapter(Activity context, List<JSONObject> reviews) {
        super(context, 0, reviews);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        JSONObject review = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_reviews, parent, false);
        }
        try {
            TextView tv = (TextView) convertView.findViewById(R.id.reviewTextview);
            tv.setText(review.getString("author") + "\n" + review.get("content"));
            return convertView;
        }
        catch (Exception e) {
            return null;
        }

    }
}
