package com.fat7y.movies;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fat7y.movies.data.DBInsertTask;
import com.fat7y.movies.data.DBRemoveTask;

public class CustomClickListener implements View.OnClickListener {
    private DetailActivity.DetailFragment fragment;
    private Movie movie;
    public CustomClickListener(DetailActivity.DetailFragment fragment, Movie movie) {
        this.fragment = fragment;
        this.movie = movie;
    }
    @Override
    public void onClick(View v) {
        movie.selected = !movie.selected;
        ((Button)v).setSelected(movie.selected);
        if(movie.selected) {    //like
            DBInsertTask insert = new DBInsertTask(fragment);
            insert.execute(movie);
        }
        else {  //unlike
            DBRemoveTask remove = new DBRemoveTask(fragment);
            remove.execute(movie);
        }
        Toast.makeText(fragment.getContext(), movie.name + " is " +
                (movie.selected ?  "added to" : "removed from") +
                " Favourites", Toast.LENGTH_SHORT).show();
        try{
            ((MoviesFragment.CallbackUpdate)fragment.getActivity()).requestUpdate();
        }
        catch(Exception e) {    //not a tablet
            e.printStackTrace();
        }

    }
}
