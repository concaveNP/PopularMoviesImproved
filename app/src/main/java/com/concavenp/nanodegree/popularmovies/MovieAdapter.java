package com.concavenp.nanodegree.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dave on 11/2/2015.
 */
public class MovieAdapter extends ArrayAdapter<MovieItem> {

    /**
     * The logging tag string to be associated with log data for this class
     */
    private static final String TAG = MovieAdapter.class.getSimpleName();

    /**
     * The model data that this adapter will provide to the associated view
     */
    private final List<MovieItem> mModel;

    public MovieAdapter(Context context, int resource, List<MovieItem> model) {
        super(context, resource, model);

        mModel = model;
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
        TextView movieTitle = (TextView) result.findViewById(R.id.movie_TextView);

        movieButton.setImageResource(R.drawable.fight_club);
        movieTitle.setText("Fight Club");

        return result;
    }

}

