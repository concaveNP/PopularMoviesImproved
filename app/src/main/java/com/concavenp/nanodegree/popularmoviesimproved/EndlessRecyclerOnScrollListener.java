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

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Code reused from:
 * https://gist.github.com/ssinss/e06f12ef66c51252563e
 * <p>
 * <p>
 * Created by dave on 1/21/2016.
 */
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    // The total number of items in the dataset after the last load
    private int mPreviousTotal;

    // True if we are still waiting for the last set of data to load.
    private boolean mLoading;
    private int mVisibleThreshold;
    private int mCurrentPage;
    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {

        mLinearLayoutManager = linearLayoutManager;

        // Initializes the fields to a starting point
        initValues();

    }

    public void initValues() {
        mPreviousTotal = 0;
        mLoading = false;
        mVisibleThreshold = 5;
        mCurrentPage = 1;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = mLinearLayoutManager.getItemCount();
        int firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (mLinearLayoutManager instanceof GridLayoutManager) {
            // The minimum amount of items to have below your current scroll position before loading more.  This should be equal to the number of columns in the grid.  Thus, one row.
            mVisibleThreshold = ((GridLayoutManager) mLinearLayoutManager).getSpanCount();
        }

        if (mLoading) {

            if (totalItemCount > mPreviousTotal) {

                mLoading = false;
                mPreviousTotal = totalItemCount;

            }

        }

        // Check to see if the end has been reached
        if ((!mLoading) && ((totalItemCount - visibleItemCount) <= (firstVisibleItem + mVisibleThreshold))) {

            mCurrentPage++;

            onLoadMore(mCurrentPage);

            mLoading = true;
        }
    }

    /**
     * Abstract method that class extenders must implement in order for new data to be loaded when
     * scrolling beyond a defined threshold.
     *
     * @param current_page A value specifically used within RESTful interfaces calls for more data
     */
    public abstract void onLoadMore(int current_page);

}
