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

package io.shulie.tro.web.app.service.scriptmanage;

import java.util.List;

import com.pamirs.tro.entity.domain.dto.scenemanage.ScriptCheckDTO;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.request.scriptmanage.ScriptManageDeployCreateRequest;
import io.shulie.tro.web.app.request.scriptmanage.ScriptManageDeployPageQueryRequest;
import io.shulie.tro.web.app.request.scriptmanage.ScriptManageDeployUpdateRequest;
import io.shulie.tro.web.app.request.scriptmanage.ScriptTagCreateRefRequest;
import io.shulie.tro.web.app.request.scriptmanage.SupportJmeterPluginNameRequest;
import io.shulie.tro.web.app.request.scriptmanage.SupportJmeterPluginVersionRequest;
import io.shulie.tro.web.app.response.scriptmanage.ScriptManageActivityResponse;
import io.shulie.tro.web.app.response.scriptmanage.ScriptManageDeployDetailResponse;
import io.shulie.tro.web.app.response.scriptmanage.ScriptManageDeployResponse;
import io.shulie.tro.web.app.response.scriptmanage.ScriptManageSceneManageResponse;
import io.shulie.tro.web.app.response.scriptmanage.SupportJmeterPluginNameResponse;
import io.shulie.tro.web.app.response.scriptmanage.SupportJmeterPluginVersionResponse;
import io.shulie.tro.web.app.request.scriptmanage.*;
import io.shulie.tro.web.app.response.scriptmanage.*;
import io.shulie.tro.web.app.response.tagmanage.TagManageResponse;

/**
 * @author zhaoyong
 */
public interface ScriptManageService {
    /**
     * 根据场景id获取相关文件压缩包
     *
     * @param scriptDeployId
     * @return
     */
    String getZipFileUrl(Long scriptDeployId);

    /**
     * 创建脚本
     *
     * @param scriptManageDeployCreateRequest
     */
    Long createScriptManage(ScriptManageDeployCreateRequest scriptManageDeployCreateRequest);

    /**
     * 根据关联活动和脚本上传路径，校验脚本信息
     *
     * @param refType
     * @param refValue
     * @param scriptFileUploadPath
     * @return
     */
    ScriptCheckDTO checkAndUpdateScript(String refType, String refValue, String scriptFileUploadPath);

    /**
     * 分页查询脚本列表
     *
     * @param scriptManageDeployPageQueryRequest
     * @return
     */
    PagingList<ScriptManageDeployResponse> pageQueryScriptManage(
        ScriptManageDeployPageQueryRequest scriptManageDeployPageQueryRequest);

    /**
     * 删除脚本发布实例，如果实例完全被删除，同时删除脚本
     *
     * 删除脚本发布实例，同时删除该脚本实例对应的脚本下的所有脚本实例，同时删除脚本
     * 就是把所有的都删掉了
     * @param scriptDeployId
     */
    void deleteScriptManage(Long scriptDeployId);

    /**
     * 创建脚本发布实例和标签的关联关系
     *
     * @param scriptTagCreateRefRequest
     */
    void createScriptTagRef(ScriptTagCreateRefRequest scriptTagCreateRefRequest);

    /**
     * 查询所有标签
     *
     * @return
     */
    List<TagManageResponse> queryScriptTagList();

    /**
     * 查询脚本实例详情
     *
     * @param scriptDeployId
     * @return
     */
    ScriptManageDeployDetailResponse getScriptManageDeployDetail(Long scriptDeployId);

    /**
     * 修改脚本文件
     *
     * @param scriptManageDeployUpdateRequest
     */
    void updateScriptManage(ScriptManageDeployUpdateRequest scriptManageDeployUpdateRequest);

    /**
     * 查询所有业务流程，再将所有关联的脚本id附带出来
     *
     * @param businessFlowName
     * @return
     */
    List<ScriptManageSceneManageResponse> getAllScenes(String businessFlowName);

    /**
     * 查询所有业务活动，再将所有关联的脚本id附带出来
     *
     * @param activityName 业务活动名称
     * @return 脚本关联的业务活动列表
     */
    List<ScriptManageActivityResponse> listAllActivities(String activityName);

    /**
     * 解析脚本文件
     *
     * @param scriptFileUploadPath
     * @return
     */
    String explainScriptFile(String scriptFileUploadPath);

    /**
     * 获取文件下载路径
     *
     * @param filePath
     * @return
     */
    String getFileDownLoadUrl(String filePath);

    /**
     * 获取支持的jmeter插件列表名称
     *
     * @param nameRequest
     * @return
     */
    List<SupportJmeterPluginNameResponse> getSupportJmeterPluginNameList(SupportJmeterPluginNameRequest nameRequest);

    /**
     * 获取支持的jmeter插件版本列表
     *
     * @param versionRequest
     * @return
     */
    SupportJmeterPluginVersionResponse getSupportJmeterPluginVersionList(
        SupportJmeterPluginVersionRequest versionRequest);

    /**
     * 根据脚本id查询脚本实例id列表
     * @param scriptId
     * @return
     */
    List<ScriptManageDeployResponse> listScriptDeployByScriptId(Long scriptId);

    /**
     * 将该脚本实例回滚到最新版本
     * @param scriptDeployId
     */
    String rollbackScriptDeploy(Long scriptDeployId);

    /**
     * 根据脚本实例id查询对应脚本文件xml内容
     * @param scriptManageDeployIds
     * @return
     */
    List<ScriptManageXmlContentResponse> getScriptManageDeployXmlContent(List<Long> scriptManageDeployIds);
}
