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
package com.shulie.instrument.simulator.module.model.info;

/**
 * command 信息
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/4/16 7:08 下午
 */
public class CommandInfo {
    /**
     * 模块 ID
     */
    private String moduleId;

    /**
     * 命令名称
     */
    private String command;

    /**
     * 命令描述
     */
    private String commandDescription;

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommandDescription() {
        return commandDescription;
    }

    public void setCommandDescription(String commandDescription) {
        this.commandDescription = commandDescription;
    }

    @Override
    public String toString() {
        return "{" +
                "moduleId='" + moduleId + '\'' +
                ", command='" + command + '\'' +
                ", commandDescription='" + commandDescription + '\'' +
                '}';
    }
}
