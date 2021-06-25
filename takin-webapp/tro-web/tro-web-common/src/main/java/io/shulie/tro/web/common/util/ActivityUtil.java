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

package io.shulie.tro.web.common.util;

import java.util.UUID;

import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author shiyajian
 * create: 2021-01-12
 */
public class ActivityUtil {

    public static String createLinkId(String serviceName, String methodName, String appName, String rpcType,
        String extend) {
        StringBuffer tags = new StringBuffer();
        tags.append(serviceName)
            .append("|").append(methodName)
            .append("|").append(appName)
            .append("|").append(rpcType)
            .append("|").append(extend);
        try {
            return MD5Tool.getMD5(tags.toString());
        } catch (Exception e) {
            return UUID.randomUUID().toString();
        }
    }

    public static String buildEntrance(String applicationName, String methodName, String serviceName, String rpcType) {
        return StringUtils.join(Lists.newArrayList(applicationName, methodName, serviceName, rpcType), "|");
    }

    public static String serviceNameLabel(String serviceName, String methodName) {
        return serviceName + "#" + methodName;
    }

    /**
     * 1、应用名称
     * 2、methodName
     * 3、serviceName
     * 4、rpcType
     */
    public static EntranceJoinEntity covertEntrance(String dbEntrance) {
        String[] split = StringUtils.split(dbEntrance, "\\|");
        if (split.length != 4) {
            return new EntranceJoinEntity();
        }
        EntranceJoinEntity entranceJoinEntity = new EntranceJoinEntity();
        entranceJoinEntity.setApplicationName(split[0]);
        entranceJoinEntity.setMethodName(split[1]);
        entranceJoinEntity.setServiceName(split[2]);
        entranceJoinEntity.setRpcType(split[3]);
        return entranceJoinEntity;
    }

    public static String toEntrance(EntranceJoinEntity entranceJoinEntity) {
        return StringUtils.join(
            Lists.newArrayList(entranceJoinEntity.getApplicationName(),
                entranceJoinEntity.getMethodName(),
                entranceJoinEntity.getServiceName(),
                entranceJoinEntity.getRpcType()
            ), "|"
        );
    }

    @Data
    public static class EntranceJoinEntity {

        private String applicationName;

        private String methodName;

        private String serviceName;

        private String rpcType;

    }
}
