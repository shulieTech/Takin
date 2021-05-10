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
package com.pamirs.attach.plugin.common.web;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * Request 包装类
 * 注意：这种使用方法并不是最佳实践，很容易带来类找不到的问题，所以在重写方法的时候需要注意避免加载新的业务类
 * 因为这个时候Simulator 已经获取不到业务类加载器了，就会报找不到类的错误
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/13 8:02 下午
 */
public class BufferedServletRequestWrapper extends HttpServletRequestWrapper implements IBufferedServletRequestWrapper {

    private byte[] buffer;
    private HttpServletRequest request;

    public BufferedServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    private void initBuffer() {
        try {
            InputStream is = request.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte buff[] = new byte[1024];
            int read;
            while ((read = is.read(buff)) > 0) {
                baos.write(buff, 0, read);
            }
            this.buffer = baos.toByteArray();
        } catch (IOException e) {
            //ignore
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (this.buffer == null) {
            initBuffer();
        }
        return new BufferedServletInputStream(this.buffer);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (this.buffer == null) {
            initBuffer();
        }
        String characterEncoding = request.getCharacterEncoding();
        if (characterEncoding == null) {
            characterEncoding = "ISO8859-1";
        }
        return new BufferedReader(new InputStreamReader(getInputStream(), characterEncoding));
    }

    @Override
    public byte[] getBody() {
        if (null == this.buffer) {
            return null;
        }
        return this.buffer;
    }

    static class BufferedServletInputStream extends ServletInputStream {
        private ByteArrayInputStream inputStream;

        public BufferedServletInputStream(byte[] buffer) {
            this.inputStream = new ByteArrayInputStream(buffer);
        }

        @Override
        public int available() throws IOException {
            return inputStream.available();
        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return inputStream.read(b, off, len);
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }

    }
}
