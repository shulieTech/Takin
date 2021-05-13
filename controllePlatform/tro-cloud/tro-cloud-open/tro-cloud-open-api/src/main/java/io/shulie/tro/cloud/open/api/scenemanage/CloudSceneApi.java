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

package io.shulie.tro.cloud.open.api.scenemanage;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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

/**
 * @author 何仲奇
 * @Package io.shulie.tro.cloud.client.api
 * @date 2020/10/20 3:42 下午
 */
public interface CloudSceneApi {

    /**
     * 根据脚本实例id, 更新所有的场景对应该脚本的文件
     *
     * @param updateSceneFileRequest 请求入参
     * @return 是否成功结果
     */
    Object updateSceneFileByScriptId(UpdateSceneFileRequest updateSceneFileRequest);

    /**
     * 保存
     *
     * @param sceneManageWrapperReq
     * @return
     */
    ResponseResult<Long> saveScene(SceneManageWrapperReq sceneManageWrapperReq);

    /**
     * 更新
     *
     * @param sceneManageWrapperReq
     * @return
     */
    ResponseResult<String> updateScene(SceneManageWrapperReq sceneManageWrapperReq);

    /**
     * 删除
     *
     * @param sceneManageDeleteReq
     * @return
     */
    ResponseResult<String> deleteScene(SceneManageDeleteReq sceneManageDeleteReq);

    /**
     * 获取场景明细 供编辑使用
     *
     * @param sceneManageIdVO
     * @return
     */
    ResponseResult<SceneManageWrapperResp> getSceneDetail(SceneManageIdReq sceneManageIdVO);

    /**
     * 不分页查询所有场景带脚本信息
     *
     * @param request
     * @return
     */
    ResponseResult<List<SceneManageListResp>> getSceneManageList(HttpCloudRequest request);

    /**
     * 获取压测场景列表
     *
     * @param sceneManageQueryReq
     * @return
     */
    ResponseResult<List<SceneManageListResp>> getSceneList(SceneManageQueryReq sceneManageQueryReq);


    /**
     * 流量计算
     *
     * @param sceneManageWrapperReq
     * @return
     */
    ResponseResult<BigDecimal> calcFlow(SceneManageWrapperReq sceneManageWrapperReq);

    /**
     * 获取机器数量范围
     *
     * @param sceneIpNumReq
     * @return
     */
    ResponseResult<StrategyResp> getIpNum(SceneIpNumReq sceneIpNumReq);

    /**
     * 解析脚本
     *
     * @param sceneParseReq
     * @return
     */
    ResponseResult<Map<String, Object>> parseScript(SceneParseReq sceneParseReq);

    /**
     * 解析并更新脚本
     *
     * @param sceneParseReq
     * @return
     */
    ResponseResult<Map<String, Object>> parseAndUpdateScript(SceneParseReq sceneParseReq);


    /**
     * 压测场景-分配负责人
     *
     * @return
     */
    ResponseResult<String> allocationSceneUser(SceneAllocationUserReq req);

    /**
     * 压测报表-分配负责人
     *
     * @param req
     * @return
     */
    ResponseResult<String> allocationReportUser(ReportAllocationUserReq req);

    ResponseResult<List<SceneManageWrapperResp>> queryByIds (SceneManageQueryByIdsReq req) ;

}
