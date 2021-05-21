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
package com.pamirs.attach.plugin.catalina.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * @author angju
 * @date 2021/5/18 20:28
 */
public class BufferedReaderWrapper extends BufferedReader {

    private static int defaultCharBufferSize = 8192;

    public BufferedReaderWrapper(Reader in, int sz) {
        super(in, sz);
    }

    public BufferedReaderWrapper(Reader in) {
        this(in, defaultCharBufferSize);
    }


    @Override
    public void mark(int readAheadLimit) throws IOException {
        super.mark(readAheadLimit + 1);
    }

}
