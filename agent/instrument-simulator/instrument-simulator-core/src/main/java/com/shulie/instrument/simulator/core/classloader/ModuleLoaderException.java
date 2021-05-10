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
package com.shulie.instrument.simulator.core.classloader;

/**
 * 模块异常
 *
 */
public class ModuleLoaderException extends RuntimeException {

    public ModuleLoaderException() {
    }

    public ModuleLoaderException(String message) {
        super(message);
    }

    public ModuleLoaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModuleLoaderException(Throwable cause) {
        super(cause);
    }

    public ModuleLoaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
