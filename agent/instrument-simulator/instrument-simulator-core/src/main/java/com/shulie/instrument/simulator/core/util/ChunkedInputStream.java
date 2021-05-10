/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shulie.instrument.simulator.core.util;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ChunkedInputStream extends InputStream {

    private InputStream in;

    private boolean closed = false;

    private boolean bof = true;

    private boolean eof = false;

    private int chunkSize;

    private int pos;

    public ChunkedInputStream(final InputStream in) {
        if (in == null) {
            throw new IllegalArgumentException("InputStream parameter may not be null");
        }
        this.in = in;
        this.pos = 0;
    }

    @Override
    public int read() throws IOException {
        if (closed) {
            throw new IOException("Attempted read from closed stream.");
        }
        if (eof) {
            return -1;
        }
        if (pos >= chunkSize) {
            nextChunk();
            if (eof) {
                return -1;
            }
        }
        pos++;
        return in.read();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {

        if (closed) {
            throw new IOException("Attempted read from closed stream.");
        }

        if (eof) {
            return -1;
        }
        if (pos >= chunkSize) {
            nextChunk();
            if (eof) {
                return -1;
            }
        }
        len = Math.min(len, chunkSize - pos);
        int count = in.read(b, off, len);
        pos += count;
        return count;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    private void readCRLF() throws IOException {
        int cr = in.read();
        int lf = in.read();
        if ((cr != '\r') || (lf != '\n')) {
            throw new IOException(
                    "CRLF expected at end of chunk: " + cr + "/" + lf);
        }
    }

    private void nextChunk() throws IOException {
        if (!bof) {
            readCRLF();
        }
        chunkSize = getChunkSizeFromInputStream(in);
        bof = false;
        pos = 0;
        if (chunkSize == 0) {
            HttpUtils.readHeaders(in);
            eof = true;
        }
    }

    private static int getChunkSizeFromInputStream(final InputStream in)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int state = 0;
        while (state != -1) {
            int b = in.read();
            if (b == -1) {
                throw new IOException("chunked stream ended unexpectedly");
            }
            switch (state) {
                case 0:
                    switch (b) {
                        case '\r':
                            state = 1;
                            break;
                        case '\"':
                            state = 2;
                            /* fall through */
                        default:
                            baos.write(b);
                    }
                    break;

                case 1:
                    if (b == '\n') {
                        state = -1;
                    } else {
                        // this was not CRLF
                        throw new IOException("Protocol violation: Unexpected"
                                + " single newline character in chunk size");
                    }
                    break;

                case 2:
                    switch (b) {
                        case '\\':
                            b = in.read();
                            baos.write(b);
                            break;
                        case '\"':
                            state = 0;
                            /* fall through */
                        default:
                            baos.write(b);
                    }
                    break;
                default:
                    throw new RuntimeException("assertion failed");
            }
        }

        //parse data
        String dataString = new String(baos.toByteArray(), "US-ASCII");
        int separator = dataString.indexOf(';');
        dataString = (separator > 0)
                ? dataString.substring(0, separator).trim()
                : dataString.trim();

        int result;
        try {
            result = Integer.parseInt(dataString.trim(), 16);
        } catch (NumberFormatException e) {
            throw new IOException("Bad chunk size: " + dataString);
        }
        return result;
    }

    @Override
    public void close() throws IOException {
        if (!closed) {
            try {
                if (!eof) {
                    HttpUtils.exhaustInputStream(this);
                }
            } finally {
                eof = true;
                closed = true;
            }
        }
    }

}
