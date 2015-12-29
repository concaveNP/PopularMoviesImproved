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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

public class ByteArrayPoolTest {
    @Test public void reusesBuffer() {
        ByteArrayPool pool = new ByteArrayPool(32);

        byte[] buf1 = pool.getBuf(16);
        byte[] buf2 = pool.getBuf(16);

        pool.returnBuf(buf1);
        pool.returnBuf(buf2);

        byte[] buf3 = pool.getBuf(16);
        byte[] buf4 = pool.getBuf(16);
        assertTrue(buf3 == buf1 || buf3 == buf2);
        assertTrue(buf4 == buf1 || buf4 == buf2);
        assertTrue(buf3 != buf4);
    }

    @Test public void obeysSizeLimit() {
        ByteArrayPool pool = new ByteArrayPool(32);

        byte[] buf1 = pool.getBuf(16);
        byte[] buf2 = pool.getBuf(16);
        byte[] buf3 = pool.getBuf(16);

        pool.returnBuf(buf1);
        pool.returnBuf(buf2);
        pool.returnBuf(buf3);

        byte[] buf4 = pool.getBuf(16);
        byte[] buf5 = pool.getBuf(16);
        byte[] buf6 = pool.getBuf(16);

        assertTrue(buf4 == buf2 || buf4 == buf3);
        assertTrue(buf5 == buf2 || buf5 == buf3);
        assertTrue(buf4 != buf5);
        assertTrue(buf6 != buf1 && buf6 != buf2 && buf6 != buf3);
    }

    @Test public void returnsBufferWithRightSize() {
        ByteArrayPool pool = new ByteArrayPool(32);

        byte[] buf1 = pool.getBuf(16);
        pool.returnBuf(buf1);

        byte[] buf2 = pool.getBuf(17);
        assertNotSame(buf2, buf1);

        byte[] buf3 = pool.getBuf(15);
        assertSame(buf3, buf1);
    }
}
