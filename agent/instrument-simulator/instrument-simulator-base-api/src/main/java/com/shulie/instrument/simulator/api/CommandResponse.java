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
package com.shulie.instrument.simulator.api;

/**
 * 命令响应
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/9/29 1:45 下午
 */
public class CommandResponse<T> {
    private boolean success;
    private T result;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static <T> CommandResponse<T> success(T result) {
        CommandResponse<T> response = new CommandResponse<T>();
        response.setSuccess(true);
        response.setResult(result);
        return response;
    }

    public static <T> CommandResponse<T> failure(String message) {
        CommandResponse<T> response = new CommandResponse<T>();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }

    public static <T> CommandResponse<T> failure(Throwable e) {
        CommandResponse<T> response = new CommandResponse<T>();
        response.setSuccess(false);
        response.setMessage(ThrowableUtils.toString(e));
        return response;
    }

    public static <T> CommandResponse<T> failure(String message, Throwable e) {
        CommandResponse<T> response = new CommandResponse<T>();
        response.setSuccess(false);
        response.setMessage(message + " " + ThrowableUtils.toString(e));
        return response;
    }

    @Override
    public String toString() {
        if (isSuccess()) {
            return result == null ? "" : result.toString();
        } else {
            return message;
        }
    }
}
