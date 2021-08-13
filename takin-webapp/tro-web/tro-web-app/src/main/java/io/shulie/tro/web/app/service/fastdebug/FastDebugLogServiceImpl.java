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

package io.shulie.tro.web.app.service.fastdebug;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import com.github.pagehelper.util.StringUtil;
import com.google.common.collect.Lists;
import com.pamirs.tro.common.constant.LogPullStatusEnum;
import io.shulie.tro.channel.bean.CommandResponse;
import io.shulie.tro.cloud.common.redis.RedisClientUtils;
import io.shulie.tro.web.app.agent.AgentCommandEnum;
import io.shulie.tro.web.app.agent.AgentCommandFactory;
import io.shulie.tro.web.app.agent.CommandSendDTO;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.constant.FastDebugLogPathFactory;
import io.shulie.tro.web.app.exception.TroWebException;
import io.shulie.tro.web.app.request.fastdebug.FastDebugAgentLogRequest;
import io.shulie.tro.web.app.request.fastdebug.FastDebugAppLogRequest;
import io.shulie.tro.web.app.response.fastdebug.AgentLogNamesResponse;
import io.shulie.tro.web.app.response.fastdebug.FastDebugLogResponse;
import io.shulie.tro.web.app.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author: mubai
 * @Date: 2020-12-29 10:07
 * @Description:
 */

@Service
@Slf4j
public class FastDebugLogServiceImpl implements FastDebugLogService {

    @Value("${fast.debug.upload.log.path:/data/fastdebug/log/}")
    private String prePath;

    @Autowired
    private FastDebugLogPathFactory pathFactory;

    @Autowired
    private RedisClientUtils redisClientUtils;

    @Autowired
    private AgentCommandFactory agentCommandFactory;

    /**
     * 第一次拉取日志，需要去agent下发命令
     *
     * @param logRequest
     * @return
     */
    @Override
    public FastDebugLogResponse getAppLog(FastDebugAppLogRequest logRequest) throws Exception {

        String path = logRequest.getFilePath();
        if (!path.contains("/") || path.endsWith("/")) {
            throw new RuntimeException("输入路径不合法，请确认！");
        }
        Long customerId = RestContext.getUser().getCustomerId();
        String isExist = redisClientUtils.getString(
            pathFactory.appLogIsExistKey(customerId, logRequest.getAgentId(), logRequest.getFilePath()));
        if (StringUtils.isNotBlank(isExist) && "false".equals(isExist)) {
            throw new RuntimeException("日志路径不存在，请重新填写路径");
        }

        FastDebugLogResponse response = new FastDebugLogResponse();
        String logPullStatus = getAppLogPullStatus(logRequest);
        response.setLogPullStatus(logPullStatus);
        if (logPullStatus == null) {
            //第一次拉取
            String filePath = logRequest.getFilePath();
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
            String appLogPullStatusKey = pathFactory.appLogPullStatusKey(customerId, logRequest.getAppName(),
                logRequest.getAgentId(),
                logRequest.getTraceId(), filePath, fileName);
            redisClientUtils.setString(appLogPullStatusKey, LogPullStatusEnum.PULLING.name(), 1, TimeUnit.MINUTES);
            //下发命令
            sendPullLogCommand(
                buildPullAppCommand(logRequest.getAppName(), logRequest.getAgentId(), logRequest.getTraceId(),
                    logRequest.getFilePath()));
            response.setTotalCount(0);
            response.setLogPullStatus(LogPullStatusEnum.PULLING.getName());
            return response;
        }
        if (logPullStatus.equals(LogPullStatusEnum.PULLING.getName()) || logPullStatus.equals(
            LogPullStatusEnum.TIMEOUT.name())) {
            response.setTotalCount(0);
            return response;
        }
        int currentPage = logRequest.getCurrent();
        int pageSize = logRequest.getPageSize();
        int startLine = currentPage * pageSize;
        //判断拉取的数量，如果是最后一页则触发新拉取一批数据
        String fileNameOld = logRequest.getFilePath();
        String fileName = fileNameOld.substring(fileNameOld.lastIndexOf("/") + 1, fileNameOld.length());

        String filePath = pathFactory.appLogPath(customerId, logRequest.getAppName(), logRequest.getAgentId(),
            logRequest.getTraceId(), replacePath(logRequest.getFilePath()), fileName);
        FastDebugLogResponse result = assembleLogResp(filePath, startLine, pageSize, currentPage);
        if (result != null && result.getNeedPull() == true) {
            try {
                sendPullLogCommand(
                    buildPullAppCommand(logRequest.getAppName(), logRequest.getAgentId(), logRequest.getTraceId(),
                        logRequest.getFilePath()));
            } catch (Exception e) {
                log.error("拉取下一页应用日志失败 ...; message:{}", e.getMessage());
            }
        }
        return result;
    }

