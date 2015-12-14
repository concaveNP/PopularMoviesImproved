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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

/**
 * Activity that is brought up when the user selects a movie item from the main activity.  For the
 * given movie it will display extra information about the movie.
 */
public class MovieDetailsActivity extends AppCompatActivity {

    /**
     * String used when creating the activity via intent.  This key will be used to retrieve the
     * JSON string data passed in that contains all of the details about the movie in question.
     */
    public static final String EXTRA_DATA = "json_movie_item";

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

        /*
        * Put this in for the next iteration of this app in order to allow voting.
        *
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        // Translate JSON into GSON object
        Intent intent = getIntent();
        final String mParam1 = intent.getStringExtra(EXTRA_DATA);
        Gson gson = new Gson();
        MovieItems.MovieItem model = gson.fromJson(mParam1, MovieItems.MovieItem.class);

        ImageView posterImageView = (ImageView) findViewById(R.id.poster_ImageView);
        TextView titleTextView = (TextView) findViewById(R.id.title_TextView);
        TextView popularityTextView = (TextView) findViewById(R.id.popularity_TextView);
        TextView votesTextView = (TextView) findViewById(R.id.votes_TextView);
        TextView synopsisTextView = (TextView) findViewById(R.id.synopsis_TextView);

        // Get the movie poster UID from the GSON object
        ImageView backdropImageView = (ImageView) findViewById(R.id.movie_details_backdrop);
        String posterURL = getResources().getString(R.string.base_url_image_retrieval) + model.getPoster_path();
        Picasso.with(this).load(posterURL).into(posterImageView);

        // Get the movie backdrop UID from the GSON object
        String backdropURL = getResources().getString(R.string.base_url_backdrop_image_retrieval) + model.getBackdrop_path();
        Picasso.with(this).load(backdropURL).into(backdropImageView);

        // The the title of the movie along with the year it was released.  Also, I've noticed some
        // of the movies do not have proper release_date entries.  So, I'm checking for that here.
        String releaseDate = model.getRelease_date();
        String year;
        if (releaseDate.length() >= 4) {
            year = model.getRelease_date().substring(0,4);
        } else {
            if (releaseDate.isEmpty()) {
                year = "unknown";
            } else {
                year = releaseDate;
            }
        }
        String formattedTitle = String.format(getResources().getString(R.string.formatted_movie_title), model.getTitle(), year);
        titleTextView.setText(formattedTitle);

        // Set the popularity stats of the movie in question
        Long longPopularity = Math.round(model.getPopularity());
        Integer popularity = longPopularity.intValue();
        String formattedPopularity = String.format(getResources().getString(R.string.formatted_popularity_string), popularity);
        popularityTextView.setText(formattedPopularity);

        // Set the user voting stats of the movie in question
        Integer votes = model.getVote_count();
        String formattedVoteAverage = new DecimalFormat("#0.0").format(model.getVote_average());
        String formattedVotes = String.format(getResources().getString(R.string.formatted_votes_string), formattedVoteAverage, votes);
        votesTextView.setText(formattedVotes);

        // Set the test displaying the movie synopsis
        synopsisTextView.setText(model.getOverview());
    }

}
