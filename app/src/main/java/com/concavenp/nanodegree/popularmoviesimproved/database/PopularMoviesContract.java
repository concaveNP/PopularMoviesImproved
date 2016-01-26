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

package com.concavenp.nanodegree.popularmoviesimproved.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Online References:
 * - http://developer.android.com/guide/topics/providers/content-provider-creating.html
 * - http://developer.android.com/training/basics/data-storage/databases.html
 * Book References:
 * - Gargenta & Masumi (2014) Learning Android: Develop Mobile Apps Using Java and Eclipse. Sebastopol, CA: O'Reilly Media, Inc.
 * - Chapter 11 Content Providers
 */
public class PopularMoviesContract {

    /**
     * The Content Authority name
     */
    public static final String AUTHORITY = "com.concavenp.nanodegree.popularmoviesimproved.database.PopularMoviesProvider";

    // Database specific constants
    public static final String DB_NAME = "PopularMovies.db";
    public static final int DB_VERSION = 1;

    // Table names:
    public static final String DB_FAVORITES_TABLE = "favorites";

    // Uri:
    public static final Uri FAVORITES_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + DB_FAVORITES_TABLE);

    public static final int FAVORITES_ITEM = 1;
    public static final int FAVORITES_DIR = 2;

    public static final String FAVORITES_TYPE_ITEM = "vnd.android.cursor.item/vnd.com.concavenp.nanodegree.popularmoviesimproved." + DB_FAVORITES_TABLE;
    public static final String FAVORITES_TYPE_DIR = "vnd.android.cursor.dir/vnd.com.concavenp.nanodegree.popularmoviesimproved." + DB_FAVORITES_TABLE;

    // Default sorts:
    public static final String FAVORITES_DEFAULT_SORT = FavoritesColumns._ID + " DESC";

    public class FavoritesColumns implements BaseColumns {

        public static final String MOVIE_ID = "movieId";
        public static final String JSON = "json";

    }


}
