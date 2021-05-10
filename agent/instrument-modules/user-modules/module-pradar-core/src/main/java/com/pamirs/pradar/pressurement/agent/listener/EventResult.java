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
package com.pamirs.pradar.pressurement.agent.listener;

import java.io.Serializable;

/**
 * @Author: mubai<chengjiacai @ shulie.io>
 * @Date: 2020-04-07 10:46
 * @Description:
 */
public class EventResult implements Serializable {
    /**
     * 如果忽略该事件则必须返回此对象
     */
    public final static EventResult IGNORE = new EventResult(null, true, "");

    private final static long serialVersionUID = 1L;

    /**
     * 插件唯一标记（不允许为空）
     */
    private Object target;

    /**
     * 执行结果
     */
    private boolean success = false;

    /**
     * 异常信息
     */
    private String errorMsg;

    /**
     * 是否关闭压测全局开关
     */
    private Boolean isClosePradar = Boolean.FALSE;

    /**
     * 配置名称
     */
    private String configName;

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public EventResult() {
    }

    public EventResult(Object target, boolean success, String errorMsg) {
        this.target = target;
        this.success = success;
        this.errorMsg = errorMsg;
    }

    public EventResult(Object target, boolean success, String errorMsg, String configName) {
        this.target = target;
        this.success = success;
        this.errorMsg = errorMsg;
        this.configName = configName;
    }

    public String getConfigName() {
        return configName;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Boolean getClosePradar() {
        return isClosePradar;
    }

    public void setClosePradar(Boolean closePradar) {
        isClosePradar = closePradar;
    }

    public static EventResult success(Object target) {
        EventResult result = new EventResult();
        result.setSuccess(true);
        result.setTarget(target);
        return result;
    }

    public static EventResult error(Object target, String errorMsg) {
        EventResult result = new EventResult();
        result.setSuccess(false);
        result.setTarget(target);
        result.setErrorMsg(errorMsg);
        return result;
    }
}
