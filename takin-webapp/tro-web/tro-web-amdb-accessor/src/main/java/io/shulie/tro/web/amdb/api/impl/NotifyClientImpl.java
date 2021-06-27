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

package io.shulie.tro.web.amdb.api.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

//import io.shulie.amdb.common.request.link.CalculateParam;
import io.shulie.tro.web.amdb.api.NotifyClient;
import io.shulie.tro.web.amdb.bean.common.AmdbResult;
import io.shulie.tro.web.amdb.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.tro.properties.AmdbClientProperties;
import org.springframework.stereotype.Component;

/**
 * @author shiyajian
 * create: 2020-12-29
 */
@Component
@Slf4j
public class NotifyClientImpl implements NotifyClient {

    public static final String NOTIFY_START_CALCULATE_PATH = "/amdb/linkConfig/openLinkConfig";

    public static final String NOTIFY_STOP_CALCULATE_PATH = "/amdb/linkConfig/closeLinkConfig";

    @Autowired
    private AmdbClientProperties properties;

    //@Override
    //public boolean startApplicationEntrancesCalculate(String applicationName, String serviceName,
    //    String method, String type,String extend) {
    //    String url = properties.getUrl().getAmdb() + NOTIFY_START_CALCULATE_PATH;
    //    CalculateParam calculateParam = new CalculateParam();
    //    calculateParam.setAppName(applicationName);
    //    calculateParam.setRpcType(type);
    //    calculateParam.setServiceName(serviceName);
    //    calculateParam.setMethod(method);
    //    calculateParam.setExtend(extend);
    //    try {
    //        String responseEntity = HttpClientUtil.sendPost(url, calculateParam);
    //        if (StringUtils.isBlank(responseEntity)) {
    //            log.error("前往amdb开始链路计算返回异常,请求地址：{}，请求参数：{}", url, JSON.toJSONString(calculateParam));
    //            return false;
    //        }
    //        AmdbResult<String> amdbResponse = JSON.parseObject(responseEntity,
    //            new TypeReference<AmdbResult<String>>() {
    //            });
    //        if (amdbResponse == null || !amdbResponse.getSuccess()) {
    //            log.error("前往amdb开始链路计算返回异常,请求地址：{}，请求参数：{}", url, JSON.toJSONString(calculateParam));
    //            return false;
    //        }
    //        return true;
    //    } catch (Exception e) {
    //        log.error("前往amdb开始链路计算返回异常,请求地址：{}，请求参数：{}", url, JSON.toJSONString(calculateParam), e);
    //        return false;
    //    }
    //}
    //
    //@Override
    //public boolean stopApplicationEntrancesCalculate(String applicationName, String serviceName, String method,
    //    String rpcType,String extend) {
    //    String url = properties.getUrl().getAmdb() + NOTIFY_STOP_CALCULATE_PATH;
    //    CalculateParam calculateParam = new CalculateParam();
    //    calculateParam.setAppName(applicationName);
    //    calculateParam.setRpcType(rpcType);
    //    calculateParam.setServiceName(serviceName);
    //    calculateParam.setMethod(method);
    //    calculateParam.setExtend(extend);
    //    try {
    //        String responseEntity = HttpClientUtil.sendPost(url, calculateParam);
    //        if (StringUtils.isBlank(responseEntity)) {
    //            log.error("前往amdb结束链路计算返回异常,请求地址：{}，请求参数：{}", url, JSON.toJSONString(calculateParam));
    //            return false;
    //        }
    //        AmdbResult<String> amdbResponse = JSON.parseObject(responseEntity,
    //            new TypeReference<AmdbResult<String>>() {
    //            });
    //        if (amdbResponse == null || !amdbResponse.getSuccess()) {
    //            log.error("前往amdb结束链路计算返回异常,请求地址：{}，请求参数：{}", url, JSON.toJSONString(calculateParam));
    //            return false;
    //        }
    //        return true;
    //    } catch (Exception e) {
    //        log.error("前往amdb结束链路计算返回异常,请求地址：{}，请求参数：{}", url, JSON.toJSONString(calculateParam), e);
    //        return false;
    //    }
    //}
}
