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
package com.pamirs.attach.plugin.command.handler;

import com.shulie.instrument.simulator.api.resource.ModuleCommandInvoker;
import io.shulie.tro.channel.bean.CommandPacket;
import io.shulie.tro.channel.bean.CommandResponse;
import io.shulie.tro.channel.bean.CommandSend;
import io.shulie.tro.channel.bean.CommandStatus;
import io.shulie.tro.channel.handler.CommandHandler;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 与控制台交互的日志拉取命令处理器
 *
 * @author wangjian
 * @since 2021/1/5 19:18
 */
public class DispatcherCommandHandler implements CommandHandler {

    private ModuleCommandInvoker invoker;

    public DispatcherCommandHandler(ModuleCommandInvoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public CommandResponse handle(CommandPacket commandPacket) {
        CommandResponse commandResponse = new CommandResponse();
        if (!CommandStatus.COMMAND_RUNNING.equals(commandPacket.getStatus())) {
            return commandResponse;
        }
        CommandSend send = commandPacket.getSend();
        if (send == null) {
            return CommandResponse.failure("CommandSend param is null.");
        }
        if (StringUtils.isBlank(send.getModuleId())) {
            return CommandResponse.failure("moduleId can't be empty.");
        }

        if (StringUtils.isBlank(send.getCommand())) {
            return CommandResponse.failure("command can't be empty.");
        }

        Map<String, Object> param = send.getParam();

        Map<String, String> request = new HashMap<String, String>();
        if (param != null) {
            for (Map.Entry<String, Object> entry : send.getParam().entrySet()) {
                request.put(entry.getKey(), entry.getValue() == null ? null : entry.getValue().toString());
            }
        }

        com.shulie.instrument.simulator.api.CommandResponse response = invoker.invokeCommand(send.getModuleId(), send.getCommand(), request);
        if (!response.isSuccess()) {
            return CommandResponse.failure(response.getMessage());
        } else {
            return CommandResponse.success(response.getResult());
        }
    }
}
