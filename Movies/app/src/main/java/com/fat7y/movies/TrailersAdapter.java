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

import java.util.List;

public class TrailersAdapter extends ArrayAdapter<Trailer> {
    private int size = 0;
    public TrailersAdapter(Activity context, List<Trailer> trailers) {
        super(context, 0, trailers);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Trailer trailer = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_trailer, parent, false);
        }
        try {
            TextView tv = (TextView) convertView.findViewById(R.id.trailerTextview);
            tv.setText(trailer.name);
            Button button = (Button) convertView.findViewById(R.id.trailer_button);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(trailer.link));
                    v.getContext().startActivity(intent);
                }
            });
            return convertView;
        }
        catch (Exception e) {
            return null;
        }
    }
    @Override
    public void addAll(Trailer[] trailers){
        super.addAll(trailers);
        if(trailers != null)
            size = trailers.length;
    }
    public int size() {
        return size;
    }
}
