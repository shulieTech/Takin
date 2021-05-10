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
package com.pamirs.pradar;

import java.io.Serializable;

/**
 * @author xiaobin.zfb
 * @since 2020/7/1 1:56 下午
 */
public class CutOffResult implements Serializable {
    private final static long serialVersionUID = 1L;

    public static final CutOffResult PASSED = new CutOffResult(false, null);

    /**
     * 是否截流
     */
    private boolean cutoff;

    /**
     * 替换参数
     */
    private Object result;

    public CutOffResult() {
    }

    public CutOffResult(boolean cutoff, Object result) {
        this.cutoff = cutoff;
        this.result = result;
    }

    public boolean isCutoff() {
        return cutoff;
    }

    public void setCutoff(boolean cutoff) {
        this.cutoff = cutoff;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean isPassed() {
        return !cutoff;
    }

    public static CutOffResult cutoff(Object result) {
        return new CutOffResult(true, result);
    }

    public static CutOffResult passed() {
        return new CutOffResult(false, null);
    }
}
