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

import java.util.Collections;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import com.google.common.collect.Lists;
//import io.shulie.amdb.common.dto.link.entrance.ServiceInfoDTO;
//import io.shulie.amdb.common.enums.EdgeTypeGroupEnum;
//import io.shulie.amdb.common.enums.RpcType;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.amdb.api.ApplicationClient;
import io.shulie.tro.web.amdb.bean.common.AmdbResponse;
import io.shulie.tro.web.amdb.bean.common.AmdbResult;
import io.shulie.tro.web.amdb.bean.query.application.ApplicationErrorQueryDTO;
import io.shulie.tro.web.amdb.bean.query.application.ApplicationInterfaceQueryDTO;
import io.shulie.tro.web.amdb.bean.query.application.ApplicationNodeQueryDTO;
import io.shulie.tro.web.amdb.bean.query.application.ApplicationQueryDTO;
import io.shulie.tro.web.amdb.bean.result.application.ApplicationDTO;
import io.shulie.tro.web.amdb.bean.result.application.ApplicationErrorDTO;
import io.shulie.tro.web.amdb.bean.result.application.ApplicationInterfaceDTO;
import io.shulie.tro.web.amdb.bean.result.application.ApplicationNodeDTO;
import io.shulie.tro.web.amdb.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.tro.properties.AmdbClientProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @author shiyajian
 * create: 2020-10-13
 */
@Component
@Slf4j
public class ApplicationClientImpl implements ApplicationClient {

    //private static final String INTERFACE_PATH = "/WhiteList?appName=@appName@";
    private static final String INTERFACE_PATH = "/amdb/link/getServiceList";
    private static final String APPLICATION_QUERY_PATH = "/amdb/db/api/app/selectByBatchAppParams";
    private static final String APPLICATION_QUERY_ALL_PATH = "/amdb/db/api/app/selectAllAppName";
    private static final String APPLICATION_NODE_QUERY_PATH = "/amdb/db/api/appInstance/selectByBatchAppParams";
    private static final String APPLICATION_ERROR_QUERY_PATH = "/amdb/db/api/appInstance/selectErrorInfoByParams";

    @Autowired
    private AmdbClientProperties properties;

    @Override
    public List<ApplicationInterfaceDTO> listInterfaces(ApplicationInterfaceQueryDTO query) {
        return pageInterfaces(query).getList();
    }

    //private List<ApplicationInterfaceDTO> getApplicationInterfaceDTOS(AmdbResult<List<ServiceInfoDTO>> amdbResponse) {
    //    return amdbResponse.getData().stream().map(serviceInfoDTO -> {
    //        ApplicationInterfaceDTO interfaceDTO = new ApplicationInterfaceDTO();
    //        interfaceDTO.setId("0");
    //        String interfaceName;
    //        switch (Integer.parseInt(serviceInfoDTO.getRpcType())) {
    //            case RpcType.TYPE_WEB_SERVER:
    //                interfaceName = serviceInfoDTO.getServiceName();
    //                break;
    //            case RpcType.TYPE_RPC:
    //                // 去掉参数列表  setUser(com.example.clientdemo.userModel)   setUser~(com.example.clientdemo
    //                // .userModel)
    //                interfaceName = serviceInfoDTO.getServiceName().split(":")[0] + "#" + serviceInfoDTO.getMethodName()
    //                    .split("~")[0].split("\\(")[0];
    //                break;
    //            default:
    //                interfaceName = serviceInfoDTO.getServiceName() + "#" + serviceInfoDTO.getMethodName();
    //        }
    //        interfaceDTO.setInterfaceName(interfaceName);
    //        EdgeTypeGroupEnum edgeTypeGroupEnum = EdgeTypeGroupEnum.getEdgeTypeEnum(
    //            serviceInfoDTO.getMiddlewareName());
    //        interfaceDTO.setInterfaceType(edgeTypeGroupEnum.getType());
    //        // 应用名
    //        interfaceDTO.setAppName(serviceInfoDTO.getAppName());
    //        return interfaceDTO;
    //    }).distinct().collect(Collectors.toList());
    //}

    @Override
    public PagingList<ApplicationInterfaceDTO> pageInterfaces(ApplicationInterfaceQueryDTO query) {
        return PagingList.empty();
        //String url = properties.getUrl().getAmdb() + INTERFACE_PATH;
        //query.setFieldNames("appName,middlewareName,serviceName,methodName,rpcType");
        //query.setRpcType(StringUtils.join(Lists.newArrayList(String.valueOf(RpcType.TYPE_WEB_SERVER), String.valueOf(RpcType.TYPE_RPC)), ","));
        //try {
        //    String responseEntity = HttpClientUtil.sendPost(url,query);
        //    if (StringUtils.isBlank(responseEntity)) {
        //        log.error("前往pardar查询应用的接口信息报错,请求地址：{}，响应信息：{}", url, responseEntity);
        //        return PagingList.empty();
        //    } else {
        //        AmdbResult<List<ServiceInfoDTO>> amdbResponse = JSON.parseObject(responseEntity,
        //            new TypeReference<AmdbResult<List<ServiceInfoDTO>>>() {
        //            });
        //        //List<ServiceInfoDTO> serviceInfoDTOList = JSON.parseArray(responseEntity, ServiceInfoDTO.class);
        //        List<ApplicationInterfaceDTO> dtos =  getApplicationInterfaceDTOS(amdbResponse);
        //        return PagingList.of(dtos,amdbResponse.getTotal());
        //    }
        //} catch (Exception e) {
        //    log.error("前往pardar查询应用的接口信息报错,请求地址：{}，异常信息：{}", url, e.getMessage());
        //    return PagingList.empty();
        //}
    }

