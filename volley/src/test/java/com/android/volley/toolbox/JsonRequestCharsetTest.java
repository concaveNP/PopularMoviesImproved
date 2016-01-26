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

import com.android.volley.NetworkResponse;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class JsonRequestCharsetTest {

    /**
     * String in Czech - "Retezec v cestine."
     */
    private static final String TEXT_VALUE = "\u0158et\u011bzec v \u010de\u0161tin\u011b.";
    private static final String TEXT_NAME = "text";
    private static final int TEXT_INDEX = 0;

    /**
     * Copyright symbol has different encoding in utf-8 and ISO-8859-1,
     * and it doesn't exists in ISO-8859-2
     */
    private static final String COPY_VALUE = "\u00a9";
    private static final String COPY_NAME = "copyright";
    private static final int COPY_INDEX = 1;

    private static String jsonObjectString() throws Exception {
        JSONObject json = new JSONObject().put(TEXT_NAME, TEXT_VALUE).put(COPY_NAME, COPY_VALUE);
        return json.toString();
    }

    private static String jsonArrayString() throws Exception {
        JSONArray json = new JSONArray().put(TEXT_INDEX, TEXT_VALUE).put(COPY_INDEX, COPY_VALUE);
        return json.toString();
    }

    @Test public void defaultCharsetJsonObject() throws Exception {
        // UTF-8 is default charset for JSON
        byte[] data = jsonObjectString().getBytes(Charset.forName("UTF-8"));
        NetworkResponse network = new NetworkResponse(data);
        JsonObjectRequest objectRequest = new JsonObjectRequest("", null, null, null);
        Response<JSONObject> objectResponse = objectRequest.parseNetworkResponse(network);

        assertNotNull(objectResponse);
        assertTrue(objectResponse.isSuccess());
        assertEquals(TEXT_VALUE, objectResponse.result.getString(TEXT_NAME));
        assertEquals(COPY_VALUE, objectResponse.result.getString(COPY_NAME));
    }

    @Test public void defaultCharsetJsonArray() throws Exception {
        // UTF-8 is default charset for JSON
        byte[] data = jsonArrayString().getBytes(Charset.forName("UTF-8"));
        NetworkResponse network = new NetworkResponse(data);
        JsonArrayRequest arrayRequest = new JsonArrayRequest("", null, null);
        Response<JSONArray> arrayResponse = arrayRequest.parseNetworkResponse(network);

        assertNotNull(arrayResponse);
        assertTrue(arrayResponse.isSuccess());
        assertEquals(TEXT_VALUE, arrayResponse.result.getString(TEXT_INDEX));
        assertEquals(COPY_VALUE, arrayResponse.result.getString(COPY_INDEX));
    }

    @Test public void specifiedCharsetJsonObject() throws Exception {
        byte[] data = jsonObjectString().getBytes(Charset.forName("ISO-8859-1"));
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=iso-8859-1");
        NetworkResponse network = new NetworkResponse(data, headers);
        JsonObjectRequest objectRequest = new JsonObjectRequest("", null, null, null);
        Response<JSONObject> objectResponse = objectRequest.parseNetworkResponse(network);

        assertNotNull(objectResponse);
        assertTrue(objectResponse.isSuccess());
        //don't check the text in Czech, ISO-8859-1 doesn't support some Czech characters
        assertEquals(COPY_VALUE, objectResponse.result.getString(COPY_NAME));
    }

    @Test public void specifiedCharsetJsonArray() throws Exception {
        byte[] data = jsonArrayString().getBytes(Charset.forName("ISO-8859-2"));
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=iso-8859-2");
        NetworkResponse network = new NetworkResponse(data, headers);
        JsonArrayRequest arrayRequest = new JsonArrayRequest("", null, null);
        Response<JSONArray> arrayResponse = arrayRequest.parseNetworkResponse(network);

        assertNotNull(arrayResponse);
        assertTrue(arrayResponse.isSuccess());
        assertEquals(TEXT_VALUE, arrayResponse.result.getString(TEXT_INDEX));
        // don't check the copyright symbol, ISO-8859-2 doesn't have it, but it has Czech characters
    }
}
