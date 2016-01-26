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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.concavenp.nanodegree.popularmoviesimproved.gson.ReviewItems;

import java.util.ArrayList;

/**
 * This adapter class is used for adapting a {@code MovieItem} for views displayed within
 * the list of movies in the {@code MovieListingFragment}.
 * <p/>
 * Items are passed into the class when results are retrieved from a RESTful call.  The model data
 * given to the class is in the form of GSON objects that have been built from returned JSON data.
 * <p/>
 * This adapter establishes a link between the displayed movie item view and bringing up details
 * about the movie via a OnClickListener on view.  Clicking it will transition to a movie details
 * activity {@code MovieDetailsActivity}.
 */
class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    /**
     * The logging tag string to be associated with log data for this class
     */
    private static final String TAG = ReviewsAdapter.class.getSimpleName();

    private ArrayList<ReviewItems.ReviewItem> mReviewsItems = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View result = inflater.inflate(R.layout.review_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(result);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        // Get the ImageView that will be manipulated
        View view = holder.mView;

        // Get the context that will be used
        Context context = view.getContext();

        // Get the model data that will be used to determine what image will be displayed
        ReviewItems.ReviewItem item = mReviewsItems.get(position);

        // Set the author of the review
        TextView authorTextView = (TextView) view.findViewById(R.id.author_TextView);
        authorTextView.setText(item.getAuthor());

        // Set the content of the review
        TextView contentTextView = (TextView) view.findViewById(R.id.content_TextView);
        contentTextView.setText(item.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviewsItems.size();
    }

    public void add(ReviewItems reviewItems) {
        mReviewsItems.addAll(reviewItems.getResults());
        this.notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
        }
    }

}

