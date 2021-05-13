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

package io.shulie.tro.web.diff.cloud.impl.scenemange;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import io.shulie.tro.cloud.open.api.engine.CloudEngineApi;
import io.shulie.tro.cloud.open.api.scenemanage.CloudSceneApi;
import io.shulie.tro.cloud.open.req.HttpCloudRequest;
import io.shulie.tro.cloud.open.req.engine.EnginePluginDetailsWrapperReq;
import io.shulie.tro.cloud.open.req.engine.EnginePluginFetchWrapperReq;
import io.shulie.tro.cloud.open.req.report.ReportAllocationUserReq;
import io.shulie.tro.cloud.open.req.scenemanage.SceneAllocationUserReq;
import io.shulie.tro.cloud.open.req.scenemanage.SceneIpNumReq;
import io.shulie.tro.cloud.open.req.scenemanage.SceneManageDeleteReq;
import io.shulie.tro.cloud.open.req.scenemanage.SceneManageIdReq;
import io.shulie.tro.cloud.open.req.scenemanage.SceneManageQueryByIdsReq;
import io.shulie.tro.cloud.open.req.scenemanage.SceneManageQueryReq;
import io.shulie.tro.cloud.open.req.scenemanage.SceneManageWrapperReq;
import io.shulie.tro.cloud.open.req.scenemanage.SceneParseReq;
import io.shulie.tro.cloud.open.req.scenemanage.UpdateSceneFileRequest;
import io.shulie.tro.cloud.open.resp.engine.EnginePluginDetailResp;
import io.shulie.tro.cloud.open.resp.engine.EnginePluginSimpleInfoResp;
import io.shulie.tro.cloud.open.resp.scenemanage.SceneManageListResp;
import io.shulie.tro.cloud.open.resp.scenemanage.SceneManageWrapperResp;
import io.shulie.tro.cloud.open.resp.strategy.StrategyResp;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.web.common.constant.RemoteConstant;
import io.shulie.tro.web.diff.api.scenemanage.SceneManageApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhaoyong
 */
@Component
public class SceneManageApiImpl implements SceneManageApi {

    @Autowired
    private CloudSceneApi cloudSceneApi;

    @Autowired
    private CloudEngineApi cloudEngineApi;

    @Override
    public ResponseResult updateSceneFileByScriptId(UpdateSceneFileRequest updateSceneFileRequest) {
        return (ResponseResult)cloudSceneApi.updateSceneFileByScriptId(updateSceneFileRequest);
    }

    @Override
    public ResponseResult<Long> saveScene(SceneManageWrapperReq sceneManageWrapperReq) {
        return cloudSceneApi.saveScene(sceneManageWrapperReq);
    }

    @Override
    public ResponseResult updateScene(SceneManageWrapperReq req) {
        return cloudSceneApi.updateScene(req);
    }

    @Override
    public ResponseResult deleteScene(SceneManageDeleteReq sceneManageDeleteReq) {
        return cloudSceneApi.deleteScene(sceneManageDeleteReq);
    }

    @Override
    public ResponseResult<SceneManageWrapperResp> getSceneDetail(SceneManageIdReq sceneManageIdVO) {
        return cloudSceneApi.getSceneDetail(sceneManageIdVO);
    }

    @Override
    public ResponseResult<List<SceneManageListResp>> getSceneList(SceneManageQueryReq sceneManageQueryReq) {
        return cloudSceneApi.getSceneList(sceneManageQueryReq);
    }

    @Override
    public ResponseResult<BigDecimal> calcFlow(SceneManageWrapperReq sceneManageWrapperReq) {
        return cloudSceneApi.calcFlow(sceneManageWrapperReq);
    }

    @Override
    public ResponseResult<StrategyResp> getIpNum(SceneIpNumReq sceneIpNumReq) {
        return cloudSceneApi.getIpNum(sceneIpNumReq);
    }

    @Override
    public ResponseResult<Map<String, Object>> parseScript(SceneParseReq sceneParseReq) {
        return cloudSceneApi.parseScript(sceneParseReq);
    }

    @Override
    public ResponseResult<List<SceneManageWrapperResp>> getByIds(SceneManageQueryByIdsReq req) {
        req.setLicense(RemoteConstant.LICENSE_VALUE);
        return cloudSceneApi.queryByIds(req);
    }

    @Override
    public ResponseResult<Map<String, Object>> parseAndUpdateScript(SceneParseReq sceneParseReq) {
        return cloudSceneApi.parseAndUpdateScript(sceneParseReq);
    }
    /**
     * 压测场景-分配负责人
     *
     * @param req
     * @return
     */
    @Override
    public ResponseResult allocationSceneUser(SceneAllocationUserReq req) {
        return cloudSceneApi.allocationSceneUser(req);
    }

    /**
     * 压测报表-分配负责人
     *
     * @param req
     * @return
     */
    @Override
    public ResponseResult allocationReportUser(ReportAllocationUserReq req) {
        return cloudSceneApi.allocationReportUser(req);
    }

    @Override
    public ResponseResult<List<SceneManageListResp>> getSceneManageList() {
        HttpCloudRequest httpCloudRequest = new HttpCloudRequest();
        httpCloudRequest.setLicense(RemoteConstant.LICENSE_VALUE);
        return cloudSceneApi.getSceneManageList(httpCloudRequest);
    }

    /**
     * 获取支持的jmeter插件列表
     *
     * @param wrapperReq
     * @return
     */
    @Override
    public ResponseResult<Map<String, List<EnginePluginSimpleInfoResp>>> listEnginePlugins(
        EnginePluginFetchWrapperReq wrapperReq) {
        return cloudEngineApi.listEnginePlugins(wrapperReq);
    }

    /**
     * 获取支持的jmeter插件详情
     *
     * @param wrapperReq
     * @return
     */
    @Override
    public ResponseResult<EnginePluginDetailResp> getEnginePluginDetails(EnginePluginDetailsWrapperReq wrapperReq) {
        return cloudEngineApi.getEnginePluginDetails(wrapperReq);
    }
}
