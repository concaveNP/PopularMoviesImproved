package com.concavenp.nanodegree.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import com.squareup.picasso.Picasso;

/**
 * Created by dave on 11/2/2015.
 */
public class MovieAdapter extends ArrayAdapter<MovieItems.MovieItem> {

    /**
     * The logging tag string to be associated with log data for this class
     */
    private static final String TAG = MovieAdapter.class.getSimpleName();

    /**
     * The base URL that will be used to query The Movie DB web service
     */
    // TODO: 11/5/2015 - Make the code smart enough to choose was width size to use
    private static final String BASE_URL = "http://image.tmdb.org/t/p/w185";

    /**
     * The model data that this adapter will provide to the associated view
     */
    private MovieItems mModel;

    public MovieAdapter(Context context, int resource, MovieItems model) {
        super(context, resource, model.getResults());

        mModel = model;
    }

    /**
     * Overloaded method to take a container {@code MovieItems} of the list of MovieItem(s).
     *
     * @param movieItems The GSON populated object that contains all of the received JSON data
     */
    public void add(MovieItems movieItems) {
        super.addAll(movieItems.getResults());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "Position: " + position);

        // The resulting value that will be returned
        View result = null;

        // TODO: 11/5/2015 - use a ViewHolder for caching

        LayoutInflater inflator = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        result = inflator.inflate(R.layout.movie_item, null);
        ImageButton movieButton = (ImageButton)result.findViewById(R.id.movie_ImageButton);

        // Get the movie poster UID from the GSON object
        String posterURL = BASE_URL + mModel.getResults().get(position).getPoster_path();
        Picasso.with(getContext()).load(posterURL).into(movieButton);

        return result;
    }

}

