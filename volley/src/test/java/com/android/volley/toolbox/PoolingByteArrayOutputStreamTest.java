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

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.*;

public class PoolingByteArrayOutputStreamTest {
    @Test public void pooledOneBuffer() throws IOException {
        ByteArrayPool pool = new ByteArrayPool(32768);
        writeOneBuffer(pool);
        writeOneBuffer(pool);
        writeOneBuffer(pool);
    }

    @Test public void pooledIndividualWrites() throws IOException {
        ByteArrayPool pool = new ByteArrayPool(32768);
        writeBytesIndividually(pool);
        writeBytesIndividually(pool);
        writeBytesIndividually(pool);
    }

    @Test public void unpooled() throws IOException {
        ByteArrayPool pool = new ByteArrayPool(0);
        writeOneBuffer(pool);
        writeOneBuffer(pool);
        writeOneBuffer(pool);
    }

    @Test public void unpooledIndividualWrites() throws IOException {
        ByteArrayPool pool = new ByteArrayPool(0);
        writeBytesIndividually(pool);
        writeBytesIndividually(pool);
        writeBytesIndividually(pool);
    }

    private void writeOneBuffer(ByteArrayPool pool) throws IOException {
        byte[] data = new byte[16384];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (i & 0xff);
        }
        PoolingByteArrayOutputStream os = new PoolingByteArrayOutputStream(pool);
        os.write(data);

        assertTrue(Arrays.equals(data, os.toByteArray()));
    }

    private void writeBytesIndividually(ByteArrayPool pool) {
        byte[] data = new byte[16384];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (i & 0xff);
        }
        PoolingByteArrayOutputStream os = new PoolingByteArrayOutputStream(pool);
        for (int i = 0; i < data.length; i++) {
            os.write(data[i]);
        }

        assertTrue(Arrays.equals(data, os.toByteArray()));
    }
}