    String replacePath(String oldPath) {
        return oldPath.replace("/", "-");
    }

    @Override
    public AgentLogNamesResponse getAgentLogNames(String appName, String agentId, String traceId) throws Exception {

        //查看是否已经发起，存入redis，已经发起则查看文件目录，未发起直接返回拉取中
        String status = getAgentLogNameStatus(appName, agentId, traceId);
        AgentLogNamesResponse response = new AgentLogNamesResponse();
        response.setFileNames(Lists.newArrayList());
        response.setStatus(status);
        if (status == null) {
            //设置拉取状态
            redisClientUtils.setString(
                pathFactory.getAgentLogNamesStatus(RestContext.getCustomerId(), appName, agentId, traceId),
                LogPullStatusEnum.PULLING.name(), 1, TimeUnit.MINUTES);
            // 发送zk命令 获取文件名 ；
            sendPullLogCommand(buildPullAgentCommand(appName, agentId, traceId, null));
            response.setStatus(LogPullStatusEnum.PULLING.name());
            return response;
        }
        if (status.equals(LogPullStatusEnum.PULLING.getName()) || status.equals(LogPullStatusEnum.TIMEOUT.name())) {
            return response;
        }
        //从文件目录，查询实际存在的文件
        response.setFileNames(
            FileUtils.getFileNames(pathFactory.agentLogDir(RestContext.getCustomerId(), appName, agentId, traceId)));
        response.setStatus(LogPullStatusEnum.PULLED.getName());
        return response;
    }

    /**
     * 拉取agent日志文件
     *
     * @param request
     * @return
     */
    @Override
    public FastDebugLogResponse getAgentLog(FastDebugAgentLogRequest request) throws Exception {
        FastDebugLogResponse response = new FastDebugLogResponse();
        response.setLogPullStatus(LogPullStatusEnum.PULLING.getName());
        String agentLogPullStatus = getAgentLogPullStatus(request);
        if (agentLogPullStatus == null) {
            //发起command 命令，拉取agent日志
            sendPullLogCommand(
                buildPullAgentCommand(request.getAppName(), request.getAgentId(), request.getTraceId(),
                    request.getFileName()));
            redisClientUtils.setString(
                pathFactory.agentLogPullStatusKey(RestContext.getCustomerId(), request.getAppName(),
                    request.getAgentId(), request.getTraceId(), request.getFileName()),
                LogPullStatusEnum.PULLING.getName(), 1,
                TimeUnit.MINUTES);
            return response;
        }
        if (agentLogPullStatus.equals(LogPullStatusEnum.PULLING.getName())) {
            return response;
        }
        //进行拉取日志， 从文件读取
        int currentPage = request.getCurrent();
        int pageSize = request.getPageSize();
        int startLine = currentPage * pageSize;
        String filePath = pathFactory.agentLogPath(RestContext.getCustomerId(), request.getAppName(),
            request.getAgentId(), request.getTraceId(), request.getFileName());
        FastDebugLogResponse result = assembleLogResp(filePath, startLine, pageSize, currentPage);
        if (result != null && result.getNeedPull()) {
            try {
                sendPullLogCommand(
                    buildPullAgentCommand(request.getAppName(), request.getAgentId(), request.getTraceId(),
                        request.getFileName()));
            } catch (Exception e) {
                log.error("拉取下一页失败 ...");
            }
        }
        return result;
    }

