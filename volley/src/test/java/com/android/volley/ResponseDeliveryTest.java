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

package com.android.volley;

import com.android.volley.mock.MockRequest;
import com.android.volley.utils.CacheTestUtils;
import com.android.volley.utils.ImmediateResponseDelivery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class ResponseDeliveryTest {

    private ExecutorDelivery mDelivery;
    private MockRequest mRequest;
    private Response<byte[]> mSuccessResponse;

    @Before public void setUp() throws Exception {
        // Make the delivery just run its posted responses immediately.
        mDelivery = new ImmediateResponseDelivery();
        mRequest = new MockRequest();
        mRequest.setSequence(1);
        byte[] data = new byte[16];
        Cache.Entry cacheEntry = CacheTestUtils.makeRandomCacheEntry(data);
        mSuccessResponse = Response.success(data, cacheEntry);
    }

    @Test public void postResponseCallsDeliverResponse() {
        mDelivery.postResponse(mRequest, mSuccessResponse);
        assertTrue(mRequest.deliverResponse_called);
        assertFalse(mRequest.deliverError_called);
    }

    @Test public void postResponseSuppressesCanceled() {
        mRequest.cancel();
        mDelivery.postResponse(mRequest, mSuccessResponse);
        assertFalse(mRequest.deliverResponse_called);
        assertFalse(mRequest.deliverError_called);
    }

    @Test public void postErrorCallsDeliverError() {
        Response<byte[]> errorResponse = Response.error(new ServerError());

        mDelivery.postResponse(mRequest, errorResponse);
        assertTrue(mRequest.deliverError_called);
        assertFalse(mRequest.deliverResponse_called);
    }
}
