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

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.shulie.amdb.common;

public class ErrorInfo {
    private transient Object[] args;
    private String code;
    private transient String msgTemplate;
    private String msg;

    public ErrorInfo(){}

    public ErrorInfo(String code, String msgTemp, Object... args) {
        this.code = code;
        this.msgTemplate = msgTemp;
        this.args = args;
        this.msg = this.formatErrorMsg();
    }

    public static ErrorInfo build(String code, String msgTemp, Object... args) {
        return new ErrorInfo(code, msgTemp, args);
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsgTemplate() {
        return this.msgTemplate;
    }

    public void setMsgTemplate(String msgTemplate) {
        this.msgTemplate = msgTemplate;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String formatErrorMsg() {
        return this.args != null ? String.format(this.msgTemplate, this.args) : this.msgTemplate;
    }
}
