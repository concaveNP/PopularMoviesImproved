/*
 *     PopularMovies is an Android application that displays the most
 *     currently trending popular movies as listed by themoviedb.org
 *     website.
 *
 *     Copyright (C) 2015 authored by David A. Todd
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     You can contact the author via https://github.com/concaveNP
 */

package com.concavenp.nanodegree.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

/**
 * This adapter class is used for adapting a {@code MovieItem} for views displayed within
 * the list of movies in the {@code MovieListingFragment}.
 *
 * Items are passed into the class when results are retrieved from a RESTful call.  The model data
 * given to the class is in the form of GSON objects that have been built from returned JSON data.
 *
 * This adapter establishes a link between the displayed movie item view and bringing up details
 * about the movie via a OnClickListener on view.  Clicking it will transition to a movie details
 * activity {@code MovieDetailsActivity}.
 */
class MovieAdapter extends ArrayAdapter<MovieItems.MovieItem> {

    /**
     * The logging tag string to be associated with log data for this class
     */
    private static final String TAG = MovieAdapter.class.getSimpleName();

    /**
     * The model data that this adapter will provide to the associated view
     */
    private MovieItems mModel;

    /**
     * Constructor for the adapter that takes a {@code MovieItems} as the model container of
     * individual items.
     *
     * @param context The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param model The model data to represent in the ListView.
     */
    public MovieAdapter(Context context, int resource, MovieItems model) {
        super(context, resource, model.getResults());

        // This class is the model data that will be used by this adapter class
        setModel(model);
    }

    /**
     * Overloaded method to take a container {@code MovieItems} of the list of MovieItem(s).
     *
     * @param movieItems The GSON object that contains all of the received JSON data
     */
    public void add(MovieItems movieItems) {
        super.addAll(movieItems.getResults());
    }

    /**
     * Getter for the model data
     *
     * @return The model
     */
    private MovieItems getModel() {
        return mModel;
    }

    /**
     * Setter for the model data
     *
     * @param model The model of data be set
     */
    private void setModel(MovieItems model) {
        this.mModel = model;
    }

    /**
     * Getter for the view of one of the items in our data model.  This overridden method will
     * create an ImageView, if required, and populate via Picasso. The URL for the movie poster is
     * built from the {@code MovieItems} class that we populated via GSON.
     * <p>
     * Reference:
     * - This reference allowed me to see that my code needed the "setAdjustViewBounds(true)" in
     * order to resize the posters correctly.  Its took a lot of time to find this one out, but I
     * saw someone mention it within this post:
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
        String poster = getModelItem(position).getPoster_path();
        if (poster != null) {
            String posterURL = getContext().getResources().getString(R.string.base_url_image_retrieval) + poster;
            Picasso.with(getContext()).load(posterURL).into(imageView);
        }

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Put out the movie title to the user
                Toast.makeText(getContext(),getModelItem(position).getTitle(), Toast.LENGTH_SHORT).show();

                // This ImageView has consumed the long click
                return true;
            }
        });

        // Add a click listener to the view in order for the user to get more details about a selected movie
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Convert the GSON object back to a JSON string in order to pass to the activity
                Gson gson = new Gson();
                String json = gson.toJson(getModelItem(position));

                // Create and start the details activity along with passing it the Movie Item details information via JSON string
                Context context = view.getContext();
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra(MovieDetailsActivity.EXTRA_DATA, json);
                context.startActivity(intent);
            }
        });

        return imageView;
    }

    /**
     * Convenience method to get one specified {@code MovieItem} from the model.
     *
     * @param index The index of desired item
     * @return A {@code MovieItem}
     */
    private MovieItems.MovieItem getModelItem(int index) {
        MovieItems.MovieItem result;

        if (getModel() != null) {
            result = getModel().getResults().get(index);
        }
        else {
            result = new MovieItems.MovieItem();
        }

        return result;
    }

}

