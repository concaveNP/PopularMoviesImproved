/*
 *     PopularMoviesImproved is an Android application that displays the most
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

package com.concavenp.nanodegree.popularmoviesimproved;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.concavenp.nanodegree.popularmoviesimproved.gson.GsonRequest;
import com.concavenp.nanodegree.popularmoviesimproved.gson.MovieItems;

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
     *
     */
    private String mSortOrder;

    private RecyclerView mRecyclerView;

    /**
     * The Adapter which will be used to populate the GridView with Views.
     */
    private MovieAdapter mAdapter;

    /**
     * Interface that will be used for the signalling of a movie selection
     */
    private OnMovieSelectionListener mListener;

    private EndlessRecyclerOnScrollListener mScrollListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieListingFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instantiate the RequestQueue.
        mRequestQueue = Volley.newRequestQueue(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Get the sort type that should be used when requesting data from the movie DB
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String newSortOrder = prefs.getString(getResources().getString(R.string.sorting_preference_type_key), getResources().getString(R.string.default_sorting_preference_value));

        // Check to see if the sort orders have changed
        if (!newSortOrder.equals(mSortOrder)) {

            // The order has changed, save off the new sort order
            mSortOrder = newSortOrder;

            // Data in the adapter and scroll listener must be cleared out
            mAdapter.clearData();
            mScrollListener.initValues();

            // Make the first web request for page one of the data
            requestData(1);

        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movieitem_grid, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.main_Movies_GridView);
        mAdapter = new MovieAdapter(mListener);
        mRecyclerView.setAdapter(mAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2); // TODO: 1/21/2016 - this should be driven by a resource value determined by phone/tablet
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mScrollListener = new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                requestData(current_page);
            }
        };
        mRecyclerView.addOnScrollListener(mScrollListener);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        return view;
    }

    /**
     * Method that will create a request object that will be added the Volley request queue for
     * processing.  The request will translate the JSON response data into a GSON populated object.
     * The adapter will then be given the new data which will in turn update the displayed listing
     * of movies to the user.
     */
    private void requestData(int page) {

        String url =
                getResources().getString(R.string.base_url_data_request) +
                        mSortOrder +
                        getResources().getString(R.string.part_url_api_key) +
                        getResources().getString(R.string.THE_MOVIE_DB_API_TOKEN) +
                        "&page=" + page;

        // Request a string response from the provided URL.
        GsonRequest<MovieItems> request = new GsonRequest<>(url, MovieItems.class, null, new Response.Listener<MovieItems>() {
            @Override
            public void onResponse(MovieItems response) {

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMovieSelectionListener) {
            mListener = (OnMovieSelectionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMovieSelectionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMovieSelectionListener {
        void onMovieSelection(MovieItems.MovieItem item);
    }


}
