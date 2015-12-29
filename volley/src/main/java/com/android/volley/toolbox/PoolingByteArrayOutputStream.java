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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A variation of {@link java.io.ByteArrayOutputStream} that uses a pool of byte[] buffers instead
 * of always allocating them fresh, saving on heap churn.
 */
public class PoolingByteArrayOutputStream extends ByteArrayOutputStream {
    /**
     * If the {@link #PoolingByteArrayOutputStream(ByteArrayPool)} constructor is called, this is
     * the default size to which the underlying byte array is initialized.
     */
    private static final int DEFAULT_SIZE = 256;

    private final ByteArrayPool mPool;

    /**
     * Constructs a new PoolingByteArrayOutputStream with a default size. If more bytes are written
     * to this instance, the underlying byte array will expand.
     */
    public PoolingByteArrayOutputStream(ByteArrayPool pool) {
        this(pool, DEFAULT_SIZE);
    }

    /**
     * Constructs a new {@code ByteArrayOutputStream} with a default size of {@code size} bytes. If
     * more than {@code size} bytes are written to this instance, the underlying byte array will
     * expand.
     *
     * @param size initial size for the underlying byte array. The value will be pinned to a default
     *             minimum size.
     */
    public PoolingByteArrayOutputStream(ByteArrayPool pool, int size) {
        mPool = pool;
        buf = mPool.getBuf(Math.max(size, DEFAULT_SIZE));
    }

    @Override
    public void close() throws IOException {
        mPool.returnBuf(buf);
        buf = null;
        super.close();
    }

    @Override
    public void finalize() {
        mPool.returnBuf(buf);
    }

    /**
     * Ensures there is enough space in the buffer for the given number of additional bytes.
     */
    private void expand(int i) {
        /* Can the buffer handle @i more bytes, if not expand it */
        if (count + i <= buf.length) {
            return;
        }
        byte[] newbuf = mPool.getBuf((count + i) * 2);
        System.arraycopy(buf, 0, newbuf, 0, count);
        mPool.returnBuf(buf);
        buf = newbuf;
    }

    @Override
    public synchronized void write(byte[] buffer, int offset, int len) {
        expand(len);
        super.write(buffer, offset, len);
    }

    @Override
    public synchronized void write(int oneByte) {
        expand(1);
        super.write(oneByte);
    }
}
