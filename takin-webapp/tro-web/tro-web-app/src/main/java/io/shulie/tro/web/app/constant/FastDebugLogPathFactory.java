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

package io.shulie.tro.web.app.constant;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author: mubai
 * @Date: 2020-12-29 10:51
 * @Description:
 */

@Service
public class FastDebugLogPathFactory {

    @Value("${fast.debug.upload.log.path:/data/fastdebug/log/}")
    String logPathPre;

    /**
     * 应用日志具体路径
     *
     * @param appName
     * @param agentId
     * @param fileName
     * @return
     */
    public String appLogPath(Long customerId, String appName, String agentId, String traceId, String filePath,
        String fileName) {
        StringBuilder builder = new StringBuilder();
        builder.append(logPathPre);
        builder.append("applog");
        builder.append(File.separator);
        builder.append(customerId);
        builder.append(File.separator);
        builder.append(appName);
        builder.append(File.separator);
        builder.append(agentId);
        builder.append(File.separator);
        builder.append(traceId);
        builder.append(File.separator);
        builder.append(filePath);
        builder.append(File.separator);
        builder.append(fileName);
        return builder.toString();
        //return logPathPre + "applog" + File.separator + customerId + File.separator + appName
        //    + File.separator + agentId + File.separator + traceId + File.separator + fileName;
    }

    /**
     * agent 日志的详细路径
     *
     * @param appName
     * @param agentId
     * @param fileName
     * @return
     */
    public String agentLogPath(Long customerId, String appName, String agentId, String traceId, String fileName) {
        return logPathPre + "agentlog" + File.separator + customerId + File.separator + appName
            + File.separator + agentId + File.separator + traceId + File.separator + fileName;
    }

    /**
     * 拉取agent日志文件名状态
     *
     * @param appName
     * @param agentId
     * @return
     */
    public String getAgentLogNamesStatus(Long customerId, String appName, String agentId, String traceId) {
        return customerId + "-" + appName + "-" + agentId + "-" + traceId;
    }

    /**
     * agent 日志目录
     *
     * @param appName
     * @param agentId
     * @return
     */
    public String agentLogDir(Long customerId, String appName, String agentId, String traceId) {
        return logPathPre + "agentlog" + File.separator + customerId + File.separator + appName + File.separator
            + agentId + File.separator + traceId + File.separator;
    }

    /**
     * agent 日志文件拉取状态
     *
     * @param appName
     * @param agentId
     * @param fileName
     * @return
     */
    public String agentLogPullStatusKey(Long customerId, String appName, String agentId, String traceId,
        String fileName) {
        return "agent:log:pull:status:" + customerId + "-" + appName + "-" + agentId + "-" + traceId + "-" + fileName;
    }

    /**
     * app 日志文件拉取状态
     *
     * @param appName
     * @param agentId
     * @param fileName
     * @return¬ 存储拉取状态： PULLED \PULLING
     */
    public String appLogPullStatusKey(Long customerId, String appName, String agentId, String traceId, String filePath,
        String fileName) {
        return "app:log:pull:status:" + customerId + "-" + appName + "-" + agentId + "-" + traceId + "-" + filePath
            + "-" + fileName;
    }

    /**
     * agent 调试堆栈信息上报次数
     *
     * @param traceId
     * @return
     */
    public String agentStackUploadTimesKey(String traceId) {
        return "agent-stack-upload-times-" + traceId;
    }

    /**
     * 获取app日志上次上传的行数 key
     *
     * @param agentId
     * @param filePath
     * @return
     */
    public String appLogUploadLastLineKey(Long customerId, String agentId, String traceId, String filePath) {
        return customerId + "-" + agentId + "-" + traceId + "-" + filePath;
    }

    /**
     * 标记应用日志文件是否不存在
     *
     * @param customerId
     * @param agentId
     * @param filePath
     * @return
     */
    public String appLogIsExistKey(Long customerId, String agentId, String filePath) {
        return customerId + "-" + agentId + "-" + filePath;
    }

    /**
     * 获取agent日志上次上传的行数
     *
     * @param agentId
     * @param fileName
     * @return
     */
    public String agentLogUploadLastLineKey(Long customerId, String agentId, String traceId, String fileName) {
        return customerId + "-" + agentId + "-" + traceId + "-" + fileName;
    }

    /**
     * 获取日志根目录
     *
     * @return
     */
    public String getLogPathPre() {
        return logPathPre;
    }

    /**
     * 获取agent下一页是否已经发送key
     *
     * @param appName
     * @param agentId
     * @param traceId
     * @param fileName
     * @param lineStart
     * @return
     */
    public String agentNextPageHasSendedKey(String appName, String agentId, String traceId, String fileName,
        Integer lineStart) {
        StringBuilder builder = new StringBuilder();
        builder.append(appName);
        builder.append("-");
        builder.append(agentId);
        builder.append("-");
        builder.append(traceId);
        builder.append("-");
        builder.append(fileName);
        builder.append("-");
        builder.append(lineStart);
        return builder.toString();
    }

    /**
     * 获取app下一页是否已经发送
     *
     * @param appName
     * @param agentId
     * @param traceId
     * @param filePath
     * @param lineStart
     * @return
     */
    public String appNextPageHasSendedKey(String appName, String agentId, String traceId, String filePath,
        Integer lineStart) {
        StringBuilder builder = new StringBuilder();
        builder.append(appName);
        builder.append("-");
        builder.append(agentId);
        builder.append("-");
        builder.append(traceId);
        builder.append("-");
        builder.append(filePath);
        builder.append("-");
        builder.append(lineStart);
        return builder.toString();
    }

}
