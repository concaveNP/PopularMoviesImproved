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

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.concavenp.nanodegree.popularmoviesimproved.database.PopularMoviesContract;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

/**
 * Activity that is brought up when the user selects a movie item from the main activity.  For the
 * given movie it will display extra information about the movie.
 *
 * References:
 * - For CardView example:
 *      - http://arjunu.com/2015/10/android-recyclerview-with-different-cardviews/
 *
 */
public class MovieDetailsActivity extends AppCompatActivity {

    /**
     * String used when creating the activity via intent.  This key will be used to retrieve the
     * JSON string data passed in that contains all of the details about the movie in question.
     */
    public static final String EXTRA_DATA = "json_movie_item";

    /**
     * The logging tag string to be associated with log data for this class
     */
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    private static final int MAX_REVIEWS = 2;

    private static final int LOADER_ID = 92;

    private MovieItems.MovieItem mModel;

    private FloatingActionButton mFab;

    /**
     * The Adapter which will be used to populate the favorite star.
     */
    private SimpleCursorAdapter mAdapter;

    /**
     * A Volley queue used for managing web interface requests
     */
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.movie_details_toolbar);
        setSupportActionBar(toolbar);

        // Don't show a title (check for null via lint)
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // This activity is not the home, so show the back arrow.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Allow marking this movie as a favorite
        mFab = (FloatingActionButton) findViewById(R.id.favorite_fab);
        mFab.setTag(new Boolean(false));
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatingActionButton fab = (FloatingActionButton) view;

                // Get current state via view tag
                boolean favorite = (Boolean) fab.getTag();

                // Toggle state
                favorite = !favorite;
                fab.setTag(favorite);

                // Update the DB to reflect the favorite selection
                updateFavoriteMovieDB(favorite);

                // Set button image accordingly
                if (favorite) {
                    fab.setImageResource(android.R.drawable.star_on);
                    Snackbar.make(view, "Movie is now a favorite", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    fab.setImageResource(android.R.drawable.star_off);
                    Snackbar.make(view, "Movie is no longer a favorite", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

        // Translate JSON into GSON object
        Intent intent = getIntent();
        final String mParam1 = intent.getStringExtra(EXTRA_DATA);
        Gson gson = new Gson();
        mModel = gson.fromJson(mParam1, MovieItems.MovieItem.class);

        ImageView posterImageView = (ImageView) findViewById(R.id.poster_ImageView);
        TextView titleTextView = (TextView) findViewById(R.id.title_TextView);
        TextView popularityTextView = (TextView) findViewById(R.id.popularity_TextView);
        TextView votesTextView = (TextView) findViewById(R.id.votes_TextView);
        TextView synopsisTextView = (TextView) findViewById(R.id.synopsis_TextView);

        // Get the movie poster UID from the GSON object
        ImageView backdropImageView = (ImageView) findViewById(R.id.movie_details_backdrop);
        String posterURL = getResources().getString(R.string.base_url_image_retrieval) + mModel.getPoster_path();
        Picasso.with(this).load(posterURL).into(posterImageView);

        // Get the movie backdrop UID from the GSON object
        String backdropURL = getResources().getString(R.string.base_url_backdrop_image_retrieval) + mModel.getBackdrop_path();
        Picasso.with(this).load(backdropURL).into(backdropImageView);

        // The the title of the movie along with the year it was released.  Also, I've noticed some
        // of the movies do not have proper release_date entries.  So, I'm checking for that here.
        String releaseDate = mModel.getRelease_date();
        String year;
        if (releaseDate.length() >= 4) {
            year = mModel.getRelease_date().substring(0, 4);
        } else {
            if (releaseDate.isEmpty()) {
                year = "unknown";
            } else {
                year = releaseDate;
            }
        }
        String formattedTitle = String.format(getResources().getString(R.string.formatted_movie_title), mModel.getTitle(), year);
        titleTextView.setText(formattedTitle);

        // Set the popularity stats of the movie in question
        Long longPopularity = Math.round(mModel.getPopularity());
        Integer popularity = longPopularity.intValue();
        String formattedPopularity = String.format(getResources().getString(R.string.formatted_popularity_string), popularity);
        popularityTextView.setText(formattedPopularity);

        // Set the user voting stats of the movie in question
        Integer votes = mModel.getVote_count();
        String formattedVoteAverage = new DecimalFormat("#0.0").format(mModel.getVote_average());
        String formattedVotes = String.format(getResources().getString(R.string.formatted_votes_string), formattedVoteAverage, votes);
        votesTextView.setText(formattedVotes);

        // Set the test displaying the movie synopsis
        synopsisTextView.setText(mModel.getOverview());

        // Instantiate the RequestQueue
        mRequestQueue = Volley.newRequestQueue(this);

        // Have the loader retrieve data from the DB to correctly populate this movie as a favorite
        new LoadSearchTask().execute(mModel.getId());

        // Make the web request for trailer data
        requestTrailerData(mModel.getId());

        // Make the web request for reviews data
        requestReviewsData(mModel.getId());
    }

    private void updateFavoriteMovieDB(boolean favorite) {

        if (favorite) {
            ContentValues values = new ContentValues();
            values.put(PopularMoviesContract.FavoritesColumns.MOVIE_ID, mModel.getId());
            values.put(PopularMoviesContract.FavoritesColumns.POSTER_PATH, mModel.getPoster_path());

            String selection = PopularMoviesContract.FavoritesColumns.MOVIE_ID + " = ?";
            String[] selectionArgs =
                    {
                            Integer.toString(mModel.getId())
                    };

            // Add and entry in the JOIN table for this filter and newly added result
            //getContentResolver().update(PopularMoviesContract.FAVORITES_CONTENT_URI, values, selection, selectionArgs );
            getContentResolver().insert(PopularMoviesContract.FAVORITES_CONTENT_URI, values);
        } else {
            String selection = PopularMoviesContract.FavoritesColumns.MOVIE_ID + " = ?";
            String[] selectionArgs =
                    {
                            Integer.toString(mModel.getId())
                    };
            getContentResolver().delete(PopularMoviesContract.FAVORITES_CONTENT_URI, selection, selectionArgs);
        }

    }

    /**
     * Method that will create a request object that will be added the Volley request queue for
     * processing.  The request will translate the JSON response data into a GSON populated object.
     * The adapter will then be given the new data which will in turn update the displayed listing
     * of movie trailers to the user.
     *
     * @param id - This themoviedb database index for the movie question
     */
    private void requestTrailerData(int id) {

        String url =
                String.format(getResources().getString(R.string.base_url_trailer_request), id) +
                        "?" +
                        getResources().getString(R.string.part_url_api_key_dude) +
                        getResources().getString(R.string.THE_MOVIE_DB_API_TOKEN);

        // Request a string response from the provided URL.
        final GsonRequest<TrailerItems> request = new GsonRequest<>(url, TrailerItems.class, null, new Response.Listener<TrailerItems>() {
            @Override
            public void onResponse(TrailerItems response) {
                GridLayout grid = (GridLayout) findViewById(R.id.gridLayout);

                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                for (TrailerItems.TrailerItem item : response.getResults()) {

                    View result = inflater.inflate(R.layout.trailers_item, grid, false);

                    ImageButton imageButton = (ImageButton) result.findViewById(R.id.play_ImageButton);
                    imageButton.setTag(item.getKey());
                    imageButton.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {
                                                           startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + v.getTag())));
                                                       }
                                                   }
                    );

                    // Set the title of the trailer
                    TextView textView = (TextView) result.findViewById(R.id.trailer_preview_name_TextView);
                    String title = item.getName();

                    // I've seen the title for various things turn up null, thus check for it.
                    if (title != null) {
                        textView.setText(title);
                    }

                    GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                    param.columnSpec = GridLayout.spec(0, 2);
                    result.setLayoutParams(param);

                    grid.addView(result);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = getResources().getString(R.string.service_request_failure_message);

                // Log the data
                Log.e(TAG, errorMessage + ": " + error);
                error.printStackTrace();

                // Show a message to the user
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        mRequestQueue.add(request);
    }

    private void requestReviewsData(int id) {

        String url =
                String.format(getResources().getString(R.string.base_url_review_request), id) +
                        "?" +
                        getResources().getString(R.string.part_url_api_key_dude) +
                        getResources().getString(R.string.THE_MOVIE_DB_API_TOKEN);

        // Request a string response from the provided URL.
        final GsonRequest<ReviewItems> request = new GsonRequest<>(url, ReviewItems.class, null, new Response.Listener<ReviewItems>() {
            @Override
            public void onResponse(ReviewItems response) {
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.short_review_list);

                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                for (ReviewItems.ReviewItem item : response.getResults()) {

                    View result = inflater.inflate(R.layout.review_item, linearLayout, false);

                    // Set the author of the review
                    TextView authorTextView = (TextView) result.findViewById(R.id.author_TextView);
                    authorTextView.setText(item.getAuthor());

                    // Set the content of the review
                    TextView contentTextView = (TextView) result.findViewById(R.id.content_TextView);
                    contentTextView.setText(item.getContent());

                    linearLayout.addView(result);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = getResources().getString(R.string.service_request_failure_message);

                // Log the data
                Log.e(TAG, errorMessage + ": " + error);
                error.printStackTrace();

                // Show a message to the user
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        mRequestQueue.add(request);
    }

    private class LoadSearchTask extends AsyncTask<Integer, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Integer... params) {
            Cursor cursor = null;

            // proper SQL syntax for us.
            SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();

            // Set the table we're querying.
            qBuilder.setTables(PopularMoviesContract.DB_FAVORITES_TABLE);

            // TODO: 1/18/2016 - null gives everything, but we don't need it
            String[] projection = null;

            String selection =
                    PopularMoviesContract.DB_FAVORITES_TABLE + "." + PopularMoviesContract.FavoritesColumns.MOVIE_ID + " = ?";

            String[] selectionArgs =
                    {
                            params[0].toString()
                    };

            // Build a query to see if the name has an entry in the filters table
            cursor = getApplicationContext().getContentResolver().query(PopularMoviesContract.FAVORITES_CONTENT_URI, projection, selection, selectionArgs, PopularMoviesContract.FAVORITES_DEFAULT_SORT);

            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            // Check to see if this movie is already a favorite
            if ((cursor != null) && (cursor.getCount() > 0)) {
                // Yes, this is a favorite
                mFab.setTag(new Boolean(true));

                // Set the URL of the image that should be loaded into this view, and
                // specify the ImageLoader that will be used to make the request.
                mFab.setImageResource(android.R.drawable.star_on);
            } else {
                // No, this is NOT a favorite
                mFab.setTag(new Boolean(false));

                // Set the URL of the image that should be loaded into this view, and
                // specify the ImageLoader that will be used to make the request.
                mFab.setImageResource(android.R.drawable.star_off);
            }
        }

    }

}
