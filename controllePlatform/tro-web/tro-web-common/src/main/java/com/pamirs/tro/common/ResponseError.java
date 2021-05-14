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

package com.pamirs.tro.common;

import java.util.HashMap;

import com.pamirs.tro.common.constant.TROErrorEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 说明: 失败信息返回类
 *
 * @author shulie
 * @version v1.0
 * @date 2018年4月13日
 */
@Deprecated
public class ResponseError {

    /**
     * 创建失败信息返回类
     *
     * @param msg 失败信息
     * @return 包含失败信息的失败信息返回类
     * @author shulie
     * @date 2018年5月21日
     * @version v1.0
     */
    public static ResponseEntity<Object> create(String msg) {
        ResponseResult error = new ResponseResult();
        error.setCode(400);
        error.setMessage(msg);
        return ResponseEntity.status(HttpStatus.OK).body(error);
    }

    public static ResponseEntity<Object> create(TROErrorEnum errorEnum) {
        ResponseResult error = new ResponseResult();
        error.setCode(errorEnum.getErrorCode());
        error.setMessage(errorEnum.getErrorMessage());
        return ResponseEntity.status(HttpStatus.OK).body(error);
    }

    /**
     * 创建失败信息返回类
     *
     * @param code 失败码
     * @param msg  失败信息
     * @return 包含失败信息的失败信息返回类
     * @author shulie
     * @date 2018年5月21日
     * @version v1.0
     */
    public static ResponseEntity<Object> create(int code, String msg) {
        ResponseResult error = new ResponseResult();
        error.setCode(code);
        error.setMessage(msg);
        return ResponseEntity.status(HttpStatus.OK).body(error);
    }

    /**
     * 创建失败信息返回类
     *
     * @param code 失败码
     * @param msg  失败信息
     * @param data 失败后返回的响应对象
     * @return 包含失败信息的失败信息返回类
     * @author shulie
     * @date 2018年5月21日
     * @version v1.0
     */
    public static ResponseEntity<Object> create(int code, String msg, Object data) {
        ResponseResult error = new ResponseResult();
        error.setCode(code);
        error.setMessage(msg);
        error.put("data", data);
        return ResponseEntity.status(HttpStatus.OK).body(error);
    }

    /**
     * 响应结果类(内部类)
     *
     * @author shulie
     * @version v1.0
     * @date 2018年5月21日
     */
    public static class ResponseResult extends HashMap<String, Object> {

        private static final long serialVersionUID = 1L;

        /**
         * 获取响应信息
         *
         * @return 响应信息
         * @author shulie
         * @date 2018年5月21日
         * @version v1.0
         */
        public String getMessage() {
            return (String)this.get("message");
        }

        /**
         * 设置响应信息
         *
         * @param message 响应信息
         * @author shulie
         * @date 2018年5月21日
         * @version v1.0
         */
        public void setMessage(String message) {
            this.put("message", message);
        }

        /**
         * 获取响应码
         *
         * @return 响应码
         * @author shulie
         * @date 2018年5月21日
         * @version v1.0
         */
        public int getCode() {
            return (Integer)this.get("code");
        }

        /**
         * 设置响应码
         *
         * @param code 响应码
         * @author shulie
         * @date 2018年5月21日
         * @version v1.0
         */
        public void setCode(int code) {
            this.put("code", code);
        }

    }
}
