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

package com.android.volley.toolbox;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class ImageLoaderTest {
    private RequestQueue mRequestQueue;
    private ImageLoader.ImageCache mImageCache;
    private ImageLoader mImageLoader;

    @Before
    public void setUp() {
        mRequestQueue = mock(RequestQueue.class);
        mImageCache = mock(ImageLoader.ImageCache.class);
        mImageLoader = new ImageLoader(mRequestQueue, mImageCache);
    }

    @Test
    public void isCachedChecksCache() throws Exception {
        when(mImageCache.getBitmap(anyString())).thenReturn(null);
        Assert.assertFalse(mImageLoader.isCached("http://foo", 0, 0));
    }

    @Test
    public void getWithCacheHit() throws Exception {
        Bitmap bitmap = Bitmap.createBitmap(1, 1, null);
        ImageLoader.ImageListener listener = mock(ImageLoader.ImageListener.class);
        when(mImageCache.getBitmap(anyString())).thenReturn(bitmap);
        ImageLoader.ImageContainer ic = mImageLoader.get("http://foo", listener);
        Assert.assertSame(bitmap, ic.getBitmap());
        verify(listener).onResponse(ic, true);
    }

    @Test
    public void getWithCacheMiss() throws Exception {
        when(mImageCache.getBitmap(anyString())).thenReturn(null);
        ImageLoader.ImageListener listener = mock(ImageLoader.ImageListener.class);
        // Ask for the image to be loaded.
        mImageLoader.get("http://foo", listener);
        // Second pass to test deduping logic.
        mImageLoader.get("http://foo", listener);
        // Response callback should be called both times.
        verify(listener, times(2)).onResponse(any(ImageLoader.ImageContainer.class), eq(true));
        // But request should be enqueued only once.
        verify(mRequestQueue, times(1)).add(any(Request.class));
    }

    @Test
    public void publicMethods() throws Exception {
        // Catch API breaking changes.
        ImageLoader.getImageListener(null, -1, -1);
        mImageLoader.setBatchedResponseDelay(1000);

        assertNotNull(ImageLoader.class.getConstructor(RequestQueue.class,
                ImageLoader.ImageCache.class));

        assertNotNull(ImageLoader.class.getMethod("getImageListener", ImageView.class,
                int.class, int.class));
        assertNotNull(ImageLoader.class.getMethod("isCached", String.class, int.class, int.class));
        assertNotNull(ImageLoader.class.getMethod("isCached", String.class, int.class, int.class,
                ImageView.ScaleType.class));
        assertNotNull(ImageLoader.class.getMethod("get", String.class,
                ImageLoader.ImageListener.class));
        assertNotNull(ImageLoader.class.getMethod("get", String.class,
                ImageLoader.ImageListener.class, int.class, int.class));
        assertNotNull(ImageLoader.class.getMethod("get", String.class,
                ImageLoader.ImageListener.class, int.class, int.class, ImageView.ScaleType.class));
        assertNotNull(ImageLoader.class.getMethod("setBatchedResponseDelay", int.class));

        assertNotNull(ImageLoader.ImageListener.class.getMethod("onResponse",
                ImageLoader.ImageContainer.class, boolean.class));
    }
}

