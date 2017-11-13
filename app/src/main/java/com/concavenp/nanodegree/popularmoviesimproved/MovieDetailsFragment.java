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

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.concavenp.nanodegree.popularmoviesimproved.database.PopularMoviesContract;
import com.concavenp.nanodegree.popularmoviesimproved.gson.MovieItems;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 * <p/>
 * Reference:
 * - ActionBar items:
 * - http://www.grokkingandroid.com/adding-action-items-from-within-fragments/
 * - Intents:
 * - http://android-developers.blogspot.com/2012/02/share-with-intents.html
 * - http://developer.android.com/training/sharing/send.html
 * - Formatted text strings:
 * - (http://alvinalexander.com/blog/post/java/use-string-format-java-string-output)
 */
public class MovieDetailsFragment extends Fragment {

    // The fragment initialization parameter name used to store data as a argument
    private static final String ARG_MOVIE_ITEM = "movie_item";

    // The star button that allows the user to choose this movie as a favorite
    private ImageButton mFavoriteButton;

    // This flipper allows the content of the fragment to show the movie details or a message to
    // the user telling them to make a movie selection in order for details to be seen.
    private ViewFlipper mFlipper;

    // The model data that the details are populated with.  Defaulted to null on purpose.
    private MovieItems.MovieItem mModel = null;

    // The CardView that shows all of the trails.  It is a field here because I need the first
    // trailer entry from it in order to populate the share intent.
    private TrailersCard mTrailersCard;

    // The Adapter which will be used to populate the favorite star.
    private SimpleCursorAdapter mAdapter;

    /**
     * A required empty constructor
     */
    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param item The is a MovieItems.MovieItem object of GSON data
     * @return A new instance of fragment MovieDetailsFragment.
     */
    public static MovieDetailsFragment newInstance(MovieItems.MovieItem item) {

        // Convert the GSON object back to a JSON string in order to pass as a string param
        Gson gson = new Gson();
        String json = gson.toJson(item);

        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MOVIE_ITEM, json);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            // Get the JSON string
            String json = getArguments().getString(ARG_MOVIE_ITEM);

            // Translate JSON into GSON object
            if (json != null) {
                Gson gson = new Gson();
                mModel = gson.fromJson(json, MovieItems.MovieItem.class);
            }

        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Need to display the share trailer action bar icon
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        // Save off the flipper for use in decided which view to show
        mFlipper = view.findViewById(R.id.fragment_movie_ViewFlipper);

        return view;
    }

    @Override
    public void onStart() {

        super.onStart();

        Bundle args = getArguments();

        if (args != null) {

            // Get the JSON string
            String json = args.getString(ARG_MOVIE_ITEM);

            // Translate JSON into GSON object
            Gson gson = new Gson();
            MovieItems.MovieItem item = gson.fromJson(json, MovieItems.MovieItem.class);

            // Set movie details based on argument passed in
            updateMovieDetailInfo(item);

        } else if (mModel != null) {

            // Set article based on saved instance state defined during onCreateView
            updateMovieDetailInfo(mModel);

        } else {
            // There is no data to display so tell the user to choose something
            mFlipper.setDisplayedChild(mFlipper.indexOfChild(mFlipper.findViewById(R.id.fragment_movie_TextView)));
            setMenuVisibility(false);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.share_trailer, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {

            case R.id.share_trailer_item: {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mTrailersCard.getFirstTrailer());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share movie link"));

                return true;

            }
            default: {
                return super.onOptionsItemSelected(item);
            }

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Convert the GSON object back to a JSON string in order to pass to the activity
        Gson gson = new Gson();
        String json = gson.toJson(mModel);

        // Save the current article selection in case we need to recreate the fragment
        outState.putString(ARG_MOVIE_ITEM, json);
    }

    /**
     * Method that allows the model data to be updated
     *
     * @param item - The new movie details to set as the model and update the display accordingly
     */
    public void updateMovieDetailInfo(MovieItems.MovieItem item) {

        mModel = item;

        // If there is model data then show the details otherwise tell the user to choose something
        if (mModel != null) {
            mFlipper.setDisplayedChild(mFlipper.indexOfChild(mFlipper.findViewById(R.id.content_movie_FrameLayout)));
            setMenuVisibility(true);
        } else {
            mFlipper.setDisplayedChild(mFlipper.indexOfChild(mFlipper.findViewById(R.id.fragment_movie_TextView)));
            setMenuVisibility(false);
        }

        // Allow marking this movie as a favorite
        mFavoriteButton = getActivity().findViewById(R.id.imageButton);
        mFavoriteButton.setTag(Boolean.FALSE);
        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton imageButton = (ImageButton) view;

                // Get current state via view tag
                boolean favorite = (Boolean) imageButton.getTag();

                // Toggle state
                favorite = !favorite;
                imageButton.setTag(favorite);

                // Update the DB to reflect the favorite selection
                updateFavoriteMovieDB(favorite);

                // Set button image accordingly
                if (favorite) {
                    imageButton.setImageResource(android.R.drawable.btn_star_big_on);
                    Snackbar.make(view, "Movie is now a favorite", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    imageButton.setImageResource(android.R.drawable.btn_star_big_off);
                    Snackbar.make(view, "Movie is no longer a favorite", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

        // Spawn off thread to retrieve data from the DB to correctly populate this movie as a favorite
        new LoadSearchTask().execute(mModel.getId());

        ImageView posterImageView = getActivity().findViewById(R.id.poster_ImageView);
        TextView titleTextView = getActivity().findViewById(R.id.title_TextView);
        TextView popularityTextView = getActivity().findViewById(R.id.popularity_TextView);
        TextView votesTextView = getActivity().findViewById(R.id.votes_TextView);
        TextView synopsisTextView = getActivity().findViewById(R.id.synopsis_TextView);

        // Get the movie poster UID from the GSON object
        String posterURL = getResources().getString(R.string.base_url_image_retrieval) + mModel.getPoster_path();
        Picasso.with(getActivity()).load(posterURL).into(posterImageView);

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

        // Make the web request for trailer data
        mTrailersCard = getActivity().findViewById(R.id.trailers_cardview);
        mTrailersCard.removeAllViews();
        mTrailersCard.requestTrailersData(mModel.getId());

        // Make the web request for reviews data
        ReviewsCard reviewsCard = getActivity().findViewById(R.id.reviews_cardview);
        reviewsCard.removeAllViews();
        reviewsCard.requestReviewsData(mModel.getId());
    }

    /**
     * Updates the DB according to the favorite state the user has chosen.
     *
     * @param favorite - True if a favorite, false otherwise
     */
    private void updateFavoriteMovieDB(boolean favorite) {

        // If it is a favorite then put an entry in the DB table, otherwise remove the
        // entry from the table.
        if (favorite) {

            // Convert the GSON object back to a JSON string in order to pass to the activity
            Gson gson = new Gson();
            String json = gson.toJson(mModel);

            ContentValues values = new ContentValues();
            values.put(PopularMoviesContract.FavoritesColumns.MOVIE_ID, mModel.getId());
            values.put(PopularMoviesContract.FavoritesColumns.JSON, json);

            getContext().getContentResolver().insert(PopularMoviesContract.FAVORITES_CONTENT_URI, values);

        } else {

            String selection = PopularMoviesContract.FavoritesColumns.MOVIE_ID + " = ?";
            String[] selectionArgs =
                    {
                            Integer.toString(mModel.getId())
                    };
            getContext().getContentResolver().delete(PopularMoviesContract.FAVORITES_CONTENT_URI, selection, selectionArgs);

        }

    }

    private class LoadSearchTask extends AsyncTask<Integer, Void, Cursor> {

        /**
         * The background work will query the DB to see if the movie of interest is a favorite or not.
         *
         * @param params - Integer is the movie ID
         * @return The results from the DB query
         */
        @Override
        protected Cursor doInBackground(Integer... params) {
            Cursor cursor;

            // proper SQL syntax for us.
            SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();

            // Set the table we're querying.
            qBuilder.setTables(PopularMoviesContract.DB_FAVORITES_TABLE);

            // Null gives everything.  This can potentially be updated for a little more smarts.
            String[] projection = null;

            String selection =
                    PopularMoviesContract.DB_FAVORITES_TABLE + "." + PopularMoviesContract.FavoritesColumns.MOVIE_ID + " = ?";

            String[] selectionArgs =
                    {
                            params[0].toString()
                    };

            // Build a query to see if the name has an entry in the filters table
            cursor = getContext().getContentResolver().query(PopularMoviesContract.FAVORITES_CONTENT_URI,
                    projection, selection, selectionArgs, PopularMoviesContract.FAVORITES_DEFAULT_SORT);

            return cursor;
        }

        /**
         * This will change the star to reflect the favorite state.  This just checks to see if
         * there is any data found from the query.  If something was found then this is a favorite.
         *
         * @param cursor - The DB cursor of resulting data
         */
        @Override
        protected void onPostExecute(Cursor cursor) {

            // Check to see if this movie is already a favorite
            if ((cursor != null) && (cursor.getCount() > 0)) {

                // Yes, this is a favorite
                mFavoriteButton.setTag(Boolean.TRUE);

                // Set the URL of the image that should be loaded into this view, and
                // specify the ImageLoader that will be used to make the request.
                mFavoriteButton.setImageResource(android.R.drawable.btn_star_big_on);

            } else {

                // No, this is NOT a favorite
                mFavoriteButton.setTag(Boolean.FALSE);

                // Set the URL of the image that should be loaded into this view, and
                // specify the ImageLoader that will be used to make the request.
                mFavoriteButton.setImageResource(android.R.drawable.btn_star_big_off);

            }
        }

    }

}
