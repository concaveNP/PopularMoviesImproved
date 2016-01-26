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

package com.android.volley;

import com.android.volley.mock.MockCache;
import com.android.volley.mock.MockNetwork;
import com.android.volley.mock.MockRequest;
import com.android.volley.mock.MockResponseDelivery;
import com.android.volley.mock.WaitableQueue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class NetworkDispatcherTest {
    private static final byte[] CANNED_DATA = "Ceci n'est pas une vraie reponse".getBytes();
    private static final long TIMEOUT_MILLIS = 5000;
    private NetworkDispatcher mDispatcher;
    private MockResponseDelivery mDelivery;
    private WaitableQueue mNetworkQueue;
    private MockNetwork mNetwork;
    private MockCache mCache;
    private MockRequest mRequest;

    @Before public void setUp() throws Exception {
        mDelivery = new MockResponseDelivery();
        mNetworkQueue = new WaitableQueue();
        mNetwork = new MockNetwork();
        mCache = new MockCache();
        mRequest = new MockRequest();
        mDispatcher = new NetworkDispatcher(mNetworkQueue, mNetwork, mCache, mDelivery);
        mDispatcher.start();
    }

    @After public void tearDown() throws Exception {
        mDispatcher.quit();
        mDispatcher.join();
    }

    @Test public void successPostsResponse() throws Exception {
        mNetwork.setDataToReturn(CANNED_DATA);
        mNetwork.setNumExceptionsToThrow(0);
        mNetworkQueue.add(mRequest);
        mNetworkQueue.waitUntilEmpty(TIMEOUT_MILLIS);
        assertFalse(mDelivery.postError_called);
        assertTrue(mDelivery.postResponse_called);
        Response<?> response = mDelivery.responsePosted;
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertTrue(Arrays.equals((byte[])response.result, CANNED_DATA));
    }

    @Test public void exceptionPostsError() throws Exception {
        mNetwork.setNumExceptionsToThrow(MockNetwork.ALWAYS_THROW_EXCEPTIONS);
        mNetworkQueue.add(mRequest);
        mNetworkQueue.waitUntilEmpty(TIMEOUT_MILLIS);
        assertFalse(mDelivery.postResponse_called);
        assertTrue(mDelivery.postError_called);
    }

    @Test public void shouldCacheFalse() throws Exception {
        mRequest.setShouldCache(false);
        mNetworkQueue.add(mRequest);
        mNetworkQueue.waitUntilEmpty(TIMEOUT_MILLIS);
        assertFalse(mCache.putCalled);
    }

    @Test public void shouldCacheTrue() throws Exception {
        mNetwork.setDataToReturn(CANNED_DATA);
        mRequest.setShouldCache(true);
        mRequest.setCacheKey("bananaphone");
        mNetworkQueue.add(mRequest);
        mNetworkQueue.waitUntilEmpty(TIMEOUT_MILLIS);
        assertTrue(mCache.putCalled);
        assertNotNull(mCache.entryPut);
        assertTrue(Arrays.equals(mCache.entryPut.data, CANNED_DATA));
        assertEquals("bananaphone", mCache.keyPut);
    }
}
