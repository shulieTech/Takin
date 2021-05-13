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

package io.shulie.tro.cloud.open.api.impl.scenemanage;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import io.shulie.tro.cloud.common.exception.TroCloudExceptionEnum;
import io.shulie.tro.cloud.open.api.impl.CloudCommonApi;
import io.shulie.tro.cloud.open.api.impl.aop.annotation.ApiPointCut;
import io.shulie.tro.cloud.open.api.impl.util.UrlBusinessUtil;
import io.shulie.tro.cloud.open.api.scenemanage.CloudSceneApi;
import io.shulie.tro.cloud.open.constant.CloudApiConstant;
import io.shulie.tro.cloud.open.req.HttpCloudRequest;
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
import io.shulie.tro.cloud.open.resp.scenemanage.SceneManageListResp;
import io.shulie.tro.cloud.open.resp.scenemanage.SceneManageWrapperResp;
import io.shulie.tro.cloud.open.resp.strategy.StrategyResp;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.utils.http.HttpHelper;

import org.apache.commons.lang3.StringUtils;
import io.shulie.tro.utils.http.TroResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.tro.properties.TroCloudClientProperties;
import org.springframework.stereotype.Component;

/**
 * @author 何仲奇
 * @Package io.shulie.tro.cloud.client.api.impl
 * @date 2020/10/21 3:03 下午
 */
@Component
public class CloudSceneApiImpl extends CloudCommonApi implements CloudSceneApi {

    @Autowired
    private TroCloudClientProperties troCloudClientProperties;

    @ApiPointCut(name = "cloudScene", errorCode = TroCloudExceptionEnum.SCENE_MANAGE_UPDATE_FILE_ERROR)
    @Override
    public Object updateSceneFileByScriptId(UpdateSceneFileRequest request) {
        return HttpHelper.doPut(UrlBusinessUtil.getSceneMangeUpdateFileUrl(),
            this.getHeaders(request.getLicense()), new TypeReference<ResponseResult<Object>>() {}, request);
    }

    @Override
    public ResponseResult<Long> saveScene(SceneManageWrapperReq req) {
        TroResponseEntity<ResponseResult<Long>> troResponseEntity =
            HttpHelper.doPost(troCloudClientProperties.getUrl() + CloudApiConstant.SCENE_MANAGE_URL,
                getHeaders(req.getLicense()), new TypeReference<ResponseResult<Long>>() {},req);
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");
    }

    @Override
    public ResponseResult<String> updateScene(SceneManageWrapperReq req) {

        TroResponseEntity<ResponseResult<String>> troResponseEntity =
            HttpHelper.doPut(troCloudClientProperties.getUrl() + CloudApiConstant.SCENE_MANAGE_URL,
                getHeaders(req.getLicense()), new TypeReference<ResponseResult<String>>() {},req);
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");
    }

    @Override
    public ResponseResult<String> deleteScene(SceneManageDeleteReq req) {
        TroResponseEntity<ResponseResult<String>> troResponseEntity =
            HttpHelper.doDelete(troCloudClientProperties.getUrl() + CloudApiConstant.SCENE_MANAGE_URL,
                getHeaders(req.getLicense()), new TypeReference<ResponseResult<String>>() {},req);
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");
    }

    @Override
    public ResponseResult<SceneManageWrapperResp> getSceneDetail(SceneManageIdReq req) {
        TroResponseEntity<ResponseResult<SceneManageWrapperResp>> troResponseEntity =
            HttpHelper.doGet(troCloudClientProperties.getUrl() + CloudApiConstant.SCENE_MANAGE_DETAIL_URL,
                getHeaders(req.getLicense()),req,new TypeReference<ResponseResult<SceneManageWrapperResp>>() {});
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");
    }

    @Override
    public ResponseResult<List<SceneManageListResp>> getSceneManageList(HttpCloudRequest req) {
        TroResponseEntity<ResponseResult<List<SceneManageListResp>>> troResponseEntity =
            HttpHelper.doGet(troCloudClientProperties.getUrl() + CloudApiConstant.SCENE_MANAGE_All_LIST_URL,
                getHeaders(req.getLicense()),req,new TypeReference<ResponseResult<List<SceneManageListResp>>>() {});
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");
    }

