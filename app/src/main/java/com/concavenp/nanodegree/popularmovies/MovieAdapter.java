package com.concavenp.nanodegree.popularmovies;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

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

        // This class is the model data that will be used by this adapter class
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

    /**
     * Getter for the view of one of the items in our data model.  This overridden method will
     * create an ImageView, if required, and populate via Picasso. The URL for the movie poster is
     * built from the {@code MovieItems} class that we populated via GSON.
     * <p>
     * Reference:
     * - This reference allowed me to see that my code needed the "setAdjustViewBounds(true)" in
     * order to resize the posters correctly.  Its took a lot of time to find this one out, but I
     * say someone mention it within this post:
     * http://stackoverflow.com/questions/21889735/resize-image-to-full-width-and-variable-height-with-picasso/22009875#22009875
     * <p>
     * {@inheritDoc}
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "Position: " + position);

        ImageView imageView;

        // Check to see if this view is being recycled and reuse it if so, otherwise create a new one.
        if (convertView == null) {
            imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_START);
            imageView.setAdjustViewBounds(true);
            imageView.setClickable(true);
            imageView.setLongClickable(true);
        }
        else {
            // This view is being recycled
            imageView = (ImageView) convertView;
        }

        // Get the movie poster UID from the GSON object
        String posterURL = BASE_URL + mModel.getResults().get(position).getPoster_path();
        Picasso.with(getContext()).load(posterURL).into(imageView);

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Put out the movie title to the user
                Toast.makeText(getContext(),mModel.getResults().get(position).getTitle(), Toast.LENGTH_SHORT).show();

                // This ImageView has consumed the long click
                return true;
            }
        });

        return imageView;
    }


}

