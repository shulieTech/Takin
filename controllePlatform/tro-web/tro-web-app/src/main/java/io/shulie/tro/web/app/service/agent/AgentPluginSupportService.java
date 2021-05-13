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

package io.shulie.tro.web.app.service.agent;

import java.util.List;

import io.shulie.tro.web.app.response.application.AgentPluginSupportResponse;
import io.shulie.tro.web.app.response.linkmanage.MiddleWareResponse;

/**
 * agent 中插件支持的功能
 *
 * @author shiyajian
 * create: 2020-10-09
 */
public interface AgentPluginSupportService {

    /**
     * 获得agent插件的支持列表
     */
    List<AgentPluginSupportResponse> queryAgentPluginSupportList();

    // 2、新增 agent 插件和jar包的关联关系

    // 3、删除 agent 插件和 jar包的关联关系

    /**
     * 判断 agent 是否支持当前jar包
     */
    Boolean isSupportLib(List<AgentPluginSupportResponse> supportList, String libName);

    MiddleWareResponse convertLibInfo(List<AgentPluginSupportResponse> supportList, String libName);
}
