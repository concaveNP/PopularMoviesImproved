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

import com.android.volley.mock.MockCache;
import com.android.volley.mock.MockRequest;
import com.android.volley.mock.MockResponseDelivery;
import com.android.volley.mock.WaitableQueue;
import com.android.volley.utils.CacheTestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@SuppressWarnings("rawtypes")
public class CacheDispatcherTest {
    private CacheDispatcher mDispatcher;
    private WaitableQueue mCacheQueue;
    private WaitableQueue mNetworkQueue;
    private MockCache mCache;
    private MockResponseDelivery mDelivery;
    private MockRequest mRequest;

    private static final long TIMEOUT_MILLIS = 5000;

    @Before public void setUp() throws Exception {
        mCacheQueue = new WaitableQueue();
        mNetworkQueue = new WaitableQueue();
        mCache = new MockCache();
        mDelivery = new MockResponseDelivery();

        mRequest = new MockRequest();

        mDispatcher = new CacheDispatcher(mCacheQueue, mNetworkQueue, mCache, mDelivery);
        mDispatcher.start();
    }

    @After public void tearDown() throws Exception {
        mDispatcher.quit();
        mDispatcher.join();
    }

    // A cancelled request should not be processed at all.
    @Test public void cancelledRequest() throws Exception {
        mRequest.cancel();
        mCacheQueue.add(mRequest);
        mCacheQueue.waitUntilEmpty(TIMEOUT_MILLIS);
        assertFalse(mCache.getCalled);
        assertFalse(mDelivery.wasEitherResponseCalled());
    }

    // A cache miss does not post a response and puts the request on the network queue.
    @Test public void cacheMiss() throws Exception {
        mCacheQueue.add(mRequest);
        mCacheQueue.waitUntilEmpty(TIMEOUT_MILLIS);
        assertFalse(mDelivery.wasEitherResponseCalled());
        assertTrue(mNetworkQueue.size() > 0);
        Request request = mNetworkQueue.take();
        assertNull(request.getCacheEntry());
    }

    // A non-expired cache hit posts a response and does not queue to the network.
    @Test public void nonExpiredCacheHit() throws Exception {
        Cache.Entry entry = CacheTestUtils.makeRandomCacheEntry(null, false, false);
        mCache.setEntryToReturn(entry);
        mCacheQueue.add(mRequest);
        mCacheQueue.waitUntilEmpty(TIMEOUT_MILLIS);
        assertTrue(mDelivery.postResponse_called);
        assertFalse(mDelivery.postError_called);
    }

    // A soft-expired cache hit posts a response and queues to the network.
    @Test public void softExpiredCacheHit() throws Exception {
        Cache.Entry entry = CacheTestUtils.makeRandomCacheEntry(null, false, true);
        mCache.setEntryToReturn(entry);
        mCacheQueue.add(mRequest);
        mCacheQueue.waitUntilEmpty(TIMEOUT_MILLIS);
        assertTrue(mDelivery.postResponse_called);
        assertFalse(mDelivery.postError_called);
        assertTrue(mNetworkQueue.size() > 0);
        Request request = mNetworkQueue.take();
        assertSame(entry, request.getCacheEntry());
    }

    // An expired cache hit does not post a response and queues to the network.
    @Test public void expiredCacheHit() throws Exception {
        Cache.Entry entry = CacheTestUtils.makeRandomCacheEntry(null, true, true);
        mCache.setEntryToReturn(entry);
        mCacheQueue.add(mRequest);
        mCacheQueue.waitUntilEmpty(TIMEOUT_MILLIS);
        assertFalse(mDelivery.wasEitherResponseCalled());
        assertTrue(mNetworkQueue.size() > 0);
        Request request = mNetworkQueue.take();
        assertSame(entry, request.getCacheEntry());
    }
}
