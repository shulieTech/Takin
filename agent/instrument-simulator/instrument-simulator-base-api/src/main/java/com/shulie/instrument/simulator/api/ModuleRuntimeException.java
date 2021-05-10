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
 * 模块异常
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public class ModuleRuntimeException extends RuntimeException {

    /**
     * 模块ID
     */
    private final String uniqueId;

    /**
     * 错误码
     */
    private final ErrorCode errorCode;

    /**
     * 构造模块异常
     *
     * @param uniqueId  模块ID
     * @param errorCode 错误码
     */
    public ModuleRuntimeException(final String uniqueId,
                                  final ErrorCode errorCode) {
        this.uniqueId = uniqueId;
        this.errorCode = errorCode;
    }

    /**
     * 构造模块异常
     *
     * @param uniqueId  模块ID
     * @param errorCode 错误码
     * @param cause     错误原因
     */
    public ModuleRuntimeException(final String uniqueId,
                                  final ErrorCode errorCode,
                                  final Throwable cause) {
        super(cause);
        this.uniqueId = uniqueId;
        this.errorCode = errorCode;
    }

    /**
     * 获取模块ID
     *
     * @return 模块ID
     */
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * 错误码
     */
    public enum ErrorCode {

        /**
         * 模块不存在
         */
        MODULE_NOT_EXISTED,
        /**
         * 模块命令不存在
         */
        MODULE_COMMAND_NOT_EXISTED,

        /**
         * 模块加载失败
         */
        MODULE_LOAD_ERROR,

        /**
         * 模块卸载失败
         */
        MODULE_UNLOAD_ERROR,

        /**
         * 模块激活失败
         */
        MODULE_ACTIVE_ERROR,

        /**
         * 模块冻结失败
         */
        MODULE_FROZEN_ERROR,

        /**
         * 模块依赖错误
         */
        MODULE_DEPENDENCY_ERROR,
    }
}
