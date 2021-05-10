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
package com.shulie.instrument.simulator.module.util;

import com.shulie.instrument.simulator.module.model.stack.StackElement;
import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

abstract public class ThreadUtil {

    /**
     * 获取方法执行堆栈信息
     *
     * @return 方法堆栈信息
     */
    public static List<StackElement> getThreadStack(Thread currentThread) {
        StackTraceElement[] stackTraceElementArray = currentThread.getStackTrace();
        List<StackElement> stackElements = new ArrayList<StackElement>();
        if (ArrayUtils.isNotEmpty(stackTraceElementArray)) {
            for (StackTraceElement element : stackTraceElementArray) {
                StackElement stackElement = new StackElement();
                stackElement.setClassName(element.getClassName());
                stackElement.setMethodName(element.getMethodName());
                stackElement.setLineNumber(element.getLineNumber());
                stackElement.setFileName(element.getFileName());
                stackElement.setNativeMethod(element.isNativeMethod());
                stackElements.add(stackElement);
            }
        }
        return stackElements;
    }
}
