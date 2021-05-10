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
package com.shulie.instrument.simulator.module.model.profiler;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/3/9 3:34 下午
 */
public class ProfilerModel implements Serializable {
    private final static long serialVersionUID = 1L;

    private String action;
    private String actionArg;
    private String executeResult;
    private Collection<String> supportedActions;
    private String outputFile;
    private Long duration;

    public ProfilerModel() {
    }

    public ProfilerModel(Collection<String> supportedActions) {
        this.supportedActions = supportedActions;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionArg() {
        return actionArg;
    }

    public void setActionArg(String actionArg) {
        this.actionArg = actionArg;
    }

    public Collection<String> getSupportedActions() {
        return supportedActions;
    }

    public void setSupportedActions(Collection<String> supportedActions) {
        this.supportedActions = supportedActions;
    }

    public String getExecuteResult() {
        return executeResult;
    }

    public void setExecuteResult(String executeResult) {
        this.executeResult = executeResult;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "ProfilerModel{" +
                "action='" + action + '\'' +
                ", actionArg='" + actionArg + '\'' +
                ", executeResult='" + executeResult + '\'' +
                ", supportedActions=" + supportedActions +
                ", outputFile='" + outputFile + '\'' +
                ", duration=" + duration +
                '}';
    }
}
