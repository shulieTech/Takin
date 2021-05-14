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

package io.shulie.tro.web.data.result.baseserver;

import java.time.Instant;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

/**
 * @Author: xingchen
 * @ClassName: BaseServerResult
 * @Package: io.shulie.tro.report.vo
 * @Date: 2020/7/2717:14
 * @Description:
 */
@Measurement(name = "app_base_data")
@Data
public class BaseServerResult {
    /**
     * 时间
     */
    @Column(name = "time")
    private Instant time;

    /**
     * 应用ip
     */
    @Column(name = "app_ip")
    private String appIp;

    /**
     * 应用名
     */
    @Column(name = "app_name")
    private String appName;
    /**
     * cpu使用率
     */
    @Column(name = "cpu_rate")
    private Double cpuRate;
    /**
     * cpuload
     */
    @Column(name = "cpu_load")
    private Double cpuLoad;
    /**
     * 内存使用率
     */
    @Column(name = "mem_rate")
    private Double memRate;
    /**
     * io等待率
     */
    @Column(name = "iowait")
    private Double ioWait;
    /**
     * 网络带宽使用率
     */
    @Column(name = "net_bandwidth_rate")
    private Double netBandWidthRate;
    /**
     * 网络带宽
     */
    @Column(name = "net_bandwidth")
    private Double netBandwidth;
    /**
     * cpu核数
     */
    @Column(name = "cpu_cores")
    private Double cpuCores;
    /**
     * 磁盘大小
     */
    @Column(name = "disk")
    private Double disk;
    /**
     * 内存大小
     */
    @Column(name = "memory")
    private Double memory;

    /**
     * 应用ip
     */
    @Column(name = "tag_app_ip", tag = true)
    private String tagAppIp;

    /**
     * 应用名
     */
    @Column(name = "tag_app_name", tag = true)
    private String tagAppName;

    /**
     * tag_agent_id
     */
    @Column(name = "tag_agent_id", tag = true)
    private String tagAgentId;

    private Double tps;

    /**
     * 获取毫秒的时间
     *
     * @return
     */
    public long getExtTime() {
        return this.getTime().getEpochSecond() * 1000;
    }
}
