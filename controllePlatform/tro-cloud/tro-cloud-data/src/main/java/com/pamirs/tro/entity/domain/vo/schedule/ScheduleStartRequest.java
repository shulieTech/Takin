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

package com.pamirs.tro.entity.domain.vo.schedule;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author 莫问
 * @Date 2020-05-12
 */
@Data
public class ScheduleStartRequest extends ScheduleEventRequest implements Serializable {

    /**
     * 脚本引擎
     */
    private String engineType;

    /**
     * 施压模式
     */
    private String pressureMode;

    /**
     * 脚本文件路径
     */
    private String scriptPath;

    /**
     * IP数量
     */
    private Integer totalIp;

    /**
     * 数据文件
     */
    private List<DataFile> dataFile;

    /**
     * 压测时长
     */
    private Long continuedTime;

    /**
     * 施压类型,0:并发,1:tps,2:自定义;不填默认为0
     */
    private Integer pressureType;

    /**
     * 最大并发
     */
    private Integer expectThroughput;

    /**
     * 递增时长
     */
    private Long rampUp;

    /**
     * 阶梯层数
     */
    private Integer steps;

    /**
     * 控制台地址
     */
    private String console;

    /**
     * 目标tps
     */
    private Integer tps;

    /**
     * 业务指标，目标rt
     */
    private Map<String, String> businessData;

    /**
     * 业务指标，目标tps
     */
    private Map<String, Integer> businessTpsData;

    /**
     * 压测引擎插件文件位置  一个压测场景可能有多个插件 一个插件也有可能有多个文件
     */
    private List<String> enginePluginsFilePath;

    /**
     * 添加引擎插件路径
     *
     * @param enginePluginsFilePath
     * @author lipeng
     * @return
     */
    public List<String> addEnginePluginsFilePath(String enginePluginsFilePath) {
        this.enginePluginsFilePath.add(enginePluginsFilePath);
        return this.enginePluginsFilePath;
    }

    @Data
    public static class DataFile implements Serializable {

        /**
         * 文件名称
         */
        private String name;

        /**
         * 文件路径
         */
        private String path;

        /**
         * 是否分割文件
         */
        private boolean split;
    }
}
