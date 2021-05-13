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

package io.shulie.tro.web.app.service.dsManage;

import java.util.List;

import com.pamirs.tro.common.enums.ds.DsTypeEnum;
import com.pamirs.tro.entity.domain.entity.DsModelWithBLOBs;
import com.pamirs.tro.entity.domain.entity.simplify.AppBusinessTableInfo;
import com.pamirs.tro.entity.domain.query.agent.AppBusinessTableQuery;
import com.pamirs.tro.entity.domain.vo.dsmanage.DsAgentVO;
import com.pamirs.tro.entity.domain.vo.dsmanage.DsServerVO;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.request.application.ApplicationDsCreateRequest;
import io.shulie.tro.web.app.request.application.ApplicationDsDeleteRequest;
import io.shulie.tro.web.app.request.application.ApplicationDsEnableRequest;
import io.shulie.tro.web.app.request.application.ApplicationDsUpdateRequest;
import io.shulie.tro.web.app.response.application.ApplicationDsDetailResponse;
import io.shulie.tro.web.app.response.application.ShadowServerConfigurationResponse;

/**
 * @Author: fanxx
 * @Date: 2020/3/12 下午3:35
 * @Description:
 */
public interface DsService {

    String parseShadowDbUrl(String config);

    Response dsAdd(ApplicationDsCreateRequest createRequest);

    Response dsUpdate(ApplicationDsUpdateRequest updateRequest);

    Response dsQuery(Long applicationId);

    /**
     * 影子详情
     *
     * @param dsId         dsId
     * @param isOldVersion 是否是老版本
     * @return 影子详情示例
     */
    Response<ApplicationDsDetailResponse> dsQueryDetail(Long dsId, boolean isOldVersion);

    Response enableConfig(ApplicationDsEnableRequest enableRequest);

    Response dsDelete(ApplicationDsDeleteRequest dsDeleteRequest);

    List<DsAgentVO> getConfigs(String appName);

    List<ShadowServerConfigurationResponse> getShadowServerConfigs(String appName);

    void addBusiness(AppBusinessTableInfo info);

    Response queryPageBusiness(AppBusinessTableQuery query);

    List<DsModelWithBLOBs> getAllEnabledDbConfig(Long applicationId);

    /**
     * 安全初始化
     * @return
     */
    Response secureInit();

    /**
     * 查询影子配置数据
     * @param namespace 命令空间
     * @param shadowHbaseServer 影子枚举类型
     * @return
     */
    List<DsServerVO> getShadowDsServerConfigs(String namespace, DsTypeEnum shadowHbaseServer);
}
