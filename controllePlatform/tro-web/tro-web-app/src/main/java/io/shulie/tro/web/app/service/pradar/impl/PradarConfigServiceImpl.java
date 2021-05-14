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

package io.shulie.tro.web.app.service.pradar.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import io.shulie.tro.web.app.response.pradar.PradarZKConfigResponse;
import io.shulie.tro.web.app.service.pradar.PradarConfigService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class PradarConfigServiceImpl implements PradarConfigService {

    List<PradarZKConfig> pradarZKConfigList = Arrays.asList(
        new PradarZKConfig(1, "/pradar/config/rt/hdfsDisable", PradarZKDataType.Boolean, "false",
            "hdfs开关配置，控制数据是否不写入hdfs"),
        new PradarZKConfig(2, "/pradar/config/rt/hdfsSampling", PradarZKDataType.Int, "1",
            "日志原文写入hdfs采样率配置，代表每多少条数据采样1条"),
        new PradarZKConfig(3, "/pradar/config/rt/monitroDisable", PradarZKDataType.Boolean, "false",
            "基础信息处理开关，控制压测指标信息是否不处理"),
        new PradarZKConfig(4, "/pradar/config/rt/clickhouseDisable", PradarZKDataType.Boolean, "false",
            "日志原文写入clickhouse开关，控制日志原文是否不写入clickhouse"),
        new PradarZKConfig(5, "/pradar/config/rt/clickhouseSampling", PradarZKDataType.Int, "1",
            "日志原文写入clickhouse采样率，代表每多少条数据采样1条"),
        new PradarZKConfig(6, "/config/log/trace/simpling", PradarZKDataType.Int, "1",
            "全局日志采样率")
    );
    @Autowired
    private Environment environment;
    private CuratorFramework client;

    @PostConstruct
    public void init() {
        String zkAddr = environment.getProperty("tro.config.zk.addr");
        if (StringUtils.isEmpty(zkAddr)) {
            throw new RuntimeException("配置中心zk地址没有填写，请核对校验`tro.config.zk.addr`");
        }
        int timeout = 3000;

        try {
            String timeoutString = environment.getProperty("tro.config.zk.timeout");
            if (timeoutString != null) {
                timeout = Integer.parseInt(timeoutString);
            }
        } catch (Exception e) {
            // ignore
        }
        client = CuratorFrameworkFactory
            .builder()
            .connectString(zkAddr)
            .sessionTimeoutMs(timeout)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
        client.start();
    }

    @Override
    public List<PradarZKConfigResponse> getConfigList() {
        Stat stat = new Stat();
        return pradarZKConfigList.stream().map(pradarZKConfig -> new PradarZKConfigResponse(
            pradarZKConfig.getId(),
            pradarZKConfig.getZkPath(),
            pradarZKConfig.getType().type,
            getNode(stat, pradarZKConfig.getZkPath()),
            pradarZKConfig.getRemark(),
            stat.getCtime(),
            stat.getMtime()
        )).collect(Collectors.toList());
    }

    @Override
    public void initZKData() {
        pradarZKConfigList.forEach(pradarZKConfig -> {
            if (!checkNodeExists(pradarZKConfig.zkPath)) {
                addNode(pradarZKConfig.zkPath, pradarZKConfig.defaultValue);
            }
        });
    }

    @Override
    public boolean updateConfig(int id, String value) {
        try {
            pradarZKConfigList.stream().filter(pradarZKConfig -> id == pradarZKConfig.id).forEach(pradarZKConfig -> {
                updateNode(pradarZKConfig.zkPath, value);
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void addNode(String path, String data) {
        try {
            client.create().creatingParentContainersIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(path, data.getBytes());
        } catch (Exception e) {
            log.error("创建zk数据节点失败;path={},data={}", path, data, e);
        }
    }

    private String getNode(Stat stat, String path) {
        if (!checkNodeExists(path)) {
            return null;
        }
        byte[] bytes = new byte[0];
        try {
            bytes = client.getData().storingStatIn(stat).forPath(path);
        } catch (Exception e) {
            log.error("读取zk数据节点失败;path={}", path, e);
        }
        return new String(bytes);
    }

    private boolean checkNodeExists(String path) {
        try {
            return client.checkExists().forPath(path) != null;
        } catch (Exception e) {
            log.error("判断数据节点是否存在失败;path={}", path, e);
        }
        return false;
    }

    private void updateNode(String path, String data) {
        try {
            client.setData().forPath(path, data.getBytes());
        } catch (Exception e) {
            log.error("创建zk数据节点失败;path={},data={}", path, data, e);
        }
    }

    private enum PradarZKDataType {

        String("String"),
        Int("Int"),
        Boolean("Boolean");

        String type;

        PradarZKDataType(String type) {
            this.type = type;
        }
    }

    @Data
    private static class PradarZKConfig {
        int id;
        String zkPath;
        PradarZKDataType type;
        String defaultValue;
        String remark;

        PradarZKConfig(int id, String zkPath, PradarZKDataType type, String defaultValue, String remark) {
            this.id = id;
            this.zkPath = zkPath;
            this.type = type;
            this.defaultValue = defaultValue;
            this.remark = remark;
        }
    }
}
