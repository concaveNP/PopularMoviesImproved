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

package com.android.volley.mock;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ResponseDelivery;
import com.android.volley.VolleyError;

public class MockResponseDelivery implements ResponseDelivery {

    public boolean postResponse_called = false;
    public boolean postError_called = false;

    public boolean wasEitherResponseCalled() {
        return postResponse_called || postError_called;
    }

    public Response<?> responsePosted = null;
    @Override
    public void postResponse(Request<?> request, Response<?> response) {
        postResponse_called = true;
        responsePosted = response;
    }

    @Override
    public void postResponse(Request<?> request, Response<?> response, Runnable runnable) {
        postResponse_called = true;
        responsePosted = response;
        runnable.run();
    }

    @Override
    public void postError(Request<?> request, VolleyError error) {
        postError_called = true;
    }
}
