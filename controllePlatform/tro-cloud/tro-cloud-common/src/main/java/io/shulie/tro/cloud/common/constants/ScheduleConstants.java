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

package io.shulie.tro.cloud.common.constants;

/**
 * @Author 莫问
 * @Date 2020-05-21
 */

public class ScheduleConstants {

    /**
     *
     */
    public static final String IMAGE_PULL_POLICY = "IfNotPresent";

    /**
     *
     */
    public static final String RESTART_POLICY_NEVER = "Never";

    /**
     * 引擎脚本文件名称
     */
    public static final String ENGINE_SCRIPT_FILE_NAME = "script-file";

    /**
     * 引擎脚本文件路径
     */
    public static final String ENGINE_SCRIPT_FILE_PATH = "/etc/engine/script/";

    /**
     * 引擎插件文件夹路径 add by lipeng
     */
    public static final String ENGINE_PLUGINS_FOLDER_PATH = "/etc/engine/plugins/";

    /**
     * 引擎配置文件名称
     */
    public static final String ENGINE_CONFIG_FILE_NAME = "engine-conf";

    /**
     * 引擎配置文件路径
     */
    public static final String ENGINE_CONFIG_FILE_PATH = "/etc/engine/config";


    /**
     * 引擎时区配置名称
     */
    public static final String ENGINE_TIMEZONE_CONFIG_NAME = "host-time";

    /**
     * 引擎时区配置文件路径
     */
    public static final String ENGINE_TIMEZONE_FILE_PATH = "/etc/localtime";

    /**
     * 调度状态: 失败
     */
    public static final int SCHEDULE_STATUS_0 = 0;

    /**
     * 调度状态：成功
     */
    public static final int SCHEDULE_STATUS_1 = 1;

    public static String TEMP_FAIL_SIGN = "temp-fail-";
    public static String FIRST_SIGN = "-first";
    public static String LAST_SIGN = "-last";
    public static String TEMP_TIMESTAMP_SIGN = "temp-timestamp-";
    public static String INTERRUPT_POD = "interrupt-pod-";
    public static String TEMP_LAST_SIGN = "temp-last-";

    /**
     * 文件分割调度名称
     */

    public static String getFileSplitScheduleName(Long scenId, Long reportId, Long customerId) {
        // 兼容原始redis key
        if (null == customerId) {
            return String.format("file-split-%s-%s", scenId, reportId);
        }
        return String.format("file-split-%s-%s-%s", scenId, reportId, customerId);
    }

    /**
     * 文件分割存储的队列
     */
    public static String getFileSplitQueue(Long scenId, Long reportId, Long customerId) {
        // 兼容原始redis key
        if (null == customerId) {
            return String.format("file-split-queue-%s-%s", scenId, reportId);
        }
        return String.format("file-split-queue-%s-%s-%s", scenId, reportId, customerId);
    }

    /**
     * 调度名称
     *
     * @return
     */
    public static String getScheduleName(Long sceneId, Long taskId, Long customerId) {
        // 兼容原始redis key
        if (null == customerId) {
            return String.format("scene-task-%s-%s", sceneId, taskId);
        }
        return String.format("scene-task-%s-%s-%s", sceneId, taskId, customerId);
    }

    /**
     * ConfigMap名称
     *
     * @return
     */
    public static String getConfigMapName(Long sceneId, Long taskId, Long customerId) {
        // 兼容原始redis key
        if (null == customerId) {
            return String.format("engine-config-%s-%s.json", sceneId, taskId);
        }
        return String.format("engine-config-%s-%s-%s.json", sceneId, taskId, customerId);
    }

    /**
     * 获取url
     *
     * @return
     */
    public static String getConsoleUrl(Long sceneId, Long taskId, Long customerId) {
        // 兼容原始redis key
        if (null == customerId) {
            return String.format("/api/collector/receive?sceneId=%s&reportId=%s", sceneId, taskId);
        }
        return String.format("/api/collector/receive?sceneId=%s&reportId=%s&customerId=%s", sceneId, taskId,
            customerId);
    }

    /**
     * pod 引擎名
     *
     * @return
     */
    public static String getEngineName(Long sceneId, Long reportId, Long customerId) {
        // 兼容原始redis key
        if (null == customerId) {
            return String.format("pod-engine-%s-%s", sceneId, reportId);
        }
        return String.format("pod-engine-%s-%s-%s", sceneId, reportId, customerId);
    }

    /**
     * pod 引擎名
     *
     * @return
     */
    public static String getPodTotal(Long sceneId, Long reportId, Long customerId) {
        // 兼容原始redis key
        if (null == customerId) {
            return String.format("pod-total-%s-%s", sceneId, reportId);
        }
        return String.format("pod-total-%s-%s-%s", sceneId, reportId, customerId);
    }

    /**
     * pod 名
     *
     * @return
     */
    public static String getPodName(Long sceneId, Long reportId, Long customerId) {
        // 兼容原始redis key
        if (null == customerId) {
            return String.format("pod-%s-%s", sceneId, reportId);
        }
        return String.format("pod-%s-%s-%s", sceneId, reportId, customerId);
    }

}
