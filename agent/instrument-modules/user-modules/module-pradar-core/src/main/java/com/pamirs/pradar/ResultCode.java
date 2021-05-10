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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * @author xiaobin.zfb
 * @since 2020/7/8 2:22 下午
 */
public final class ResultCode {
    /**
     * 返回成功
     */
    static public final String INVOKE_RESULT_SUCCESS = "00";

    /**
     * 返回失败，一般是业务失败
     */
    static public final String INVOKE_RESULT_FAILED = "01";

    /**
     * 返回DUBBO错误
     */
    static public final String INVOKE_RESULT_DUBBO_ERR = "02";

    /**
     * 返回超时错误
     */
    static public final String INVOKE_RESULT_TIMEOUT = "03";

    /**
     * 未知
     */
    static public final String INVOKE_RESULT_UNKNOWN = "04";

    /**
     * 判断结果码是否是成功
     *
     * @param resultCode 结果编码
     * @return 返回结果编码是否是成功
     */
    public static boolean isOk(String resultCode) {
        if (StringUtils.equals(resultCode, INVOKE_RESULT_SUCCESS)) {
            return true;
        }
        if (NumberUtils.isDigits(resultCode)) {
            Integer status = Integer.valueOf(resultCode);
            if (status >= 200 && status < 400) {
                return true;
            }
        }
        return false;
    }
}