    public FastDebugLogResponse assembleLogResp(String filePath, int startLine, int pageSize, int currentPage) {
        FastDebugLogResponse response = new FastDebugLogResponse();
        JSONObject json = FileUtils.readLine(filePath, startLine, pageSize);
        List<String> list = json.getObject("list", List.class);
        Long lineNum = json.getLong("lineNum");
        //如果是最后一页，触发去agent那边拉取；
        if (isLastPage(currentPage, lineNum.intValue(), pageSize)) {
            response.setNeedPull(true);
        }
        StringBuilder builder = new StringBuilder();
        if (CollectionUtils.isNotEmpty(list)) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                builder.append(list.get(i));
                if (i + 1 < size) {
                    builder.append(System.lineSeparator());
                }
            }
        }

        response.setContent(builder.toString());
        response.setTotalCount(lineNum);
        response.setLogPullStatus(LogPullStatusEnum.PULLED.getName());
        return response;
    }

    /**
     * 判断命令是否已经发送过，同一条命令保证1分钟
     */
    public boolean nextPageHasSended(String nextPageKey) {
        String hasPulled = redisClientUtils.getString(nextPageKey);
        if (StringUtil.isNotEmpty(hasPulled)) {
            return true;
        } else {
            redisClientUtils.setString(nextPageKey, LogPullStatusEnum.PULLED.name(), 30, TimeUnit.SECONDS);
            return false;
        }
    }

    public String getAgentLogNameStatus(String appName, String agentId, String traceId) {

        String agentLogDir = pathFactory.agentLogDir(RestContext.getCustomerId(), appName, agentId, traceId);
        List<String> fileNames = FileUtils.getFileNames(agentLogDir);
        if (CollectionUtils.isEmpty(fileNames)) {
            //判断拉取时间,超过30秒，返回上报超时
            String agentLogNamesStatusKey = pathFactory.getAgentLogNamesStatus(RestContext.getCustomerId(), appName,
                agentId, traceId);
            return redisClientUtils.getString(agentLogNamesStatusKey);
        } else {
            return LogPullStatusEnum.PULLED.getName();
        }
    }

    public String getAppLogPullStatus(FastDebugAppLogRequest request) {
        String filePath = request.getFilePath();
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
        //再次判断是否存在拉取后的文件
        String existfilePath = pathFactory.appLogPath(RestContext.getCustomerId(), request.getAppName(),
            request.getAgentId(), request.getTraceId(), replacePath(request.getFilePath()), fileName);
        if (!FileUtils.existFile(existfilePath)) {
            redisClientUtils.delete(pathFactory
                .appLogUploadLastLineKey(RestContext.getCustomerId(), request.getAgentId(), request.getTraceId(),
                    request.getFilePath()));
            //判断拉取日志是否为进行中
            String appLogPullStatusKey = pathFactory.appLogPullStatusKey(RestContext.getCustomerId(),
                request.getAppName(), request.getAgentId(),
                request.getTraceId(), filePath, fileName);
            String cacheTime = redisClientUtils.getString(appLogPullStatusKey);
            if (StringUtils.isNotBlank(cacheTime)) {
                return cacheTime;
            } else {
                return null;
            }
        } else {
            redisClientUtils.setString(
                pathFactory.appLogPullStatusKey(RestContext.getCustomerId(), request.getAppName(), request.getAgentId(),
                    request.getTraceId(), filePath, fileName),
                LogPullStatusEnum.PULLED.getName(), 3, TimeUnit.MINUTES);
            return LogPullStatusEnum.PULLED.getName();
        }
    }

    public String getAgentLogPullStatus(FastDebugAgentLogRequest request) {
        //再次判断是否存在拉取后的文件
        String existfilePath = pathFactory.agentLogPath(RestContext.getCustomerId(), request.getAppName(),
            request.getAgentId(), request.getTraceId(), request.getFileName());
        if (!FileUtils.existFile(existfilePath)) {
            //当文件不存在时，将上次上报lastline 设置为null
            redisClientUtils.delete(
                pathFactory
                    .agentLogUploadLastLineKey(RestContext.getCustomerId(), request.getAgentId(), request.getTraceId(),
                        request.getFileName()));
            return null;
        } else {
            redisClientUtils.setString(
                pathFactory
                    .agentLogPullStatusKey(RestContext.getCustomerId(), request.getAppName(), request.getAgentId(),
                        request.getTraceId(), request.getFileName()),
                LogPullStatusEnum.PULLED.name(), 15, TimeUnit.DAYS);
            return LogPullStatusEnum.PULLED.name();
        }
    }

    /**
     * 判断是不是最后一页，如果是则触发向agent重新拉取日志操作，提前三页触发向agent 发送
     * totalLine = 读取文件时获取的行数
     */

    public boolean isLastPage(Integer currentPage, Integer totalLine, Integer pageSize) {
        int i = totalLine / pageSize;
        if (currentPage + 1 >= i) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 组装拉取app日志的command
     *
     * @return
     */
    @Override
    public CommandSendDTO buildPullAppCommand(String appName, String agentId, String traceId, String filePath) {

        /**
         * {
         *     "appName":"",
         *     "agentId":"",
         *     "filePath":"日志路径",
         *     "lineStart": "起始行数",
         *     "batchLines":"一次拉取最大行数"
         * }
         */
        Integer lineStart = null;
        String lastLine = redisClientUtils.getString(
            pathFactory.appLogUploadLastLineKey(RestContext.getCustomerId(), agentId, traceId, filePath));
        if (lastLine != null) {
            lineStart = Integer.valueOf(lastLine);
        }
        CommandSendDTO commandSend = new CommandSendDTO();
        Map<String, Object> param = new HashMap<>();
        param.put("appName", appName);
        param.put("agentId", agentId);
        param.put("filePath", filePath);
        param.put("lineStart", lineStart);
        param.put("batchLines", 500);
        param.put("traceId", traceId);
        commandSend.setAgentId(agentId);
        commandSend.setParam(param);
        commandSend.setAgentCommandEnum(AgentCommandEnum.PULL_APP_LOG_COMMAND);
        if (nextPageHasSended(pathFactory.appNextPageHasSendedKey(appName, agentId, traceId, filePath, lineStart))) {
            return null;
        }

        return commandSend;
    }

    /**
     * 组装拉取app日志的command
     *
     * @return
     */
    @Override
    public CommandSendDTO buildPullAgentCommand(String appName, String agentId, String traceId, String fileName) {

        /**
         * {
         *     "appName":"",
         *     "agentId":"",
         *     "fileName":"agent日志文件名",
         *     "lineStart": "起始行数",
         *     "batchLines":"一次拉取最大行数"
         * }
         */
        Integer lineStart = null;
        if (StringUtils.isNotBlank(fileName)) {
            String lastLine = redisClientUtils.getString(
                pathFactory.agentLogUploadLastLineKey(RestContext.getCustomerId(), agentId, traceId, fileName));
            if (lastLine != null) {
                lineStart = Integer.valueOf(lastLine);
            }
        }
        CommandSendDTO commandSend = new CommandSendDTO();
        Map<String, Object> param = new HashMap<>();
        param.put("appName", appName);
        param.put("agentId", agentId);
        param.put("fileName", fileName);
        param.put("lineStart", lineStart);
        param.put("batchLines", 500);
        param.put("traceId", traceId);
        commandSend.setAgentId(agentId);
        commandSend.setParam(param);
        commandSend.setAgentCommandEnum(AgentCommandEnum.PULL_AGENT_LOG_COMMAND);
        if (nextPageHasSended(pathFactory.agentNextPageHasSendedKey(appName, agentId, traceId, fileName, lineStart))) {
            return null;
        }
        return commandSend;
    }

    /**
     * 发送拉取日志命令
     */
    @Override
    public void sendPullLogCommand(CommandSendDTO send) throws Exception {
        log.info("send command: {}", JSON.toJSONString(send, SerializerFeature.WriteMapNullValue));
        try {
            CommandResponse response = agentCommandFactory.send(send.getAgentCommandEnum(), send.getAgentId(),
                send.getParam());
            if (response != null && !response.isSuccess()) {
                log.error("发送拉取日志命令失败...");
            }
        } catch (TroWebException e) {
            String msg = e.getSource() == null ? "" : e.getSource().toString();
            log.error(msg);
            throw new RuntimeException("正在拉取中，请稍等...");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
