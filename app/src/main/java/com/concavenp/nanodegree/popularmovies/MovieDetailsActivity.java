package com.concavenp.nanodegree.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_DATA = "json_movie_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.movie_deatils_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Translate JSON into GSON object
        Intent intent = getIntent();
        final String mParam1 = intent.getStringExtra(EXTRA_DATA);
        Gson gson = new Gson();
        MovieItems.MovieItem model = gson.fromJson(mParam1, MovieItems.MovieItem.class);

        ImageView posterImageView = (ImageView) findViewById(R.id.poster_ImageView);
        TextView titleTextView = (TextView) findViewById(R.id.title_TextView);
        TextView populatrityTextView = (TextView) findViewById(R.id.popularity_TextView);
        TextView synopsisTextView = (TextView) findViewById(R.id.synopsis_TextView);
        ImageView backdropImageView = (ImageView) findViewById(R.id.movie_details_backdrop);

        // Get the movie poster UID from the GSON object
        String posterURL = getResources().getString(R.string.base_url_image_retrieval) + model.getPoster_path();
        Picasso.with(this).load(posterURL).into(posterImageView);

        // Get the movie backdrop UID from the GSON object
        String backdropURL = getResources().getString(R.string.base_url_image_retrieval) + model.getBackdrop_path();
        Picasso.with(this).load(backdropURL).into(backdropImageView);

        titleTextView.setText(model.getTitle());
        populatrityTextView.setText(Double.toString(model.getPopularity()));
        synopsisTextView.setText(model.getOverview());
    }

}
