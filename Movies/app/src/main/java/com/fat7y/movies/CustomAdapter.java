package com.fat7y.movies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Movie> {
    private static final String LOG_TAG = CustomAdapter.class.getSimpleName();
    public CustomAdapter(Activity activity, List<Movie> movies) {
        super(activity, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item_movies, parent, false);
        }
        try {
            ImageView iconView = (ImageView) convertView.findViewById(R.id.imageView);
            Picasso.with(getContext()).load(movie.poster).error(getContext().getResources()
                    .getDrawable(R.drawable.not_found)).into(iconView);
            return convertView;
        }
        catch (Exception e) {
            return null;
        }

    }
}
