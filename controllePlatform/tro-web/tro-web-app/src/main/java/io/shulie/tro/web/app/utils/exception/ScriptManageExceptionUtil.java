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

package io.shulie.tro.web.app.utils.exception;

import io.shulie.tro.web.app.exception.ExceptionCode;
import io.shulie.tro.web.app.exception.TroWebException;

/**
 * 脚本相关异常工具
 *
 * @author liuchuan
 * @date 2021/4/20 4:32 下午
 */
public class ScriptManageExceptionUtil {

    /**
     * 脚本异常下的 创建验证错误
     *
     * @param message 错误信息
     * @return 创建验证错误
     */
    public static TroWebException getCreateValidError(String message) {
        return new TroWebException(ExceptionCode.SCRIPT_MANAGE_CREATE_VALID_ERROR, message);
    }

    /**
     * 是否是 更新验证错误
     * 先判断条件, 如果真, 就抛出异常
     *
     * @param condition 条件
     * @param message   错误信息
     */
    public static void isCreateValidError(boolean condition, String message) {
        if (condition) {
            throw getCreateValidError(message);
        }
    }

    /**
     * 脚本异常下的 更新验证错误
     *
     * @param message 错误信息
     * @return 更新验证错误
     */
    public static TroWebException getUpdateValidError(String message) {
        return new TroWebException(ExceptionCode.SCRIPT_MANAGE_UPDATE_VALID_ERROR, message);
    }

    /**
     * 是否是 更新验证错误
     * 先判断条件, 如果真, 就抛出异常
     *
     * @param condition 条件
     * @param message   错误信息
     */
    public static void isUpdateValidError(boolean condition, String message) {
        if (condition) {
            throw getUpdateValidError(message);
        }
    }

}
