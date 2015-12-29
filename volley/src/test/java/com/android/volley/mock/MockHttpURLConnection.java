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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MockHttpURLConnection extends HttpURLConnection {

    private boolean mDoOutput;
    private String mRequestMethod;
    private OutputStream mOutputStream;

    public MockHttpURLConnection() throws MalformedURLException {
        super(new URL("http://foo.com"));
        mDoOutput = false;
        mRequestMethod = "GET";
        mOutputStream = new ByteArrayOutputStream();
    }

    @Override
    public void setDoOutput(boolean flag) {
        mDoOutput = flag;
    }

    @Override
    public boolean getDoOutput() {
        return mDoOutput;
    }

    @Override
    public void setRequestMethod(String method) {
        mRequestMethod = method;
    }

    @Override
    public String getRequestMethod() {
        return mRequestMethod;
    }

    @Override
    public OutputStream getOutputStream() {
        return mOutputStream;
    }

    @Override
    public void disconnect() {
    }

    @Override
    public boolean usingProxy() {
        return false;
    }

    @Override
    public void connect() throws IOException {
    }

}
