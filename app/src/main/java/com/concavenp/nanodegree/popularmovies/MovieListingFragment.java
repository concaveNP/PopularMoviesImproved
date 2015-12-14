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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

/**
 * A fragment representing a list of MovieItem(s).
 * <p>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 */
public class MovieListingFragment extends Fragment {

    /**
     * The logging tag string to be associated with log data for this class
     */
    private static final String TAG = MovieListingFragment.class.getSimpleName();

    /**
     * A Volley queue used for managing web interface requests
     */
    private RequestQueue mRequestQueue;

    /**
     * This
     */
    private String mSortOrder;

    /**
     * The Adapter which will be used to populate the GridView with Views.
     */
    private MovieAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieListingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movieitem_grid, container, false);

        // Create the grid of views who's content will be driven by the adapter
        AbsListView gridView = (GridView)view.findViewById(R.id.main_Movies_GridView);
        mAdapter = new MovieAdapter(getActivity(), R.id.main_Movies_GridView, new MovieItems());
        gridView.setAdapter(mAdapter);

        // Instantiate the RequestQueue.
        mRequestQueue = Volley.newRequestQueue(getActivity());

        // Get the sort type that should be used when requesting data from the movie DB
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        mSortOrder = prefs.getString(getResources().getString(R.string.sorting_preference_type_key),getResources().getString(R.string.default_sorting_preference_value)) ;

        // Make the web request for data
        requestData();

        return view;
    }

    /**
     * Method that will create a request object that will be added the Volley request queue for
     * processing.  The request will translate the JSON response data into a GSON populated object.
     * The adapter will then be given the new data which will in turn update the displayed listing
     * of movies to the user.
     */
    private void requestData() {

        String url =
                getResources().getString(R.string.base_url_data_request) +
                        mSortOrder +
                        getResources().getString(R.string.part_url_api_key) +
                        getResources().getString(R.string.THE_MOVIE_DB_API_TOKEN);

        // Request a string response from the provided URL.
        GsonRequest<MovieItems> request = new GsonRequest<>(url, MovieItems.class, null, new Response.Listener<MovieItems>() {
            @Override
            public void onResponse(MovieItems response) {
                // First clear out any old data
                mAdapter.clear();

                // Add the new data
                mAdapter.add(response);
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

}
