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
import android.graphics.Bitmap.Config;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowBitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class ImageRequestTest {

    @Test public void parseNetworkResponse_resizing() throws Exception {
        // This is a horrible hack but Robolectric doesn't have a way to provide
        // width and height hints for decodeByteArray. It works because the byte array
        // "file:fake" is ASCII encodable and thus the name in Robolectric's fake
        // bitmap creator survives as-is, and provideWidthAndHeightHints puts
        // "file:" + name in its lookaside map. I write all this because it will
        // probably break mysteriously at some point and I feel terrible about your
        // having to debug it.
        byte[] jpegBytes = "file:fake".getBytes();
        ShadowBitmapFactory.provideWidthAndHeightHints("fake", 1024, 500);
        NetworkResponse jpeg = new NetworkResponse(jpegBytes);

        // Scale the image uniformly (maintain the image's aspect ratio) so that
        // both dimensions (width and height) of the image will be equal to or
        // less than the corresponding dimension of the view.
        ScaleType scalteType = ScaleType.CENTER_INSIDE;

        // Exact sizes
        verifyResize(jpeg, 512, 250, scalteType, 512, 250); // exactly half
        verifyResize(jpeg, 511, 249, scalteType, 509, 249); // just under half
        verifyResize(jpeg, 1080, 500, scalteType, 1024, 500); // larger
        verifyResize(jpeg, 500, 500, scalteType, 500, 244); // keep same ratio

        // Specify only width, preserve aspect ratio
        verifyResize(jpeg, 512, 0, scalteType, 512, 250);
        verifyResize(jpeg, 800, 0, scalteType, 800, 390);
        verifyResize(jpeg, 1024, 0, scalteType, 1024, 500);

        // Specify only height, preserve aspect ratio
        verifyResize(jpeg, 0, 250, scalteType, 512, 250);
        verifyResize(jpeg, 0, 391, scalteType, 800, 391);
        verifyResize(jpeg, 0, 500, scalteType, 1024, 500);

        // No resize
        verifyResize(jpeg, 0, 0, scalteType, 1024, 500);


        // Scale the image uniformly (maintain the image's aspect ratio) so that
        // both dimensions (width and height) of the image will be equal to or
        // larger than the corresponding dimension of the view.
        scalteType = ScaleType.CENTER_CROP;

        // Exact sizes
        verifyResize(jpeg, 512, 250, scalteType, 512, 250);
        verifyResize(jpeg, 511, 249, scalteType, 511, 249);
        verifyResize(jpeg, 1080, 500, scalteType, 1024, 500);
        verifyResize(jpeg, 500, 500, scalteType, 1024, 500);

        // Specify only width
        verifyResize(jpeg, 512, 0, scalteType, 512, 250);
        verifyResize(jpeg, 800, 0, scalteType, 800, 390);
        verifyResize(jpeg, 1024, 0, scalteType, 1024, 500);

        // Specify only height
        verifyResize(jpeg, 0, 250, scalteType, 512, 250);
        verifyResize(jpeg, 0, 391, scalteType, 800, 391);
        verifyResize(jpeg, 0, 500, scalteType, 1024, 500);

        // No resize
        verifyResize(jpeg, 0, 0, scalteType, 1024, 500);


        // Scale in X and Y independently, so that src matches dst exactly. This
        // may change the aspect ratio of the src.
        scalteType = ScaleType.FIT_XY;

        // Exact sizes
        verifyResize(jpeg, 512, 250, scalteType, 512, 250);
        verifyResize(jpeg, 511, 249, scalteType, 511, 249);
        verifyResize(jpeg, 1080, 500, scalteType, 1024, 500);
        verifyResize(jpeg, 500, 500, scalteType, 500, 500);

        // Specify only width
        verifyResize(jpeg, 512, 0, scalteType, 512, 500);
        verifyResize(jpeg, 800, 0, scalteType, 800, 500);
        verifyResize(jpeg, 1024, 0, scalteType, 1024, 500);

        // Specify only height
        verifyResize(jpeg, 0, 250, scalteType, 1024, 250);
        verifyResize(jpeg, 0, 391, scalteType, 1024, 391);
        verifyResize(jpeg, 0, 500, scalteType, 1024, 500);

        // No resize
        verifyResize(jpeg, 0, 0, scalteType, 1024, 500);
    }

    private void verifyResize(NetworkResponse networkResponse, int maxWidth, int maxHeight,
                              ScaleType scaleType, int expectedWidth, int expectedHeight) {
        ImageRequest request = new ImageRequest("", null, maxWidth, maxHeight, scaleType,
                Config.RGB_565, null);
        Response<Bitmap> response = request.parseNetworkResponse(networkResponse);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        Bitmap bitmap = response.result;
        assertNotNull(bitmap);
        assertEquals(expectedWidth, bitmap.getWidth());
        assertEquals(expectedHeight, bitmap.getHeight());
    }

    @Test public void findBestSampleSize() {
        // desired == actual == 1
        assertEquals(1, ImageRequest.findBestSampleSize(100, 150, 100, 150));

        // exactly half == 2
        assertEquals(2, ImageRequest.findBestSampleSize(280, 160, 140, 80));

        // just over half == 1
        assertEquals(1, ImageRequest.findBestSampleSize(1000, 800, 501, 401));

        // just under 1/4 == 4
        assertEquals(4, ImageRequest.findBestSampleSize(100, 200, 24, 50));
    }

    private static byte[] readInputStream(InputStream in) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count;
        while ((count = in.read(buffer)) != -1) {
            bytes.write(buffer, 0, count);
        }
        in.close();
        return bytes.toByteArray();
    }

    @Test
    public void publicMethods() throws Exception {
        // Catch-all test to find API-breaking changes.
        assertNotNull(ImageRequest.class.getConstructor(String.class, Response.Listener.class,
                int.class, int.class, Bitmap.Config.class, Response.ErrorListener.class));
        assertNotNull(ImageRequest.class.getConstructor(String.class, Response.Listener.class,
                int.class, int.class, ImageView.ScaleType.class, Bitmap.Config.class,
                Response.ErrorListener.class));
    }
}
