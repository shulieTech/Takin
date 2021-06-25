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

package io.shulie.tro.web.app.agent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import com.google.common.collect.Sets;
import io.shulie.tro.channel.ServerChannel;
import io.shulie.tro.channel.bean.CommandPacket;
import io.shulie.tro.channel.bean.CommandRespType;
import io.shulie.tro.channel.bean.CommandResponse;
import io.shulie.tro.channel.bean.CommandSend;
import io.shulie.tro.channel.bean.CommandStatus;
import io.shulie.tro.web.app.exception.ExceptionCode;
import io.shulie.tro.web.app.exception.TroWebException;
import io.shulie.tro.web.common.future.ResponseFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.factory
 * @date 2021/1/22 3:37 下午
 */
@Component
@Slf4j
public class AgentCommandFactory {

    @Autowired
    private ServerChannel serverChannel;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * agentId:command:moduleId:id
     */
    private String agentKey = "%s:%s:%s:%s";

    @Value("${agent.interactive.tro.web.url:http://192.168.1.107:10086/tro-web}")
    private String troWebUrl;

    public CommandResponse send(AgentCommandEnum commandEnum, String agentId, Map<String, Object> params)
        throws Exception {
        TroWebCommandPacket troPacket = getSendPacket(commandEnum, agentId, params);
        checkPacket(troPacket);
        String key = String.format(agentKey, troPacket.getAgentId(), troPacket.getSend().getCommand(),
            troPacket.getSend().getModuleId(), troPacket.getId());
        ResponseFuture<CommandPacket> future = new ResponseFuture<>(
            troPacket.getTimeoutMillis() == null ? 3000 : troPacket.getTimeoutMillis());
        CommandPacket commandPacket = new CommandPacket();
        BeanUtils.copyProperties(troPacket, commandPacket);
        boolean sendResult = false;
        try {
            sendResult = serverChannel.send(commandPacket, future::success);
        } catch (Exception e) {
            throw new TroWebException(ExceptionCode.AGENT_SEND_ERROR, "agentId：" + troPacket.getAgentId() +
                "上传命令失败：" + e.getLocalizedMessage());
        }
        if (!sendResult) {
            log.error("send command failed, serverChannel.send got a false result ...");
            throw new TroWebException(ExceptionCode.AGENT_REGISTER_ERROR, "agentId：" + troPacket.getAgentId() + "未注册");
        }

        CommandPacket result = null;
        try {
            result = future.waitFor();
        } catch (InterruptedException e) {
            throw new TroWebException(ExceptionCode.AGENT_SEND_ERROR, "agentId：" + troPacket.getAgentId() +
                "获取返回结果失败：" + e.getLocalizedMessage());
        }
        if(CommandRespType.COMMAND_HTTP_PUSH.equals(troPacket.getCommandRespType())) {
            return CommandResponse.success("返回数据至" + troPacket.getResponsePushUrl());
        }
        if (result == null) {
            throw new TroWebException(ExceptionCode.AGENT_RESPONSE_ERROR, "agentId：" + troPacket.getAgentId() +
                "响应结果为空");
        }
        CommandStatus commandStatus = result.getStatus();
        String commandModule = commandPacket.getSend().getModuleId();
        switch (commandStatus) {
            case COMMAND_SEND:
                log.error("execute command error. no response for this command [{}]:", commandModule);
                redisTemplate.opsForValue().set(key, CommandStatus.COMMAND_SEND);
                throw new TroWebException(ExceptionCode.AGENT_RESPONSE_ERROR, "agentId："
                    + troPacket.getAgentId() + "执行" + commandModule + "超过" + troPacket.getTimeoutMillis()
                    + "未得到agent响应");
            case COMMAND_RUNNING:
                log.error("execute command error. execute [{}] command timeout: " + commandModule);
                redisTemplate.opsForValue().set(key, CommandStatus.COMMAND_RUNNING);
                throw new TroWebException(ExceptionCode.AGENT_RESPONSE_ERROR, "agentId："
                    + troPacket.getAgentId() + "执行" + commandModule + "超时" + troPacket.getTimeoutMillis());
            case COMMAND_COMPLETED_FAIL:
                log.error("execute command error. execute [{}] command failed: " + commandModule);
                throw new TroWebException(ExceptionCode.AGENT_RESPONSE_ERROR, "agentId："
                    + troPacket.getAgentId() + "执行" + commandModule + "失败");
            case COMMAND_COMPLETED_SUCCESS:
                log.info("execute command success. execute [{}] command success: " + commandModule);
                return result.getResponse();
            default: {}
        }
        throw new TroWebException(ExceptionCode.AGENT_RESPONSE_ERROR, "agentId：" + troPacket.getAgentId() + "响应失败");
    }

