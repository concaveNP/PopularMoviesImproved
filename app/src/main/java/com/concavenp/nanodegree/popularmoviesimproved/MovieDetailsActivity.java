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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.concavenp.nanodegree.popularmoviesimproved.gson.MovieItems;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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

        // Translate JSON into GSON object
        Intent intent = getIntent();
        final String mParam1 = intent.getStringExtra(EXTRA_DATA);
        Gson gson = new Gson();
        MovieItems.MovieItem item = gson.fromJson(mParam1, MovieItems.MovieItem.class);

        // Get the movie backdrop UID from the GSON object
        ImageView backdropImageView = (ImageView) findViewById(R.id.movie_details_backdrop);
        String backdropURL = getResources().getString(R.string.base_url_backdrop_image_retrieval) + item.getBackdrop_path();
        Picasso.with(this).load(backdropURL).into(backdropImageView);

        // Create the new fragment and give it the movie data
        MovieDetailsFragment fragment = MovieDetailsFragment.newInstance(item);

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();

    }

}
