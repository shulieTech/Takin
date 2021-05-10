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
package com.shulie.instrument.simulator.module.model.jad;

import java.io.Serializable;

/**
 * jad 反编译结果
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/11 6:48 下午
 */
public class JadInfo implements Serializable {
    private final static long serialVersionUID = 1L;

    /**
     * classloader 的信息
     */
    private String classloader;

    /**
     * 位置
     */
    private String location;

    /**
     * class源文件内容
     */
    private String source;

    public String getClassloader() {
        return classloader;
    }

    public void setClassloader(String classloader) {
        this.classloader = classloader;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        builder.append("/**\n * ClassLoader: ");
        builder.append(classloader);
        builder.append("\n * Location: ");
        builder.append(location);
        builder.append("\n */ \n");
        builder.append(source);
        builder.append("\n");
        return builder.toString();
    }
}
