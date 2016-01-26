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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.concavenp.nanodegree.popularmoviesimproved.gson.GsonRequest;
import com.concavenp.nanodegree.popularmoviesimproved.gson.ReviewItems;

public class MovieReviewsActivity extends AppCompatActivity {

    /**
     * String used when creating the activity via intent.  This key will be used to retrieve the
     * movie id used to lookup all of the reviews for the movie.
     */
    public static final String EXTRA_DATA = "movie_id";
    /**
     * The logging tag string to be associated with log data for this class
     */
    private static final String TAG = MovieReviewsActivity.class.getSimpleName();
    private static final int DEFAULT_MOVIE_ID = -1;
    /**
     * A Volley queue used for managing web interface requests
     */
    private RequestQueue mRequestQueue;
    /**
     * The Adapter which will be used to populate the RecyclerView with Views.
     */
    private ReviewsAdapter mAdapter;
    private int mMovieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_reviews);

        // This activity is not the home, so show the back arrow.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mMovieId = intent.getIntExtra(EXTRA_DATA, DEFAULT_MOVIE_ID);

        // TODO: 1/23/2016 - error check for the DEFAULT_MOVIE_ID

        // Instantiate the RequestQueue.
        mRequestQueue = Volley.newRequestQueue(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.reviews_RecyclerView);
        mAdapter = new ReviewsAdapter();
        recyclerView.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                requestData(current_page);
            }
        });
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // Make the first web request for page one of the data
        requestData(1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method that will create a request object that will be added the Volley request queue for
     * processing.  The request will translate the JSON response data into a GSON populated object.
     * The adapter will then be given the new data which will in turn update the displayed listing
     * of movies to the user.
     */
    private void requestData(int page) {

        // TODO: 1/23/2016 - fix magic string
        String url =
                String.format(getResources().getString(R.string.base_url_review_request), mMovieId) +
                        "?" +
                        getResources().getString(R.string.part_url_api_key_dude) +
                        getResources().getString(R.string.THE_MOVIE_DB_API_TOKEN) +
                        "&page=" + page;

        // Request a string response from the provided URL.
        GsonRequest<ReviewItems> request = new GsonRequest<>(url, ReviewItems.class, null, new Response.Listener<ReviewItems>() {
            @Override
            public void onResponse(ReviewItems response) {

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

                // TODO: 1/23/2016 - verify the 'this' usage for makeText
                // Show a message to the user
                Toast.makeText(MovieReviewsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        mRequestQueue.add(request);
    }

}
