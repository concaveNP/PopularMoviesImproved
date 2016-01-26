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

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView.ScaleType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class NetworkImageViewTest {
    private NetworkImageView mNIV;
    private MockImageLoader mMockImageLoader;

    @Before public void setUp() throws Exception {
        mMockImageLoader = new MockImageLoader();
        mNIV = new NetworkImageView(RuntimeEnvironment.application);
    }

    @Test public void setImageUrl_requestsImage() {
        mNIV.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mNIV.setImageUrl("http://foo", mMockImageLoader);
        assertEquals("http://foo", mMockImageLoader.lastRequestUrl);
        assertEquals(0, mMockImageLoader.lastMaxWidth);
        assertEquals(0, mMockImageLoader.lastMaxHeight);
    }

    // public void testSetImageUrl_setsMaxSize() {
    // // TODO: Not sure how to make getWidth() return something from an
    // // instrumentation test. Write this test once it's figured out.
    // }

    @Test
    public void publicMethods() throws Exception {
        // Catch-all test to find API-breaking changes.
        assertNotNull(NetworkImageView.class.getConstructor(Context.class));
        assertNotNull(NetworkImageView.class.getConstructor(Context.class, AttributeSet.class));
        assertNotNull(NetworkImageView.class.getConstructor(Context.class, AttributeSet.class,
                int.class));

        assertNotNull(NetworkImageView.class.getMethod("setImageUrl", String.class, ImageLoader.class));
        assertNotNull(NetworkImageView.class.getMethod("setDefaultImageResId", int.class));
        assertNotNull(NetworkImageView.class.getMethod("setErrorImageResId", int.class));
    }

    private class MockImageLoader extends ImageLoader {
        public String lastRequestUrl;
        public int lastMaxWidth;
        public int lastMaxHeight;
        public MockImageLoader() {
            super(null, null);
        }

        public ImageContainer get(String requestUrl, ImageListener imageListener, int maxWidth,
                int maxHeight, ScaleType scaleType) {
            lastRequestUrl = requestUrl;
            lastMaxWidth = maxWidth;
            lastMaxHeight = maxHeight;
            return null;
        }
    }
}
