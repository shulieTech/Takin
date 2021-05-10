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
package com.shulie.instrument.simulator.core.inject.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by xiaobin on 2017/1/19.
 */
public class JarReader {

    private final Logger logger = LoggerFactory.getLogger(JarReader.class.getName());

    private static final int BUFFER_SIZE = 1024 * 4;

    private final JarFile jarFile;

    public JarReader(JarFile jarFile) {
        if (jarFile == null) {
            throw new NullPointerException("jarFile must not be null");
        }
        this.jarFile = jarFile;
    }

    public List<FileBinary> read(JarEntryFilter jarEntryFilter) throws IOException {
        if (jarEntryFilter == null) {
            throw new NullPointerException("jarEntryFilter must not be null");
        }

        final BufferedContext bufferedContext = new BufferedContext();

        String jarFileName = jarFile.getName();
        Enumeration<JarEntry> entries = jarFile.entries();
        List<FileBinary> fileBinaryList = new ArrayList<FileBinary>();
        while (entries.hasMoreElements()) {
            final JarEntry jarEntry = entries.nextElement();
            if (jarEntryFilter.filter(jarEntry)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("SIMULATOR: filter fileName:{}, JarFile:{}", jarEntry.getName(), jarFileName);
                }
                FileBinary fileBinary = newFileBinary(bufferedContext, jarEntry, jarFileName);
                fileBinaryList.add(fileBinary);
            }
        }
        return fileBinaryList;
    }

    private FileBinary newFileBinary(BufferedContext bufferedContext, JarEntry jarEntry, String jarFileName) throws IOException {
        byte[] binary = bufferedContext.read(jarEntry);
        FileBinary fileBinary = new FileBinary(jarEntry.getName(), binary, jarFileName);
        return fileBinary;


    }

    private class BufferedContext {

        private final byte[] buffer = new byte[BUFFER_SIZE];
        private final ByteArrayOutputStream output = new ByteArrayOutputStream(BUFFER_SIZE);

        private BufferedContext() {
        }

        private byte[] read(JarEntry jarEntry) throws IOException {
            InputStream inputStream = null;
            try {
                inputStream = jarFile.getInputStream(jarEntry);
                if (inputStream == null) {
                    logger.warn("SIMULATOR: jarEntry not found. jarFile:{} jarEntry{}", jarFile, jarEntry);
                    return null;
                }
                return read(inputStream);
            } catch (IOException ioe) {
                logger.warn("SIMULATOR: jarFile read error jarFile:{} jarEntry{} {}", jarFile, jarEntry, ioe.getMessage(), ioe);
                throw ioe;
            } finally {
                close(inputStream);
            }
        }

        public byte[] read(InputStream input) throws IOException {
            this.output.reset();
            read(input, output);
            return output.toByteArray();
        }

        public void read(InputStream input, OutputStream output) throws IOException {
            int readIndex;
            while ((readIndex = input.read(buffer)) != -1) {
                output.write(buffer, 0, readIndex);
            }
        }

        private void close(Closeable closeable) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException ignore) {
                }
            }
        }
    }


}
