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

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by dave on 1/17/2016.
 */
public class PopularMoviesProvider extends ContentProvider {

    private static final UriMatcher ourUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        ourUriMatcher.addURI(PopularMoviesContract.AUTHORITY, PopularMoviesContract.DB_FAVORITES_TABLE, PopularMoviesContract.FAVORITES_DIR);
        ourUriMatcher.addURI(PopularMoviesContract.AUTHORITY, PopularMoviesContract.DB_FAVORITES_TABLE + "/#", PopularMoviesContract.FAVORITES_ITEM);
    }

    PopularMoviesDatabase mPopularMoviesDatabase;

    @Override
    public boolean onCreate() {

        mPopularMoviesDatabase = new PopularMoviesDatabase(getContext());

        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        String sort;

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (ourUriMatcher.match(uri)) {
            case PopularMoviesContract.FAVORITES_DIR: {
                sort = PopularMoviesContract.FAVORITES_DEFAULT_SORT;
                qb.setTables(PopularMoviesContract.DB_FAVORITES_TABLE);
                break;
            }
            case PopularMoviesContract.FAVORITES_ITEM: {
                qb.appendWhere(PopularMoviesContract.DB_FAVORITES_TABLE + "." + PopularMoviesContract.FavoritesColumns._ID + "=" + uri.getLastPathSegment());
                sort = PopularMoviesContract.FAVORITES_DEFAULT_SORT;
                qb.setTables(PopularMoviesContract.DB_FAVORITES_TABLE);
                break;
            }
            default: {
                throw new IllegalArgumentException("Illegal uri: " + uri);
            }
        }

        String orderBy = (TextUtils.isEmpty(sortOrder)) ? sort : sortOrder;

        SQLiteDatabase db = mPopularMoviesDatabase.getReadableDatabase();
        Cursor result = qb.query(db, projection, selection, selectionArgs, null, null, sort);

        result.setNotificationUri(getContext().getContentResolver(), uri);

        return result;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch (ourUriMatcher.match(uri)) {
            case PopularMoviesContract.FAVORITES_DIR: {
                return PopularMoviesContract.FAVORITES_TYPE_DIR;
            }
            case PopularMoviesContract.FAVORITES_ITEM: {
                return PopularMoviesContract.FAVORITES_TYPE_ITEM;
            }
            default: {
                throw new IllegalArgumentException("Illegal uri: " + uri);
            }
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        Uri result = null;

        String table;

        SQLiteDatabase db = mPopularMoviesDatabase.getWritableDatabase();

        switch (ourUriMatcher.match(uri)) {
            case PopularMoviesContract.FAVORITES_DIR: {
                table = PopularMoviesContract.DB_FAVORITES_TABLE;
                break;
            }
            case PopularMoviesContract.FAVORITES_ITEM: {
                table = PopularMoviesContract.DB_FAVORITES_TABLE;
                break;
            }
            default: {
                throw new IllegalArgumentException("Illegal uri: " + uri);
            }
        }

        long rowId = db.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        // Was insert good?
        if (rowId != -1) {
            //long id = values.getAsLong(column);
            result = ContentUris.withAppendedId(uri, rowId);//id);

            // Notify that data for this uri has changed
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return result;

    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        String where;
        String table;

        switch (ourUriMatcher.match(uri)) {
            case PopularMoviesContract.FAVORITES_DIR: {

                // So we count deleted rows
                where = (selection == null) ? "1" : selection;
                table = PopularMoviesContract.DB_FAVORITES_TABLE;

                break;
            }
            case PopularMoviesContract.FAVORITES_ITEM: {
                long id = ContentUris.parseId(uri);

                where = PopularMoviesContract.FavoritesColumns._ID + "=" + id + (TextUtils.isEmpty(selection) ? "" : " and ( " + selection + " )");
                table = PopularMoviesContract.DB_FAVORITES_TABLE;

                break;
            }
            default: {
                throw new IllegalArgumentException("Illegal uri: " + uri);
            }
        }

        SQLiteDatabase db = mPopularMoviesDatabase.getWritableDatabase();
        int result = db.delete(table, where, selectionArgs);

        // Was insert good?
        if (result > 0) {

            // Notify that data for this uri has changed
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return result;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String where;
        String table;

        switch (ourUriMatcher.match(uri)) {
            case PopularMoviesContract.FAVORITES_DIR: {

                // So we count updated rows
                where = selection;
                table = PopularMoviesContract.DB_FAVORITES_TABLE;

                break;
            }
            case PopularMoviesContract.FAVORITES_ITEM: {
                long parseId = ContentUris.parseId(uri);

                where = PopularMoviesContract.FavoritesColumns._ID + "=" + parseId + (TextUtils.isEmpty(selection) ? "" : " and ( " + selection + " )");
                table = PopularMoviesContract.DB_FAVORITES_TABLE;

                break;
            }
            default: {
                throw new IllegalArgumentException("Illegal uri: " + uri);
            }
        }

        SQLiteDatabase db = mPopularMoviesDatabase.getWritableDatabase();
        int result = db.update(table, values, where, selectionArgs);

        // Was insert good?
        if (result > 0) {
            //long id = values.getAsLong(GuardianDatabaseContract.ResultsColumn._ID);
            // Notify that data for this uri has changed
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return result;

    }
}
