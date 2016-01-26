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
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.concavenp.nanodegree.popularmoviesimproved.gson.GsonRequest;
import com.concavenp.nanodegree.popularmoviesimproved.gson.ReviewItems;

/**
 * Created by dave on 1/19/2016.
 */
public class ReviewsCard extends CardView {

    // The logging tag string to be associated with log data for this class
    private static final String TAG = ReviewsCard.class.getSimpleName();

    // The maximum number of reviews to show
    private static final int MAX_REVIEWS = 2;

    // A Volley queue used for managing web interface requests
    private RequestQueue mRequestQueue;

    // The themoviedb DB ID for this movie in question
    private int mMovieId;

    /**
     * A default CardView constructor, but with the addition of initializing a Volley queue.
     *
     * @param context - View context we are in
     * @param attrs   - View attributes to use
     */
    public ReviewsCard(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Instantiate the RequestQueue
        mRequestQueue = Volley.newRequestQueue(context);
    }

    /**
     * Allows for the clearing of all data within the CardView.
     */
    public void removeAllViews() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.short_review_list);
        linearLayout.removeAllViews();

        // The button is "GONE" by default, so make it so
        Button reviewsButton = (Button) findViewById(R.id.reviews_Button);
        reviewsButton.setVisibility(GONE);
    }

    /**
     * Method that will create a request object that will be added the Volley request queue for
     * processing.  The request will translate the JSON response data into a GSON populated object.
     * The adapter will then be given the new data which will in turn update the displayed listing
     * of movie trailers to the user.
     *
     * @param id - This themoviedb database index for the movie question
     */
    public void requestReviewsData(int id) {

        mMovieId = id;

        String url =
                String.format(getResources().getString(R.string.base_url_review_request), id) +
                        "?" +
                        getResources().getString(R.string.part_url_api_key_dude) +
                        getResources().getString(R.string.THE_MOVIE_DB_API_TOKEN);

        // Request a string response from the provided URL.
        final GsonRequest<ReviewItems> request = new GsonRequest<>(url, ReviewItems.class, null, new Response.Listener<ReviewItems>() {
            @Override
            public void onResponse(ReviewItems response) {

                processResponse(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = getResources().getString(R.string.service_request_failure_message);

                // Log the data
                Log.e(TAG, errorMessage + ": " + error);
                error.printStackTrace();

                // Show a message to the user
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        mRequestQueue.add(request);
    }

    /**
     * Processes the response back from the web request.  It will dynamically add views to the
     * card view for display.
     *
     * @param reviewItems - The list of data items to display
     */
    private void processResponse(ReviewItems reviewItems) {

        Log.d(TAG, "total number of reviews: " + reviewItems.getTotal_results());

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.short_review_list);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        boolean showMoreReviewsButton = false;

        // The number reviews to display
        int numberOfReviewsToDisplay;

        // Only what to show a maximum number of reviews, so determine how many here
        if (reviewItems.getResults().size() > MAX_REVIEWS) {
            showMoreReviewsButton = true;
            numberOfReviewsToDisplay = MAX_REVIEWS;
        } else {
            numberOfReviewsToDisplay = reviewItems.getResults().size();
        }

        // Only show a maximum number of reviews.  Notify the user of the other reviews by showing
        // more reviews button.  Also, show the button if any one of the reviews is too long.
        for (int index = 0; index < numberOfReviewsToDisplay; index++) {

            // Extract the review in question for this loop iteration
            ReviewItems.ReviewItem item = reviewItems.getResults().get(index);

            View result = inflater.inflate(R.layout.review_item, linearLayout, false);

            // Set the author of the review
            TextView authorTextView = (TextView) result.findViewById(R.id.author_TextView);
            authorTextView.setText(item.getAuthor());

            // Set the content of the review
            TextView contentTextView = (TextView) result.findViewById(R.id.content_TextView);
            String content = item.getContent();
            if (content.length() > 200) {
                content = content.substring(0, 200);
                showMoreReviewsButton = true;

                // Show the ellipsis text in order to signify data displayed is only partial
                TextView ellipsisTextView = (TextView) result.findViewById(R.id.ellipsis_TextView);
                ellipsisTextView.setVisibility(VISIBLE);
            }
            contentTextView.setText(content);

            linearLayout.addView(result);
        }

        if (showMoreReviewsButton) {

            Button reviewsButton = (Button) findViewById(R.id.reviews_Button);

            // The button is "GONE" by default, so make it visible
            reviewsButton.setVisibility(VISIBLE);

            // Listen for clicks in order to start the reviews activity
            reviewsButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    Context context = view.getContext();
                    Intent intent = new Intent(context, MovieReviewsActivity.class);
                    intent.putExtra(MovieReviewsActivity.EXTRA_DATA, mMovieId);
                    context.startActivity(intent);

                }
            });

        }

    }

}
