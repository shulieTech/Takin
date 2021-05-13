/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.tro.k8s.model;

/**
 * @author angju
 * @date 2019/3/3 21:25
 */
public class PaasResponseResult<T> {
    /**
     * 接口请求成功结果
     */
    private boolean result;
    private String msg;
    private T data;

    public PaasResponseResult() {

    }

    public PaasResponseResult(boolean result, String msg) {
        this.result = result;
        this.msg = msg;
    }

    public PaasResponseResult(boolean result, T data) {
        this.result = result;
        this.data = data;
    }

    public PaasResponseResult(boolean result, String msg, T data) {
        this.result = result;
        this.msg = msg;
        this.data = data;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
