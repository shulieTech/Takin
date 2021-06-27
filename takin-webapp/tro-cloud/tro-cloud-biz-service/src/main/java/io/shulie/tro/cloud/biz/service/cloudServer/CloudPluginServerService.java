///*
// * Copyright 2021 Shulie Technology, Co.Ltd
// * Email: shulie@shulie.io
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package io.shulie.tro.cloud.biz.service.cloudServer;
//
//import io.shulie.flpt.core.plugin.BaseCreateRequest;
//import io.shulie.flpt.core.plugin.BaseDeleteRequest;
//import io.shulie.flpt.core.plugin.BaseInitRequest;
//import io.shulie.flpt.core.plugin.BaseQueryRequest;
//import io.shulie.flpt.core.plugin.BaseStartRequest;
//import io.shulie.tro.common.beans.response.ResponseResult;
//
///**
// * @Author: mubai
// * @Date: 2020-05-09 14:43
// * @Description:
// */
//public interface CloudPluginServerService {
//
//    /**
//     * 购买机器
//     */
//    ResponseResult createInstance(String cloudName, BaseCreateRequest request);
//
//    ResponseResult queryInstance(String cloudName, BaseQueryRequest request);
//
//    /**
//     * 启动机器
//     */
//    ResponseResult startInstance(String cloudName, BaseStartRequest baseStartRequest);
//
//    /**
//     * 销毁机器
//     */
//    ResponseResult deleteInstance(String cloudName, BaseDeleteRequest baseDeleteRequest);
//
//    /**
//     * 初始化环境
//     */
//    ResponseResult initialize(String cloudName, BaseInitRequest baseInitRequest);
//
//    /**
//     * 重启机器
//     *
//     * @return
//     */
//    ResponseResult rebootInstance(String cloudName);
//
//    ResponseResult queryInstanceMap(String cloudName);
//
//}
