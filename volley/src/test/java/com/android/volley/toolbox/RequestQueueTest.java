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

package com.android.volley.toolbox;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ResponseDelivery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class RequestQueueTest {

    @Test
    public void publicMethods() throws Exception {
        // Catch-all test to find API-breaking changes.
        assertNotNull(RequestQueue.class.getConstructor(Cache.class, Network.class, int.class,
                ResponseDelivery.class));
        assertNotNull(RequestQueue.class.getConstructor(Cache.class, Network.class, int.class));
        assertNotNull(RequestQueue.class.getConstructor(Cache.class, Network.class));

        assertNotNull(RequestQueue.class.getMethod("start"));
        assertNotNull(RequestQueue.class.getMethod("stop"));
        assertNotNull(RequestQueue.class.getMethod("getSequenceNumber"));
        assertNotNull(RequestQueue.class.getMethod("getCache"));
        assertNotNull(RequestQueue.class.getMethod("cancelAll", RequestQueue.RequestFilter.class));
        assertNotNull(RequestQueue.class.getMethod("cancelAll", Object.class));
        assertNotNull(RequestQueue.class.getMethod("add", Request.class));
        assertNotNull(RequestQueue.class.getDeclaredMethod("finish", Request.class));
    }
}
