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

package com.android.volley.mock;

import com.android.volley.Cache;

public class MockCache implements Cache {

    public boolean clearCalled = false;
    @Override
    public void clear() {
        clearCalled = true;
    }

    public boolean getCalled = false;
    private Entry mFakeEntry = null;

    public void setEntryToReturn(Entry entry) {
        mFakeEntry = entry;
    }

    @Override
    public Entry get(String key) {
        getCalled = true;
        return mFakeEntry;
    }

    public boolean putCalled = false;
    public String keyPut = null;
    public Entry entryPut = null;

    @Override
    public void put(String key, Entry entry) {
        putCalled = true;
        keyPut = key;
        entryPut = entry;
    }

    @Override
    public void invalidate(String key, boolean fullExpire) {
    }

    @Override
    public void remove(String key) {
    }

	@Override
	public void initialize() {
	}

}
