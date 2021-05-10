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
package com.shulie.instrument.simulator.module.model.stack;

import java.io.Serializable;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/3 10:47 上午
 */
public class StackElement implements Serializable {
    private final static long serialVersionUID = 1L;

    /**
     * 声明的类
     */
    private String className;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 行号
     */
    private int lineNumber;

    /**
     * 是否是本地方法
     */
    private boolean nativeMethod;

    /**
     * 文件名称
     */
    private String fileName;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public boolean isNativeMethod() {
        return nativeMethod;
    }

    public void setNativeMethod(boolean nativeMethod) {
        this.nativeMethod = nativeMethod;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        String s = className;
        return s + "." + methodName + "(" +
                (isNativeMethod() ? "Native Method)" :
                        (fileName != null && lineNumber >= 0 ?
                                fileName + ":" + lineNumber + ")" :
                                (fileName != null ? "" + fileName + ")" : "Unknown Source)")));
    }
}
