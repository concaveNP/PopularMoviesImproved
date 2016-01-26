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
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Map;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class ResponseTest {

    @Test
    public void publicMethods() throws Exception {
        // Catch-all test to find API-breaking changes.
        assertNotNull(Response.class.getMethod("success", Object.class, Cache.Entry.class));
        assertNotNull(Response.class.getMethod("error", VolleyError.class));
        assertNotNull(Response.class.getMethod("isSuccess"));

        assertNotNull(Response.Listener.class.getDeclaredMethod("onResponse", Object.class));

        assertNotNull(Response.ErrorListener.class.getDeclaredMethod("onErrorResponse",
                VolleyError.class));

        assertNotNull(NetworkResponse.class.getConstructor(int.class, byte[].class, Map.class,
                boolean.class, long.class));
        assertNotNull(NetworkResponse.class.getConstructor(int.class, byte[].class, Map.class,
                boolean.class));
        assertNotNull(NetworkResponse.class.getConstructor(byte[].class));
        assertNotNull(NetworkResponse.class.getConstructor(byte[].class, Map.class));
    }
}
