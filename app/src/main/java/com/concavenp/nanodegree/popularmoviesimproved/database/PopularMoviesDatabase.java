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

package com.concavenp.nanodegree.popularmoviesimproved.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by dave on 1/17/2016.
 */
public class PopularMoviesDatabase extends SQLiteOpenHelper {

    /**
     * The logging tag string to be associated with log data for this class
     */
    private static final String TAG = PopularMoviesDatabase.class.getSimpleName();

    public PopularMoviesDatabase(Context context) {
        // TODO: find out what a CursorFactory is - its the null arg
        super(context, PopularMoviesContract.DB_NAME, null, PopularMoviesContract.DB_VERSION);
    }

    /**
     * Call when the database is created
     *
     * @param db - The actual DB object that Android is providing us
     *           <p>
     *           {@inheritDoc}
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create the favorites table
        createFavorites(db);

    }

    private void createFavorites(SQLiteDatabase db) {

        // This table should match the same data api that is provided via the content provider
        String sql = String.format(
                "CREATE TABLE %s " +                                        // table name
                        "(" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + // _ID - index
                        "%s INTEGER NOT NULL, " +                           // MOVIE_ID - themoviedb number
                        "%s TEXT" +                                         // POSTER_PATH - the poster path for the given movie
                        ")",
                PopularMoviesContract.DB_FAVORITES_TABLE,
                PopularMoviesContract.FavoritesColumns._ID,
                PopularMoviesContract.FavoritesColumns.MOVIE_ID,
                PopularMoviesContract.FavoritesColumns.POSTER_PATH
        );

        // Log the creation of the DB table SQL statement
        Log.d(TAG, "onCreate with SQL: " + sql);

        // Execute the SQL statement that will build the database table if needed
        db.execSQL(sql);
    }

    /**
     * Gets called whenever existing version does not equal the new version
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Typically you do ALTER TABLE in order to update it.  In this case the table will just
        // be replaced.
        db.execSQL("drop table if exists " + PopularMoviesContract.DB_FAVORITES_TABLE);
        onCreate(db);
    }

}