    @Override
    public PagingList<ApplicationDTO> pageApplications(ApplicationQueryDTO query) {
        String url = properties.getUrl().getAmdb() + APPLICATION_QUERY_PATH;
        try {
            String responseEntity = HttpClientUtil.sendGet(url, query);
            if (StringUtils.isEmpty(responseEntity)) {
                return PagingList.empty();
            }
            AmdbResponse<ApplicationDTO> amdbResponse = JSON.parseObject(responseEntity,
                new TypeReference<AmdbResponse<ApplicationDTO>>() {
                });
            if (amdbResponse == null || !amdbResponse.getSuccess()) {
                log.error("前往amdb查询应用信息返回异常,响应信息：{}", JSON.toJSONString(amdbResponse));
                return PagingList.empty();
            }
            List<ApplicationDTO> data = amdbResponse.getData();
            if (CollectionUtils.isEmpty(data)) {
                return PagingList.empty();
            }
            return PagingList.of(data, amdbResponse.getTotal());

        } catch (Exception e) {
            log.error("前往amdb查询应用信息信息报错：{}", JSON.toJSONString(query), e);
            return PagingList.empty();
        }
    }

    @Override
    public PagingList<ApplicationNodeDTO> pageApplicationNodes(ApplicationNodeQueryDTO query) {
        String url = properties.getUrl().getAmdb() + APPLICATION_NODE_QUERY_PATH;
        try {
            String responseEntity = HttpClientUtil.sendGet(url, query);
            AmdbResponse<ApplicationNodeDTO> amdbResponse = JSON.parseObject(responseEntity,
                new TypeReference<AmdbResponse<ApplicationNodeDTO>>() {
                });
            if (amdbResponse == null || !amdbResponse.getSuccess()) {
                log.error("前往amdb查询应用节点返回异常,响应信息：{}", JSON.toJSONString(amdbResponse));
                return PagingList.empty();
            }
            List<ApplicationNodeDTO> data = amdbResponse.getData();
            if (CollectionUtils.isEmpty(data)) {
                return PagingList.empty();
            }
            return PagingList.of(data, amdbResponse.getTotal());

        } catch (Exception e) {
            log.error("前往amdb查询应用节点信息报错：{}", JSON.toJSONString(query), e);
            return PagingList.empty();
        }
    }

    @Override
    public List<ApplicationErrorDTO> listErrors(ApplicationErrorQueryDTO query) {
        String url = properties.getUrl().getAmdb() + APPLICATION_ERROR_QUERY_PATH;
        try {
            String responseEntity = HttpClientUtil.sendGet(url, query);
            AmdbResponse<ApplicationErrorDTO> amdbResponse = JSON.parseObject(responseEntity,
                new TypeReference<AmdbResponse<ApplicationErrorDTO>>() {
                });
            if (amdbResponse == null || !amdbResponse.getSuccess()) {
                log.error("前往amdb查询应用异常信息返回异常,响应信息：{}", JSON.toJSONString(amdbResponse));
                return Collections.emptyList();
            }
            List<ApplicationErrorDTO> data = amdbResponse.getData();
            if (CollectionUtils.isEmpty(data)) {
                return Collections.emptyList();
            }
            return amdbResponse.getData();

        } catch (Exception e) {
            log.error("前往amdb查询应用异常信息报错：{}", JSON.toJSONString(query), e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> getAllApplicationName() {
        String url = properties.getUrl().getAmdb() + APPLICATION_QUERY_ALL_PATH;
        try {
            String responseEntity = HttpClientUtil.sendGet(url);
            if (StringUtils.isEmpty(responseEntity)) {
                return Lists.newArrayList();
            }
            AmdbResult<List<String>> amdbResponse = JSON.parseObject(responseEntity,
                new TypeReference<AmdbResult<List<String>>>() {
                });
            if (amdbResponse == null || !amdbResponse.getSuccess()) {
                log.error("前往amdb查询应用信息返回异常,响应信息：{}", JSON.toJSONString(amdbResponse));
                return Lists.newArrayList();
            }
            List<String> data = amdbResponse.getData();
            if (CollectionUtils.isEmpty(data)) {
                return Lists.newArrayList();
            }
            return data;

        } catch (Exception e) {
            log.error("前往amdb查询全部应用信息信息报错", e);
            return Lists.newArrayList();
        }
    }
}
