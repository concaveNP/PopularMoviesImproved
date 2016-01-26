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
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class RequestTest {

    @Test
    public void publicMethods() throws Exception {
        // Catch-all test to find API-breaking changes.
        assertNotNull(Request.class.getConstructor(int.class, String.class,
                Response.ErrorListener.class));

        assertNotNull(Request.class.getMethod("getMethod"));
        assertNotNull(Request.class.getMethod("setTag", Object.class));
        assertNotNull(Request.class.getMethod("getTag"));
        assertNotNull(Request.class.getMethod("getErrorListener"));
        assertNotNull(Request.class.getMethod("getTrafficStatsTag"));
        assertNotNull(Request.class.getMethod("setRetryPolicy", RetryPolicy.class));
        assertNotNull(Request.class.getMethod("addMarker", String.class));
        assertNotNull(Request.class.getDeclaredMethod("finish", String.class));
        assertNotNull(Request.class.getMethod("setRequestQueue", RequestQueue.class));
        assertNotNull(Request.class.getMethod("setSequence", int.class));
        assertNotNull(Request.class.getMethod("getSequence"));
        assertNotNull(Request.class.getMethod("getUrl"));
        assertNotNull(Request.class.getMethod("getCacheKey"));
        assertNotNull(Request.class.getMethod("setCacheEntry", Cache.Entry.class));
        assertNotNull(Request.class.getMethod("getCacheEntry"));
        assertNotNull(Request.class.getMethod("cancel"));
        assertNotNull(Request.class.getMethod("isCanceled"));
        assertNotNull(Request.class.getMethod("getHeaders"));
        assertNotNull(Request.class.getDeclaredMethod("getParams"));
        assertNotNull(Request.class.getDeclaredMethod("getParamsEncoding"));
        assertNotNull(Request.class.getMethod("getBodyContentType"));
        assertNotNull(Request.class.getMethod("getBody"));
        assertNotNull(Request.class.getMethod("setShouldCache", boolean.class));
        assertNotNull(Request.class.getMethod("shouldCache"));
        assertNotNull(Request.class.getMethod("getPriority"));
        assertNotNull(Request.class.getMethod("getTimeoutMs"));
        assertNotNull(Request.class.getMethod("getRetryPolicy"));
        assertNotNull(Request.class.getMethod("markDelivered"));
        assertNotNull(Request.class.getMethod("hasHadResponseDelivered"));
        assertNotNull(Request.class.getDeclaredMethod("parseNetworkResponse", NetworkResponse.class));
        assertNotNull(Request.class.getDeclaredMethod("parseNetworkError", VolleyError.class));
        assertNotNull(Request.class.getDeclaredMethod("deliverResponse", Object.class));
        assertNotNull(Request.class.getMethod("deliverError", VolleyError.class));
    }
}
