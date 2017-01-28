package com.fat7y.movies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ReviewsAdapter extends ArrayAdapter<Review> {
    private int size = 0;
    public ReviewsAdapter(Activity context, List<Review> reviews) {
        super(context, 0, reviews);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Review review = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_reviews, parent, false);
        }
        try {
            TextView tv = (TextView) convertView.findViewById(R.id.reviewTextview);
            tv.setText((position + 1) + ". " + review.author + ":\n" + review.content);
            return convertView;
        }
        catch (Exception e) {
            return null;
        }
    }
    @Override
    public void addAll(Review[] reviews){
        super.addAll(reviews);
        if(reviews != null)
            size = reviews.length;
    }
    public int size() {
        return size;
    }
}
