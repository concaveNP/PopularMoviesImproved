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

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

// TODO: the name of this class sucks
@SuppressWarnings("serial")
public class WaitableQueue extends PriorityBlockingQueue<Request<?>> {
    private final Request<?> mStopRequest = new MagicStopRequest();
    private final Semaphore mStopEvent = new Semaphore(0);

    // TODO: this isn't really "until empty" it's "until next call to take() after empty"
    public void waitUntilEmpty(long timeoutMillis)
            throws TimeoutException, InterruptedException {
        add(mStopRequest);
        if (!mStopEvent.tryAcquire(timeoutMillis, TimeUnit.MILLISECONDS)) {
            throw new TimeoutException();
        }
    }

    @Override
    public Request<?> take() throws InterruptedException {
        Request<?> item = super.take();
        if (item == mStopRequest) {
            mStopEvent.release();
            return take();
        }
        return item;
    }

    private static class MagicStopRequest extends Request<Object> {
        public MagicStopRequest() {
            super(Request.Method.GET, "", null);
        }

        @Override
        public Priority getPriority() {
            return Priority.LOW;
        }

        @Override
        protected Response<Object> parseNetworkResponse(NetworkResponse response) {
            return null;
        }

        @Override
        protected void deliverResponse(Object response) {
        }
    }
}
