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
package com.shulie.instrument.simulator.compatible.jdk9.module;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ServiceDescriptor 的解析器
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/9 8:23 下午
 */
public class ServiceDescriptorParser {

    /**
     * 按行读取流中的内容
     *
     * @param inputStream 输入流
     * @return 返回流中的所有内容
     * @throws IOException
     */
    public List<String> parse(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        BufferedReader bufferedInputStream = new BufferedReader(inputStreamReader);
        Set<String> lineSet = new HashSet<>();
        String line;
        while ((line = bufferedInputStream.readLine()) != null) {
            line = trim(line);
            if (line != null) {
                lineSet.add(line);
            }
        }
        return new ArrayList<>(lineSet);
    }

    /**
     * trim 行数据
     *
     * @param line 行数据
     * @return
     */
    private String trim(String line) {
        final int commentIndex = line.indexOf('#');
        if (commentIndex != -1) {
            line = line.substring(0, commentIndex);
        }
        line = line.trim();
        if (line.isEmpty()) {
            return null;
        }
        return line;
    }
}
