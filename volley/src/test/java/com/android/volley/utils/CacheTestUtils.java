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

package com.android.volley.utils;

import com.android.volley.Cache;

import java.util.Random;

public class CacheTestUtils {

    /**
     * Makes a random cache entry.
     * @param data Data to use, or null to use random data
     * @param isExpired Whether the TTLs should be set such that this entry is expired
     * @param needsRefresh Whether the TTLs should be set such that this entry needs refresh
     */
    public static Cache.Entry makeRandomCacheEntry(
            byte[] data, boolean isExpired, boolean needsRefresh) {
        Random random = new Random();
        Cache.Entry entry = new Cache.Entry();
        if (data != null) {
            entry.data = data;
        } else {
            entry.data = new byte[random.nextInt(1024)];
        }
        entry.etag = String.valueOf(random.nextLong());
        entry.lastModified = random.nextLong();
        entry.ttl = isExpired ? 0 : Long.MAX_VALUE;
        entry.softTtl = needsRefresh ? 0 : Long.MAX_VALUE;
        return entry;
    }

    /**
     * Like {@link #makeRandomCacheEntry(byte[], boolean, boolean)} but
     * defaults to an unexpired entry.
     */
    public static Cache.Entry makeRandomCacheEntry(byte[] data) {
        return makeRandomCacheEntry(data, false, false);
    }
}