    /**
     * 发送命令体
     *
     * @return
     */
    private TroWebCommandPacket getSendPacket(AgentCommandEnum commandEnum, String agentId,
        Map<String, Object> params) {
        CommandSend commandSend = new CommandSend();
        commandSend.setModuleId(commandEnum.getModuleId());
        commandSend.setCommand(commandEnum.getCommand());
        commandSend.setAgentId(agentId);
        commandSend.setCommandId(commandEnum.getCommandId());
        if (params != null) {
            commandSend.setParam(params);
        }
        TroWebCommandPacket troWebCommandPacket = new TroWebCommandPacket();
        troWebCommandPacket.setStatus(CommandStatus.COMMAND_SEND);
        troWebCommandPacket.setSend(commandSend);
        troWebCommandPacket.setId(UUID.randomUUID().toString());
        troWebCommandPacket.setCommandRespType(commandEnum.getCommandRespType());
        if (StringUtils.isNotBlank(commandEnum.getResponsePushUrl())) {
            troWebCommandPacket.setResponsePushUrl(troWebUrl + commandEnum.getResponsePushUrl());
        }
        troWebCommandPacket.setAgentId(agentId);
        if (commandEnum.getTimeoutMillis() != null) {
            troWebCommandPacket.setTimeoutMillis(commandEnum.getTimeoutMillis());
        }
        troWebCommandPacket.setIsAllowMultipleExecute(commandEnum.getIsAllowMultipleExecute());
        return troWebCommandPacket;
    }

    private void checkPacket(TroWebCommandPacket troWebPacket) {
        if (!troWebPacket.getIsAllowMultipleExecute()) {
            // 不允许多次执行，检测命令执行状态
            Set<String> keys = this.keys(String.format(agentKey, troWebPacket.getAgentId(),
                troWebPacket.getSend().getCommand(), troWebPacket.getSend().getModuleId(), "*"));
            if (keys.size() > 0) {
                for (String agentKey : keys) {
                    CommandStatus commandStatus = null;
                    try {
                        String[] temp = agentKey.split(":");
                        // 最后一个
                        CommandPacket commandPacket = serverChannel.getCurrentCommand(temp[0], temp[temp.length - 1]);
                        commandStatus = Optional.ofNullable(commandPacket).map(CommandPacket::getStatus).orElse(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (commandStatus == null) {
                        continue;
                    }
                    if (CommandStatus.COMMAND_COMPLETED_SUCCESS.equals(commandStatus)
                        || CommandStatus.COMMAND_COMPLETED_FAIL.equals(commandStatus)) {
                        redisTemplate.delete(agentKey);
                        continue;
                    }
                    log.error("send command failed,命令{} serverChannel.send not allow multiple execute",
                        troWebPacket.getSend().getModuleId());
                    throw new TroWebException(ExceptionCode.AGENT_SEND_ERROR, "agentId：" + troWebPacket.getAgentId()
                        + ",命令" + troWebPacket.getSend().getModuleId() + "不允许多次执行");
                }
            }
        }
    }

    public Set<String> keys(String pattern) {
        Set<String> keys = Sets.newHashSet();
        this.scan(pattern, item -> {
            //符合条件的key
            String key = new String(item, StandardCharsets.UTF_8);
            keys.add(key);
        });
        return keys;
    }

    private void scan(String pattern, Consumer<byte[]> consumer) {
        this.redisTemplate.execute((RedisConnection connection) -> {
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().count(Long.MAX_VALUE).match(pattern)
                .build())) {
                cursor.forEachRemaining(consumer);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

}
