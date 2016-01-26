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

package com.android.volley.mock;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.utils.CacheTestUtils;

import java.util.HashMap;
import java.util.Map;

public class MockRequest extends Request<byte[]> {
    public boolean deliverResponse_called = false;
    public boolean parseResponse_called = false;
    public boolean deliverError_called = false;
    public boolean cancel_called = false;
    private Map<String, String> mPostParams = new HashMap<String, String>();
    private String mCacheKey = super.getCacheKey();
    private Priority mPriority = super.getPriority();

    public MockRequest() {
        super(Request.Method.GET, "http://foo.com", null);
    }

    public MockRequest(String url, ErrorListener listener) {
        super(Request.Method.GET, url, listener);
    }

    @Override
    public Map<String, String> getPostParams() {
        return mPostParams;
    }

    public void setPostParams(Map<String, String> postParams) {
        mPostParams = postParams;
    }

    @Override
    public String getCacheKey() {
        return mCacheKey;
    }

    public void setCacheKey(String cacheKey) {
        mCacheKey = cacheKey;
    }

    @Override
    protected void deliverResponse(byte[] response) {
        deliverResponse_called = true;
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
        deliverError_called = true;
    }

    @Override
    public void cancel() {
        cancel_called = true;
        super.cancel();
    }

    @Override
    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority(Priority priority) {
        mPriority = priority;
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        parseResponse_called = true;
        return Response.success(response.data, CacheTestUtils.makeRandomCacheEntry(response.data));
    }

}
