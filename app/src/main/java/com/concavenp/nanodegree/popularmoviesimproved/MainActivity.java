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
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.concavenp.nanodegree.popularmoviesimproved.gson.MovieItems;
import com.google.gson.Gson;

/**
 * This is the main activity of the application.  It displays a fragment that contains a listing
 * of the movies found via an API request to the themoviedb.org website service.
 *
 * There is a settings menu option that is displayed that will open a Preferences activity for the
 * user to choose what data is requested in a web service.
 *
 * Development References:
 * - Creating a Fragment - http://developer.android.com/training/basics/fragments/creating.html
 * - Learning Android: Develop Mobile Apps Using Java and Eclipse - Chapter 8 Fragments
 */
public class MainActivity extends AppCompatActivity implements MovieDetailsFragment.OnFragmentInteractionListener, MovieListingFragment.OnMovieSelectionListener {

    private boolean mPhoneLayout = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // If the application has not run before then initialize the preference settings with default values
        if (savedInstanceState == null) {
            // These are the "general" preferences (its all this app has)
            PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        }

        // TODO: 1/24/2016 - fix comment
        // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, then we are a phone layout vs. a tablet.
        if (findViewById(R.id.main_frame) == null) {
            mPhoneLayout = false;
        }

        /*
        // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.fragment_container) != null) {
            // Create an instance of movie listing fragment
            MovieListingFragment listingFragment = new MovieListingFragment();

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.main_content, listingFragment).commit();
        }
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            // Create and start the settings activity
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);

            // For now, give the activity some extra parameters that will tell it to use a specific
            // PreferenceFragment when starting up.  These lines can be removed when the user
            // preference settings gets more complex and require displaying preference headers to the user.
            intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.GeneralPreferenceFragment.class.getName());
            intent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true);

            // Start the preference activity
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onMovieSelection(MovieItems.MovieItem item) {
        if (mPhoneLayout) {

            // Convert the GSON object back to a JSON string in order to pass to the activity
            Gson gson = new Gson();
            String json = gson.toJson(item);

            // Create and start the details activity along with passing it the Movie Item details information via JSON string
            Intent intent = new Intent(this, MovieDetailsActivity.class);
            intent.putExtra(MovieDetailsActivity.EXTRA_DATA, json);
            startActivity(intent);

        } else {

        }
    }
}
