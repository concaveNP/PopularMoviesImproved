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

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

public class TestRequest {
    private static final String TEST_URL = "http://foo.com";

    /** Base Request class for testing allowing both the deprecated and new constructor. */
    private static class Base extends Request<byte[]> {
        @SuppressWarnings("deprecation")
        public Base(String url, Response.ErrorListener listener) {
            super(url, listener);
        }

        public Base(int method, String url, Response.ErrorListener listener) {
            super(method, url, listener);
        }

        @Override
        protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
            return null;
        }

        @Override
        protected void deliverResponse(byte[] response) {
        }
    }

    /** Test example of a GET request in the deprecated style. */
    public static class DeprecatedGet extends Base {
        public DeprecatedGet() {
            super(TEST_URL, null);
        }
    }

    /** Test example of a POST request in the deprecated style. */
    public static class DeprecatedPost extends Base {
        private Map<String, String> mPostParams;

        public DeprecatedPost() {
            super(TEST_URL, null);
            mPostParams = new HashMap<String, String>();
            mPostParams.put("requestpost", "foo");
        }

        @Override
        protected Map<String, String> getPostParams() {
            return mPostParams;
        }
    }

    /** Test example of a GET request in the new style. */
    public static class Get extends Base {
        public Get() {
            super(Method.GET, TEST_URL, null);
        }
    }

    /**
     * Test example of a POST request in the new style.  In the new style, it is possible
     * to have a POST with no body.
     */
    public static class Post extends Base {
        public Post() {
            super(Method.POST, TEST_URL, null);
        }
    }

    /** Test example of a POST request in the new style with a body. */
    public static class PostWithBody extends Post {
        private Map<String, String> mParams;

        public PostWithBody() {
            mParams = new HashMap<String, String>();
            mParams.put("testKey", "testValue");
        }

        @Override
        public Map<String, String> getParams() {
            return mParams;
        }
    }

    /**
     * Test example of a PUT request in the new style.  In the new style, it is possible to have a
     * PUT with no body.
     */
    public static class Put extends Base {
        public Put() {
            super(Method.PUT, TEST_URL, null);
        }
    }

    /** Test example of a PUT request in the new style with a body. */
    public static class PutWithBody extends Put {
        private Map<String, String> mParams = new HashMap<String, String>();

        public PutWithBody() {
            mParams = new HashMap<String, String>();
            mParams.put("testKey", "testValue");
        }

        @Override
        public Map<String, String> getParams() {
            return mParams;
        }
    }

    /** Test example of a DELETE request in the new style. */
    public static class Delete extends Base {
        public Delete() {
            super(Method.DELETE, TEST_URL, null);
        }
    }

    /** Test example of a HEAD request in the new style. */
    public static class Head extends Base {
        public Head() {
            super(Method.HEAD, TEST_URL, null);
        }
    }

    /** Test example of a OPTIONS request in the new style. */
    public static class Options extends Base {
        public Options() {
            super(Method.OPTIONS, TEST_URL, null);
        }
    }

    /** Test example of a TRACE request in the new style. */
    public static class Trace extends Base {
        public Trace() {
            super(Method.TRACE, TEST_URL, null);
        }
    }

    /** Test example of a PATCH request in the new style. */
    public static class Patch extends Base {
        public Patch() {
            super(Method.PATCH, TEST_URL, null);
        }
    }

    /** Test example of a PATCH request in the new style with a body. */
    public static class PatchWithBody extends Patch {
        private Map<String, String> mParams = new HashMap<String, String>();

        public PatchWithBody() {
            mParams = new HashMap<String, String>();
            mParams.put("testKey", "testValue");
        }

        @Override
        public Map<String, String> getParams() {
            return mParams;
        }
    }
}