    @Override
    public ResponseResult<List<SceneManageListResp>> getSceneList(SceneManageQueryReq req) {
        TroResponseEntity<ResponseResult<List<SceneManageListResp>>> troResponseEntity =
            HttpHelper.doGet(troCloudClientProperties.getUrl() + CloudApiConstant.SCENE_MANAGE_LIST_URL,
                //过滤
                getHeaders(req.getLicense(),req.getFilterSql()),req,new TypeReference<ResponseResult<List<SceneManageListResp>>>() {});
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");

    }

    @Override
    public ResponseResult<BigDecimal> calcFlow(SceneManageWrapperReq req) {
        TroResponseEntity<ResponseResult<BigDecimal>> troResponseEntity =
            HttpHelper.doPost(troCloudClientProperties.getUrl() + CloudApiConstant.SCENE_MANAGE_FLOWCALC_URL,
                getHeaders(req.getLicense()),new TypeReference<ResponseResult<BigDecimal>>() {},req);
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");
    }


    @Override
    public ResponseResult<StrategyResp> getIpNum(SceneIpNumReq req) {
        TroResponseEntity<ResponseResult<StrategyResp>> troResponseEntity =
            HttpHelper.doGet(troCloudClientProperties.getUrl() + CloudApiConstant.SCENE_MANAGE_IPNUM_URL,
                getHeaders(req.getLicense()),req,new TypeReference<ResponseResult<StrategyResp>>() {});
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");
    }

    @Override
    public ResponseResult<Map<String, Object>> parseScript(SceneParseReq req) {
        TroResponseEntity<ResponseResult<Map<String, Object>>> troResponseEntity =
            HttpHelper.doGet(troCloudClientProperties.getUrl() + CloudApiConstant.SCENE_MANAGE_PARSE_URL,
                getHeaders(req.getLicense()),req,new TypeReference<ResponseResult<Map<String, Object>>>() {});
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");
    }

    @Override
    public ResponseResult<Map<String, Object>> parseAndUpdateScript(SceneParseReq req) {
        TroResponseEntity<ResponseResult<Map<String, Object>>> troResponseEntity =
                HttpHelper.doGet(troCloudClientProperties.getUrl() + CloudApiConstant.SCENE_MANAGE_PARSE_AND_UPDATE_URL,
                        getHeaders(req.getLicense()),req,new TypeReference<ResponseResult<Map<String, Object>>>() {});
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
                troResponseEntity.getErrorMsg(),"查看cloud日志");
    }

    /**
     * 压测场景-分配负责人
     *
     * @param req
     * @return
     */
    @Override
    public ResponseResult<String> allocationSceneUser(SceneAllocationUserReq req) {
        TroResponseEntity<ResponseResult<String>> troResponseEntity =
            HttpHelper.doGet(troCloudClientProperties.getUrl() + CloudApiConstant.SCENE_MANAGE_ALLOCATION,
                getHeaders(req.getLicense()),req,new TypeReference<ResponseResult<String>>() {});
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");
    }

    /**
     * 压测报表-分配负责人
     *
     * @param req
     * @return
     */
    @Override
    public ResponseResult<String> allocationReportUser(ReportAllocationUserReq req) {
        TroResponseEntity<ResponseResult<String>> troResponseEntity =
            HttpHelper.doGet(troCloudClientProperties.getUrl() + CloudApiConstant.SCENE_REPORT_ALLOCATION,
                getHeaders(req.getLicense()),req,new TypeReference<ResponseResult<String>>() {});
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");
    }

    @Override
    public ResponseResult<List<SceneManageWrapperResp>> queryByIds(SceneManageQueryByIdsReq req) {

        String url = troCloudClientProperties.getUrl() + CloudApiConstant.SCENE_MANAGE_BY_SCENE_IDS ;
        List<Long> sceneIds = req.getSceneIds();
        String join = StringUtils.join(sceneIds, ",");
        url = url + "?sceneIds=" + join;
        TroResponseEntity<ResponseResult<List<SceneManageWrapperResp>>> troResponseEntity =
            HttpHelper.doGet(url, getHeaders(req.getLicense()),new TypeReference<ResponseResult<List<SceneManageWrapperResp>>>() {});
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");
    }

}
