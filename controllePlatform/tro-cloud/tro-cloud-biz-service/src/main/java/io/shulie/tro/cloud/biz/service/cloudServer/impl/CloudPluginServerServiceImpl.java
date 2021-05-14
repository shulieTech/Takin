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

package io.shulie.tro.cloud.biz.service.cloudServer.impl;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.util.StringUtil;
import io.shulie.flpt.core.plugin.BaseCreateRequest;
import io.shulie.flpt.core.plugin.BaseDeleteRequest;
import io.shulie.flpt.core.plugin.BaseInitRequest;
import io.shulie.flpt.core.plugin.BaseQueryRequest;
import io.shulie.flpt.core.plugin.BaseStartRequest;
import io.shulie.flpt.core.plugin.CloudServerPlugin;
import io.shulie.flpt.core.plugin.CreateResponse;
import io.shulie.flpt.core.plugin.DeleteResponse;
import io.shulie.flpt.core.plugin.InitResponse;
import io.shulie.flpt.core.plugin.QueryResponse;
import io.shulie.flpt.core.plugin.StartResponse;
import io.shulie.tro.common.beans.response.ResponseResult;
import org.springframework.stereotype.Service;
import io.shulie.tro.cloud.biz.service.cloudServer.CloudPluginServerService;

/**
 * @Author: mubai
 * @Date: 2020-05-09 16:12
 * @Description:
 */

@Service
public class CloudPluginServerServiceImpl implements CloudPluginServerService {

    @Override
    public ResponseResult createInstance(String cloudName, BaseCreateRequest request) {
        if (StringUtil.isEmpty(cloudName)) {
            throw new RuntimeException("cloudServer name can not be null");
        }
        CloudServerPlugin cloudServerPlugin = null;
        //CloudSdkLoader.CLOUD_PLUGINS.get(cloudName);
        if (cloudServerPlugin != null) {
            CreateResponse createResponse = cloudServerPlugin.createInstance(request);
            return ResponseResult.success(createResponse);
        } else {
            throw new RuntimeException("cloud platform do not has this sdk；cloudName：  " +
                cloudName);
        }
    }

    @Override
    public ResponseResult queryInstance(String cloudName, BaseQueryRequest request) {
        if (StringUtil.isEmpty(cloudName)) {
            throw new RuntimeException("cloudServer name can not be null");
        }
        CloudServerPlugin cloudServerPlugin = null;
        //CloudSdkLoader.CLOUD_PLUGINS.get(cloudName);
        if (cloudServerPlugin != null) {
            QueryResponse queryResponse = cloudServerPlugin.queryInstance(request);
            return ResponseResult.success(queryResponse);
        } else {
            throw new RuntimeException("cloud platform do not has this sdk；cloudName：  " +
                cloudName);
        }
    }

    @Override
    public ResponseResult startInstance(String cloudName, BaseStartRequest baseStartRequest) {

        if (StringUtil.isEmpty(cloudName)) {
            throw new RuntimeException("cloudServer name can not be null");
        }
        CloudServerPlugin cloudServerPlugin = null;
        //CloudSdkLoader.CLOUD_PLUGINS.get(cloudName);
        if (cloudServerPlugin != null) {
            List<StartResponse> startResponseList = cloudServerPlugin.startInstance(baseStartRequest);
            return ResponseResult.success(startResponseList);
        } else {
            throw new RuntimeException("cloud platform do not has this sdk；cloudName：  " +
                cloudName);
        }
    }

    @Override
    public ResponseResult deleteInstance(String cloudName, BaseDeleteRequest baseDeleteRequest) {
        if (StringUtil.isEmpty(cloudName)) {
            throw new RuntimeException("cloudServer name can not be null");
        }
        CloudServerPlugin cloudServerPlugin = null;
        //CloudSdkLoader.CLOUD_PLUGINS.get(cloudName);
        if (cloudServerPlugin != null) {
            List<DeleteResponse> deleteResponseList = cloudServerPlugin.deleteInstance(baseDeleteRequest);
            return ResponseResult.success(deleteResponseList);
        } else {
            throw new RuntimeException("cloud platform do not has this sdk；cloudName：  " +
                cloudName);
        }
    }

    @Override
    public ResponseResult initialize(String cloudName, BaseInitRequest baseInitRequest) {
        if (StringUtil.isEmpty(cloudName)) {
            throw new RuntimeException("cloudServer name can not be null");
        }
        CloudServerPlugin cloudServerPlugin = null;
        //CloudSdkLoader.CLOUD_PLUGINS.get(cloudName);
        if (cloudServerPlugin != null) {
            List<InitResponse> initResponseList = cloudServerPlugin.initialize(baseInitRequest);
            return ResponseResult.success(initResponseList);
        } else {
            throw new RuntimeException("cloud platform do not has this sdk；cloudName：  " +
                cloudName);
        }
    }

    @Override
    public ResponseResult rebootInstance(String cloudName) {
        return null;
    }

    @Override
    public ResponseResult queryInstanceMap(String cloudName) {
        if (StringUtil.isEmpty(cloudName)) {
            throw new RuntimeException("cloudServer name can not be null");
        }
        CloudServerPlugin cloudServerPlugin = null;
        //CloudSdkLoader.CLOUD_PLUGINS.get(cloudName);
        if (cloudServerPlugin != null) {
            Map<String, String> specMap = cloudServerPlugin.getSpecMap();
            return ResponseResult.success(specMap);
        } else {
            throw new RuntimeException("cloud platform do not has this sdk；cloudName：  " +
                cloudName);
        }
    }
}
