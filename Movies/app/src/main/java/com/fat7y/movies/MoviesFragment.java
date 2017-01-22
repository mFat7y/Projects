package com.fat7y.movies;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.fat7y.movies.data.DatabaseHelper;

import java.util.ArrayList;

public class MoviesFragment extends Fragment {
    public final static String OWM_TITLE = "title";
    public final static String OWM_RESULT = "results";
    public final static String OWM_POSTER = "poster_path";
    public final static String OWM_DESCRIPTION = "overview";
    public final static String OWM_DATE = "release_date";
    public final static String OWM_VOTE = "vote_average";
    public static String lastState = "popular";
    private CustomAdapter moviesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        moviesAdapter = new CustomAdapter(getActivity(), new ArrayList<Movie>());
    }

    @Override
    public void onStart () {
        super.onStart();
        if(!MainActivity.haveNetworkConnection(getActivity())){
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            lastState = "favourite";
        }
        updateMovies(lastState);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh) {
            updateMovies(lastState);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void requestUpdate() {
        if(lastState.equals("favourite")) {
            updateMovies(lastState);
        }
    }

    public void updateMovies(String sortBy) {
        lastState = sortBy;
        if(lastState.equals("favourite")) {
            DBFetchTask dbFetchTask = new DBFetchTask();
            dbFetchTask.execute();
        }
        else {
            FetchMoviesTask moviesTask = new FetchMoviesTask(moviesAdapter);
            moviesTask.execute(sortBy);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridView_movies);

        gridView.setAdapter(moviesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((Callback) getActivity()).onItemSelected(position);
            }
        });
        return rootView;
    }
    public class DBFetchTask extends AsyncTask<Void, Void, Movie[]> {
        @Override
        protected Movie[] doInBackground(Void... params) {  //search in database for favourites
            DatabaseHelper dbhelper = new DatabaseHelper(getContext());
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            String query = "Select * from movies";
            Cursor cursor = db.rawQuery(query, null);
            Movie[] movies = new Movie[cursor.getCount()];
            int i = 0;
            while(cursor.moveToNext()) {
                Movie movie = new Movie(cursor);
                MainActivity.mp.put(i, movie);
                movies[i++] = movie;
            }
            return movies;
        }
        @Override
        protected void onPostExecute(Movie[] movies) {
            try{
                moviesAdapter.clear();
                moviesAdapter.addAll(movies);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    public interface Callback {     //link detail fragment with MainActivity
        public void onItemSelected(int position);
    }
    public interface CallbackUpdate {
        public void requestUpdate();
    }
}
