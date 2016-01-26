/*
 *     PopularMoviesImproved is an Android application that displays the most
 *     currently trending popular movies as listed by themoviedb.org
 *     website.
 *
 *     Copyright (C) 2016 authored by David A. Todd
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

package com.concavenp.nanodegree.popularmoviesimproved;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.concavenp.nanodegree.popularmoviesimproved.gson.TrailerItems;

/**
 * Created by dave on 1/9/2016.
 */
public class TrailerAdapter extends ArrayAdapter<TrailerItems.TrailerItem> {

    /**
     * The logging tag string to be associated with log data for this class
     */
    private static final String TAG = TrailerAdapter.class.getSimpleName();

    /**
     * The model data that this adapter will provide to the associated view
     */
    private TrailerItems mModel;

    /**
     * Constructor for the adapter that takes a {@code MovieItems} as the model container of
     * individual items.
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param model    The model data to represent in the ListView.
     */
    public TrailerAdapter(Context context, int resource, TrailerItems model) {
        super(context, resource, model.getResults());

        // This class is the model data that will be used by this adapter class
        setModel(model);
    }

    /**
     * Overloaded method to take a container {@code TrailerItems} of the list of TrailerItem(s).
     *
     * @param trailerItems The GSON object that contains all of the received JSON data
     */
    public void add(TrailerItems trailerItems) {
        super.addAll(trailerItems.getResults());
    }

    /**
     * Getter for the model data
     *
     * @return The model
     */
    private TrailerItems getModel() {
        return mModel;
    }

    /**
     * Setter for the model data
     *
     * @param model The model of data be set
     */
    private void setModel(TrailerItems model) {
        this.mModel = model;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "Position: " + position);

        View result;

        // Check to see if this view is being recycled and reuse it if so, otherwise create a new one.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            result = inflater.inflate(R.layout.trailers_item, parent, false);
        } else {
            // This view is being recycled
            result = convertView;
        }

        // Set the title of the trailer
        TextView textView = (TextView) result.findViewById(R.id.trailer_preview_name_TextView);
        String title = getModelItem(position).getName();
        if (title != null) {
            textView.setText(title);
        }

        /*
        // Add a click listener to the view in order for the user to get more details about a selected movie
        result.setOnClickListener(new View.OnClickListener() {
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

        */

        return result;
    }

    /**
     * Convenience method to get one specified {@code TrailerItem} from the model.
     *
     * @param index The index of desired item
     * @return A {@code TrailerItem}
     */
    private TrailerItems.TrailerItem getModelItem(int index) {
        TrailerItems.TrailerItem result;

        if (getModel() != null) {
//            result = getModel().getResults().get(index);
            result = getItem(index);
        } else {
            result = new TrailerItems.TrailerItem();
        }

        return result;
    }
}
