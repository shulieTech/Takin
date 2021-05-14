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

package io.shulie.tro.web.data.dao.scriptmanage;

import java.util.List;
import java.util.Map;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.data.param.scriptmanage.ScriptExecuteResultCreateParam;
import io.shulie.tro.web.data.model.mysql.ScriptManageEntity;
import io.shulie.tro.web.data.param.scriptmanage.ScriptExecuteResultCreateParam;
import io.shulie.tro.web.data.param.scriptmanage.ScriptManageDeployCreateParam;
import io.shulie.tro.web.data.param.scriptmanage.ScriptManageDeployPageQueryParam;
import io.shulie.tro.web.data.param.scriptmanage.shell.ShellExecuteParam;
import io.shulie.tro.web.data.result.scriptmanage.ScriptExecuteResult;
import io.shulie.tro.web.data.result.scriptmanage.ScriptManageDeployResult;
import io.shulie.tro.web.data.result.scriptmanage.ScriptManageResult;

/**
 * @author zhaoyong
 */
public interface ScriptManageDAO extends IService<ScriptManageEntity> {
    /**
     * 根据id查询脚本
     *
     * @param scriptId
     * @return
     */
    ScriptManageResult selectScriptManageById(Long scriptId);

    /**
     * 根据id查询脚本实例
     *
     * @param scriptDeployId
     * @return
     */
    ScriptManageDeployResult selectScriptManageDeployById(Long scriptDeployId);

    /**
     * 根据版本获取实例，
     * @param scriptId
     * @param scriptVersion
     * @return
     */
    ScriptManageDeployResult selectScriptManageDeployByVersion(Long scriptId, Integer scriptVersion);

    /**
     * 删除脚本发布实例，如果一个脚本的所有实例都被删除，删除这个脚本
     *
     * @param scriptDeployId
     */
    void deleteScriptManageDeployById(Long scriptDeployId);

    /**
     * 分页查询脚本发布实例列表
     *
     * @param scriptManageDeployPageQueryParam
     * @return
     */
    PagingList<ScriptManageDeployResult> pageQueryScriptManageDeploy(
        ScriptManageDeployPageQueryParam scriptManageDeployPageQueryParam);

    /**
     * 新增脚本发布实例，同时如果没有脚本id的话，新增脚本
     *
     * @param scriptManageDeployCreateParam 创建所需要的参数
     * @return 创建后的结果
     */
    ScriptManageDeployResult createScriptManageDeploy(ScriptManageDeployCreateParam scriptManageDeployCreateParam);

    /**
     * 根据脚本名称查询脚本
     *
     * @param name
     * @return
     */
    List<ScriptManageResult> selectScriptManageByName(String name);

    /**
     * 更新脚本的版本号，同时将历史脚本状态更新为历史状态
     *
     * @param scriptId 脚本id
     * @param scriptVersion 脚本最新版本号
     */
    void updateScriptVersion(Long scriptId, Integer scriptVersion);

    /**
     * 切换版本
     * @param scriptId
     * @param scriptVersion
     */
    void switchScriptVersion(Long scriptId, Integer scriptVersion);

    /**
     * 根据关联关系查询脚本实例列表
     *
     * @param activityIds
     * @param refType
     * @return
     */
    List<ScriptManageDeployResult> selectByRefIdsAndType(List<String> activityIds, String refType,
        List<Integer> status);

    /**
     * 分页查询脚本，展示其中最新的一条脚本实例（分页信息走脚本表）
     *
     * @param scriptManageDeployPageQueryParam
     * @return
     */
    PagingList<ScriptManageDeployResult> pageQueryRecentScriptManageDeploy(
        ScriptManageDeployPageQueryParam scriptManageDeployPageQueryParam);

    /**
     * 物理删除脚本和对应的脚本实例
     *
     * @param scriptId
     */
    void deleteScriptManageAndDeploy(Long scriptId);

    /**
     * 根据脚本id查询脚本实例列表
     *
     * @param scriptId
     * @return
     */
    List<ScriptManageDeployResult> selectScriptManageDeployByScriptId(Long scriptId);

    /**
     * 指定责任人-脚本列表
     *
     * @param scriptId 脚本id
     * @param userId   负责人id
     * @return
     */
    int allocationUser(Long scriptId, Long userId);

    /**
     * 脚本实例数量
     * @return
     */
    Map<Long, Long> selectScriptDeployNumResult();

    /**
     * 创建执行记录
     * @param param
     * @return
     */
    Long createScriptExecuteResult(ScriptExecuteResultCreateParam param);

    /**
     * 获取执行记录
     * @param param
     * @return
     */
    PagingList<ScriptExecuteResult> getExecuteResult(ShellExecuteParam param);


}
