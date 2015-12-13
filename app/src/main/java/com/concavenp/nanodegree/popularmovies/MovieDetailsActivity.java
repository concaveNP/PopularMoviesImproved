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

public class MovieDetailsActivity extends AppCompatActivity {

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

        // I've noticed some of the movies do not have proper release_date entries.  So, I'm checking for that here.
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

        Long longPopularity = Math.round(model.getPopularity());
        Integer popularity = longPopularity.intValue();
        String formattedPopularity = String.format(getResources().getString(R.string.formatted_popularity_string), popularity);
        popularityTextView.setText(formattedPopularity);

        Integer votes = model.getVote_count();
        String formattedVoteAverage = new DecimalFormat("#0.0").format(model.getVote_average());
        String formattedVotes = String.format(getResources().getString(R.string.formatted_votes_string), formattedVoteAverage, votes);
        votesTextView.setText(formattedVotes);


        synopsisTextView.setText(model.getOverview());
    }

}
