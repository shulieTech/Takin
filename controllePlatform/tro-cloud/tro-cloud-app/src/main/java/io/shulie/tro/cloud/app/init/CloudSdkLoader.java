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

package io.shulie.tro.cloud.app.init;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;

import com.pamirs.tro.entity.dao.cloudserver.TCloudPlatformMapper;
import com.pamirs.tro.entity.domain.entity.cloudserver.CloudPlatform;
import com.pamirs.tro.entity.domain.query.CloudPlatformQueryParam;
import io.shulie.flpt.core.plugin.CloudServerPlugin;
import io.shulie.tro.cloud.common.utils.FileUtils;
import io.shulie.tro.cloud.common.utils.LinuxUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: mubai
 * @Date: 2020-05-09 14:25
 * @Description:
 */

@Component
public class CloudSdkLoader {

    public static final Map<String, CloudServerPlugin> CLOUD_PLUGINS = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(CloudSdkLoader.class);
    @Value("${pradar_cloud_sdk_path}")
    private String SDK_PATH;
    @Resource
    private TCloudPlatformMapper TCloudPlatformMapper;

    public void loadSDK() {
        CLOUD_PLUGINS.clear();
        List<File> pluginJarFiles = FileUtils.getDirectoryFiles(SDK_PATH, ".jar");
        if (pluginJarFiles == null || pluginJarFiles.isEmpty()) {
            logger.warn("No plugin found for cloud SDK");
            return;
        }
        CloudPlatformQueryParam queryParam = new CloudPlatformQueryParam();
        List<CloudPlatform> cloudPlatforms = TCloudPlatformMapper.selectByExample(queryParam);
        if (cloudPlatforms == null || cloudPlatforms.size() == 0) {
            logger.warn("No cloud platform in db !");
            return;
        }
        Map<String, String> cloudNameJarMap = new HashMap<>();
        for (CloudPlatform platform : cloudPlatforms) {
            if (StringUtils.isNotBlank(platform.getJarName())) {
                cloudNameJarMap.put(platform.getJarName(), platform.getName());
            }
        }

        try {
            for (File file : pluginJarFiles) {
                String jarPath = file.getCanonicalPath();
                String jarName = file.getName();
                if (cloudNameJarMap.containsKey(jarName)) {
                    String urlStr = "file:" + jarPath;
                    initSDK(null, cloudNameJarMap.get(jarName), null, urlStr);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void initSDK(Long id, String cloudName, String jarName, String urlStr) {
        try {
            URL[] urls = new URL[1];
            urls[0] = new URL(urlStr);
            ServiceLoader<CloudServerPlugin> serviceLoader = null;
            try {
                URLClassLoader urlClassLoader = new URLClassLoader(urls,
                    Thread.currentThread().getContextClassLoader());
                serviceLoader = ServiceLoader.load(CloudServerPlugin.class, urlClassLoader);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            if (serviceLoader != null) {
                for (CloudServerPlugin plugin : serviceLoader) {
                    CLOUD_PLUGINS.put(cloudName, plugin);
                }
            }
        } catch (Exception e) {
            logger.error("初始化sdk 发生部分失败", e);
        }
    }

    public void loadJar(Long id, String cloudName, String jarname, String urlStr, Boolean initPlatform)
        throws Exception {
        URL[] urls = new URL[1];
        urls[0] = new URL(urlStr);
        URLClassLoader urlClassLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
        ServiceLoader<CloudServerPlugin> serviceLoader = ServiceLoader.load(CloudServerPlugin.class, urlClassLoader);
        Iterator<CloudServerPlugin> iterator = serviceLoader.iterator();
        if (!iterator.hasNext()) {
            if (StringUtils.isNotBlank(jarname) && StringUtils.isNotBlank(SDK_PATH)) {
                String jarPath = SDK_PATH + "/" + jarname;
                LinuxUtil.executeLinuxCmd(" rm -rf " + jarPath);
            }
            throw new RuntimeException("上传的jar不符合要求！");
        }
        while (iterator.hasNext()) {
            CloudServerPlugin plugin = iterator.next();
            CLOUD_PLUGINS.put(cloudName, plugin);
            if (initPlatform) {
                CloudPlatformQueryParam param = new CloudPlatformQueryParam();
                if (id != null) {
                    param.setId(id);
                } else if (cloudName != null) {
                    param.setName(cloudName);
                }
                List<CloudPlatform> cloudPlatforms = TCloudPlatformMapper.selectByExample(param);
                if (cloudPlatforms == null || cloudPlatforms.size() == 0) {
                    CloudPlatform dbPlatform = new CloudPlatform();
                    dbPlatform.setName(plugin.getCloudServerName());
                    if (plugin.getAuthorizeParam() != null) {
                        dbPlatform.setAuthorizeParam(JSON.toJSONString(plugin.getAuthorizeParam()));
                    }
                    dbPlatform.setClassPath(plugin.getClass().getName());
                    dbPlatform.setJarName(jarname);
                    dbPlatform.setName(cloudName);
                    TCloudPlatformMapper.insertSelective(dbPlatform);
                } else {
                    CloudPlatform dbPlatform = cloudPlatforms.get(0);
                    CloudPlatform updatePlatform = new CloudPlatform();
                    updatePlatform.setId(dbPlatform.getId());
                    if (plugin.getAuthorizeParam() != null) {
                        updatePlatform.setAuthorizeParam(JSON.toJSONString(plugin.getAuthorizeParam()));
                    }
                    updatePlatform.setClassPath(plugin.getClass().getName());
                    updatePlatform.setJarName(jarname);
                    if (StringUtils.isNotBlank(cloudName)) {
                        updatePlatform.setName(cloudName);
                    }
                    TCloudPlatformMapper.updateByPrimaryKeySelective(updatePlatform);
                }
            }
        }
    }

}
