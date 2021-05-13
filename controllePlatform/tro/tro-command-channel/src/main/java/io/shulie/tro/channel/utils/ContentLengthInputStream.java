package io.shulie.tro.channel.utils;

import java.io.IOException;
import java.io.InputStream;

public class ContentLengthInputStream extends InputStream {

    private long contentLength;

    private long pos = 0;

    private boolean closed = false;

    private InputStream wrappedStream = null;

    public ContentLengthInputStream(InputStream in, int contentLength) {
        this(in, (long) contentLength);
    }

    public ContentLengthInputStream(InputStream in, long contentLength) {
        super();
        this.wrappedStream = in;
        this.contentLength = contentLength;
    }

    public void close() throws IOException {
        if (!closed) {
            try {
                HttpUtils.exhaustInputStream(this);
            } finally {
                closed = true;
            }
        }
    }

    public int read() throws IOException {
        if (closed) {
            throw new IOException("Attempted read from closed stream.");
        }

        if (pos >= contentLength) {
            return -1;
        }
        pos++;
        return this.wrappedStream.read();
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (closed) {
            throw new IOException("Attempted read from closed stream.");
        }

        if (pos >= contentLength) {
            return -1;
        }

        if (pos + len > contentLength) {
            len = (int) (contentLength - pos);
        }
        int count = this.wrappedStream.read(b, off, len);
        pos += count;
        return count;
    }

    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    public long skip(long n) throws IOException {
        // make sure we don't skip more bytes than are
        // still available
        long length = Math.min(n, contentLength - pos);
        // skip and keep track of the bytes actually skipped
        length = this.wrappedStream.skip(length);
        // only add the skipped bytes to the current position
        // if bytes were actually skipped
        if (length > 0) {
            pos += length;
        }
        return length;
    }

    public int available() throws IOException {
        if (this.closed) {
            return 0;
        }
        int avail = this.wrappedStream.available();
        if (this.pos + avail > this.contentLength) {
            avail = (int) (this.contentLength - this.pos);
        }
        return avail;
    }

}
