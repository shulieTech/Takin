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

package io.shulie.tro.web.common.domain;

/**
 * @Auther: vernon
 * @Date: 2019/9/20 17:57
 * @Description:
 */
@Deprecated
public class ErrorInfo {

    /**
     * 错误信息中的参数, 不参与序列化
     */
    private transient Object[] args;

    /**
     * 错误code
     */
    private String code;

    /**
     * 错误信息 ，可以用 %s 占位符 , 不参与序列化
     */
    private transient String msgTemplate;

    /**
     * 最终展示的信息
     */
    private String msg;

    public ErrorInfo() {
    }

    public ErrorInfo(String code, String msgTemp, Object... args) {
        this.code = code;
        this.msgTemplate = msgTemp;
        this.args = args;
        this.msg = this.formatErrorMsg();
    }

    public static ErrorInfo build(String code, String msgTemp, Object... args) {
        return new ErrorInfo(code, msgTemp, args);
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsgTemplate() {
        return msgTemplate;
    }

    public void setMsgTemplate(String msgTemplate) {
        this.msgTemplate = msgTemplate;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String formatErrorMsg() {
        return this.args != null ? String.format(this.msgTemplate, this.args) : this.msgTemplate;
    }
}
