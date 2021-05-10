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
package com.shulie.instrument.simulator.api.filter;

/**
 * Access标记体系
 * <p>
 * 用于修饰{@link Filter#doClassFilter(ClassDescriptor)}和
 * {@link Filter#doMethodFilter(MethodDescriptor)}的access
 * </p>
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public class AccessFlags {

    private static final int BASE = 0x01;

    public static final int ACF_PUBLIC = BASE << 0;
    public static final int ACF_PRIVATE = BASE << 1;
    public static final int ACF_PROTECTED = BASE << 2;
    public static final int ACF_STATIC = BASE << 3;
    public static final int ACF_FINAL = BASE << 4;
    public static final int ACF_INTERFACE = BASE << 5;
    public static final int ACF_NATIVE = BASE << 6;
    public static final int ACF_ABSTRACT = BASE << 7;
    public static final int ACF_ENUM = BASE << 8;
    public static final int ACF_ANNOTATION = BASE << 9;

    /**
     * 访问修改符
     */
    private final int accessFlag;

    public AccessFlags(int accessFlag) {
        this.accessFlag = accessFlag;
    }

    public int getAccessFlag() {
        return accessFlag;
    }

}
