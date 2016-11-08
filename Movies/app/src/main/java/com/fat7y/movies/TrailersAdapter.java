package com.fat7y.movies;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by user on 11/5/2016.
 */
public class TrailersAdapter extends ArrayAdapter<JSONObject> {
    public TrailersAdapter(Activity context, List<JSONObject> reviews) {
        super(context, 0, reviews);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        JSONObject trailer = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_trailer, parent, false);
        }
        try {
            TextView tv = (TextView) convertView.findViewById(R.id.trailerTextview);
            tv.setText(trailer.getString("name"));
            Button button = (Button) convertView.findViewById(R.id.trailer_button);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + getItem(position).getString("key")));
                        v.getContext().startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return convertView;
        }
        catch (Exception e) {
            return null;
        }

    }
}
