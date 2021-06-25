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

package io.shulie.tro.web.app.context;

/**
 * @author shiyajian
 * create: 2020-09-16
 */
public final class OperationLogContextHolder {

    private final static InheritableThreadLocal<OperationLogContext> HOLDER = new InheritableThreadLocal<>();

    private OperationLogContextHolder() { /* no instance */ }

    public static void operationType(String type) {
        OperationLogContext operationLogContext = HOLDER.get();
        if (operationLogContext == null) {
            operationLogContext = new OperationLogContext();
            HOLDER.set(operationLogContext);
        }
        operationLogContext.setOperationType(type);
    }

    public static void addVars(String key, String value) {
        OperationLogContext operationLogContext = HOLDER.get();
        if (operationLogContext == null) {
            operationLogContext = new OperationLogContext();
            HOLDER.set(operationLogContext);
        }
        operationLogContext.getVars().put(key, value);
    }

    public static void ignoreLog() {
        OperationLogContext operationLogContext = HOLDER.get();
        if (operationLogContext == null) {
            operationLogContext = new OperationLogContext();
            HOLDER.set(operationLogContext);
        }
        operationLogContext.setIgnore(true);
    }

    public static OperationLogContext get() {
        return HOLDER.get();
    }

    public static void reset() {
        HOLDER.remove();
        HOLDER.set(new OperationLogContext());
    }

    public static void start() {
        OperationLogContext operationLogContext = HOLDER.get();
        operationLogContext.setStartTime(System.currentTimeMillis());
    }

    public static void end(boolean isSuccess) {
        OperationLogContext operationLogContext = HOLDER.get();
        Long endTime = System.currentTimeMillis();
        operationLogContext.setEndTime(endTime);
        operationLogContext.setSuccess(isSuccess);
        operationLogContext.setCostTime(endTime - operationLogContext.getStartTime());
    }
}
