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
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.concavenp.nanodegree.popularmoviesimproved.gson.GsonRequest;
import com.concavenp.nanodegree.popularmoviesimproved.gson.TrailerItems;

/**
 * Created by dave on 1/19/2016.
 */
public class TrailersCard extends CardView {

    // The logging tag string to be associated with log data for this class
    private static final String TAG = TrailersCard.class.getSimpleName();

    // The is the string for the first trailer link.  It is initialized to null on purpose.
    private String mFirstTrailer = null;

    // A Volley queue used for managing web interface requests
    private RequestQueue mRequestQueue;

    /**
     * A default CardView constructor, but with the addition of initializing a Volley queue.
     *
     * @param context - View context we are in
     * @param attrs   - View attributes to use
     */
    public TrailersCard(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Instantiate the RequestQueue
        mRequestQueue = Volley.newRequestQueue(context);
    }

    /**
     * Allows for the clearing of all data within the CardView.
     */
    public void removeAllViews() {
        LinearLayout linearLayout = findViewById(R.id.trailers_list);
        linearLayout.removeAllViews();

        // Clear the first trailer
        mFirstTrailer = null;
    }

    /**
     * Method that will create a request object that will be added the Volley request queue for
     * processing.  The request will translate the JSON response data into a GSON populated object.
     * The adapter will then be given the new data which will in turn update the displayed listing
     * of movie trailers to the user.
     *
     * @param id - This themoviedb database index for the movie question
     */
    public void requestTrailersData(int id) {

        String url =
                String.format(getResources().getString(R.string.base_url_trailer_request), id) +
                        "?" +
                        getResources().getString(R.string.part_url_api_key_dude) +
                        getResources().getString(R.string.THE_MOVIE_DB_API_TOKEN);

        // Request a string response from the provided URL.
        final GsonRequest<TrailerItems> request = new GsonRequest<>(url, TrailerItems.class, null, new Response.Listener<TrailerItems>() {
            @Override
            public void onResponse(TrailerItems response) {

                processResponse(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = getResources().getString(R.string.service_request_failure_message);

                // Log the data
                Log.e(TAG, errorMessage + ": " + error);
                error.printStackTrace();

                // Show a message to the user
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        mRequestQueue.add(request);
    }

    /**
     * Processes the response back from the web request.  It will dynamically add views to the
     * card view for display.
     *
     * @param trailerItems - The list of trailers to be added
     */
    private void processResponse(TrailerItems trailerItems) {

        LinearLayout linearLayout = findViewById(R.id.trailers_list);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (TrailerItems.TrailerItem item : trailerItems.getResults()) {

            if (mFirstTrailer == null) {
                mFirstTrailer = getResources().getString(R.string.intent_youtube_string) + item.getKey();
            }

            View result = inflater.inflate(R.layout.trailers_item, linearLayout, false);

            // Listener that will open up the YouTube web site to the specified address
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.intent_youtube_string) + v.getTag())));
                }
            };

            // Add a listener for clicks
            ImageButton imageButton = result.findViewById(R.id.play_ImageButton);
            imageButton.setTag(item.getKey());
            imageButton.setOnClickListener(listener);

            // Set the title of the trailer also add a listener for clicks
            TextView textView = result.findViewById(R.id.trailer_preview_name_TextView);
            String title = item.getName();
            textView.setTag(item.getKey());
            textView.setOnClickListener(listener);

            // I've seen the title for various things turn up null, thus check for it.
            if (title != null) {
                textView.setText(title);
            }

            linearLayout.addView(result);
        }

    }

    /**
     * Allows the containing view to call this method in order to populate a sharing intent.
     *
     * @return The string web address to the first trailer
     */
    public String getFirstTrailer() {
        return mFirstTrailer;
    }

}
