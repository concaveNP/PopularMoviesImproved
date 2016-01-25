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
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.concavenp.nanodegree.popularmoviesimproved.gson.MovieItems;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * This adapter class is used for adapting a {@code MovieItem} for views displayed within
 * the list of movies in the {@code MovieListingFragment}.
 *
 * Items are passed into the class when results are retrieved from a RESTful call.  The model data
 * given to the class is in the form of GSON objects that have been built from returned JSON data.
 *
 * This adapter establishes a link between the displayed movie item view and bringing up details
 * about the movie via a OnClickListener on view.  Clicking it will transition to a movie details
 * activity {@code MovieDetailsActivity}.
 */
class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    /**
     * The logging tag string to be associated with log data for this class
     */
    private static final String TAG = MovieAdapter.class.getSimpleName();

    private ArrayList<MovieItems.MovieItem> mMovieItems = new ArrayList<>();

    /**
     * Interface that will be used for the signalling of a movie selection
     */
    private MovieListingFragment.OnMovieSelectionListener mListener;

    public MovieAdapter(MovieListingFragment.OnMovieSelectionListener listener) {
        super();
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ImageView imageView;

        imageView = new ImageView(parent.getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_START);
        imageView.setAdjustViewBounds(true);
        imageView.setClickable(true);

        ViewHolder viewHolder = new ViewHolder(imageView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        // Get the ImageView that will be manipulated
        ImageView imageView = holder.mImageView;

        // Get the context that will be used
        Context context = imageView.getContext();

        // Get the model data that will be used to determine what image will be displayed
        MovieItems.MovieItem item = mMovieItems.get(position);

        // Get the movie poster UID from the GSON object
        String poster = mMovieItems.get(position).getPoster_path();
        if (poster != null) {
            String posterURL = context.getResources().getString(R.string.base_url_image_retrieval) + poster;
            Picasso.with(context).load(posterURL).into(imageView);
        }

        // Add a click listener to the view in order for the user to get more details about a selected movie
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Notify the the listener (aka MainActivity) of the movie selection
                mListener.onMovieSelection(mMovieItems.get(position));

            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovieItems.size();
    }

    public void add(MovieItems movieItems) {
        mMovieItems.addAll(movieItems.getResults());
        this.notifyDataSetChanged();
    }

    public void clearData() {
        mMovieItems.clear();
        this.notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view;
        }
    }

}

